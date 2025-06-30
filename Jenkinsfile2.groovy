node {
    def tfDir = 'terraform/terraform-aws-eks/tests/eks-managed-node-group'

    stage('Clean Workspace') {
        cleanWs()
    }

    stage('Clone Repo') {
        sshagent(['git-ssh-key']) {
            git url: 'git@github.com:hanumanthvendra/cyderes-k8s-coding-challenge.git', branch: 'main'
        }
    }

    stage('Terraform Init') {
        dir(tfDir) {
            sh 'terraform init'
        }
    }

    stage('Terraform Plan') {
        dir(tfDir) {
            sh 'terraform plan'
        }
    }

    stage('Approval') {
        timeout(time: 15, unit: 'MINUTES') {
            input message: 'Approve to run Terraform Apply?', ok: 'Apply Now'
        }
    }

    stage('Terraform Apply') {
        dir(tfDir) {
            sh 'terraform apply -auto-approve'
        }
    }
}
