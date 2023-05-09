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
            agent {
                kubernetes {
                    label 'sideCar'
                    yaml '''
                        apiVersion: v1
                        kind: Pod
                        metadata:
                        name: jenkins-sidecar
                        spec:
                        containers:
                        - name: jenkins
                            image: jenkins/jenkins:lts
                            ports:
                            - containerPort: 8080
                            name: http
                            protocol: TCP
                            - containerPort: 50000
                            name: agent
                            protocol: TCP
                        - name: docker
                            image: docker:stable-dind
                            securityContext:
                            privileged: true
                            volumeMounts:
                            - name: dockersock
                            mountPath: /var/run/docker.sock
                        volumes:
                        - name: dockersock
                            hostPath:
                            path: /var/run/docker.sock
            '''
                }
            }
            steps {
                // Build the Docker image
                sh 'docker build -t order-manager-service:latest .'
            }
        }

        stage('Push to Registry') {
            steps {
                // Push the Docker image to a Docker registry
                sh '''gcloud auth activate-service-account order-manager@zinc-reason-385105.iam.gserviceaccount.com --key-file=$GCLOUD_CREDS
cat $GCLOUD_CREDS | docker login -u _json_key --password-stdin \
https://us.gcr.io
gcloud auth configure-docker
docker tag order-manager-service gcr.io/zinc-reason-385105/order-manager-service
docker push gcr.io/zinc-reason-385105/order-manager-service:latest'''  
            }
        }

        stage('Deploy to GKE') {
            steps {
                // Deploy the application to GKE
                sh '''gcloud auth activate-service-account order-manager@zinc-reason-385105.iam.gserviceaccount.com --key-file=$GCLOUD_CREDS
gcloud container clusters get-credentials order-manager-prod --region us-central1 --project zinc-reason-385105
kubectl apply -f order-manager-service.yaml'''
            
            }
        }
    }
}

