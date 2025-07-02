pipeline {
    agent any

    environment {
        SSH_KEY = 'linux-server'
        HOST_KAFKA = 'immactavish@linux-085'
        HOST_CONNECTOR = 'immactavish@linux-084'
        REPO_URL = 'https://github.com/Ching-Chieh-Wang/zwap.git'
    }

    triggers {
        GenericTrigger(
            genericVariables: [
                [key: 'modified_files', value: '\$.commits[*].modified[*]', expressionType: 'JSONPath'],
                [key: 'added_files', value: '\$.commits[*].added[*]', expressionType: 'JSONPath'],
                [key: 'removed_files', value: '\$.commits[*].removed[*]', expressionType: 'JSONPath'],
                [key: 'changed_file', value: '\$.commits[0].modified[0]', expressionType: 'JSONPath']
            ],
            causeString: 'Triggered on changes to: \$modified_files \$added_files \$removed_files',
            token: 'xiuxiulovejingjie',
            printContributedVariables: true,
            printPostContent: true,
            regexpFilterText: '\$changed_file',
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
                                echo "[Kafka Stop] Killing Killing PID \$PID on port 50003"
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

        stage('Copy .env and JKS to Kafka/Connector') {
            steps {
                sshagent([env.SSH_KEY]) {
                    withCredentials([
                        file(credentialsId: 'kafka-env-file', variable: 'KAFKA_ENV_FILE'),
                        file(credentialsId: 'elasticsearch-jks', variable: 'JKS_FILE')
                    ]) {
                        sh """
                            scp -o StrictHostKeyChecking=no "\${KAFKA_ENV_FILE}" "\${HOST_KAFKA}:~/zwap/services/kafka/.env"
                            scp -o StrictHostKeyChecking=no "\${JKS_FILE}" "\${HOST_CONNECTOR}:~/elasticsearch.jks"
                        """
                    }
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
                            if ss -tulwn | grep ":50003"; then
                                echo "[Kafka Health] Kafka is running on port 50003"
                            else
                                echo "[Kafka Health] Kafka is NOT running on port 50003"
                                echo "[Kafka Health] Showing kafka.log tail for debugging:"
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
                            if ss -tulwn | grep ":50001"; then
                                echo "[Connector Health] Connector is running on port 50001"
                            else
                                echo "[Connector Health] Connector is NOT running on port 50001"
                                echo "[Connector Health] Showing connector.log tail for debugging:"
                                cat zwap/services/kafka/connector.log || true
                                exit 1
                            fi
                        '
                    """
                }
            }
        }
    }
}
