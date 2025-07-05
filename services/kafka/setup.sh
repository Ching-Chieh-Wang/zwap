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

echo "[+] Downloading Redis sink connector (0.9.1)..."
curl -sSL -o redis.zip \
  https://hub-downloads.confluent.io/api/plugins/jcustenborder/kafka-connect-redis/versions/0.0.8/jcustenborder-kafka-connect-redis-0.0.8.zip

unzip -qo redis.zip -d tmp-redis
cp tmp-redis/jcustenborder-kafka-connect-redis-0.0.8/lib/*.jar \
   plugins/redis-sink/
rm -rf tmp-redis redis.zip
echo "[+] Redis sink connector JARs copied to plugins/redis-sink."

# Download Netty Native JARs for Linux (Epoll + Unix Common)
PLUGIN_DIR="plugins/redis-sink"
mkdir -p "$PLUGIN_DIR"
NETTY_VERSION="4.1.109.Final"

wget -nv -O "$PLUGIN_DIR/netty-handler-$NETTY_VERSION.jar" \
  "https://repo1.maven.org/maven2/io/netty/netty-handler/$NETTY_VERSION/netty-handler-$NETTY_VERSION.jar"

wget -nv -O "$PLUGIN_DIR/netty-buffer-$NETTY_VERSION.jar" \
  "https://repo1.maven.org/maven2/io/netty/netty-buffer/$NETTY_VERSION/netty-buffer-$NETTY_VERSION.jar"

wget -nv -O "$PLUGIN_DIR/netty-common-$NETTY_VERSION.jar" \
  "https://repo1.maven.org/maven2/io/netty/netty-common/$NETTY_VERSION/netty-common-$NETTY_VERSION.jar"

wget -nv -O "$PLUGIN_DIR/netty-codec-$NETTY_VERSION.jar" \
  "https://repo1.maven.org/maven2/io/netty/netty-codec/$NETTY_VERSION/netty-codec-$NETTY_VERSION.jar"

wget -nv -O "$PLUGIN_DIR/netty-transport-$NETTY_VERSION.jar" \
  "https://repo1.maven.org/maven2/io/netty/netty-transport/$NETTY_VERSION/netty-transport-$NETTY_VERSION.jar"


echo "Netty native JARs installed in $PLUGIN_DIR"
echo "[+] Netty JARs downloaded."

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
