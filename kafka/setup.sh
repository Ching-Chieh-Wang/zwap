#!/bin/bash
set -e

echo "[+] Step 1: Creating required directories..."
mkdir -p config logs meta

echo "[+] Step 2: Building Apptainer image (if not already built)..."
if [ ! -d kafka_sandbox ]; then
  apptainer build --sandbox kafka_sandbox kafka.def
else
  echo "[✓] kafka_sandbox already exists, skipping build."
fi

echo "[+] Step 3: Generating kraft-broker.properties..."
export HOSTNAME=$(hostname)

if [ ! -f ./config/kraft-broker.properties ]; then
  echo "❌ Missing ./config/kraft-broker.properties"
  exit 1
fi

envsubst < ./config/kraft-broker.properties > ./config/kraft-broker-resolved.properties

echo "[+] Step 4: Formatting Kafka metadata (only if not already formatted)..."
if [ ! -f meta/meta.properties ]; then
  apptainer exec \
    --env HOSTNAME="$HOSTNAME" \
    --bind "$PWD/config:/opt/kafka/config" \
    --bind "$PWD/meta:/opt/kafka/meta" \
    --bind "$PWD/logs:/opt/kafka/logs" \
    kafka_sandbox \
    /opt/bitnami/kafka/bin/kafka-storage.sh format \
      --config /opt/kafka/config/kraft-broker-resolved.properties \
      --cluster-id $(uuidgen)
  echo "[✓] Metadata formatted."
else
  echo "[✓] Metadata already exists. Skipping format."
fi
