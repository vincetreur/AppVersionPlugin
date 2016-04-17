/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import org.gradle.api.Project

class CommandLineHelper {
    private Project mProject
    
    def CommandLineHelper(Project project) {
        mProject = project
    }
    
    /**
     * Returns a string property with the given name on the project if it exists.
     * Returns the given attribute otherwise
     * @param propertyName The name of the property on the gradle project
     * @param defaultValue The attribute that is used as fallback
     * @return The value of the project property (if it exists) or the value of the given attribute
     */
    def fetchStringOption(String propertyName, def defaultValue) {
        if (mProject.hasProperty(propertyName)) {
            String var = mProject.getProperty(propertyName)
            if (var.startsWith('"')) {
                var = var.substring(1)
            }
            if (var.endsWith('"')) {
                var = var.take(var.length() - 1)
            }
            return var
        }
        return defaultValue
    }

    /**
     * Returns a boolean property with the given name on the project if it exists.
     * Returns the given attribute otherwise
     * @param propertyName The name of the property on the gradle project
     * @param defaultValue The attribute that is used as fallback
     * @return The value of the project property (if it exists) or the value of the given attribute
     */
    def fetchBooleanOption(String propertyName, def defaultValue) {
        if (mProject.hasProperty(propertyName)) {
            String var = mProject.getProperty(propertyName)
            return Boolean.parseBoolean(var)
        }
        return defaultValue
    }

    /**
     * Returns an integer property with the given name on the project if it exists.
     * Returns the given attribute otherwise
     * @param propertyName The name of the property on the gradle project
     * @param defaultValue The attribute that is used as fallback
     * @return The value of the project property (if it exists) or the value of the given attribute
     */
    def fetchIntegerOption(String propertyName, def defaultValue) {
        if (mProject.hasProperty(propertyName)) {
            String var = mProject.getProperty(propertyName)
            try {
                return Integer.parseInt(var)
            } catch (NumberFormatException ignored) {
                mProject.logger.warn "${AndroidAppVersionConst.TAG} Command line property ${propertyName} ('${var}') could not be converted to an integer, using ${defaultValue} from your gradle script instead"
                return defaultValue
            }
        }
        return defaultValue
    }

}
