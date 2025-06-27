pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Ching-Chieh-Wang/zwap.git'
    }

    stages {
        stage('Stop Kafka (085)') {
            steps {
                sshagent(['immactavish']) {
                    timeout(time: 30, unit: 'SECONDS') {
                        sh '''
                        ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                            pkill -f run_kafka.sh || true
                            echo "[Stopped] Kafka"
                            exit 0
                        '
                        '''
                    }
                }
            }
        }

        stage('Stop Connector (084)') {
            steps {
                sshagent(['immactavish']) {
                    timeout(time: 30, unit: 'SECONDS') {
                        sh '''
                        ssh -o StrictHostKeyChecking=no immactavish@linux-084 '
                            pkill -f run_connector.sh || true
                            echo "[Stopped] Connector"
                            exit 0
                        '
                        '''
                    }
                }
            }
        }

        stage('Clean Kafka Folder (085)') {
            steps {
                sshagent(['immactavish']) {
                    timeout(time: 30, unit: 'SECONDS') {
                        sh '''
                        ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                            rm -rf zwap/services/kafka || true
                            echo "[Cleaned] kafka folder"
                            exit 0
                        '
                        '''
                    }
                }
            }
        }

        stage('Clone Kafka Only (085)') {
            steps {
                sshagent(['immactavish']) {
                    timeout(time: 45, unit: 'SECONDS') {
                        sh '''
                        ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                            cd zwap || mkdir zwap && cd zwap &&
                            git init &&
                            git remote add origin $REPO_URL &&
                            git config core.sparseCheckout true &&
                            echo "services/kafka/" > .git/info/sparse-checkout &&
                            git pull origin main
                            exit 0
                        '
                        '''
                    }
                }
            }
        }

        stage('Kafka Setup (085)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./setup.sh
                        exit 0
                    '
                    '''
                }
            }
        }

        stage('Kafka Bootstrap (085)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./bootstrap.sh
                        exit 0
                    '
                    '''
                }
            }
        }

        stage('Run Kafka (085)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./run_kafka.sh &
                        echo "[Started] Kafka"
                        exit 0
                    '
                    '''
                }
            }
        }

        stage('Run Connector (084)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-084 '
                        cd zwap/services/kafka &&
                        ./run_connector.sh &
                        echo "[Started] Connector"
                        exit 0
                    '
                    '''
                }
            }
        }

        stage('Verify Kafka Running (085)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        sleep 60
                        pgrep -f run_kafka.sh > /dev/null && echo "Kafka is running" || echo "Kafka is NOT running"
                        exit 0
                    '
                    '''
                }
            }
        }

        stage('Verify Connector Running (084)') {
            steps {
                sshagent(['immactavish']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-084 '
                        sleep 60
                        pgrep -f run_connector.sh > /dev/null && echo "Connector is running" || echo "Connector is NOT running"
                        exit 0
                    '
                    '''
                }
            }
        }
    }
}
