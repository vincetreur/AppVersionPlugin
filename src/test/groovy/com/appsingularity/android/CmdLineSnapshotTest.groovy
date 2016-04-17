/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.Manifest
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult
/**
 * Test the isSnapshot specified from the command line
 */
class CmdLineSnapshotTest extends BaseTest {

    def 'Specify snapshot with true'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", "-PappVersion.isSnapshot=true")

        then:
        def code = '10203'
        def name = '1.2.3-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'Specify snapshot with false'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", "-PappVersion.isSnapshot=false")

        then:
        def code = '10203'
        def name = '1.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override snapshot with true'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", "-PappVersion.isSnapshot=true")

        then:
        def code = '10203'
        def name = '1.2.3-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override snapshot with false'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot true
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", "-PappVersion.isSnapshot=false")

        then:
        def code = '10203'
        def name = '1.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}