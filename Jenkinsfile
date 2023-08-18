pipeline {
    agent any

    environment {
        // AWS Secrets Manager에서 필요한 정보를 추출
        AWS_SECRETS = """
            aws secretsmanager get-secret-value --secret-id /secret/chat/spring/prod | jq -r '.SecretString' | jq -r .
        """
        AWS_REGION = sh(script: '${AWS_SECRETS} | jq -r ".AWS_REGION"', returnStdout: true).trim()
        DOCKERHUB_USERNAME = sh(script: '${AWS_SECRETS} | jq -r ".DOCKERHUB_USERNAME"', returnStdout: true).trim()
        DOCKERHUB_PASSWORD = sh(script: '${AWS_SECRETS} | jq -r ".DOCKERHUB_PASSWORD"', returnStdout: true).trim()
        GIT_CREDENTIALS_ID = sh(script: '${AWS_SECRETS} | jq -r ".GIT_CREDENTIALS_ID"', returnStdout: true).trim()
    }

    stages {
        // Docker Hub에 로그인 
        stage('Docker login') {
            steps {
                script {
                    sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD}"
                }
            }
        }

        












        // Docker 이미지를 빌드하고 Push
        stage('Build and Push Docker Images') {
            steps {
                script {
                    // Git commit hash를 사용해 새로운 이미지 태그를 생성
                    def NEW_IMAGE_TAG = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()

                    // chat-application 이미지 빌드 및 푸시
                    dir('chat-application') {
                        sh """
                            ./gradlew build --exclude-task test
                            docker build --build-arg AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
                                         --build-arg AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
                                         --build-arg AWS_REGION=${AWS_REGION} \
                                         --build-arg SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} \
                                         -t lsjpjs1/chat:${NEW_IMAGE_TAG} .
                            docker push lsjpjs1/chat:${NEW_IMAGE_TAG}
                        """
                    }

                    // message-classifier 이미지 빌드 및 푸시
                    dir('message-classifier') {
                        sh """
                            ./gradlew build --exclude-task test
                            docker build --build-arg AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
                                         --build-arg AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
                                         --build-arg AWS_REGION=${AWS_REGION} \
                                         --build-arg SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} \
                                         -t lsjpjs1/message-classifier:${NEW_IMAGE_TAG} .
                            docker push lsjpjs1/message-classifier:${NEW_IMAGE_TAG}
                        """
                    }
                }
            }
        }

        // Kubernetes 구성 파일의 이미지를 업데이트
        stage('Update Kubernetes Configuration') {
            steps {
                script {
                    def NEW_IMAGE_TAG = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    // chat-core-statefulset.yaml 이미지 업데이트
                    sh """
                        sed -i 's|lsjpjs1/chat:.*|lsjpjs1/chat:${NEW_IMAGE_TAG}|' kubernetes/chat-core-statefulset.yaml
                    """

                    // message-classifier-deployment.yaml 이미지 업데이트
                    sh """
                        sed -i 's|lsjpjs1/message-classifier:.*|lsjpjs1/message-classifier:${NEW_IMAGE_TAG}|' kubernetes/message-classifier-deployment.yaml
                    """
                }
            }
        }

        // GitHub에 변경 사항을 Push
        stage('Push to GitHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: GIT_CREDENTIALS_ID, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        sh """
                            git config --global user.name "Yu Kyung Hwan"
                            git config --global user.email "dbrudghks333@naver.com"
                            git add kubernetes/chat-core-statefulset.yaml kubernetes/message-classifier-deployment.yaml
                            git commit -m "Update k8s configurations with new image tags"
                            git push https://$GIT_USERNAME:$GIT_PASSWORD@github.com/chat-practice-organization/Chat-Practice.git HEAD:development-yu
                        """
                    }
                }
            }
        }
    }
}






