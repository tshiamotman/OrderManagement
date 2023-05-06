pipeline {
    agent any
    environment {
        CLOUDSDK_CORE_PROUCT="zinc-reason-385105"
        GCLOUD_CREDS=credentials("zinc-reason-385105")
    }
    stages {
        stage('Build') {
            steps {
                // Checkout the source code from Git
                git branch: 'main', 
                credentialsId: 'c5bffbae-df84-4461-bc34-2636389bdcd4',
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
                sh 'gcloud auth activate-service-account order-manager@zinc-reason-385105.iam.gserviceaccount.com --key-file=$GCLOUD_CREDS'
                sh "docker tag order-manager-service gcr.io/zinc-reason-385105/order-manager-service"
                sh 'docker push gcr.io/zinc-reason-385105/order-manager-service:latest'
            
            }
        }

        stage('Deploy to GKE') {
            steps {
                // Deploy the application to GKE
                sh 'gcloud auth activate-service-account order-manager@zinc-reason-385105.iam.gserviceaccount.com --key-file=$GCLOUD_CREDS'
                sh 'gcloud container clusters get-credentials order-manager-prod --zone us-central1-a --project order-manager'
                sh 'kubectl apply -f order-manager-service.yaml'
            
            }
        }
    }
}

