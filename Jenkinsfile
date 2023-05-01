pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Checkout the source code from Git
                git branch: 'main', 
                credentialsId: '94d268ca-6756-4d96-ac76-58a38f70923f',
                url: 'https://github.com/tshiamotman/OrderManagement.git'
        
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
                sh "docker tag order-manager-service gcr.io/zinc-reason-385105/order-manager-service"
                sh 'docker push gcr.io/zinc-reason-385105/order-manager-service:latest'
            }
        }
    }

    stage('Deploy to GKE') {
        steps {
            // Deploy the application to GKE
            withCredentials([googleServiceAccountKey(credentialsId: 'order-manager', jsonKeyVariable: 'GCP_SA_KEY')]) {
                sh 'gcloud auth activate-service-account --key-file zinc-reason-385105-b34667611b3e.json'
                sh 'gcloud container clusters get-credentials order-manager-prod --zone us-central1-a --project order-manager'
                sh 'kubectl apply -f order-manager-service.yaml'
            }
        }
    }
}

