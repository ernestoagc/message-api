pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'Java'
    }
    environment {
        ecr_registry = "880382163732.dkr.ecr.us-east-1.amazonaws.com"
		ecr_cred ="us-east-1:ecr-jenkins"
		k8s_host_remote ="ubuntu@12.0.133.16"
        repository_image="onlock-api"
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
        			   sh "mvn sonar:sonar \
        							-Dsonar.projectName=message-api \
        							-Dsonar.projectKey=message-api \
                                    -Dsonar.sources=src/main/ \
                                    -Dsonar.sourceEncoding=UTF-8 \
                                    -Dsonar.tests=src/test/ \
                                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                    -Dsonar.host.url=http://12.0.130.85:9000"
                                    
        				   }
                }
            }
        }        

        // Uploading Docker images into AWS ECR
         stage('Pushing to ECR') {
         steps{  
             script {
						docker.withRegistry(
						"https://${ecr_registry}/${repository_image}",
						"ecr:${ecr_cred}"){
						def myImage = docker.build("${repository_image}")
        				   myImage.push("${BUILD_NUMBER}")
						}
             }
            }
         }
		
		
        stage ('K8S Deploy') {
          steps{

                script {     
                    sh "pwd"
                    echo "Updating image version in deployment file"
                    sh "chmod +x changeTag.sh" 
                    sh "./changeTag.sh ${BUILD_NUMBER}" 
                }
                
                sshagent(['k8s-ubuntu']) {
                    sh "scp -oStrictHostKeyChecking=no deployment-api.yaml ${k8s_host_remote}:/home/ubuntu/"
                    echo "ejecuto bien remoto"
                    sh "ssh ${k8s_host_remote} pwd"
                    sh "ssh ${k8s_host_remote} kubectl apply -f deployment-api.yaml"
                }
            }
        }
    }
}