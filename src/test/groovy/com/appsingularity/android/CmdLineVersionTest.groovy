/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.Manifest
import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

import static junit.framework.TestCase.assertFalse
import static junit.framework.TestCase.assertTrue

/**
 * Test the majorVersion, minorVersion and patchVersion fields specified from the command line
 */
class CmdLineVersionTest extends BaseTest {

    def 'Override majorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.majorVersion=9")

        then:
        def code = '90203'
        def name = '9.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override minorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.minorVersion=9")

        then:
        def code = '10903'
        def name = '1.9.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override patchVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.patchVersion=9")

        then:
        def code = '10209'
        def name = '1.2.9'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override with missing majorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.majorVersion=")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.majorVersion ('') could not be converted to an integer, using 1 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.majorVersion must be an integer')
    }

    def 'Override with invalid majorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.majorVersion=x")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.majorVersion ('x') could not be converted to an integer, using 1 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.majorVersion must be an integer')
    }

    def 'Override with missing minorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.minorVersion=")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.minorVersion ('') could not be converted to an integer, using 2 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.minorVersion must be an integer')
    }

    def 'Override with invalid minorVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.minorVersion=x")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.minorVersion ('x') could not be converted to an integer, using 2 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.minorVersion must be an integer')
    }

    def 'Override with missing patchVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.patchVersion=")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.patchVersion ('') could not be converted to an integer, using 3 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.patchVersion must be an integer')
    }

    def 'Override with invalid patchVersion'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.patchVersion=x")

        then:
        assertTrue buildResult.output.contains("Command line property appVersion.patchVersion ('x') could not be converted to an integer, using 3 from your gradle script instead")
        assertFalse buildResult.output.contains('appVersion.patchVersion must be an integer')
    }



    def 'Override patch version with triple digits'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.patchVersion=999")

        then:
        def code = '10299'
        def name = '1.2.99'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override minor version with triple digits'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.minorVersion=999")

        then:
        def code = '19903'
        def name = '1.99.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

    def 'Override major version with triple digits'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
        }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir, "assemble", "-PappVersion.majorVersion=999")

        then:
        def code = '9990203'
        def name = '999.2.3'
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, code, name)
    }

}