/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android

import com.appsingularity.android.internal.APKBuildFile
import com.appsingularity.android.internal.ClassPath
import com.appsingularity.android.internal.Env
import com.appsingularity.android.internal.Manifest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BaseTest extends Specification {
    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    File manifestFile
    File buildFile
    File projectDir
    List<File> pluginClasspath

    def setup() {
        projectDir = temporaryFolder.root
        buildFile = APKBuildFile.create(temporaryFolder)
        manifestFile = Manifest.create(temporaryFolder)
        Env.setupSDK(temporaryFolder)
        pluginClasspath = ClassPath.createForPlugins(this)
    }

}