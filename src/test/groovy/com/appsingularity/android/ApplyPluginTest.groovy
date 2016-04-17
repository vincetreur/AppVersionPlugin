/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.TestRun
import org.gradle.testkit.runner.BuildResult

import static junit.framework.TestCase.assertFalse
import static org.junit.Assert.assertTrue

class ApplyPluginTest extends BaseTest {

    def 'apply before android application plugin'() {
        given:
        buildFile << /plugins {
                id 'android-app-version'
                id 'com.android.application'
            }
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
            android {
                buildToolsVersion '23.0.2'
                compileSdkVersion 23
                defaultConfig {
                }
            }
        /.stripIndent()

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertFalse buildResult.output.contains('Android application/library plugin is not applied')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android application plugin')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android library plugin')
    }

    def 'apply after android application plugin'() {
        given:
        buildFile << /plugins {
                id 'com.android.application'
                id 'android-app-version'
            }
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
            android {
                buildToolsVersion "23.0.2"
                compileSdkVersion 23
                defaultConfig {
                }
            }
        /.stripIndent()

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertFalse buildResult.output.contains('Android application/library plugin is not applied')
        assertTrue buildResult.output.contains('Plugin should be applied before the Android application plugin')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android library plugin')
    }

    def 'apply without android application plugin'() {
        given:
        buildFile << /plugins {
                id 'android-app-version'
            }
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
        /.stripIndent()

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertTrue buildResult.output.contains('Android application/library plugin is not applied')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android application plugin')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android library plugin')
    }

    def 'apply before android library plugin'() {
        given:
        buildFile << /plugins {
                id 'android-app-version'
                id 'com.android.library'
            }
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
            android {
                buildToolsVersion '23.0.2'
                compileSdkVersion 23
                defaultConfig {
                }
            }
        /.stripIndent()

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertFalse buildResult.output.contains('Android application/library plugin is not applied')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android application plugin')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android library plugin')
    }

    def 'apply after android library plugin'() {
        given:
        buildFile << /plugins {
                id 'com.android.library'
                id 'android-app-version'
            }
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
            android {
                buildToolsVersion '23.0.2'
                compileSdkVersion 23
                defaultConfig {
                }
            }
        /.stripIndent()

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertFalse buildResult.output.contains('Android application/library plugin is not applied')
        assertFalse buildResult.output.contains('Plugin should be applied before the Android application plugin')
        assertTrue buildResult.output.contains('Plugin should be applied before the Android library plugin')
    }

}