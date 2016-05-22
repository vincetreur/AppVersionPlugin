node {
	ws {
		stage 'Prepare'
			checkout scm
			sh 'echo "sdk.dir="$ANDROID_HOME > local.properties'

		stage 'Build'
			gradle 'clean assemble'

		stage 'Test'
			gradle 'test'

		stage 'Publish'
			step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/*.xml'])

	}
}

def gradle(def tasks) {
    sh "./gradlew ${tasks}"
}
