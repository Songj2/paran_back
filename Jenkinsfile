pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        repositoryName = 'paran'
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs() // 워크스페이스 전체 정리
                checkout scm
                git branch: 'main', credentialsId: 'git-token', url: 'git@github.com:Songj2/paran_back.git'
                sh 'git submodule update --init --recursive'
            }
        }

        stage('gradlew +x') {
            steps {
                sh 'chmod +x ./gradlew' // 루트의 gradlew에 실행 권한 부여
            }
        }

        stage('Build') {
            steps {
                script {
                    sh '''#!/bin/bash
                    set -e
                    export JAVA_HOME="$JAVA_HOME"

                    all_modules=("server:gateway-server" "server:config-server" "server:eureka-server"
                                 "service:user-service" "service:group-service" "service:chat-service"
                                 "service:file-service" "service:room-service" "service:comment-service")

                    echo "Cleaning..."
                    ./gradlew clean

                    for module in "${all_modules[@]}"
                    do
                      echo "Building BootJar for $module"
                      ./gradlew :$module:bootJar
                    done

                    unzip -p server/config-server/build/libs/config-server-0.0.1-SNAPSHOT.jar META-INF/MANIFEST.MF
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'pwd'  // 현재 작업 디렉토리 확인
                sh 'ls -al'  // 파일 목록 확인
                dir('/var/lib/jenkins/workspace/paranmanzang') {  // docker-compose.yml 파일이 있는 디렉토리로 이동
                    sh 'docker compose build'
                    sh 'docker images' // 현재 빌드된 이미지 확인
                }
            }
        }

        stage('Remove Dangling Images') {
            steps {
                script {
                    // Dangling 이미지 목록 가져오기
                    def danglingImages = sh(script: 'docker images -f dangling=true -q', returnStdout: true).trim()

                    // 이미지가 있을 경우에만 삭제
                    if (danglingImages) {
                        // 각 이미지 ID에 대해 rmi 실행
                        for (image in danglingImages.split("\n")) {
                            sh "docker rmi --force ${image}"
                        }
                    } else {
                        echo "No dangling images to remove."
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    script {
                        def modules = ["config", "eureka", "user", "group", "chat", "file", "room", "comment", "gateway"]

                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"

                        sh 'pwd'  // 현재 작업 디렉토리 확인
                        sh 'ls -al'  // 파일 목록 확인
                        sh "docker images"  // 현재 빌드된 이미지 확인

                        modules.each { module ->
                            def imageNameWithoutTag = "${DOCKER_USERNAME}/${repositoryName}"
                            def imageTag = "${module}-latest"
                            def fullImageName = "${imageNameWithoutTag}:${imageTag}"

                            // 이미지 존재 여부 확인
                            def imageExists = sh(script: "docker image inspect ${DOCKER_USERNAME}/${repositoryName}:${module}-latest >/dev/null 2>&1", returnStatus: true) == 0

                            if (imageExists) {
                                // 태그와 푸시
                                sh "docker tag ${DOCKER_USERNAME}/${repositoryName}:${module}-latest ${fullImageName}"
                                def pushResult = sh(script: "docker push ${fullImageName}", returnStatus: true)

                                if (pushResult == 0) {
                                    echo "Successfully pushed ${fullImageName}"
                                } else {
                                    error "Failed to push ${fullImageName}"
                                }
                            } else {
                                echo "Warning: Image ${DOCKER_USERNAME}/${repositoryName}-${module}:latest does not exist. Skipping..."
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "echo $KUBECONFIG"
                    sh "kubectl apply -f /var/lib/jenkins/workspace/paranmanzang/k8s/config-server.yaml"
                    sh "kubectl apply -f /var/lib/jenkins/workspace/paranmanzang/k8s/eureka-server.yaml"
                    sh "kubectl apply -f /var/lib/jenkins/workspace/paranmanzang/k8s/paranmanzang.yaml"
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}
