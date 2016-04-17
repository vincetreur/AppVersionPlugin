/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

import static junit.framework.TestCase.assertTrue
/**
 * Test abusing versionCode and versionName
 */
class ReadOnlyVersionAttributesTest extends BaseTest {

    def 'try setting the versionCode'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3

                versionCode 123
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('You should not set appVersion.versionCode yourself')
    }

    def 'try setting the versionName'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3

                versionName '123'
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('You should not set appVersion.versionName yourself')
    }

    def 'try setting the version'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3

                version 123
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('You should not set appVersion.version yourself')
    }

}