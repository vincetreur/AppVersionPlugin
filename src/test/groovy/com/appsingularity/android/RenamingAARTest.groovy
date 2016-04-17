/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.AARBuildFile
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

class RenamingAARTest extends BaseTest {

    def 'apk is renamed for a simple library'() {
        given:
        buildFile << AARBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAARIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAARIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

}