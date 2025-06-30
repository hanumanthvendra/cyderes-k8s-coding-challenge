✅ Helm Chart Deployment with Jenkins CI/CD

# nginx-hello-friend Deployment with Helm and Jenkins CI/CD

This project contains a sample Helm chart and Jenkins pipeline to automate the build, containerization, and deployment of a simple web server (`nginx-hello-friend`) to a Kubernetes cluster using AWS ECR and Helm.

---

## 📁 Project Structure


cyderes-k8s-coding-challenge/
 ├── nginx-hello-friend/ # Application and Helm chart directory
 │ ├── Dockerfile # Dockerfile to build image
 │ ├── Chart.yaml # Helm chart metadata
 │ ├── templates/ # Helm templates (Deployment, Service, etc.)
 │ └── values.yaml # Default Helm values
 └── Jenkinsfile # Jenkins pipeline script

---

## ⚙️ Technologies Used

- **Kubernetes** (EKS)
- **Helm** for packaging and deployment
- **Docker** for containerization
- **AWS ECR Public** for image registry
- **Jenkins** for CI/CD automation

---

## 🚀 CI/CD Pipeline Overview

The CI/CD pipeline performs the following stages:

### 1. Clone the Git Repository

Using SSH credentials configured in Jenkins.

git url: 'git@github.com:<your-repo>/cyderes-k8s-coding-challenge.git', branch: 'main'

2. Build and Push Docker Image
Builds the Docker image from nginx-hello-friend/Dockerfile


Tags the image with Jenkins BUILD_NUMBER


Pushes it to AWS Public ECR:
 public.ecr.aws/i6m7c7y9/demo:<BUILD_NUMBER>


3. Deploy via Helm
Deploys the image to EKS using Helm


Uses the chart in nginx-hello-friend/


Dynamically sets image tag to match the build number:

helm upgrade nginx-hello-friend ./nginx-hello-friend \
    --install \
    --namespace nginx-hello-friend \
    --set image.repository=public.ecr.aws/i6m7c7y9/demo \
    --set image.tag=<BUILD_NUMBER>

🔐 Prerequisites
Jenkins with:


Docker installed and usable by the jenkins user


AWS credentials configured as a Jenkins credential (aws-credentials-id)


Git SSH credentials for GitHub (git-ssh-key)


EKS cluster configured and accessible from Jenkins master


Helm and kubectl installed on Jenkins master


kubeconfig at /var/lib/jenkins/.kube/config accessible by Jenkins



📦 How to Deploy Manually (Optional)

cd nginx-hello-friend

# Build and tag Docker image
docker build -t public.ecr.aws/i6m7c7y9/demo:1.0 .

# Push to ECR
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws
docker push public.ecr.aws/i6m7c7y9/demo:1.0

# Deploy with Helm
helm upgrade nginx-hello-friend . \
    --install \
    --namespace nginx-hello-friend \
    --set image.repository=public.ecr.aws/i6m7c7y9/demo \
    --set image.tag=1.0
    
![jenkins-ci-pic](https://github.com/user-attachments/assets/b39f3050-6f9d-4e85-8518-89078a75d948)
![jenkins-ci-2](https://github.com/user-attachments/assets/26841d8c-9b06-4faa-a65d-3374414d66dd)

🔹 3. Access the Application:

http://ac51886d48b5b4e1591a74d670b32fc9-846705458.us-east-1.elb.amazonaws.com/

![Screenshot 2025-06-30 114108](https://github.com/user-attachments/assets/e6737402-00e4-4a94-b668-9f2870f618f4)

## Jenkins stage views for infra and build pipelines

![Screenshot 2025-06-30 164857](https://github.com/user-attachments/assets/2b658e59-c156-4f6f-9819-3a2c3a6b2b62)

![Screenshot 2025-06-30 164930](https://github.com/user-attachments/assets/2a25614e-9768-4dfd-bf5c-31385cbbb4e4)




