pipeline {
    agent {
		docker {
			image 'mcr.microsoft.com/azure-cli:2.8.0'
			args "--user root --privileged"
        }
    }    

    stages {
        stage('deploy') {
            steps {
                sh 'printenv | sort'
				sh 'echo S41ccXCsaFi5k_.Z3H57Tt_e_uL4~3A~I6'

                sh 'az login --service-principal -u 6a174562-61ac-45e2-b6b9-334a797c00ce -p S41ccXCsaFi5k_.Z3H57Tt_e_uL4~3A~I6 -t cc0361ca-deca-4db3-b421-ee1ca53c7f00'
                sh 'az account set -s 783ed336-9aff-4c71-b6c0-8efb879033a5'
                sh 'az logout'
				
            }           
        }
    }
}


