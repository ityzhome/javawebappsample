import groovy.json.JsonSlurper

def getAcrLoginServer(def acrSettingsJson) {
  def acrSettings = new JsonSlurper().parseText(acrSettingsJson)
  return acrSettings.loginServer
}

node {
    stage('init') {
      checkout scm
    }
  
    //stage('build') {
    //  sh 'mvn clean package'
    //}
  
    stage('deploy') {
	    def webAppResourceGroup = 'testnsv'
      def webAppName = 'testnsv'
      def acrName = 'testnsv'
      def imageName = 'monimage'
      // generate version, it's important to remove the trailing new line in git describe output
         
      
      def version = sh script: 'git describe | tr -d "\n"', returnStdout: true
      withCredentials([azureServicePrincipal('credentials_id')]{
        // login Azure
        echo "My client id is $AZURE_CLIENT_ID"
        echo "My client secret is $AZURE_CLIENT_SECRET"
        echo "My tenant id is $AZURE_TENANT_ID"
        echo "My subscription id is $AZURE_SUBSCRIPTION_ID"
		    sh ('az login --service-principal --username $AZURE_CLIENT_ID --password $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID')
        sh ('az account set -s $AZURE_SUBSCRIPTION_ID')
		
         // get login server
        def acrSettingsJson = sh script: "az acr show -n $acrName", returnStdout: true
        def loginServer = getAcrLoginServer acrSettingsJson
        // login docker
        // docker.withRegistry only supports credential ID, so use native docker command to login
        // you can also use docker.withRegistry if you add a credential
        sh "docker login $acrName.azurecr.io -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET $loginServer"
		
        // build image
        def imageWithTag = "$loginServer/$imageName:$version"
        def image = docker.build imageWithTag
        // push image
        image.push()
        // update web app docker settings
        sh "az webapp config container set -g $webAppResourceGroup -n $webAppName -c $imageWithTag -r http://$loginServer -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET" 
        // log out
        sh 'az logout'
        sh "docker logout $loginServer"
      }
    }
  }
}
