/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android
import com.appsingularity.android.internal.*
import org.gradle.testkit.runner.BuildResult

class AddingRenameAPKTaskTest extends BaseTest {

    def 'is task added for a simple application project'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameReleaseAPK')
    }

    def 'is task added for a project with build types'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/, '', /
                buildTypes {
                    beta
                }
            /)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameReleaseAPK')
    }

    def 'is task added for a project with product flavors'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/, '', /
               productFlavors {
                    free { }
                    paid { }
                }
            /)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renamePaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renamePaidReleaseAPK')
    }

    def 'is task added for a project with product flavors and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/, '', /
                buildTypes {
                    beta
                }
                productFlavors {
                    free { }
                    paid { }
                }
            /)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renamePaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameFreeBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renamePaidBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renamePaidReleaseAPK')
    }

    def 'is task added for a project with product flavor groups'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/, '', /
                flavorDimensions "abi", "type"
                productFlavors {
                    free { dimension 'type' }
                    paid { dimension 'type' }
                    arm { dimension 'abi' }
                    x86 { dimension 'abi' }
                }
            /)

        when:
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidReleaseAPK')
    }

    def 'is task added for a project with product flavor groups and build types'() {
        given:
        buildFile << APKBuildFile.populate(/
            appVersion {
                majorVersion 1
                minorVersion 2
                patchVersion 3
            }/, '', /
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
        BuildResult buildResult = TestRun.build(pluginClasspath, projectDir)

        then:
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidDebugAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidBetaAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameArmFreeReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidReleaseAPK')
        TestRun.checkTaskAdded(buildResult, 'renameX86PaidReleaseAPK')
    }

}