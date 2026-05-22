pipeline {

    agent {
        label 'java-agent'
    }

    tools {
        maven 'maven3'
        jdk 'jdk17'
    }

    environment {

        APP_NAME = 'enterprise-java-app'

        DEV_DIR = '/opt/dev'
        UAT_DIR = '/opt/uat'
        PROD_DIR = '/opt/prod'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {

        stage('Checkout Code') {

            steps {
                checkout scm
            }
        }

        stage('Print Environment Info') {

            steps {

                sh '''
                    echo "Current Branch: ${BRANCH_NAME}"
                    java -version
                    mvn -version
                    hostname
                '''
            }
        }

        stage('Clean Workspace') {

            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile Application') {

            steps {
                sh 'mvn compile'
            }
        }

        stage('Run Unit Tests') {

            steps {
                sh 'mvn test'
            }

            post {

                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Application') {

            steps {
                sh 'mvn package'
            }
        }

        stage('Archive Artifact') {

            steps {

                archiveArtifacts artifacts: 'target/*.jar'
            }
        }

        stage('Deploy to DEV') {

            when {
                branch 'develop'
            }

            steps {

                sh '''
                    sudo mkdir -p $DEV_DIR

                    sudo cp target/*.jar $DEV_DIR/

                    echo "Application deployed to DEV"
                '''
            }
        }

        stage('Deploy to UAT') {

            when {
                branch 'uat'
            }

            steps {

                sh '''
                    sudo mkdir -p $UAT_DIR

                    sudo cp target/*.jar $UAT_DIR/

                    echo "Application deployed to UAT"
                '''
            }
        }

        stage('Production Approval') {

            when {
                branch 'main'
            }

            steps {

                input message: 'Approve Production Deployment?',
                ok: 'Deploy'
            }
        }

        stage('Deploy to Production') {

            when {
                branch 'main'
            }

            steps {

                sh '''
                    sudo mkdir -p $PROD_DIR

                    sudo cp target/*.jar $PROD_DIR/

                    echo "Application deployed to PROD"
                '''
            }
        }
    }

    post {

        success {

            echo 'Pipeline executed successfully'
        }

        failure {

            echo 'Pipeline failed'
        }

        always {

            cleanWs()
        }
    }
}
