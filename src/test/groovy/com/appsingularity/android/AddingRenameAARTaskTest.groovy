/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.AARBuildFile
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

class AddingRenameAARTaskTest extends BaseTest {

    def 'is task added for a simple library project'() {
        given:
        buildFile << AARBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameDebugAAR')
        TestRun.checkTaskAdded(buildResult, 'renameReleaseAAR')
    }

}