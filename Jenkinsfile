pipeline {
    agent any

    environment {
        REMOTE_KAFKA_HOST = "immactavish@linux-085"
        REMOTE_CONNECTOR_HOST = "immactavish@linux-084"
        REPO_URL = "https://github.com/Ching-Chieh-Wang/zwap.git"
    }

    stages {
        stage('Stop Kafka (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        pkill -f run_kafka.sh || true
                    '
                    '''
                }
            }
        }

        stage('Stop Connector (084)') {
            steps {
                sshagent(['linux084-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_CONNECTOR_HOST '
                        pkill -f run_connector.sh || true
                    '
                    '''
                }
            }
        }

        stage('Clean Kafka Folder (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        rm -rf zwap/services/kafka
                    '
                    '''
                }
            }
        }

        stage('Sparse Clone Kafka Folder (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        cd zwap || mkdir zwap && cd zwap &&
                        git init &&
                        git remote add origin $REPO_URL &&
                        git config core.sparseCheckout true &&
                        echo "services/kafka/" > .git/info/sparse-checkout &&
                        git pull origin main
                    '
                    '''
                }
            }
        }

        stage('Kafka Setup (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        cd zwap/services/kafka &&
                        ./setup.sh
                    '
                    '''
                }
            }
        }

        stage('Kafka Bootstrap (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        cd zwap/services/kafka &&
                        ./bootstrap.sh &
                    '
                    '''
                }
            }
        }

        stage('Run Connector (084)') {
            steps {
                sshagent(['linux084-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_CONNECTOR_HOST '
                        cd zwap/services/kafka &&
                        ./run_connector.sh &
                    '
                    '''
                }
            }
        }

        stage('Wait and Check Kafka (085)') {
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    sleep 60
                    ssh -o StrictHostKeyChecking=no $REMOTE_KAFKA_HOST '
                        if pgrep -f run_kafka.sh > /dev/null; then
                            echo "Kafka is running."
                        else
                            echo "Kafka is NOT running." && exit 1
                        fi
                    '
                    '''
                }
            }
        }

        stage('Wait and Check Connector (084)') {
            steps {
                sshagent(['linux084-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_CONNECTOR_HOST '
                        if pgrep -f run_connector.sh > /dev/null; then
                            echo "Connector is running."
                        else
                            echo "Connector is NOT running." && exit 1
                        fi
                    '
                    '''
                }
            }
        }
    }
}
