/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal

import org.junit.rules.TemporaryFolder


public class AARBuildFile {
    private static final String EMPTY_STRING = ""

    public static File create(TemporaryFolder temporaryFolder) {
        temporaryFolder.newFile('build.gradle')
    }


    public static String populate(String appVersion) {
        return populate(appVersion, EMPTY_STRING)
    }

    public static String populate(String appVersion, String defaultConfig) {
        return populate(appVersion, defaultConfig, EMPTY_STRING)
    }

    public static String populate(String appVersion, String defaultConfig, String extras) {
        '''
            plugins {
                id 'com.appsingularity.android-app-version'
                id 'com.android.library'
            }''' + appVersion + '''
            android {
                lintOptions.checkReleaseBuilds false
                buildToolsVersion "23.0.2"
                compileSdkVersion 23
                defaultConfig {
                    minSdkVersion 8
                    targetSdkVersion 23
                ''' + defaultConfig + ''' }
                ''' + extras + '''
            }
        '''.stripIndent()
    }

}
