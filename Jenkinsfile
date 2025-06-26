pipeline {
    agent any

    stages {
        stage('Remote Kafka Build') {
            when {
                changeset "**/services/kafka/**"
            }
            steps {
                sshagent(['linux085-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no immactavish@linux-085 '
                        cd ~/services/kafka &&
                        ./setup.sh &&
                        ./bootstrap.sh &&
                        ./run_kafka.sh
                    '
                    '''
                }
            }
        }
    }
}
