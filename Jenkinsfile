def readProperties()
{

	def properties_file_path = "${workspace}" + "@script/properties.yml"
	def property = readYaml file: properties_file_path
	env.APP_NAME = property.APP_NAME
        env.MS_NAME = property.MS_NAME
        env.BRANCH = property.BRANCH
        env.GIT_SOURCE_URL = property.GIT_SOURCE_URL
	env.GIT_CREDENTIALS = property.GIT_CREDENTIALS
        env.SONAR_HOST_URL = property.SONAR_HOST_URL
        env.CODE_QUALITY = property.CODE_QUALITY
        env.UNIT_TESTING = property.UNIT_TESTING
        env.CODE_COVERAGE = property.CODE_COVERAGE
        env.FUNCTIONAL_TESTING = property.FUNCTIONAL_TESTING
        env.SECURITY_TESTING = property.SECURITY_TESTING
	env.PERFORMANCE_TESTING = property.PERFORMANCE_TESTING
	env.TESTING = property.TESTING
	env.QA = property.QA
	env.PT = property.PT
	env.User = property.User
    env.DOCKER_REGISTRY = property.DOCKER_REGISTRY
	env.mailrecipient = property.mailrecipient
	
    
}



def FAILED_STAGE
podTemplate(cloud:'openshift',label: 'docker', nodeSelector:'kubernetes.io/hostname=devopenshift1-node01',
  containers: [
    containerTemplate(
      name: 'jnlp',
      image: 'manya97/jenkins_tryout',
      alwaysPullImage: true,
      args: '${computer.jnlpmac} ${computer.name}',
      ttyEnabled: true
    )],volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),hostPathVolume(hostPath: '/etc/docker/daemon.json', mountPath: '/etc/docker/daemon.json')] )
{
    
    podTemplate(cloud:'openshift',label: 'selenium',nodeSelector:'kubernetes.io/hostname=devopenshift1-node01',
  containers: [
     containerTemplate(
      name: 'jnlp',
      image: 'cloudbees/jnlp-slave-with-java-build-tools',
      alwaysPullImage: true,
      args: '${computer.jnlpmac} ${computer.name}'
    )])
{
node 
{
   def MAVEN_HOME = tool "Maven_HOME"
   def JAVA_HOME = tool "JAVA_HOME"
   env.PATH="${env.PATH}:${MAVEN_HOME}/bin:${JAVA_HOME}/bin"
   try{
   stage('Checkout')
   {
       FAILED_STAGE=env.STAGE_NAME
       readProperties()
       checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '${GIT_CREDENTIALS}', url: "${GIT_SOURCE_URL}"]]])
       sh 'oc set image --local=true -f Orchestration/deployment.yaml ${MS_NAME}=${DOCKER_REGISTRY}/testingjenkinss-dev-apps/${MS_NAME}:testingjenkinss-dev-apps --dry-run -o yaml >> Orchestration/deployment-testingjenkinss-dev.yaml'
sh 'oc set image --local=true -f Orchestration/deployment.yaml ${MS_NAME}=${DOCKER_REGISTRY}/testingjenkinss-dev-apps/${MS_NAME}:testingjenkinss-test-01-apps --dry-run -o yaml >> Orchestration/deployment-testingjenkinss-test-01.yaml'

   }

   stage('Initial Setup')
   {   
       FAILED_STAGE=env.STAGE_NAME
       sh 'mvn clean compile'
   }
   if(env.UNIT_TESTING == 'True')
   {
   	stage('Unit Testing')
   	{
        	
        	FAILED_STAGE=env.STAGE_NAME
        	sh 'mvn test'
   	}
   }
   
   if(env.CODE_QUALITY == 'True')
   {
   	stage('Code Quality Analysis')
   	{
       		FAILED_STAGE=env.STAGE_NAME
       		sh 'mvn sonar:sonar -Dsonar.host.url="${SONAR_HOST_URL}"'
   	}
   }
   
   if(env.CODE_COVERAGE == 'True')
   {
   	stage('Code Coverage')
   	{
		FAILED_STAGE=env.STAGE_NAME
		sh 'mvn package'
   	}
   }
   
  if(env.SECURITY_TESTING == 'True')
  {
	stage('Security Testing')
	{
		FAILED_STAGE=env.STAGE_NAME
		sh 'mvn findbugs:findbugs'
	}	
  }
   
   stage('Build and Tag Image for Dev')
   {
	   FAILED_STAGE=env.STAGE_NAME
	  node('docker')
    {
        
        container('jnlp')
        {
         	sh 'git clone ${GIT_SOURCE_URL}'
            sh "docker build -t ${MS_NAME}:latest '/home/jenkins/workspace/${env.JOB_NAME}/${MS_NAME}'"
            sh 'docker tag ${MS_NAME}:latest ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-dev-apps'
            sh 'docker push ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-dev-apps'
        }
    }
	   
   }
   

   
   stage('Dev - Deploy Application')
   {   
       FAILED_STAGE=env.STAGE_NAME
       
       sh 'chmod 777 Orchestration/dev/deploy.sh'
       sh 'Orchestration/dev/deploy.sh'
       
       /*sh 'oc config use-context testingjenkinss-dev-apps'
       sh 'oc config view'
       sh 'oc apply -f Orchestration/deployment-testingjenkinss-dev.yaml -n=testingjenkinss-dev-apps'
       sh 'oc apply -f Orchestration/service.yaml -n=testingjenkinss-dev-apps' */
       
   }
	
   stage('testingjenkinss-test-01 - Tagging Image for Testing')
   {
   
   
        //openshiftTag(namespace: 'testingjenkinss-dev-apps', srcStream: '$MS_NAME', srcTag: 'latest', destStream: '$MS_NAME', destTag: 'testingjenkinss-test-01-apps')
    
      FAILED_STAGE=env.STAGE_NAME
      node('docker')
    {
        
        container('jnlp')
	 {
    	 sh 'docker pull ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-dev-apps'
     	 sh 'docker tag ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-dev-apps ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-test-01'
      	 sh 'docker push ${DOCKER_REGISTRY}/$APP_NAME-dev-apps/$MS_NAME:testingjenkinss-test-01'
	 }

     } 



      
   }

	stage('testingjenkinss-test-01 - Deploy Application')
	{
	
       FAILED_STAGE=env.STAGE_NAME
       sh 'chmod 777 Orchestration/test-01/deploy.sh'
       sh 'Orchestration/test-01/deploy.sh'
	}
	  
	node('selenium')
	{
	   
	stage('Integration Testing')
	{
	    FAILED_STAGE=env.STAGE_NAME
	    container('jnlp')
	    {
		 checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '${GIT_CREDENTIALS}', url: "${GIT_SOURCE_URL}"]]])
		 sh 'mvn integration-test'
	    }
	 }
	 }


   

   

   
   
   
   }
	catch(e){
		echo "Pipeline has failed"
		emailext body: "${env.BUILD_URL} has result ${currentBuild.result} at stage ${FAILED_STAGE} with error" + e.toString(), subject: "Failure of pipeline: ${currentBuild.fullDisplayName}", to: "${mailrecipient}"
	    throw e
	
	}
	     
}
}	
}
