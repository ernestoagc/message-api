pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'Java'
    }
    environment {
        registry = "880382163732.dkr.ecr.us-east-1.amazonaws.com"
        repository_image="nshh"
    }

    stages {
        
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        
        stage('Git Clone') {
            steps{
                echo "Build number is ${currentBuild.number}"
                echo "cleaning..."
                cleanWs()
                echo "cloning"
                git branch: 'main', credentialsId: 'GIT_HUB_CREDENTIALS', url: 'https://github.com/ernestoagc/message-api'    
            }
            
        }
        stage('Build') {
            steps {
                echo "Building..."
                echo "entren solution..."
                sh 'mvn -B -DskipTests clean install'
                echo "mvn version..."
                
            }
        }
        
        stage('Code Quality'){
            steps {
                script {
                    def scannerHome = tool 'sonarqube';  
        			   withSonarQubeEnv("sonarqube-server") { 
        			   sh   "cd ${WORKSPACE}"
        			   echo "before --- 2"
        			   sh "mvn sonar:sonar -Dproject.settings=../../sonar-project.properties"
                                    
        				   }
                }
            }
        }
        
        // Building Docker images
        stage('Building image') {
          steps{
            script {
                sh "pwd"
                echo "Inicia Build" 
                     sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${registry}'
					sh 'docker build -t ${registry}/${repository_image}:${BUILD_NUMBER} .'
          
            }
          }
        }
        
        // Uploading Docker images into AWS ECR
        stage('Pushing to ECR') {
         steps{  
             script {
                      sh '''
                      docker push ${registry}/${repository_image}:${BUILD_NUMBER}
                      '''
             }
            }
        }
        
		
		
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}