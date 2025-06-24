#!/bin/bash

apptainer run \
  --bind $PWD/logs:/opt/bitnami/kafka/logs \
  --bind $PWD/meta:/opt/bitnami/kafka/meta \
  kafka.sif kafka
