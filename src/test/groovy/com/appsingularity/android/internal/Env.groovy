/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal

import org.junit.rules.TemporaryFolder

import java.util.logging.Level
import java.util.logging.Logger

import static com.appsingularity.android.AndroidAppVersionConst.TAG

public class Env {

    public static void setupSDK(TemporaryFolder temporaryFolder) {
        def name = 'local.properties'
        File origProperties = new File(name)
        if (!origProperties.exists()) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Please add a 'local.properties' to the root of this project")
            return
        }
        File localProperties = temporaryFolder.newFile(name)
        localProperties << origProperties.text
    }

}
