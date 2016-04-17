/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal

import org.junit.rules.TemporaryFolder

import static junit.framework.TestCase.assertTrue

public class Manifest {

    public static File create(TemporaryFolder temporaryFolder) {
        temporaryFolder.newFolder("src", "main")
        File manifest = temporaryFolder.newFile("src/main/AndroidManifest.xml")
        manifest << '<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="foo.bar" />'
        manifest
    }

    public static void checkDebugManifest(TemporaryFolder temporaryFolder, String versionCode,
                                          String versionName) {
        File f =  new File(temporaryFolder.getRoot(), "build/intermediates/manifests/full/debug/AndroidManifest.xml")
        assertTrue f.text.contains("android:versionCode=\"${versionCode}\"")
        assertTrue f.text.contains("android:versionName=\"${versionName}\"")
    }

}
