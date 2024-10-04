pipeline {
    agent any

    stages {
//         stage('SSH Folder Creation') {
//             steps {
//                 sshagent(credentials: ['paran-credential-secret']) {
//                     sh """
//                         ssh -o StrictHostKeyChecking=no root@serverIP 'ls -la'
//                     """
//                 }
//             }
//         }
        stage('Push') { // use 'room' branch
            steps {
                git branch: 'master', credentialsId: 'git-token', url: 'git@github.com:Songj2/paran_back.git'
            }
        }
        stage('gradlew +x'){
            steps{
                sh 'chmod +x ./gradlew' // 루트의 gradlew에 실행 권한 부여

            }
        }
        stage('Build') {
            parallel {
                stage("config-server") {
                    steps {
                        sh './gradlew :server:config-server:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("eureka-server") {
                    steps {
                        sh './gradlew :server:eureka-server:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("gateway-server") {
                    steps {
                        sh './gradlew :server:gateway-server:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("chat-service") {
                    steps {
                        sh './gradlew :service:chat-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("comment-service") {
                    steps {
                        sh './gradlew :service:comment-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("file-service") {
                    steps {
                        sh './gradlew :service:file-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("group-service") {
                    steps {
                        sh './gradlew :service:group-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("room-service") {
                    steps {
                        sh './gradlew :service:room-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
                stage("user-service") {
                    steps {
                        sh './gradlew :service:user-service:clean build --info' // 루트의 gradlew 사용
                    }
                }
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test --info' // 루트 디렉토리의 gradlew 실행
            }
        }
    }
}
