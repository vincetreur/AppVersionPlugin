/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult
/**
 * Check if the customVersionName overrides the generated name
 */
class CustomVersionNameTest extends BaseTest {

    def 'custom version name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                customVersionName 'base-name'
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name with buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                buildNumber 4
                isSnapshot false
                customVersionName 'base-name'
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name closure'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                customVersionName { 'base-name' }
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name closure with buildNumber'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                buildNumber 4
                customVersionName { 'base-name' }
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name closure with a number'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                customVersionName { 444 }
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '444'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name with snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName 'base-name'
                isSnapshot true
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'custom version name with release candidate'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName 'base-name'
                releaseCandidate 4
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name-RC4'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name closure with snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName { 'base-name' }
                isSnapshot true
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'custom version name closure with release candidate'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName { 'base-name' }
                releaseCandidate 5
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name-RC5'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'custom version name closure with versionCode'() {
        given:
        buildFile << APKBuildFile.populate('''
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
                customVersionName {
                    "base-name-${appVersion.versionCode}"
                }
            }''')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'base-name-10203'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}