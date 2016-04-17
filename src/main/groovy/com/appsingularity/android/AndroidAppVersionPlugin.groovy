/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger

import static com.appsingularity.android.Log.info
import static com.appsingularity.android.Log.warn
import static com.appsingularity.android.Log.error

class AndroidAppVersionPlugin implements Plugin<Project> {

    void apply(Project p) {
        p.extensions.create("appVersion", AndroidAppVersionExtension)
        // Check *after* the extension is created so gradle does not moan about appVersion() missing
        if (!checkPluginOrder(p)) {
            return
        }

        p.afterEvaluate { Project project, projectState ->
            Logger logger = project.logger
            AndroidAppVersionExtension extension = project.extensions.findByType(AndroidAppVersionExtension)
            if (extension == null) {
                // If we are somehow not applied, don't let this plugin crash
                // This can only happen if we return from apply(Project) *before* we create this plugins extension
                error(logger, "AppVersion extension could not be found")
                return
            }
            if (!isAndroidPluginApplied(project)) {
                return;
            }
            extension.applyCommandLineOptions(project)
            if (!extension.hasValidValues(logger)) {
                return
            }
            computeAndSetVersionValues(project, extension)
            createRenameTasks(project, extension)
        }
    }

    private static boolean isAndroidPluginApplied(Project project) {
        if (project.extensions.findByType(AppExtension) == null && project.extensions.findByType(LibraryExtension) == null) {
            error(project.logger, "Android application/library plugin is not applied")
            return false;
        }
        true;
    }

    private static void computeAndSetVersionValues(Project project, AndroidAppVersionExtension extension) {
        Logger logger = project.logger
        // Compute all the values
        extension.computeVersionCode(logger)
        info(logger, "Setting appVersion.versionCode to ${extension.versionCode}")
        extension.computeVersionName(logger)
        info(logger, "Setting appVersion.versionName to ${extension.versionName}")

        // Now set the versionCode/versionName on the android configuration
        setVersionCode(project, extension)
        setVersionName(project, extension)
    }

    private static void setVersionCode(Project project, AndroidAppVersionExtension extension) {
        Logger logger = project.logger
        def android = project.android
        // Do not overwrite if it is set already
        if (android.defaultConfig.versionCode == null) {
            android.defaultConfig.versionCode = extension.versionCode
            info(logger, "android.defaultConfig.versionCode is set to ${extension.versionCode}")
        } else {
            info(logger, "android.defaultConfig.versionCode not set, it was already specified")
            extension.versionCode = android.defaultConfig.versionCode
        }
    }

    private static void setVersionName(Project project, AndroidAppVersionExtension extension) {
        Logger logger = project.logger
        def android = project.android
        // Do not overwrite if it is set already
        if (android.defaultConfig.versionName == null) {
            android.defaultConfig.versionName = extension.versionName
            info(logger, "android.defaultConfig.versionName is set to ${extension.versionName}")
        } else {
            info(logger, "android.defaultConfig.versionName not set, it was already specified")
            extension.versionName = android.defaultConfig.versionName
        }
    }

    private void createRenameTasks(Project project, AndroidAppVersionExtension extension) {
        if (extension.renameAPK) {
            def android = project.android
            Logger logger = project.logger
            if (android.hasProperty('applicationVariants')) {
                info(logger, "Creating rename APK tasks")
                android.applicationVariants.all { ApplicationVariant variant ->
                    createRenameAPKTask(project, variant)
                }
            } else if (android.hasProperty('libraryVariants')) {
                info(logger, "Creating rename AAR tasks")
                android.libraryVariants.all { LibraryVariant variant ->
                    createRenameAARTask(project, variant, extension)
                }
            }
        }
    }

    /**
     * Check if the android application/library plugin is applied after we are.
     * @param project The gradle project
     * @return Return {@code true} if the android plugin was applied correctly, {@code false} otherwise.
     */
    private static boolean checkPluginOrder(Project project) {
        Logger logger = project.logger
        info(logger, "Checking android plugin order")
        if (project.extensions.findByType(AppExtension) != null) {
            error(logger, "Plugin should be applied before the Android application plugin")
            return false
        }
        if (project.extensions.findByType(LibraryExtension) != null) {
            error(logger, "Plugin should be applied before the Android library plugin")
            return false
        }
        return true
    }

    /**
     * Create the version (APK) task for the given variant.
     * @param project The gradle project.
     * @param variant The application variant that produces an APK.
     */
    private void createRenameAPKTask(Project project, ApplicationVariant variant) {
        Logger logger = project.logger
        def taskName = "rename${variant.name.capitalize()}APK"
        List outputs = variant.apkVariantData.outputs
        if (outputs.size() == 0) {
            warn(logger, "Variant ${variant.name} has no outputs")
            return
        }
        final def oldName = outputs[0].getMainOutputFile().getOutputFile().getName()
        final def newName = oldName.replace(".apk", "-${variant.versionName}.apk")
        final def outputDir = "${project.buildDir}/outputs/apk"
        Task task = project.tasks.create("${taskName}", {
            group 'build'
            description "Copy the ${variant.name} APK and add a version to the APK name"
            doLast {
                copyFile(getProject().logger, outputDir, oldName, newName)
            }
        })
        enableIncrementalBuilds(task, outputDir, oldName, newName)
        variant.getApkVariantData().assembleVariantTask.finalizedBy taskName
        info(logger, "Task ':${taskName}' is attached to ${variant.getApkVariantData().assembleVariantTask}")
    }

    private void createRenameAARTask(Project project, LibraryVariant variant, AndroidAppVersionExtension extension) {
        Logger logger = project.logger
        def taskName = "rename${variant.name.capitalize()}AAR"
        List outputs = variant.outputs
        if (outputs.size() == 0) {
            warn(logger, "Variant ${variant.name} has no outputs")
            return
        }
        final def oldName = outputs[0].getMainOutputFile().getOutputFile().getName()
        final def newName = oldName.replace(".aar", "-${extension.versionName}.aar")
        final def outputDir = "${project.buildDir}/outputs/aar"
        Task task = project.tasks.create("${taskName}", {
            group 'build'
            description "Copy the ${variant.name} AAR and add a version to the AAR name"
            doLast {
                copyFile(getProject().logger, outputDir, oldName, newName)
            }
        })
        enableIncrementalBuilds(task, outputDir, oldName, newName)
        variant.assemble.finalizedBy taskName
        info(logger, "Task ':${taskName}' is attached to ${variant.assemble}")
    }

    private static void enableIncrementalBuilds(Task task, outputDir, oldName, newName) {
        // Add task inputs/outputs for incremental builds
        task.inputs.file "${outputDir}/${oldName}"
        task.outputs.file "${outputDir}/${newName}"
    }

    private static void copyFile(Logger logger, outputDir, oldName, newName) {
        info(logger, "Renaming ${oldName} to ${newName}")
        File oldFile = new File("${outputDir}/${oldName}")
        File newFile = new File("${outputDir}/${newName}")
        newFile << oldFile.bytes
    }

}
