#!/bin/bash
set -e

if [[ "$1" == "kafka" ]]; then
  echo "[+] Starting Kafka..."

  # Check if metadata is already initialized
  if [ ! -f "/opt/bitnami/kafka/meta/meta.properties" ]; then
    echo "[+] Formatting Kafka storage..."
    CLUSTER_ID=$(/opt/bitnami/kafka/bin/kafka-storage.sh random-uuid)
    echo "[+] Generated Cluster ID: $CLUSTER_ID"

    /opt/bitnami/kafka/bin/kafka-storage.sh format \
      --config /opt/bitnami/kafka/config/kraft-broker.properties \
      --cluster-id "$CLUSTER_ID" \
      --ignore-formatted
  else
    echo "[*] Kafka storage already formatted. Skipping format step."
  fi

  # Start Kafka broker
  exec /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/bitnami/kafka/config/kraft-broker.properties

elif [[ "$1" == "connector" ]]; then
  echo "[+] Starting Kafka Connect standalone..."

  # Run connector in standalone mode with MongoDB and Redis connector configs
  exec /opt/bitnami/kafka/bin/connect-standalone.sh \
    /opt/bitnami/kafka/config/connect-standalone.properties \
    /opt/bitnami/kafka/config/product-mongodb-source-connector.properties \
    /opt/bitnami/kafka/config/product-mongodb-sink-connector.properties \
    /opt/bitnami/kafka/config/product-redis-sink-connector.properties \
    /opt/bitnami/kafka/config/product-elasticsearch-sink-connector.properties

else
  echo "[!] Unknown or missing argument. Use one of: kafka | connector"
  exit 1
fi
