/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

import static junit.framework.TestCase.assertFalse
import static org.junit.Assert.assertTrue

class RenameAPKTaskUpToDateTest extends BaseTest {

    def 'Rename apk task is up to date'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains(':renameDebugAPK')
        assertFalse buildResult.output.contains(':renameDebugAPK UP-TO-DATE')
        assertTrue buildResult.output.contains(':renameReleaseAPK')
        assertFalse buildResult.output.contains(':renameReleaseAPK UP-TO-DATE')

        when:
        buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains(':renameDebugAPK UP-TO-DATE')
        assertTrue buildResult.output.contains(':renameReleaseAPK UP-TO-DATE')

    }


}