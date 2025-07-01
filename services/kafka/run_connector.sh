#!/bin/bash

apptainer run \
  --bind $PWD/connect.offsets:/opt/bitnami/kafka/connect.offsets \
  --bind $PWD/logs:/opt/bitnami/kafka/logs \
  kafka.sif connector
