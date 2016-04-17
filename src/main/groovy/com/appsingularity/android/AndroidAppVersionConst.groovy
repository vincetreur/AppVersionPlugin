/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

public interface AndroidAppVersionConst {
    boolean DEBUG = false
    String SNAPSHOT = "-SNAPSHOT"
    String RELEASE_CANDIDATE_PREFIX = "-RC"
    String TAG = "[AndroidAppVersionPlugin]"
    String ANDROID_APP_PLUGIN_ID = 'com.android.application'
    String ANDROID_LIB_PLUGIN_ID = 'com.android.library'

    boolean DEFAULT_IS_SNAPSHOT = true
    boolean DEFAULT_RENAME_APK = true

}
