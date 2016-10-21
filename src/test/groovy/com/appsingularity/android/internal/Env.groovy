/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal

import org.junit.rules.TemporaryFolder

public class Env {

    public static void setupSDK(TemporaryFolder temporaryFolder) {
        def name = 'local.properties'
        File localProperties = temporaryFolder.newFile(name)
        File origProperties = new File(name)
        def dir
        if (origProperties.exists()) {
            dir = origProperties.text
        } else {
            dir = "sdk.dir=${System.getenv().get('ANDROID_HOME')}"
        }
        if (dir == null) {
            throw new IllegalStateException("Did not find Android SDK location in local.properties or ANDROID_HOME.")
        }
        localProperties << dir
    }

}
