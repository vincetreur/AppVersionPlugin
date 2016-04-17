/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.Manifest
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult
/**
 * Test the majorVersion, minorVersion and patchVersion fields specified from the command line
 */
class CmdLineBuildNumberTest extends BaseTest {

    def 'Override buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 4
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.buildNumber=9")

        then:
        def code = '10203'
        def name = '1.2.3.9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Specify buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.buildNumber=9")

        then:
        def code = '10203'
        def name = '1.2.3.9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}