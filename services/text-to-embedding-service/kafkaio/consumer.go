package kafkaio

import (
	"encoding/json"
	"fmt"
	"os"

	"text-to-embedding-service/model"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

func NewConsumer() (*kafka.Consumer, error) {
	bootstrap := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if bootstrap == "" {
		return nil, fmt.Errorf("KAFKA_BOOTSTRAP_SERVERS environment variable is not set")
	}

	return kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": bootstrap,
		"group.id":          "text-to-embedding-service-consumer",
		"auto.offset.reset": "earliest",
	})
}

func ParseMessage(msg *kafka.Message) (model.Product, error) {
	var p model.Product
	err := json.Unmarshal(msg.Value, &p)
	return p, err
}
