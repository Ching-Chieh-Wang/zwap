package model

type Product struct {
	Title       string    `json:"title"`
	Description string    `json:"description"`
	Embedding   []float64 `json:"embedding,omitempty"`
	Price       int       `json:"price"`
}
