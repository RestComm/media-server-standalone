def runUnitTests() {
    sh "mvn clean install -Dmaven.test.failure.ignore=true"
}

def setVersions() {
	sh "mvn versions:set -DnewVersion=$MAJOR_VERSION_NUMBER-$BUILD_NUMBER -DprocessDependencies=false -DprocessParent=true -Dmaven.test.skip=true"
}

def setMediaBomVersion() {
    sh "mvn versions:set-property -Dproperty=restcomm.media.bom.version -DnewVersion=${env.MEDIA_BOM_VERSION}"
}

def buildMedia() {
    sh "mvn clean install -DskipTests=true "
}

def deployMediaCXS() {
	if (env.PUBLISH_TO_CXS_NEXUS == 'true') {
		sh "mvn clean install package deploy:deploy -Pattach-sources,generate-javadoc,maven-release -DskipTests=true -DskipNexusStagingDeployMojo=true -DaltDeploymentRepository=nexus::default::$CXS_NEXUS2_URL"
	} else {
	    sh 'echo skipping CXS deployment'
	}
}

def zipAndArchiveAssembly() {
    zip archive: true, dir: "./assembly/target/media-server-standalone-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}", zipFile: "media-server-standalone-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
}

def zipAndArchiveDocsPdf() {
    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/pdf", zipFile: "media-server-standalone-docs-pdf-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
}

def zipAndArchiveDocsHtml() {
    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/html-book", zipFile: "media-server-standalone-docs-html-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
}

def publishResults() {
    junit testResults: '**/target/surefire-reports/*.xml', testDataPublishers: [[$class: 'StabilityTestDataPublisher']]
    // checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml', unHealthy: ''
    junit '**/target/surefire-reports/*.xml'

    // step( [ $class: 'JacocoPublisher' ] )

    // if ((env.BRANCH_NAME == 'master') && (currentBuild.currentResult != 'SUCCESS') ) {
    //    slackSend "Build unstable - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
    // }

    // if (env.BRANCH_NAME ==~ /^PR-\d+$/) {
    //     //step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'FailingTestSuspectsRecipientProvider']])])
    //     /*if (currentBuild.currentResult != 'SUCCESS' ) { // Other values: SUCCESS, UNSTABLE, FAILURE
    //         setGitHubPullRequestStatus (context:'CI', message:'IT unstable', state:'FAILURE')
    //     } else {
    //        setGitHubPullRequestStatus (context:'CI', message:'IT passed', state:'SUCCESS')
    //     }*/
    // }
}

node("cxs-slave-master") {

   echo sh(returnStdout: true, script: 'env')

    configFileProvider(
        [configFile(fileId: '37cb206e-6498-4d8a-9b3d-379cd0ccd99b',  targetLocation: 'settings.xml')]) {
	    sh 'mkdir -p ~/.m2 && sed -i "s|@LOCAL_REPO_PATH@|$WORKSPACE/M2_REPO|g" $WORKSPACE/settings.xml && cp $WORKSPACE/settings.xml -f ~/.m2/settings.xml'
    }

   stage ('Checkout') {
    checkout scm
   }

   stage ('Versioning') {
    setVersions()
    setMediaBomVersion()
   }

   stage ('Build') {
    buildMedia()
   }

   stage ('Test') {
    runUnitTests()
   }

   stage('PublishResults') {
    publishResults()
   }

   stage ('Deploy') {
    deployMediaCXS()
   }

   stage ('Archive') {
    zipAndArchiveAssembly()
    zipAndArchiveDocsPdf()
    zipAndArchiveDocsHtml()
   }

}
