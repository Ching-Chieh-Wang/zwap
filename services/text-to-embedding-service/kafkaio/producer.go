package kafkaio

import (
	"encoding/json"
	"os"

	"text-to-embedding-service/model"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

func NewProducer() (*kafka.Producer, error) {
	bootstrap := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if bootstrap == "" {
		bootstrap = "localhost:9092"
	}

	return kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": bootstrap})
}

func Publish(producer *kafka.Producer, topic string, p model.Product) error {
	data, err := json.Marshal(p)
	if err != nil {
		return err
	}
	return producer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Value:          data,
	}, nil)
}
