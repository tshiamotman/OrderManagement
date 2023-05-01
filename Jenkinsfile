pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Checkout the source code from Git
                git branch: 'main', url: 'git@github.com:tshiamotman/OrderManagement.git'
        
                // Build the Spring Boot application
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Dockerize') {
            steps {
                // Build the Docker image
                sh 'docker build -t order-manager-service:latest .'
            }
        }

        stage('Push to Registry') {
            steps {
                // Push the Docker image to a Docker registry
                withCredentials([usernamePassword(credentialsId: 'docker-registry', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                }
                sh 'docker push myapp:latest'
            }
        }

        stage('Deploy to GKE') {
            steps {
                // Deploy the application to GKE
                withCredentials([googleServiceAccountKey(credentialsId: 'order-manager', jsonKeyVariable: 'GCP_SA_KEY')]) {
                    sh 'gcloud auth activate-service-account --key-file zinc-reason-385105-b34667611b3e.json'
                }
                sh 'gcloud container clusters get-credentials order-manager-prod --zone us-central1-a --project order-manager'
                sh 'kubectl apply -f order-manager-service.yaml'
            }
        }
    }
}
