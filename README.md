# Identifo Android
Identifo Android is an open library that allows you instantly and easily integrate [Identify](https://github.com/MadAppGang/identifo) authentication framework.

## Structure
The library includes two modules:
1. Core module is responsible for basic logic, queries and tokens managing.
2. UI module includes basic authentication use cases which might help you build app quickly.

## Installation
To install library you should do two steps:
1. Specify the Jitpack repository in the `build.gradle` file of your root project.
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add dependencies for your `buiild.gradle` subproject file.
```
    def identifoVersion = "x.y.z"
    implementation "com.github.MadAppGang.identifo-android:core:${identifoVersion}"
    implementation "com.github.MadAppGang.identifo-android:ui:${identifoVersion}"
```