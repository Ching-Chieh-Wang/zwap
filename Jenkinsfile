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
            genericVariables: [
                [key: 'modified_files', value: '$.commits[*].modified[*]', expressionType: 'JSONPath'],
                [key: 'added_files', value: '$.commits[*].added[*]', expressionType: 'JSONPath'],
                [key: 'removed_files', value: '$.commits[*].removed[*]', expressionType: 'JSONPath']
            ],
            causeString: 'Triggered on changes to: \$modified_files \$added_files \$removed_files',
            token: 'xiuxiulovejingjie',
            printContributedVariables: false,
            printPostContent: false,
            regexpFilterText: '\$modified_files \$added_files \$removed_files',
            regexpFilterExpression: '^services/kafka/.*'
        )
    }

    options {
        skipDefaultCheckout()
    }

    stages {
        stage('Stop Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_CONNECTOR} '
                            set -e
                            PID=\$(lsof -ti :50001 || true)
                            if [ -n "\$PID" ]; then
                                echo "[Connector Stop] Killing PID \$PID on port 50001"
                                kill -9 \$PID
                            else
                                echo "[Connector Stop] No process on port 50001"
                            fi
                        '
                    """
                }
            }
        }

        stage('Stop Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_KAFKA} '
                            set -e
                            PID=\$(lsof -ti :50003 || true)
                            if [ -n "\$PID" ]; then
                                echo "[Kafka Stop] Killing PID \$PID on port 50003"
                                kill -9 \$PID
                            else
                                echo "[Kafka Stop] No process on port 50003"
                            fi
                        '
                    """
                }
            }
        }

        stage('Wait for Kafka, connector stop') {
            steps {
                echo 'Sleeping 10 seconds to let Kafka and connector start...'
                sh 'sleep 10'
            }
        }


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
                        [path: 'secret/product/mongodb', secretValues: [
                            [envVar: 'PRODUCT_MONGODB_SERVICE_URI', vaultKey: 'PRODUCT_MONGODB_SERVICE_URI']
                        ]],
                        [path: 'secret/product/redis', secretValues: [
                            [envVar: 'PRODUCT_REDIS_SERVICE_URI', vaultKey: 'PRODUCT_REDIS_SERVICE_URI']
                        ]],
                        [path: 'secret/product/elasticsearch', secretValues: [
                            [envVar: 'PRODUCT_ELASTICSEARCH_SERVICE_USERNAME', vaultKey: 'PRODUCT_ELASTICSEARCH_SERVICE_USERNAME'],
                            [envVar: 'PRODUCT_ELASTICSEARCH_SERVICE_PASSWORD', vaultKey: 'PRODUCT_ELASTICSEARCH_SERVICE_PASSWORD']
                        ]]
                    ]
                ]) {
                    sh '''
                        envsubst < zwap/services/kafka/.env.template > zwap/services/kafka/.env
                        scp -o StrictHostKeyChecking=no zwap/services/kafka/.env ${HOST_KAFKA}:~/zwap/services/kafka/.env
                    '''
                }
            }
        }

        stage('Setup Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_KAFKA} '
                            set -e
                            cd zwap/services/kafka
                            ./setup.sh
                        '
                    """
                }
            }
        }

        stage('Bootstrap Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_KAFKA} '
                            set -e
                            cd zwap/services/kafka
                            ./bootstrap.sh
                        '
                    """
                }
            }
        }

        stage('Run Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_KAFKA} '
                            set -e
                            cd zwap/services/kafka
                            nohup ./run_kafka.sh > kafka.log 2>&1 &
                            echo "[Kafka Start] Kafka started with PID \$!"
                        '
                    """
                }
            }
        }

        stage('Run Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_CONNECTOR} '
                            set -e
                            cd zwap/services/kafka
                            export LOG4J_CONFIGURATION_FILE=/opt/bitnami/kafka/config/log4j2.yaml
                            nohup ./run_connector.sh > connector.log 2>&1 &
                            echo "[Connector Start] Connector started with PID \$!"
                        '
                    """
                }
            }
        }

        stage('Wait for Kafka, connector Startup') {
            steps {
                echo 'Sleeping 30 seconds to let Kafka and connector start...'
                sh 'sleep 30'
            }
        }

        stage('Verify Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_KAFKA} '
                            set -e
                            PID=\$(pgrep -f run_kafka.sh | head -n1)
                            if [ -n "\$PID" ] && ps -p \$PID > /dev/null; then
                                echo "[Kafka Health] Kafka is running with PID \$PID"
                            else
                                echo "[Kafka Health] Kafka is NOT running"
                                echo "[Kafka Health] Showing kafka.log  for debugging:"
                                cat zwap/services/kafka/kafka.log || true
                                exit 1
                            fi
                        '
                    """
                }
            }
        }

        stage('Verify Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \${HOST_CONNECTOR} '
                            set -e
                            PID=\$(pgrep -f run_connector.sh | head -n1)
                            if [ -n "\$PID" ] && ps -p \$PID > /dev/null; then
                                echo "[Connector Health] Connector is running with PID \$PID"
                            else
                                echo "[Connector Health] Connector is NOT running"
                                echo "[Connector Health] Showing connector.log  for debugging:"
                                cat zwap/services/kafka/connector.log || true
                                exit 1
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
                        rm -rf ~/zwap/services/kafka/plugins
                        echo "[Post Cleanup] Removed plugins folder from linux-084"
                    '
                """
            }
            cleanWs()
        }
    }
}
