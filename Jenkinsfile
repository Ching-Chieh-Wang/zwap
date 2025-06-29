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
                [key: 'changed_files', value: '$.commits[*].["modified","added","removed"][*]', expressionType: 'JSONPath']
            ],
            causeString: 'GitHub Push detected. Changed: $changed_files',
            printContributedVariables: true,
            token: 'xiuxiulovejingjie',
            printPostContent: true,
            regexpFilterText: '$changed_files',
            regexpFilterExpression: 'services/kafka/.*'
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
                    ssh -o StrictHostKeyChecking=no \$HOST_CONNECTOR '
                        set +e
                        pkill -f run_connector.sh
                        echo "[Connector Stop] pkill exit code: \$?"
                        exit 0
                    ' || true
                    """
                }
            }
        }

        stage('Stop Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        set +e
                        pkill -f run_kafka.sh
                        echo "[Kafka Stop] pkill exit code: \$?"
                        exit 0
                    ' || true
                    """
                }
            }
        }

        stage('Clean Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        rm -rf zwap/services/kafka
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Sparse Clone Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        cd zwap || mkdir zwap && cd zwap
                        git init
                        git remote add origin \$REPO_URL
                        git config core.sparseCheckout true
                        echo "services/kafka/" > .git/info/sparse-checkout
                        git pull origin main
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Setup Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        cd zwap/services/kafka
                        ./setup.sh
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Bootstrap Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        cd zwap/services/kafka
                        ./bootstrap.sh
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Run Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        cd zwap/services/kafka
                        nohup ./run_kafka.sh > kafka.log 2>&1 &
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Run Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_CONNECTOR '
                        cd zwap/services/kafka
                        nohup ./run_connector.sh > connector.log 2>&1 &
                        exit 0
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
                    ssh -o StrictHostKeyChecking=no \$HOST_KAFKA '
                        nc -z localhost 50003
                        if [ \$? -eq 0 ]; then
                            echo "[Kafka Health] Kafka is running on port 50003"
                            exit 0
                        else
                            echo "[Kafka Health] Kafka is NOT running on port 50003"
                            exit 1
                        fi
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Verify Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \$HOST_CONNECTOR '
                        nc -z localhost 50001
                        if [ \$? -eq 0 ]; then
                            echo "[Connector Health] Kafka Connect is running on port 50001"
                            exit 0
                        else
                            echo "[Connector Health] Kafka Connect is NOT running on port 50001"
                            exit 1
                        fi
                        
                    '
                    """
                }
            }
        }
    }
}
