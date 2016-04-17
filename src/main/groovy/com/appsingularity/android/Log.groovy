/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import org.gradle.api.logging.Logger

import static com.appsingularity.android.AndroidAppVersionConst.DEBUG
import static com.appsingularity.android.AndroidAppVersionConst.TAG

class Log {

    Log() {
        // This class should not be instantiated
    }

    public static void info(Logger logger, String msg) {
        String logMsg = "${TAG} ${msg}"
        if (DEBUG) {
            println logMsg
        }
        logger.info logMsg
    }

    public static void warn(Logger logger, String msg) {
        String logMsg = "${TAG} ${msg}"
        logger.warn logMsg
    }

    public static void error(Logger logger, String msg) {
        String logMsg = "${TAG} ${msg}"
        logger.error logMsg
    }

}