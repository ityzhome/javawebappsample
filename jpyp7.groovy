pipeline {
    agent any

    stages {
        stage('deploy') {
            steps {
                sh 'printenv | sort'
                sh 'export HTTP_PROXY=http://lmijache:Ar%21p%40Delt%402022@pac.eurocontrol.int:9512/'
                sh 'export HTTPS_PROXY=http://lmijache:Ar%21p%40Delt%402022@pac.eurocontrol.int:9512/'
                sh 'export FTP_PROXY=http://lmijache:Ar%21p%40Delt%402022@pac.eurocontrol.int:9512/'
                sh 'az login --service-principal -u 6a174562-61ac-45e2-b6b9-334a797c00ce -p S41ccXCsaFi5k_.Z3H57Tt_e_uL4~3A~I6 -t cc0361ca-deca-4db3-b421-ee1ca53c7f00'
                sh 'az account set -s $AZURE_SUBSCRIPTION_ID'
                sh 'az logout'
            }           
        }
    }
}


