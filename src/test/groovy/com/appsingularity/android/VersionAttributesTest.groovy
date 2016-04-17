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
 * Test the majorVersion, minorVersion and patchVersion fields
 */
class VersionAttributesTest extends BaseTest {

    def 'build simple version'() {
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

    def 'build simple version with zeroes'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 0
                patchVersion 0
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '1'
        def name = '0.0.0'
        assertTrue buildResult.output.contains('appVersion.versionCode is 0, defaulting to 1')
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'build simple version with 0.0.1'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 0
                patchVersion 1
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '1'
        def name = '0.0.1'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'build simple version with 0.1.0'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 0
                minorVersion 1
                patchVersion 0
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '100'
        def name = '0.1.0'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'build simple version with 1.0.0'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 0
                patchVersion 0
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10000'
        def name = '1.0.0'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'missing appVersion closure'() {
        given:
        buildFile << APKBuildFile.populate(/
        /)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('No appVersion.majorVersion specified')
        assertTrue buildResult.output.contains('No appVersion.minorVersion specified')
        assertTrue buildResult.output.contains('No appVersion.patchVersion specified')
    }

    def 'missing all mandatory fields'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('No appVersion.majorVersion specified')
        assertTrue buildResult.output.contains('No appVersion.minorVersion specified')
        assertTrue buildResult.output.contains('No appVersion.patchVersion specified')
    }

    def 'missing majorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('No appVersion.majorVersion specified')
    }

    def 'missing minorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('No appVersion.minorVersion specified')
    }

    def 'missing patchVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('No appVersion.patchVersion specified')
    }


    def 'invalid majorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 'a'
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('appVersion.majorVersion must be an integer')
    }

    def 'invalid minorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 'Z'
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('appVersion.minorVersion must be an integer')
    }

    def 'invalid patchVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion "q"
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('appVersion.patchVersion must be an integer')
    }

    def 'patch version is double digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 99
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10299'
        def name = '1.2.99'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'minor version is double digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 99
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '19903'
        def name = '1.99.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'major version is double digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 99
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '990203'
        def name = '99.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }


    def 'patch version is triple digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 999
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10299'
        def name = '1.2.99'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'minor version is triple digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 999
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '19903'
        def name = '1.99.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'major version is triple digit'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 999
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '9990203'
        def name = '999.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}