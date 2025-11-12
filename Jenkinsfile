pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'juanjothin/vitalapp'
        DOCKER_TAG = 'latest'
        REGISTRY_CREDENTIALS = 'Token-docker'
    }

    parameters {
        // Por defecto usar la rama 'Tests' (la rama donde tienes configurado el pipeline)
        string(name: 'GIT_BRANCH', defaultValue: 'Tests', description: 'Rama a compilar desde GitHub')
    }

    options {
        timestamps()
        // ansiColor requiere el plugin "AnsiColor" instalado/activo en Jenkins.
        // Si al ejecutar aparece "Invalid option type 'ansiColor'", es porque el plugin no está instalado
        // o está desactualizado. Puedes reinstalarlo/actualizarlo y volver a habilitar la línea siguiente.
        // ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout([$class: 'GitSCM',
                          branches: [[name: "*/${params.GIT_BRANCH}"]],
                          doGenerateSubmoduleConfigurations: false,
                          extensions: [],
                          userRemoteConfigs: [[url: 'https://github.com/londgav01S/VitalCare-Back.git']]])
            }
        }

        stage('Compilar (Gradle)') {
            steps {
                sh 'chmod +x ./gradlew'
                sh "rm -rf ${env.HOME}/.gradle"
                sh './gradlew clean'
                // Construye el artefacto sin ejecutar tests
                sh './gradlew bootJar -x test'
            }
        }

        stage('Análisis SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube-local') {
                    script {
                        echo "=== DEBUG: Variables de entorno Sonar ==="
                        sh 'echo "SONAR_HOST_URL = $SONAR_HOST_URL"'
                        sh 'echo "SONAR_AUTH_TOKEN = ${SONAR_AUTH_TOKEN:+***}"'
                        sh 'echo "Ejecutando análisis con Gradle..."'
                        sh './gradlew sonarqube -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN'
                    }
                }
            }
        }

        stage('Tests (JUnit + Postman)') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    // Ejecuta los tests JUnit que ya existen en el proyecto
                    sh './gradlew test'
                    // Publica resultados JUnit
                    junit 'build/test-results/test/*.xml'

                    script {
                        // Tests de API (Postman/Newman) opcionales si existe la colección
                        if (fileExists('tests/postman_collection.json')) {
                            sh '''
                                echo "Levantando servidor Spring Boot para tests de API..."
                                nohup java -jar build/libs/*.jar --spring.profiles.active=test > app.log 2>&1 &
                                SERVER_PID=$!
                                sleep 15 || true
                                echo "Ejecutando tests Newman..."
                                newman run tests/postman_collection.json --insecure || true
                                echo "Deteniendo servidor..."
                                kill $SERVER_PID || true
                            '''
                        } else {
                            echo 'No se encontró tests/postman_collection.json; se omiten tests de Postman.'
                        }
                    }
                }
            }
        }

        stage('Construir imagen Docker') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
            }
        }

        stage('Test en Docker') {
            steps {
                sh 'docker run --rm -d -p 8081:8080 --name test-vitalapp $DOCKER_IMAGE:$DOCKER_TAG'
                sh 'sleep 15'
                sh 'curl -f http://localhost:8081/actuator/health'
                sh 'docker stop test-vitalapp'
            }
        }

        stage('Push a DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${env.REGISTRY_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $DOCKER_IMAGE:$DOCKER_TAG'
                }
            }
        }

        stage('Desplegar a Staging') {
            when {
                expression { fileExists('docker-compose.yml') }
            }
            steps {
                sh 'docker compose down || true'
                sh 'docker compose pull || true'
                sh 'docker compose up -d'
            }
        }
    }
}
