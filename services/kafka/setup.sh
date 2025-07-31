#!/bin/bash

set -e

# Load environment variables
if [[ -f .env ]]; then
  echo "[+] Loading environment variables from .env..."
  set -a
  source .env
  set +a
else
  echo "[!] .env file not found. Exiting."
  exit 1
fi

echo "[+] Cleaning up logs, meta, and plugins directories..."
rm -rf logs/* meta/* plugins/*
mkdir -p logs meta \
         plugins/product-mongodb=sink-connector \
         plugins/product-mongodb-source-connector \
         plugins/redis-sink
rm -f connect.offsets
touch connect.offsets

echo "[+] Resolving config templates with env vars..."

SUBST_VARS='$PRODUCT_MONGODB_SERVICE_URI $KAFKA_HOST $KAFKA_BROKER_PORT'
echo "    - mongodb-connector-template → mongodb-connector.properties"
envsubst "$SUBST_VARS" \
  < config/mongodb-connector-template.properties \
  > config/mongodb-connector.properties

echo "    - redis-connector-template → redis-connector.properties"
envsubst < config/redis-connector-template.properties \
         > config/redis-connector.properties

echo "    - connect-standalone-template → connect-standalone.properties"
envsubst < config/connect-standalone-template.properties \
         > config/connect-standalone.properties

echo "    - kraft-broker-template → kraft-broker.properties"
envsubst < config/kraft-broker-template.properties \
         > config/kraft-broker.properties

echo "    - elasticsearch-connector-template → elasticsearch-connector.properties"
envsubst < config/elasticsearch-connector-template.properties \
        > config/elasticsearch-connector.properties

echo "[+] All templates resolved."

echo "[+] Downloading Debezium MongoDB source connector (3.1.2.Final)..."
curl -sSL -o product-mongodb-source-connector.tar.gz \
  https://repo1.maven.org/maven2/io/debezium/debezium-connector-mongodb/3.1.2.Final/debezium-connector-mongodb-3.1.2.Final-plugin.tar.gz

mkdir -p tmp-product-mongodb-source-connector
tar -xzf product-mongodb-source-connector.tar.gz -C tmp-product-mongodb-source-connector
cp tmp-product-mongodb-source-connector/debezium-connector-mongodb/*.jar \
   plugins/product-mongodb-source-connector/
rm -rf tmp-product-mongodb-source-connector product-mongodb-source-connector.tar.gz
echo "[+] Debezium MongoDB connector JARs copied to plugins/product-mongodb-source-connector."

echo "[+] Downloading MongoDB Kafka sink connector (2.0.0)..."
curl -sSL -o mongodb-sink-connector.zip \
  https://hub-downloads.confluent.io/api/plugins/mongodb/kafka-connect-mongodb/versions/2.0.0/mongodb-kafka-connect-mongodb-2.0.0.zip

mkdir -p tmp-mongodb-sink-connector
unzip -qo mongodb-sink-connector.zip -d tmp-mongodb-sink-connector
cp tmp-mongodb-sink-connector/mongodb-kafka-connect-mongodb-2.0.0/lib/*.jar \
   plugins/mongodb-sink-connector/


echo "[+] Downloading pre-built Redis Kafka Connect connector (v7.4)..."
mkdir -p plugins/redis-sink
curl -sSL -o plugins/redis-sink/redis-kafka-connect-7.4-SNAPSHOT.jar \
  https://github.com/Ching-Chieh-Wang/redis-kafka-connect/releases/download/v7.4/redis-kafka-connect-7.4-SNAPSHOT.jar
echo "[+] Redis Kafka Connect JAR downloaded to plugins/redis-sink."


echo "[+] Downloading Elasticsearch sink connector (15.0.0)..."
curl -sSL -o elasticsearch.zip \
  https://hub-downloads.confluent.io/api/plugins/confluentinc/kafka-connect-elasticsearch/versions/15.0.0/confluentinc-kafka-connect-elasticsearch-15.0.0.zip

unzip -qo elasticsearch.zip -d tmp-elasticsearch
mkdir -p plugins/elasticsearch-sink
cp tmp-elasticsearch/confluentinc-kafka-connect-elasticsearch-15.0.0/lib/*.jar \
   plugins/elasticsearch-sink/
rm -rf tmp-elasticsearch elasticsearch.zip
echo "[+] Elasticsearch sink connector JARs copied to plugins/elasticsearch-sink."

echo "[+] Setup complete."

