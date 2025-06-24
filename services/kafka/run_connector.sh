#!/bin/bash

apptainer run \
  --bind $PWD/connect.offsets:/opt/bitnami/kafka/connect.offsets \
  kafka.sif connector
