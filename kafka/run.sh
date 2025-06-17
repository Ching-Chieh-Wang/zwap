#!/bin/bash
set -e

echo "[+] Starting Kafka..."

# Ensure environment variable for hostname is available
export HOSTNAME=$(hostname)

# Optional: direct Kafka logs to console to avoid read-only filesystem issues
export KAFKA_LOG4J_OPTS="-Dlog4j.configurationFile=/opt/kafka/config/log4j2.yaml"

# Run Kafka inside Apptainer sandbox with required binds
apptainer exec \
  --env HOSTNAME="$HOSTNAME" \
  --env KAFKA_LOG4J_OPTS="$KAFKA_LOG4J_OPTS" \
  --bind "$PWD/config:/opt/kafka/config" \
  --bind "$PWD/logs:/opt/kafka/logs" \
  --bind "$PWD/meta:/opt/kafka/meta" \
  kafka_sandbox \
  /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/kafka/config/kraft-broker-resolved.properties
