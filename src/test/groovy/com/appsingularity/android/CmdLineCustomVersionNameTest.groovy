/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.Manifest
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

/**
 * Check if the customVersionName overrides the generated name when specified from the command line
 */
class CmdLineCustomVersionNameTest extends BaseTest {

    def 'Override custom version name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName 'base-name'
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", '-PappVersion.customVersionName=somename')

        then:
        def code = '10203'
        def name = 'somename'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override custom version name closure'() {
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
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", '-PappVersion.customVersionName=somename')

        then:
        def code = '10203'
        def name = 'somename'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'Specify custom version name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", '-PappVersion.customVersionName=somename')

        then:
        def code = '10203'
        def name = 'somename'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Specify empty custom version name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", '-PappVersion.customVersionName=')

        then:
        def code = '10203'
        def name = '1.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }



    def 'Override custom version name with snapshot'() {
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
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assembleDebug", '-PappVersion.customVersionName=somename')

        then:
        def code = '10203'
        def name = 'somename-SNAPSHOT'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}