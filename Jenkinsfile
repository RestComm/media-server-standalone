def runUnitTests() {
    sh "mvn clean install -Dmaven.test.failure.ignore=true"
}

def updateParentVersion() {
    if(env.UPDATE_PARENT == 'true') {
        sh "mvn versions:update-parent"
        echo 'Align parent to latest'
    } else {
        echo 'Using default parent version'
    }
}

def setVersions() {
    sh "mvn versions:set -DnewVersion=$MAJOR_VERSION_NUMBER-$BUILD_NUMBER -DprocessDependencies=false -DprocessParent=true -Dmaven.test.skip=true"
}

def buildMedia() {
    sh "mvn clean install -DskipTests=true "
}

def deployMediaCXS() {
    if(env.SNAPSHOT == 'true') {
        sh "mvn clean install package deploy:deploy -Pattach-sources,generate-javadoc,maven-release -DskipTests=true -DskipNexusStagingDeployMojo=true -DaltDeploymentRepository=nexus::default::$CXS_NEXUS_SNAPSHOTS_URL"
    } else {
        sh "mvn clean install package deploy:deploy -Pattach-sources,generate-javadoc,maven-release -DskipTests=true -DskipNexusStagingDeployMojo=true -DaltDeploymentRepository=nexus::default::$CXS_NEXUS2_URL"
    }
}

def zipAndArchiveAssembly() {
	if(env.SNAPSHOT == 'true') {
	    // TODO - what version to assume here ??? We don't pass any versions params here... or use some regexp
	    zip archive: true, dir: "./assembly/target/media-server-standalone-8.0.0-SNAPSHOT", zipFile: "media-server-standalone-8.0.0-SNAPSHOT.zip"
	} else {
	    zip archive: true, dir: "./assembly/target/media-server-standalone-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}", zipFile: "media-server-standalone-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
	}
}

def zipAndArchiveDocsPdf() {
	if(env.SNAPSHOT == 'true') {
	    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/pdf", zipFile: "media-server-standalone-docs-pdf-8.0.0-SNAPSHOT.zip"
	} else {
	    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/pdf", zipFile: "media-server-standalone-docs-pdf-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
	}
}

def zipAndArchiveDocsHtml() {
	if(env.SNAPSHOT == 'true') {
	    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/html-book", zipFile: "media-server-standalone-docs-html-8.0.0-SNAPSHOT.zip"
	} else {
	    zip archive: true, dir: "./docs/sources-asciidoc/target/generated-docs/html-book", zipFile: "media-server-standalone-docs-html-${env.MAJOR_VERSION_NUMBER}-${env.BUILD_NUMBER}.zip"
	}
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
        if(env.SNAPSHOT == 'false') {
	        echo '>>> Update versions'
	        updateParentVersion()
	        setVersions()
	    } else {
	        echo '>>> Using SNAPSHOT versions'
	    }
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
