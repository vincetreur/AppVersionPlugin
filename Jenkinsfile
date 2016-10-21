stage('Prepare') {
	node {
		checkout scm
	}
}

stage('Build') {
	node {
	    gradle 'clean classes'
	}
}

stage('Test') {
	node {
	    try {
 		    gradle 'test'
	    } finally {
		    junit 'build/**/TEST*.xml'
	    }
	}
}

stage('Package') {
	node {
	    gradle 'jar'
	}
}

stage('Archive') {
	node {
		archive 'build/libs/*.jar'
	}
}

def gradle(def tasks) {
    sh "./gradlew ${tasks}"
}

def archive(def filename) {
	archiveArtifacts artifacts: "${filename}",
		caseSensitive: false, defaultExcludes: false, onlyIfSuccessful: true
}
