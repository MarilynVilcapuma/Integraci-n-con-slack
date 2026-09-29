pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Obteniendo el proyecto desde el repositorio...'
                checkout scm
            }
        }

        stage('Compilar') {
            steps {
                echo 'Compilando el proyecto...'
                bat 'mvn -B clean compile'
            }
        }

        stage('Pruebas unitarias y parametrizadas') {
            steps {
                echo 'Ejecutando pruebas unitarias y parametrizadas (JUnit 5 + Mockito)...'
                bat 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Reporte de cobertura (JaCoCo)') {
            steps {
                echo 'Generando reporte de cobertura con JaCoCo...'
                bat 'mvn -B jacoco:report'
            }
            post {
                always {
                    jacoco execPattern: 'target/jacoco.exec',
                           classPattern: 'target/classes',
                           sourcePattern: 'src/main/java',
                           minimumLineCoverage: '80'
                }
            }
        }

        stage('Verificar meta de cobertura') {
            steps {
                echo 'Verificando que la cobertura minima (80% lineas) se cumpla...'
                bat 'mvn -B jacoco:check'
            }
        }
    }

    post {
        success {
            echo 'Pipeline finalizado correctamente: compilacion, pruebas y cobertura OK.'
        }
        failure {
            echo 'El pipeline fallo. Revisar el resultado de las pruebas o la cobertura obtenida.'
        }
        always {
            archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
        }
    }
}
