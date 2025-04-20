#!/bin/bash

echo "*********************************************"
echo "Starting the MongoDB replica set"
echo "*********************************************"

# Wait for MongoDB to fully initialize
sleep 10
echo "Slept 10 seconds, attempting to initiate replica set..."

# Initialize the replica set
mongosh mongodb://root:example@mongodb1:27017/admin init-replica.js

echo "Init replica succesfully! Shutting down mongo init container..."