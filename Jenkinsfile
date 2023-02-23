pipeline {
    agent any

    stages {
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
}