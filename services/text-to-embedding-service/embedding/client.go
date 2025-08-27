package embedding

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"text-to-embedding-service/vault"
)

var (
	hfAPIURL  = "https://router.huggingface.co/nebius/v1/embeddings"
	modelName = "Qwen/Qwen3-Embedding-8B"
)

func GetEmbeddings(texts []string) ([][]float64, error) {
	hfApiKey, err := vault.GetSecret("secret/data/api-key", "HUGGING_FACE_API_KEY")
	if err != nil {
		return nil, err
	}

	body, _ := json.Marshal(map[string]interface{}{
		"input": texts,
		"model": modelName,
	})

	req, _ := http.NewRequest("POST", hfAPIURL, bytes.NewBuffer(body))
	req.Header.Set("Authorization", "Bearer "+hfApiKey)
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{Timeout: 30 * time.Second}
	resp, err := client.Do(req)

	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		bodyText, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("huggingface error: %s", string(bodyText))
	}

	data, _ := io.ReadAll(resp.Body)
	var raw map[string]interface{}
	if err := json.Unmarshal(data, &raw); err != nil {
		return nil, err
	}

	embeddings := make([][]float64, 0)
	dataArr, ok := raw["data"].([]interface{})
	if !ok {
		return nil, fmt.Errorf("unexpected response format: missing data array")
	}
	for _, entry := range dataArr {
		entryMap, ok := entry.(map[string]interface{})
		if !ok {
			return nil, fmt.Errorf("unexpected response format: data entry not an object")
		}
		embeddingIface, ok := entryMap["embedding"]
		if !ok {
			return nil, fmt.Errorf("unexpected response format: missing embedding field")
		}
		embeddingArr, ok := embeddingIface.([]interface{})
		if !ok {
			return nil, fmt.Errorf("unexpected response format: embedding field not an array")
		}
		embedding := make([]float64, len(embeddingArr))
		for i, v := range embeddingArr {
			val, ok := v.(float64)
			if !ok {
				return nil, fmt.Errorf("unexpected response format: embedding value not a float64")
			}
			embedding[i] = val
		}
		embeddings = append(embeddings, embedding)
	}
	return embeddings, nil
}
