/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.Manifest
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

import static org.junit.Assert.assertTrue

/**
 * Test the buildNumber
 */
class BuildNumberTest extends BaseTest {

    def 'No buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Zero buildNumber with all zeroes'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 0
                patchVersion 0
                buildNumber 0
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '1'
        def name = '0.0.0.0'
        assertTrue buildResult.output.contains('appVersion.versionCode is 0, defaulting to 1')
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'zero buildNumebr with 0.0.1'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 0
                patchVersion 1
                buildNumber 0
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '1'
        def name = '0.0.1.0'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'non zero buildNumber with 0.1.0'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 1
                patchVersion 0
                buildNumber 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '100'
        def name = '0.1.0.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'invalid buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 'q'
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('appVersion.buildNumber must be an integer')
    }

    def 'buildNumber is double digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 99
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3.99'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'buildNumber is triple digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 999
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3.999'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}