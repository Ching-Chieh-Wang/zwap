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
        GenericTrigger( // <--- Changed to GenericTrigger (capital G)
            token: 'xiuxiulovejingjie',

            // This replaces the 'postContent' block.
            // We define the variables directly in 'genericVariables'.
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

            // Optional: You can also specify a custom cause string if desired.
            // causeString: 'Generic Webhook Triggered by changes in Kafka Service'
        )
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
                        envsubst < ~/zwap/services/kafka/.env.template > ~/zwap/services/kafka/.env
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
                            echo \$! > kafka.pid
                            echo "[Kafka Start] Kafka started with PID \$(cat kafka.pid)"
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
                            echo \$! > connector.pid
                            echo "[Connector Start] Connector started with PID \$(cat connector.pid)"
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
                            cd zwap/services/kafka
                            if [ -f kafka.pid ]; then
                                PID=\$(cat kafka.pid)
                                if ps -p \$PID > /dev/null; then
                                    echo "[Kafka Health] Kafka is running with PID \$PID"
                                else
                                    echo "[Kafka Health] PID \$PID not running"
                                    cat kafka.log || true
                                    exit 1
                                fi
                            else
                                echo "[Kafka Health] kafka.pid file not found"
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
                            cd zwap/services/kafka
                            if [ -f connector.pid ]; then
                                PID=\$(cat connector.pid)
                                if ps -p \$PID > /dev/null; then
                                    echo "[Connector Health] Connector is running with PID \$PID"
                                else
                                    echo "[Connector Health] PID \$PID not running"
                                    cat connector.log || true
                                    exit 1
                                fi
                            else
                                echo "[Connector Health] connector.pid file not found"
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
