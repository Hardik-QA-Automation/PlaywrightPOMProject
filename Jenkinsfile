pipeline 
{
    agent any
    
    tools{
    	maven 'MAVEN_HOME'
        }

    stages 
    {
        stage('Build') {
            steps {
                dir('build-project') {
                    git 'https://github.com/jglick/simple-maven-project-with-tests.git'
                    sh "mvn -Dmaven.test.failure.ignore=true clean package"
                }
            }

            post {
                success {
                    junit 'build-project/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts artifacts: 'build-project/target/*.jar'
                }
            }
        }
        
        
        	
        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {

                    dir('PlaywrightPOMProject') {
                        git 'https://github.com/Hardik-QA-Automation/PlaywrightPOMProject'

                        sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_regressions.xml"
                    }
                }
            }
        }
        
        
        stage('Publish Extent Report'){
            steps{
                     publishHTML([allowMissing: false,
                                  alwaysLinkToLastBuild: false, 
                                  keepAll: true, 
                                  reportDir: 'PlaywrightPOMProject/build', 
                                  reportFiles: 'TestExecutionReport.html', 
                                  reportName: 'HTML Extent Report', 
                                  reportTitles: ''])
            }
        }
        
        
        
        
    }
}