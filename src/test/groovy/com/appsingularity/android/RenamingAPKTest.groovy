/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

class RenamingAPKTest extends BaseTest {

    def 'apk is renamed for a simple project'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
                isSnapshot false
            }/)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'apk is renamed for a project with build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            isSnapshot false
        }
        /, '', /
                buildTypes {
                    beta
                }
        /)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'apk is renamed for a project with product flavors'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            isSnapshot false
        }
        /, '', /
               productFlavors {
                    free { }
                    paid { }
                }
        /)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
    }

    def 'apk is renamed for a project with product flavors and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            isSnapshot false
        }
        /, '', /
                buildTypes {
                    beta
                }
                productFlavors {
                    free { }
                    paid { }
                }
        /)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
    }

    def 'apk is renamed for a project with product flavor groups'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            isSnapshot false
        }
        /, '', /
                flavorDimensions "abi", "type"
                productFlavors {
                    free { dimension 'type' }
                    paid { dimension 'type' }
                    arm { dimension 'abi' }
                    x86 { dimension 'abi' }
                }
        /)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
    }

    def 'apk is renamed for a project with product flavor groups and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            isSnapshot false
        }
        /, '', /
                buildTypes {
                    beta { }
                }
                flavorDimensions "abi", "type"
                productFlavors {
                    free { dimension 'type' }
                    paid { dimension 'type' }
                    arm { dimension 'abi' }
                    x86 { dimension 'abi' }
                }
        /)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkDebugAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkBetaAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkReleaseAPKIsRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
    }

}