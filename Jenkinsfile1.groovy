node {
    stage('Clone Repo') {
        sshagent(['git-ssh-key']) {
            git url: 'git@github.com:hanumanthvendra/cyderes-k8s-coding-challenge.git', branch: 'main'
        }
    }

    stage('Build and Push to ECR') {
        withCredentials([[
            $class: 'AmazonWebServicesCredentialsBinding',
            credentialsId: 'aws-credentials-id'
        ]]) {
            def imageTag = "public.ecr.aws/i6m7c7y9/demo:${env.BUILD_NUMBER}"

            dir('nginx-hello-friend') {
                sh """
                    echo "Building Docker image with tag: ${env.BUILD_NUMBER}..."
                    docker build -t ${imageTag} .

                    echo "Authenticating with public ECR..."
                    aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws

                    echo "Pushing Docker image to ECR..."
                    docker push ${imageTag}
                """
            }
        }
    }

    stage('Helm deployment') {
        withEnv([
            'KUBECONFIG=/var/lib/jenkins/.kube/config',
            'PATH+TOOLS=/usr/local/bin:/usr/bin:/bin'
        ]) {
            dir('.') {
                sh """
                    echo "Running Helm upgrade with image tag: ${env.BUILD_NUMBER}..."

                    helm upgrade nginx-hello-friend ./nginx-hello-friend \\
                        --install \\
                        --namespace nginx-hello-friend \\
                        --set image.repository=public.ecr.aws/i6m7c7y9/demo \\
                        --set image.tag=${env.BUILD_NUMBER}

                    echo "Helm upgrade complete."
                """
            }
        }
    }
}
