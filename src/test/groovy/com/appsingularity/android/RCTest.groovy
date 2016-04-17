/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

class RCTest extends BaseTest {

    def 'with default rc value'() {
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

    def 'with rc1 value'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                releaseCandidate 1
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC1'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'with rc2 value'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                releaseCandidate 2
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC2'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'with double digit rc value'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                releaseCandidate 78
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC78'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'with tripple digit rc value'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                releaseCandidate 789
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC789'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'with leading zero rc value'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                releaseCandidate 01
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC1'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'release candidate overrides snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot true
                releaseCandidate 1
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-RC1'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'release candidate follows build number'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 4
                releaseCandidate 1
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3.4-RC1'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'empty release candidate'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                releaseCandidate
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }
}