package model

type Product struct {
	ID          string    `json:"id"`
	Title       string    `json:"title"`
	Description string    `json:"description"`
	Embedding   []float64 `json:"embedding,omitempty"`
}
