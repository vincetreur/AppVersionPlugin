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
class CmdLineRCTest extends BaseTest {

    def 'Override rc'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                releaseCandidate 4
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.releaseCandidate=9")

        then:
        def code = '10203'
        def name = '1.2.3-RC9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Specify releaseCandidate'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.releaseCandidate=9")

        then:
        def code = '10203'
        def name = '1.2.3-RC9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'Specify releaseCandidate and override snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot true
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.releaseCandidate=9")

        then:
        def code = '10203'
        def name = '1.2.3-RC9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'Specify releaseCandidate and override implicit snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.releaseCandidate=9")

        then:
        def code = '10203'
        def name = '1.2.3-RC9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}