pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Ching-Chieh-Wang/zwap.git'
        SSH_KEY = 'linux-ssh-key'
        HOST_KAFKA = 'immactavish@linux-085'
        HOST_CONNECTOR = 'immactavish@linux-084'
    }

    stages {
        stage('Stop Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_KAFKA '
                        pkill -f run_kafka.sh || true
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Stop Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_CONNECTOR '
                        pkill -f run_connector.sh || true
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Clean Kafka Folder (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_KAFKA '
                        rm -rf zwap/services/kafka &&
                        mkdir -p zwap &&
                        cd zwap &&
                        git init &&
                        git remote add origin $REPO_URL || git remote set-url origin $REPO_URL &&
                        git config core.sparseCheckout true &&
                        echo "services/kafka/" > .git/info/sparse-checkout &&
                        git pull origin main
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Kafka Setup (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_KAFKA '
                        cd zwap/services/kafka &&
                        ./setup.sh &&
                        ./bootstrap.sh &&
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
                    ssh -o StrictHostKeyChecking=no $HOST_CONNECTOR '
                        cd zwap/services/kafka &&
                        nohup ./run_connector.sh > connector.log 2>&1 &
                        exit 0
                    '
                    """
                }
            }
        }

        stage('Wait for Services') {
            steps {
                echo 'Waiting 60 seconds for services to initialize...'
                sleep(time: 60, unit: 'SECONDS')
            }
        }

        stage('Verify Kafka (085)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_KAFKA '
                        pgrep -f run_kafka.sh > /dev/null &&
                        echo "Kafka is running." ||
                        (echo "Kafka is not running." && exit 1)
                    '
                    """
                }
            }
        }

        stage('Verify Connector (084)') {
            steps {
                sshagent([env.SSH_KEY]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $HOST_CONNECTOR '
                        pgrep -f run_connector.sh > /dev/null &&
                        echo "Connector is running." ||
                        (echo "Connector is not running." && exit 1)
                    '
                    """
                }
            }
        }
    }
}
