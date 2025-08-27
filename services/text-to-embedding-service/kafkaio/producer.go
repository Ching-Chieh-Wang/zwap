package kafkaio

import (
	"encoding/json"
	"fmt"
	"os"

	"text-to-embedding-service/model"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

func NewProducer() (*kafka.Producer, error) {
	bootstrap := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if bootstrap == "" {
		return nil, fmt.Errorf("KAFKA_BOOTSTRAP_SERVERS environment variable is not set")
	}

	return kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": bootstrap})
}

func Publish(producer *kafka.Producer, topic string, key []byte, p model.Product) error {
	data, err := json.Marshal(p)
	if err != nil {
		return err
	}
	return producer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Key:            key,
		Value:          data,
	}, nil)
}
