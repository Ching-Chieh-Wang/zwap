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
         plugins/debezium-mongodb \
         plugins/redis-sink
rm -f connect.offsets
touch connect.offsets

echo "[+] Resolving config templates with env vars..."

# 1) MongoDB connector needs limited envsubst so $Key/$Value survive
SUBST_VARS='$MONGO_URI $MONGO_DATABASE_INCLUDE_LIST $MONGO_TOPIC_PREFIX \
$MONGO_SNAPSHOT_MODE $KAFKA_HOST $KAFKA_BROKER_PORT $MONGO_HISTORY_TOPIC'
echo "    - mongodb-connector-template → mongodb-connector.properties"
envsubst "$SUBST_VARS" \
  < config/mongodb-connector-template.properties \
  > config/mongodb-connector.properties

# 2) All other templates can be fully substituted
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

echo "[+] Downloading Debezium MongoDB connector (3.1.2.Final)..."
curl -sSL -o debezium-mongodb.tar.gz \
  https://repo1.maven.org/maven2/io/debezium/debezium-connector-mongodb/3.1.2.Final/debezium-connector-mongodb-3.1.2.Final-plugin.tar.gz

mkdir -p tmp-debezium
tar -xzf debezium-mongodb.tar.gz -C tmp-debezium
cp tmp-debezium/debezium-connector-mongodb/*.jar \
   plugins/debezium-mongodb/
rm -rf tmp-debezium debezium-mongodb.tar.gz
echo "[+] Debezium MongoDB connector JARs copied to plugins/debezium-mongodb."

echo "[+] Cloning Redis Kafka Connect repo with tag v7.4..."
git clone --branch v7.4 --depth 1 https://github.com/redis-field-engineering/redis-kafka-connect.git tmp-redis-sink/redis-kafka-connect

echo "[+] Building Redis Kafka Connect connector..."
cd tmp-redis-sink/redis-kafka-connect
mvn clean package
cd ../../

echo "[+] Copying all connector JARs to plugins/redis-sink..."
mkdir -p plugins/redis-sink
cp tmp-redis-sink/redis-kafka-connect/target/*.jar \
   plugins/redis-sink/ ;

rm -rf tmp-redis-sink
echo "[+] Redis Kafka Connect JARs copied to kafka/plugins/redis-sink."

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

