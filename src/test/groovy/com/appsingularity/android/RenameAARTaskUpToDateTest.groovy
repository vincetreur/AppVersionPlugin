/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.AARBuildFile
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

import static junit.framework.TestCase.assertFalse
import static org.junit.Assert.assertTrue

class RenameAARTaskUpToDateTest extends BaseTest {

    def 'Rename aar task is up to date'() {
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
        assertTrue buildResult.output.contains(':renameDebugAAR')
        assertFalse buildResult.output.contains(':renameDebugAAR UP-TO-DATE')
        assertTrue buildResult.output.contains(':renameReleaseAAR')
        assertFalse buildResult.output.contains(':renameReleaseAAr UP-TO-DATE')

        when:
        buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains(':renameDebugAAR UP-TO-DATE')
        assertTrue buildResult.output.contains(':renameReleaseAAR UP-TO-DATE')

    }


}