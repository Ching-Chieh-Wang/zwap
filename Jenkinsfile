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

        stage('Setup Config') {
            steps {
                sh """
                    set -e
                    cd "$HOME/zwap/services/kafka"

                    # Load .env so envsubst can see vars
                    if [ -f .env ]; then
                      set -a; . ./.env; set +a
                    fi

                    SUBST_VARS="$PRODUCT_MONGODB_SERVICE_URI $KAFKA_HOST $KAFKA_BROKER_PORT $KAFKA_CONNECTOR_PORT"

                    mkdir -p config

                    # Render connector templates (render only if template exists)
                    [ -f config/product-mongodb-source-connector-template.properties ] && \
                      envsubst "$SUBST_VARS" < config/product-mongodb-source-connector-template.properties \
                      > config/product-mongodb-source-connector.properties || true

                    [ -f config/product-redis-sink-connector-template.properties ] && \
                      envsubst < config/product-redis-sink-connector-template.properties \
                      > config/product-redis-sink-connector.properties || true

                    [ -f config/product-elasticsearch-sink-connector-template.properties ] && \
                      envsubst < config/product-elasticsearch-sink-connector-template.properties \
                      > config/product-elasticsearch-sink-connector.properties || true

                    # Standalone & Broker
                    [ -f config/connect-standalone-template.properties ] && \
                      envsubst < config/connect-standalone-template.properties \
                      > config/connect-standalone.properties || true

                    [ -f config/kraft-broker-template.properties ] && \
                      envsubst < config/kraft-broker-template.properties \
                      > config/kraft-broker.properties || true

                    # Distributed worker config
                    [ -f config/connect-distributed-template.properties ] && \
                      envsubst "$SUBST_VARS" < config/connect-distributed-template.properties \
                      > config/connect-distributed.properties || true
                """
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
                        rm -rf ~/zwap/services/kafka/plugins
                        echo "[Post Cleanup] Removed plugins folder from linux-084"
                    '
                """
            }
            cleanWs()
        }
    }
}
