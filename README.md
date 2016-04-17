Android App Version
============

Manage the version of your Android App, Library and APK's.

Nobody likes to calculate new version codes. We all have written gradle tasks to do this calculation for us and then copied those tasks every time we start a new project.

Now you can let the `versionCode` and `versionName` be generated for you and have your APK name match that version.

Just specify the basic version information in your build file and you are done.

This plugin works with Android applications and Android libraries, everything that is described here also works for library projects/AAR files.

##A simple example

The smallest example is the following.
```groovy
plugins {
   id 'com.appsingularity.android-app-version' version '1.0.0'
   id 'com.android.application'
}

appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   isSnapshot false
}

android {
   buildToolsVersion "23.0.2"
   compileSdkVersion 23
   defaultConfig {
      applicationId "foo.bar"
      minSdkVersion 8
      targetSdkVersion 23
   }
}
```

In this example a `versionCode` with the value `1.2.3` and a `versionName` with the value `"1.2.3"` will
be inserted into `android.defaultConfig` and your `AndroidManifest.xml`.

> If a `android.defaultConfig.versionCode` and/or `android.defaultConfig.versionName` is found then the plugin will use those values.

> Notice we didn't set the versionCode and versionName in the manifest?

Also your APK will have the version in the name.
In this example your debug APK will be called `app-debug-1.2.3.apk`.


>If you do specify the versionCode and versionName in your gradle build file, then the plugin will use those.

## Order matters

You *have* to apply this plugin before you apply the android application or library plugin!

##Build numbers

If you want to keep track of the different builds for the same version then you would use the `buildNumber` to differentiate between builds.

The next build file would be equal to specifying `versionCode 10203` and `versionName "1.2.3.4"`, your APK name will be `app-debug-1.2.3.4.APK`.
```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   buildNumber 4
   isSnapshot false
}
// ...
```

##Snapshots

If you want to have your versions flagged as snapshots, just add `isSnapshot true` to your build file. The version name and your APK will have `-SNAPSHOT` added to the name.

```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   isSnapshot true
}
// ...
```

> Snapshots are enabled by default.

##Release Candidates

If you want to have a version flagged as a release candidate, just add `releaseCandidate 1` to your build file. The version name and your APK will have `-RC1` added to the name.
This also works for 2, 3, 4, etc etc.

```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   releaseCandidate 4
}
// ...
```

> If you use `releaseCandidate` then `isSnapshot` will be ignored.

##Build types, product flavors and flavor dimensions

This plugin is aware of build types, product flavors and flavor dimensions. It will not mess up renaming your APK when you use those.

##Renaming the APK

This plugins creates a copy of your APK and adds the version name to it.

> Your APK will not be renamed but copied, so any existing gradle tasks (including yours) will keep working.
> Renaming is enabled by default.

If you want to disable this, add `renameAPK false` to your build file
```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   renameAPK false
}
// ...
```
> For libraries the renameAPK attribute is **not** renameAAR but still renameAPK.


##Changing the standard behavior

###Changing the version name

You have complete control over the format of the computed versionName. You can use `customVersionName` to replace the version name with any value you would like to use.
So you could use `customVersionName "awesome"` to end up with something like `app-debug-awesome.apk`.

```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   customVersionName 'awesome'
}
// ...
```
> Only the snapshot flag/release candidate will be added to the customVersionName.

More on this in the Advanced usage section.

###Keep using defaultConfig

Whenever the plugin finds the normal `versionCode` and/or `versionName` in `android.defaultConfig it` will use those values. By doing this you are effectively disabling the plugin, except for renaming the APK.

###Overriding the build file

All the options can also be specified and overridden via the command line, like `-PappVersion.isSnapshot=false`.

> You can use the Jenkins BUILD_NUMBER to create incremental build numbers with `-PappVersion.buildNumber=$BUILD_NUMBER`


##Advanced usage

###Closures

Sometimes just specifying a static value is not enough. Luckily you can also use closures.

Within a closure you can basically do anything you want as long a you return a string that the plugin can work with.

> Remember that performing long running tasks in a closure will slow down your build.

If you wrote a function that returns a GIT branch name then you could use it as follows.
```groovy
// ...
appVersion {
   majorVersion 1
   minorVersion 1
   patchVersion 3
   isSnapshot false
   customVersionName { '${version}-${functionThatReturnsBranchName()}' }
}
// ...
```

The version name will then be `1.2.3-develop`. The `${version}` will be explained in the Variable substitution section.

> Use single quotes or gradle will try to do the substitution before the plugin can.


###Variable substitution

Within closures you can use variables supplied by this plugin. See the All options section for an almost complete list.

One variable that is not mentioned there is `version`, this is a read only variable that holds the version name.
This name is computed based on the `majorVersion`, `minorVersion`, `patchVersion` and `buildNumber`.

##All options

All these options can be used via the command line (prefix with `appVersion.`) or in closures.

| Option| Description | Type | Closure? | Overridden by |
|-----|------|:----:|:--------:|:----:|
| `majorVersion` | The a part of an a.b.c version. | Numeric | No | `customVersionName`  |
| `minorVersion` | The b part of an a.b.c version. | Numeric | No | `customVersionName`  |
| `patchVersion` | The c part of an a.b.c version. | Numeric | No | `customVersionName`  |
| `buildNumber` | Adds the build number to the version name. | Numeric | No |`customVersionName` |
| `isSnapshot` | Adds `-SNAPSHOT`, defaults to true. | Boolean | No | None |
| `releaseCandidate` | Adds `-RCx`. | Numeric | No | None |
| `renameAPK` | Should the APK be renamed? Defaults to true.| Boolean | No | None |
| `customVersionName` | Replaces the a.b.c.d part of the version name.  | String | Yes | None |

> The basic version name structure is as follows.
`${majorVersion}.${minorVersion}.${patchVersion}.${buildNumber}-SNAPSHOT`
 or `${majorVersion}.${minorVersion}.${patchVersion}.${buildNumber}-RC${releaseCandidate}`
>
> Or `${customVersionName}-SNAPSHOT` / `${customVersionName}-RC${releaseCandidate}`


#Version history

1.0.0 - Initial release


#License

    Copyright 2016 Vince M Treur

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
