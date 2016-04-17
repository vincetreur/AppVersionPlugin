/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

class NotRenamingAPKTest extends BaseTest {

    def 'apk is not versioned for a simple project'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
            }/)

        when:
        BuildResult buildResult = TestRun.assemble(pluginClasspath, projectDir)

        then:
        def root = temporaryFolder.root.getName()
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'apk is not versioned for a project with build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
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
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), root, "1.2.3")
    }

    def 'apk is not versioned for a project with product flavors'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
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
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
    }

    def 'apk is not versioned for a project with product flavors and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
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
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-paid", "1.2.3")
    }

    def 'apk is not versioned for a project with product flavor groups'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
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
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
    }

    def 'apk is not versioned for a project with product flavor groups and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
        appVersion {
            majorVersion 1
            minorVersion 2
            patchVersion 3
            renameAPK false
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
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkDebugAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkBetaAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-arm-paid", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-free", "1.2.3")
        TestRun.checkReleaseAPKIsNotRenamed(buildResult, projectDir.getAbsolutePath(), "${root}-x86-paid", "1.2.3")
    }

}