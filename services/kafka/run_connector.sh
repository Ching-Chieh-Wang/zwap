apptainer run \
  --bind $PWD/connect.offsets:/opt/bitnami/kafka/connect.offsets \
  --bind $PWD/logs:/opt/bitnami/kafka/logs \
  --bind ~/elasticsearch.jks:/elasticsearch.jks
  kafka.sif connector