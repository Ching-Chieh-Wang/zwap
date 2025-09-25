package main

import (
	"fmt"
	"log"
	"time"

	"text-to-embedding-service/embedding"
	"text-to-embedding-service/kafkaio"
	"text-to-embedding-service/model"

	"github.com/joho/godotenv"
)

func init() {
	if err := godotenv.Load(); err != nil {
		log.Println("⚠️  No .env file found, relying on system environment")
	}
}

const (
	CONSUMER_SUBSCRIBE_TOPIC = "mongo.product.products"
	PRODUCER_PUBLISH_TOPIC   = "mongo.product.products.embedded"
)

func main() {
	consumer, err := kafkaio.NewConsumer()
	if err != nil {
		panic(err)
	}
	defer consumer.Close()

	producer, err := kafkaio.NewProducer()
	if err != nil {
		panic(err)
	}
	defer producer.Close()

	consumer.SubscribeTopics([]string{CONSUMER_SUBSCRIBE_TOPIC}, nil)

	batch := []model.ProductMsg{}
	batchSize := 32
	timeout := time.Second

	for {
		msg, err := consumer.ReadMessage(timeout)
		if err == nil {
			p, err := kafkaio.ParseMessage(msg)
			if err == nil {
				batch = append(batch, model.ProductMsg{Key: msg.Key, Product: p})
			}
		}

		if len(batch) >= batchSize || (len(batch) > 0 && err != nil) {
			texts := []string{}
			for _, doc := range batch {
				combined := doc.Product.Title + " . " + doc.Product.Description
				texts = append(texts, combined)
			}

			embeddings, err := embedding.GetEmbeddings(texts)
			if err != nil {
				fmt.Println("❌ embedding error:", err)
				batch = []model.ProductMsg{}
				continue
			}

			for i, doc := range batch {
				doc.Product.Embedding = embeddings[i]
				kafkaio.Publish(producer, PRODUCER_PUBLISH_TOPIC, doc.Key, doc.Product)
			}
			producer.Flush(15 * 1000)
			fmt.Printf("✅ enriched %d products\n", len(batch))
			batch = []model.ProductMsg{}
		}
	}
}
