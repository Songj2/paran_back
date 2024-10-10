pipeline {
    agent any

        environment {
            JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
            DOCKERHUB_CREDENTIALS = credentials('docker-hub') // jenkins에 등록해 놓은 docker hub credentials 이름
            dockerUsername='songjih452'
        }

    stages {
        stage('Checkout') {
            steps {
                 git branch: 'main', credentialsId: 'git-token', url: 'git@github.com:Songj2/paran_back.git'
                 sh 'git submodule update --init --recursive'
            }
        }

        stage('gradlew +x'){
                    steps{
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
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                    sh 'pwd'  // 현재 작업 디렉토리 확인
                    sh 'ls -al'  // 파일 목록 확인
                    dir('/var/lib/jenkins/workspace/paranmanzang') {  // docker-compose.yml 파일이 있는 디렉토리로 이동
                        sh 'chmod +x /usr/share/doc/docker-compose'  //docker-compose 권한 부여
                        sh 'docker-compose up -d --build'
                        sh 'docker images' // 현재 빌드된 이미지 확인
                        sh 'docker-compose logs'  // 로그 확인
                }
            }
        }

        stage('Login'){
            steps{
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {

                script {
                    def modules = ["config", "eureka", "user", "group", "chat", "file", "room", "comment", "gateway"]


                    sh 'pwd'  // 현재 작업 디렉토리 확인
                    sh 'ls -al'  // 파일 목록 확인
                    sh "docker images"  // 현재 빌드된 이미지 확인

                    modules.each { module ->
                        def imageNameWithoutTag = "${dockerUsername}/${repositoryName}"
                        def imageTag = "${module}-${env.BUILD_ID}"
                        def fullImageName = "${imageNameWithoutTag}:${imageTag}"

                        // 이미지 존재 여부 확인
                        def imageExists = sh(script: "docker image inspect ${dockerUsername}/paran-${module}:latest >/dev/null 2>&1", returnStatus: true) == 0

                        if (imageExists) {
                            // 태그와 푸시
                            sh "docker tag ${dockerUsername}/paran-${module}:latest ${fullImageName}"
                            def pushResult = sh(script: "docker push ${fullImageName}", returnStatus: true)

                            if (pushResult == 0) {
                                echo "Successfully pushed ${fullImageName}"
                            } else {
                                error "Failed to push ${fullImageName}"
                            }
                        } else {
                            echo "Warning: Image ${dockerUsername}/paran-${module}:latest does not exist. Skipping..."
                        }
                    }
                }

            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def modules = ["gateway", "config", "eureka", "user", "group", "chat", "file", "room", "comment"]

                    for (module in modules) {
                        sh "kubectl set image deployment/${module} ${module}=songjih452/paran:${module}:${env.BUILD_ID}"
                    }
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
