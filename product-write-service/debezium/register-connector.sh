#!/bin/bash


# Wait until Kafka Connect REST API is available
until curl -s http://localhost:8083/; do
  echo "Waiting for Kafka Connect to be available..."
  sleep 2
done

# Register the connector
curl -X POST -H "Content-Type: application/json" \
  --data "@/debezium/mysql-connector.json" \
  http://localhost:8083/connectors

# Wait forever to keep container running
tail -f /dev/null