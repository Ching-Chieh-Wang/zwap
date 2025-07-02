apptainer run \
  --bind $PWD/connect.offsets:/opt/bitnami/kafka/connect.offsets \
  --bind $PWD/logs:/opt/bitnami/kafka/logs \
  --bind $PWD/elasticsearch.jks:/opt/bitnami/kafka/elasticsearch.jks \
  kafka.sif connector