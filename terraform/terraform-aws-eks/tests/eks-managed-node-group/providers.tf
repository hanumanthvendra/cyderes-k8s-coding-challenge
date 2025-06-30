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

