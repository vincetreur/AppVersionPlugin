/*
 * Copyright (c) 2016 AppSingularity
 */

package com.appsingularity.android.internal
import com.android.annotations.NonNull

public class ClassPath {

    public static List<File> createForPlugins(@NonNull Object testClass) {
        def pluginClasspathResource = testClass.getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        pluginClasspathResource.readLines()
                .collect { it.replace('\\', '\\\\') } // escape backslashes in Windows paths
                .collect { new File(it) }
    }

}
