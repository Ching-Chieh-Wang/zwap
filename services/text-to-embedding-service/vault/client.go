package vault

import (
	"errors"

	vault "github.com/hashicorp/vault/api"
)

func GetSecret(path, key string) (string, error) {
	config := vault.DefaultConfig()
	client, err := vault.NewClient(config)
	if err != nil {
		return "", err
	}

	secret, err := client.Logical().Read(path)
	if err != nil {
		return "", err
	}
	if secret == nil || secret.Data == nil {
		return "", errors.New("no data found at " + path)
	}

	data, ok := secret.Data["data"].(map[string]interface{})
	if !ok {
		return "", errors.New("invalid data format in Vault secret")
	}

	val, ok := data[key].(string)
	if !ok || val == "" {
		return "", errors.New("key " + key + " not found in Vault secret")
	}

	return val, nil
}