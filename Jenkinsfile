pipeline {
    agent any

    environment {
        SSH_KEY = 'linux-ssh-key'
        HOST_KAFKA = 'immactavish@linux-085'
        HOST_CONNECTOR = 'immactavish@linux-084'
        REPO_URL = 'https://github.com/Ching-Chieh-Wang/zwap.git'
    }

    triggers {
        GenericTrigger(
            genericVariables: [
                [key: 'modified_files', value: '$.commits[*].modified[*]', expressionType: 'JSONPath'],
                [key: 'added_files', value: '$.commits[*].added[*]', expressionType: 'JSONPath'],
                [key: 'removed_files', value: '$.commits[*].removed[*]', expressionType: 'JSONPath'],
                [key: 'changed_file', value: '$.commits[0].modified[0]', expressionType: 'JSONPath']
            ],
            causeString: 'Triggered on changes to: $modified_files $added_files $removed_files',
            token: 'xiuxiulovejingjie',
            printContributedVariables: true,
            printPostContent: true,
            regexpFilterText: '$changed_file',
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
                        ssh -o StrictHostKeyChecking=no ${HOST_CONNECTOR} '
                            set -e
                            PID=\$(lsof -ti:50001 || true)
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
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
                            set -e
                            PID=\$(lsof -ti:50003 || true)
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

        stage('Clean Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
                            set -e
                            rm -rf zwap/services/kafka
                        '
                    """
                }
            }
        }

        stage('Sparse Clone Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
                        set -e
                        mkdir -p ~/zwap
                        cd ~/zwap
        
                        if [ ! -d .git ]; then
                            git init -b main
                        fi
        
                        if git remote get-url origin >/dev/null 2>&1; then
                            git remote set-url origin ${REPO_URL}
                        else
                            git remote add origin ${REPO_URL}
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

        stage('Copy .env to Kafka') {
            steps {
                sshagent([env.SSH_KEY]) {
                    withCredentials([file(credentialsId: 'kafka-env-file', variable: 'KAFKA_ENV_FILE')]) {
                        sh '''
                            scp -o StrictHostKeyChecking=no "$KAFKA_ENV_FILE" "$HOST_KAFKA:~/zwap/services/kafka/.env"
                        '''
                    }
                }
            }
        }

        stage('Setup Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
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
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
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
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
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
                        ssh -o StrictHostKeyChecking=no ${HOST_CONNECTOR} '
                            set -e
                            cd zwap/services/kafka
                            nohup ./run_connector.sh > connector.log 2>&1 &
                        '
                    """
                }
            }
        }

        stage('Verify Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        sleep 60
                        ssh -o StrictHostKeyChecking=no ${HOST_KAFKA} '
                            set -e
                            nc -z localhost 50003
                            echo "[Kafka Health] Kafka is running on port 50003"
                        '
                    """
                }
            }
        }

        stage('Verify Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${HOST_CONNECTOR} '
                            set -e
                            nc -z localhost 50001
                            echo "[Connector Health] Kafka Connect is running on port 50001"
                        '
                    """
                }
            }
        }
    }
}
