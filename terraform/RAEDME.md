# EKS Cluster Deployment using Terraform

This project provisions a fully functional **Amazon EKS (Elastic Kubernetes Service)** cluster using [terraform-aws-modules/terraform-aws-eks](https://github.com/terraform-aws-modules/terraform-aws-eks). It includes managed node groups, custom IAM roles, secure SSH access, and S3 backend configuration.

## 📁 Directory Structure

terraform/

├── terraform-aws-eks/

│ └── tests/

│ └── eks-managed-node-group/

│ ├── locals.tf

│ ├── main.tf

│ ├── outputs.tf

│ ├── providers.tf

│ ├── variables.tf

│ └── vpc.tf


---

## 🔧 Features

- Provisions an EKS cluster with IPv4 configuration.
- Creates one managed node group with EFA support.
- Enables core cluster addons: CoreDNS, Kube-proxy, Node Monitoring Agent, and Pod Identity Agent.
- Uses AWS S3 for remote Terraform state storage and DynamoDB locking.
- Configures IAM roles and access entries for fine-grained EKS RBAC.
- Custom key pair and KMS key for volume encryption.
- Secure SSH access via custom security group.

---

## 🛠️ Setup Instructions

### 1. Prerequisites

- AWS CLI configured
- Terraform v1.5+
- IAM permissions to create EKS resources, VPCs, IAM roles, etc.

---

### 2. Backend Configuration (`providers.tf`)

terraform {
  backend "s3" {
    bucket         = "venkata-interview-bucket"
    key            = "eks/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    use_lock_file  = true
  }
}

provider "aws" {
  region = var.aws_region
}

3. How to Deploy EKS
bash
Copy
Edit
cd terraform/terraform-aws-eks/tests/eks-managed-node-group

# Initialize Terraform
terraform init

# Review the execution plan
terraform plan

# Apply the changes
terraform apply
📦 Resources Created
EKS Cluster
EKS Control Plane (IPv4)

Addons: CoreDNS, kube-proxy, Node Monitoring Agent, Pod Identity Agent

Zonal shift configuration enabled

Node Group
One managed node group (db_node_group)

EC2 instance type: m5.large

AMI: AL2023_x86_64_STANDARD

50 GB disk size

SSH access enabled via custom key pair and security group

IAM Configuration
Two IAM roles (ex-single, ex-multiple) with scoped access via AmazonEKS access policies

Additional custom policy (ec2:Describe*) applied to node group

KMS & Key Pair
KMS key with aliases: eks/<cluster-name>/ebs

Custom key pair for SSH access

🔒 Security Groups
A custom security group allows SSH access from CIDR block 10.0.0.0/8.

🚀 Deploy Nginx Ingress Controller Using Helm
After provisioning the EKS cluster, you can deploy the NGINX Ingress Controller and an example application.

📘 Reference Guide
Medium Article: Setting up NGINX Ingress Controller with EKS

✅ Deployment Steps
Add the Helm repo:

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
Install the NGINX ingress controller with the help of https://amod-kadam.medium.com/setting-up-nginx-ingress-controller-with-eks-f27390bcf804

![k8s-resources-listing](https://github.com/user-attachments/assets/f78f3252-0f3d-4546-8c78-64fec035da11)

✅ Note: The EXTERNAL-IP above is the public-facing ALB URL automatically created by AWS Load Balancer Controller or NGINX.


📤 Outputs
Check the outputs.tf file or run:


terraform output
📌 Notes
Make sure your module.vpc provides required subnet outputs (private, intra).

Instance type can be adjusted by changing instance_types inside eks_managed_node_group_defaults.

To disable EKS resources, the module includes disabled_eks and disabled_eks_managed_node_group examples.



📚 References
Terraform AWS EKS Module

AWS EKS Documentation

Helm Charts

NGINX Ingress Controller



🧑‍💻 Author
Hanumanth Vendra
GitHub: @hanumanthvendra


