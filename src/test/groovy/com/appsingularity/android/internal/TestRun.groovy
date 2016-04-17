/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

import static junit.framework.TestCase.assertFalse
import static org.junit.Assert.assertTrue

public class TestRun {

    public static BuildResult build(List<File> pluginClasspath, File projectDir) {
        GradleRunner.create()
                .withPluginClasspath(pluginClasspath)
                .withProjectDir(projectDir)
                .withArguments("-i")
                .build()
    }

    public static BuildResult buildAndFail(List<File> pluginClasspath, File projectDir) {
        GradleRunner.create()
                .withPluginClasspath(pluginClasspath)
                .withProjectDir(projectDir)
                .withArguments("-i")
                .buildAndFail()
    }

    public static BuildResult assembleDebug(List<File> pluginClasspath, File projectDir) {
        GradleRunner.create()
                .withPluginClasspath(pluginClasspath)
                .withProjectDir(projectDir)
                .withArguments("assembleDebug", "-i")
                .build()
    }

    public static BuildResult build(List<File> pluginClasspath, File projectDir, String... args) {
        List<String> argList = new ArrayList<>()
        argList.add("-i")
        argList.addAll(Arrays.asList(args))
        GradleRunner.create()
                .withPluginClasspath(pluginClasspath)
                .withProjectDir(projectDir)
                .withArguments(argList)
                .build()
    }

    public static BuildResult assemble(List<File> pluginClasspath, File projectDir) {
        GradleRunner.create()
                .withPluginClasspath(pluginClasspath)
                .withProjectDir(projectDir)
                .withArguments("assemble", "-i")
                .build()
    }


    public static void checkTaskAdded(BuildResult buildResult, String taskName) {
        assertTrue buildResult.output.contains("Task ':${taskName}' is attached to")
    }

    public static void checkResult(BuildResult buildResult, String versionCode, String versionName) {
        assertTrue buildResult.output.contains('Checking android plugin order')
        assertTrue buildResult.output.contains("Setting appVersion.versionCode to ${versionCode}")
        assertTrue buildResult.output.contains("Setting appVersion.versionName to ${versionName}")
    }

    public static void checkVersionCodeIsKept(BuildResult buildResult) {
        assertTrue buildResult.output.contains('android.defaultConfig.versionCode not set, it was already specified')
    }

    public static void checkVersionNameIsKept(BuildResult buildResult) {
        assertTrue buildResult.output.contains('android.defaultConfig.versionName not set, it was already specified')
    }

    public static void checkVersionCodeIsNotKept(BuildResult buildResult, String versionCode) {
        assertTrue buildResult.output.contains("android.defaultConfig.versionCode is set to ${versionCode}")
    }

    public static void checkVersionNameIsNotKept(BuildResult buildResult, String versionName) {
        assertTrue buildResult.output.contains("android.defaultConfig.versionName is set to ${versionName}")
    }


    public static void checkDebugAPKIsRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-debug.apk"
        def newName = "${appName}-debug-${versionName}.apk"
        checkAPKIsRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkBetaAPKIsRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-beta-unsigned.apk"
        def newName = "${appName}-beta-unsigned-${versionName}.apk"
        checkAPKIsRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkReleaseAPKIsRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-release-unsigned.apk"
        def newName = "${appName}-release-unsigned-${versionName}.apk"
        checkAPKIsRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkAPKIsRenamedInternal(BuildResult buildResult, String projectDir, String origName, String newName) {
        checkBinaryIsRenamedInternal(buildResult, projectDir, origName, newName, "apk")
    }

    public static void checkDebugAPKIsNotRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-debug.apk"
        def newName = "${appName}-debug-${versionName}.apk"
        checkAPKIsNotRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkBetaAPKIsNotRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-beta-unsigned.apk"
        def newName = "${appName}-beta-unsigned-${versionName}.apk"
        checkAPKIsNotRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkReleaseAPKIsNotRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-release-unsigned.apk"
        def newName = "${appName}-release-unsigned-${versionName}.apk"
        checkAPKIsNotRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkAPKIsNotRenamedInternal(BuildResult buildResult, String projectDir, String origName, String newName) {
        assertFalse buildResult.output.contains("Renaming ${origName} to ${newName}")
        File oldFile = new File("${projectDir}/build/outputs/apk/${origName}")
        File newFile = new File("${projectDir}/build/outputs/apk/${newName}")
        assertTrue oldFile.exists()
        assertFalse newFile.exists()
    }



    public static void checkDebugAARIsRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-debug.aar"
        def newName = "${appName}-debug-${versionName}.aar"
        checkAARIsRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkReleaseAARIsRenamed(BuildResult buildResult, String projectDir, String appName, String versionName) {
        def origName = "${appName}-release.aar"
        def newName = "${appName}-release-${versionName}.aar"
        checkAARIsRenamedInternal(buildResult, projectDir, origName, newName)
    }

    public static void checkAARIsRenamedInternal(BuildResult buildResult, String projectDir, String origName, String newName) {
        checkBinaryIsRenamedInternal(buildResult, projectDir, origName, newName, "aar")
    }

    public static void checkBinaryIsRenamedInternal(BuildResult buildResult, String projectDir, String origName, String newName, String folder) {
        assertTrue buildResult.output.contains("Renaming ${origName} to ${newName}")
        File oldFile = new File("${projectDir}/build/outputs/${folder}/${origName}")
        File newFile = new File("${projectDir}/build/outputs/${folder}/${newName}")
        assertTrue oldFile.exists()
        assertTrue newFile.exists()
        assertTrue oldFile.size() == newFile.size()
    }

}
