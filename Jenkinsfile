pipeline {
    agent any

    stages {
        stage("Verify tolling") {
            steps {
                sh'''
                    docker version
                    docker info
                    docker compose version
                    curl --version
                    jq --version
                '''
            }
        }
        stage("Prune Docker data") {
            steps {
                sh 'docker system prune -a --volumes -f'
            }
        }
        stage('Start Container') {
            steps{
                sh 'docker compose up -d --no-color --wait'
                sh 'docker compose ps'
            }
        }
        stage('Testing against the container') {
            steps {
                sh 'curl http://localhost:8082/api/v1/users | jq'
            }
        }
    }
    post {
        always {
            sh 'docker compose down --remove-orphans -v'
            sh 'docker compose ps'
        }
    }

}