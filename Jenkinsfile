pipeline {
    agent any

    stages {
        stage('Trigger Check') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                echo 'Changes detected in services/kafka/'
            }
        }

        stage('Clone or Update Repository') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        if [ -d "zwap/.git" ]; then
                            echo "Repo already exists. Pulling latest changes..."
                            cd zwap && git pull
                        else
                            echo "Cloning repo..."
                            git clone https://github.com/Ching-Chieh-Wang/zwap.git
                        fi
                    '
                    '''
                }
            }
        }

        stage('Kafka Setup') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./setup.sh
                    '
                    '''
                }
            }
        }

        stage('Kafka Bootstrap') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./bootstrap.sh
                    '
                    '''
                }
            }
        }

        stage('Kafka Run') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd zwap/services/kafka &&
                        ./run_kafka.sh
                    '
                    '''
                }
            }
        }
    }
}
