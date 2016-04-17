/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import groovy.text.SimpleTemplateEngine
import org.gradle.api.Project
import org.gradle.api.logging.Logger

import static com.appsingularity.android.AndroidAppVersionConst.*
import static com.appsingularity.android.Log.error
import static com.appsingularity.android.Log.warn
import static com.appsingularity.android.Log.info

class AndroidAppVersionExtension {

    def majorVersion
    def minorVersion
    def patchVersion
    def buildNumber

    /**
     * Adds '-SNAPSHOT' to the version name. Default is {@code false}
     */
    boolean isSnapshot = DEFAULT_IS_SNAPSHOT
    /**
     * Adds '-RCx' to the version name, where x is a number. This will disable isSnapshot
     */
    def releaseCandidate
    /**
     * Copy the apk and change the name to {@code versionCode}. Default is {@code true}
     */
    boolean renameAPK = DEFAULT_RENAME_APK

    /**
     * This is the custom version name, this replaces the generated versionCode in the generated versionName.
     */
    def customVersionName

    /**
     * The computed version code, read only value
     */
    Integer versionCode
    /**
     * The computed version name, read only value
     */
    String versionName
    /**
     * The computed raw version, read only value
     */
    def version

    /**
     * Returns the majorVersion integer
     * @return The majorVersion
     */
    def getLimitedMajorVersion() {
        majorVersion
    }

    /**
     * Returns the minorVersion integer, limited to 2 positions.
     * @return The minorVersion
     */
    def getLimitedMinorVersion() {
        limit(minorVersion)
    }

    /**
     * Returns the patchVersion integer, limited to 2 positions.
     * @return The patchVersion
     */
    def getLimitedPatchVersion() {
        limit(patchVersion)
    }


    /**
     * Overwrite settings from the gradle file with command line parameters.
     * @param project The gradle project to get the command line options from
     */
    protected void applyCommandLineOptions(Project project) {
        info(project.logger, "Applying command line options (if any)")
        CommandLineHelper cmdLine = new CommandLineHelper(project)
        majorVersion = cmdLine.fetchIntegerOption('appVersion.majorVersion', majorVersion)
        minorVersion = cmdLine.fetchIntegerOption('appVersion.minorVersion', minorVersion)
        patchVersion = cmdLine.fetchIntegerOption('appVersion.patchVersion', patchVersion)
        buildNumber = cmdLine.fetchIntegerOption('appVersion.buildNumber', buildNumber)
        isSnapshot = cmdLine.fetchBooleanOption('appVersion.isSnapshot', isSnapshot)
        releaseCandidate = cmdLine.fetchIntegerOption('appVersion.releaseCandidate', releaseCandidate)
        renameAPK = cmdLine.fetchBooleanOption('appVersion.renameAPK' ,renameAPK)
        customVersionName = cmdLine.fetchStringOption('appVersion.customVersionName', customVersionName)
    }

    /**
     * Check our attributes to see if they are present and valid.
     * @param logger A logger used to log errors
     * @return {@code true} if all checks are ok.
     */
    protected boolean hasValidValues(Logger logger) {
        info(logger, "Checking configured values")
        boolean isValid = true
        if (!isValidInteger(logger, majorVersion, "majorVersion") | !isValidInteger(logger, minorVersion, "minorVersion")
            | !isValidInteger(logger, patchVersion, "patchVersion") ) {
            isValid = false
        }
        if (buildNumber != null && !(buildNumber instanceof Integer)) {
            error(logger, "appVersion.buildNumber must be an integer")
            isValid = false
        }
        if (releaseCandidate != null && !(releaseCandidate instanceof Integer)) {
            error(logger, "appVersion.releaseCandidate must be an integer")
            isValid = false
        }

        warnForSettingReadOnlyAttributes(logger)

        return isValid
    }

    private void warnForSettingReadOnlyAttributes(Logger logger) {
        // Was versionCode set by the user?
        if (versionCode) {
            warn(logger, "You should not set appVersion.versionCode yourself")
        }
        // Was versionName set by the user?
        if (versionName) {
            warn(logger, "You should not set appVersion.versionName yourself")
        }
        // Was version set by the user?
        if (version) {
            warn(logger, "You should not set appVersion.version yourself")
        }
    }

    private static boolean isValidInteger(Logger logger, def attribute, String name) {
        if (attribute == null) {
            error(logger, "No appVersion.${name} specified")
            return false
        } else if (!(attribute instanceof Integer)) {
            error(logger, "appVersion.${name} must be an integer")
            return false
        }
        return true
    }

    /**
     * Compute the versionCode based on the major, minor and patch version.
     */
    protected void computeVersionCode(Logger logger) {
        info(logger, "Computing version code")
        versionCode = limitedMajorVersion * 10000 + limitedMinorVersion * 100 + limitedPatchVersion
        if (versionCode == 0) {
            versionCode = 1
            warn(logger, "appVersion.versionCode is 0, defaulting to 1")
        }
    }

    /**
     * Compute the version number including buildNUmber for variable substitution
     */
    private void computeVersion() {
        version = "${limitedMajorVersion}.${limitedMinorVersion}.${limitedPatchVersion}"
        if (buildNumber != null) {
            version += ".${buildNumber}"
        }
    }

    /**
     * Compute the versionName.
     */
    protected void computeVersionName(Logger logger) {
        info(logger, "Computing version name")
        computeVersion()
        // Disable snapshot if releaseCandidate is specified
        if (releaseCandidate != null) {
            isSnapshot = false;
        }
        // Set the versionName to "" so when it's referenced in a closure there is no 'null'
        versionName = ""
        StringBuilder builder = new StringBuilder()
        if (!append(builder, resolveClosure(customVersionName))) {
            builder.append("${version}")
        }
        if (releaseCandidate != null) {
            builder.append(RELEASE_CANDIDATE_PREFIX)
            builder.append(releaseCandidate)
        } else if (isSnapshot) {
            builder.append(SNAPSHOT)
        }
        versionName = substituteVars(builder.toString())
    }

    /**
     * Returns the string if given a string, a string if given a closure that returns a string, or null if both are not true.
     * References to attributes are replaced when specified in the string.
     * @param stringOrClosure A string or closure returning a string
     * @return The string value or the return value of the closure, or null if the parameter was neither
     */
    private static String resolveClosure(def stringOrClosure) {
        if (stringOrClosure instanceof String) {
            return stringOrClosure
        } else if (stringOrClosure instanceof Closure<String>) {
            return stringOrClosure.call()
        }
        return null
    }

    private String substituteVars(String input) {
        def substitutionBinding = [
                       'majorVersion': limitedMajorVersion,
                       'minorVersion': limitedMinorVersion,
                       'patchVersion': limitedPatchVersion,
                       'customVersionName': customVersionName,
                       'versionName': versionName,
                       'versionCode': versionCode,
                       'version': version
        ]
        return new SimpleTemplateEngine().createTemplate(input).make(substitutionBinding).toString()
    }

    /**
     * Append the {@code value} to the {@code builder} if the value is not null or empty
     * @param builder The builder to append to
     * @param value The value
     * @return {@code true} if value was appended, {@code false} otherwise
     */
    private static boolean append(StringBuilder builder, String value) {
        if (value != null && !value.isEmpty()) {
            builder.append(value)
            return true
        }
        return false
    }

    /**
     * Limit the given value to max 99.
     * @param i Any integer
     * @return The given integer or 99 whichever is lowest
     */
    private static def limit(def i) {
        if (i instanceof Integer) {
            return i % 100
        }
        return i
    }

}