pipeline {
    agent any

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
                withCredentials([googleServiceAccountKey(credentialsId: 'b75110cca5016fc663fc38a6b9b7b217608db889', jsonKeyVariable: 'GCP_SA_KEY')]) {
                    sh "docker tag order-manager-service gcr.io/zinc-reason-385105/order-manager-service"
                    sh 'docker push gcr.io/zinc-reason-385105/order-manager-service:latest'
                }
            }
        }

        stage('Deploy to GKE') {
            steps {
                // Deploy the application to GKE
                withCredentials([googleServiceAccountKey(credentialsId: 'b34667611b3ebabc872218458c63c54e5c40f90f', jsonKeyVariable: 'GCP_SA_KEY')]) {
                    sh 'gcloud auth activate-service-account --key-file zinc-reason-385105-b34667611b3e.json'
                    sh 'gcloud container clusters get-credentials order-manager-prod --zone us-central1-a --project order-manager'
                    sh 'kubectl apply -f order-manager-service.yaml'
                }
            }
        }
    }
}

