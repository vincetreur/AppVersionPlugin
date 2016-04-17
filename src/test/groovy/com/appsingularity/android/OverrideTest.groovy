/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult
/**
 * Test overriding the versionCode and name
 */
class OverrideTest extends BaseTest {


    def 'override version code'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }
        /, 'versionCode 4')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3'
        def root = temporaryFolder.root.getName()
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsKept(buildResult)
        TestRun.checkVersionNameIsNotKept(buildResult, name)
        Manifest.checkDebugManifest(temporaryFolder, '4', name)
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, name)
    }

    def 'override version name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
        /, 'versionName "name"')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3'
        def root = temporaryFolder.root.getName()
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsKept(buildResult)
        Manifest.checkDebugManifest(temporaryFolder, code, 'name')
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, 'name')
    }

    def 'override version code and name'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }
        /, '''
            versionCode 4
            versionName "name"
        ''')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3'
        def root = temporaryFolder.root.getName()
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsKept(buildResult)
        TestRun.checkVersionNameIsKept(buildResult)
        Manifest.checkDebugManifest(temporaryFolder, '4', 'name')
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, 'name')
    }


    def 'override version name with snapshot'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot true
            }
        /, 'versionName "name"')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = '1.2.3-SNAPSHOT'
        def root = temporaryFolder.root.getName()
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsKept(buildResult)
        Manifest.checkDebugManifest(temporaryFolder, code, 'name')
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, 'name')
    }

    def 'override version name with prefix'() {
        given:
        buildFile << APKBuildFile.populate('''
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                customVersionName 'prefix-${version}'
            }
        ''', 'versionName "name"')

        when:
        BuildResult buildResult = TestRun.assembleDebug(pluginClasspath, projectDir)

        then:
        def code = '10203'
        def name = 'prefix-1.2.3'
        def root = temporaryFolder.root.getName()
        TestRun.checkResult(buildResult, code, name)
        TestRun.checkVersionCodeIsNotKept(buildResult, code)
        TestRun.checkVersionNameIsKept(buildResult)
        Manifest.checkDebugManifest(temporaryFolder, code, 'name')
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, 'name')
    }

}