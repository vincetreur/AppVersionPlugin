/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

class CmdLineRenameAPKTest extends BaseTest {

    def 'Enable renameAPK from the command line'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.renameAPK=true")

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3-SNAPSHOT")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3-SNAPSHOT")
    }


    def 'Override renameAPK from the command line'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK true
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.renameAPK=false")

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'Disable renameAPK from the command line'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.renameAPK=false")

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'Specify illegal renameAPK from the command line'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK true
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.renameAPK=foo")

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

}