pipeline {
    agent any

    environment {
        SSH_KEY = 'linux-server'
        HOST_KAFKA = 'immactavish@linux-085'
        HOST_CONNECTOR = 'immactavish@linux-084'
        REPO_URL = 'https://github.com/Ching-Chieh-Wang/zwap.git'
        VAULT_SERVICE_URI = 'http://linux-076:50006'
    }


    triggers {
        GenericTrigger(
            token: 'xiuxiulovejingjie',

            genericVariables: [
                [
                    key: 'changed_files',
                    value: '$.commits[*].[\'modified\',\'added\',\'removed\'][*]',
                    expressionType: 'JSONPath'
                ]
            ],

            // This replaces the 'regexpFilter' block.
            // Text and expression are direct parameters.
            regexpFilterText: '$changed_files',
            regexpFilterExpression: '.*services/kafka/.*',

            printContributedVariables: false,
            printPostContent: false


        )
    }

    stages {
        stage('Sparse Clone Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
                            set -e
                            rm -rf ~/zwap
                            mkdir -p ~/zwap
                            cd ~/zwap

                            git init -b main

                            if git remote get-url origin >/dev/null 2>&1; then
                                git remote set-url origin "${REPO_URL}"
                            else
                                git remote add origin "${REPO_URL}"
                            fi

                            git config core.sparseCheckout true
                            git sparse-checkout init --no-cone
                            git sparse-checkout set services/kafka
                            git pull origin main
                        '
                    """
                }
            }
        }

        stage('Render .env from Vault and Upload') {
            steps {
                withVault([
                    vaultSecrets: [
                        [path: 'secret/product/common', secretValues: [
                            [envVar: 'PRODUCT_MONGODB_SERVICE_URI', vaultKey: 'PRODUCT_MONGODB_SERVICE_URI']
                        ]],
                        [path: 'secret/product/product-read-service', secretValues: [
                            [envVar: 'PRODUCT_REDIS_SERVICE_URI', vaultKey: 'PRODUCT_REDIS_SERVICE_URI']
                        ]],
                        [path: 'secret/product/product-search-service', secretValues: [
                            [envVar: 'PRODUCT_ELASTICSEARCH_SERVICE_PASSWORD', vaultKey: 'PRODUCT_ELASTICSEARCH_SERVICE_PASSWORD']
                        ]]
                    ]
                ]) {
                    sh '''
                        envsubst < ~/zwap/services/kafka/.env.template > ~/zwap/services/kafka/.env
                    '''
                }
            }
        }

        stage('Setup env') {
            steps {
                sh '''
                    set -e
                    cd "$HOME/zwap/services/kafka"

                    # Load .env so envsubst can see vars
                    if [ -f .env ]; then
                      set -a; . ./.env; set +a
                    fi
                '''
            }
        }

        stage('Apply Connectors via REST (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${HOST_CONNECTOR} '
                            set -e
                            cd ~/zwap/services/kafka

                            # Load .env so the worker sees env vars used by ${env:...}
                            if [ -f .env ]; then set -a; . ./.env; set +a; fi
                            PORT=${KAFKA_CONNECTOR_PORT}

                            echo "[Apply] Using Connect REST on localhost:${KAFKA_CONNECTOR_PORT}"

                            # PUT = create or update (idempotent)
                            if [ -f config/product-mongodb-source-connector.json ]; then
                              curl -s -X PUT "http://localhost:${KAFKA_CONNECTOR_PORT}/connectors/product-mongodb-source-connector/config" \
                                -H 'Content-Type: application/json' \
                                --data-binary @config/product-mongodb-source-connector.json || exit 1
                              echo "[Apply] product-mongodb-source-connector applied"
                            else
                              echo "[Skip] config/product-mongodb-source-connector.json not found"
                            fi

                            if [ -f config/product-redis-sink-connector.json ]; then
                              curl -s -X PUT "http://localhost:${KAFKA_CONNECTOR_PORT}/connectors/product-redis-sink-connector/config" \
                                -H 'Content-Type: application/json' \
                                --data-binary @config/product-redis-sink-connector.json || exit 1
                              echo "[Apply] product-redis-sink-connector applied"
                            else
                              echo "[Skip] config/product-redis-sink-connector.json not found"
                            fi

                            if [ -f config/product-elasticsearch-sink-connector.json ]; then
                              curl -s -X PUT "http://localhost:${KAFKA_CONNECTOR_PORT}/connectors/product-elasticsearch-sink-connector/config" \
                                -H 'Content-Type: application/json' \
                                --data-binary @config/product-elasticsearch-sink-connector.json || exit 1
                              echo "[Apply] product-elasticsearch-sink-connector applied"
                            else
                              echo "[Skip] config/product-elasticsearch-sink-connector.json not found"
                            fi
                        '
                    """
                }
            }
        }
    }
    post {
        always {
            echo 'Cleaning up workspace...'
            sshagent([env.SSH_KEY]) {
                sh """
                    ssh -o StrictHostKeyChecking=no \${HOST_CONNECTOR} '
                        set -e
                        rm -rf ~/zwap/services/kafka
                        echo "[Post Cleanup] Removed kafka folder from linux-084"
                    '
                """
            }
            cleanWs()
        }
    }
}
