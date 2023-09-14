// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.android.library") version "8.1.1" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
//        classpath ("com.google.gms:google-services:4.3.10")
        classpath("com.github.dcendents:android-maven-plugin:1.0")
    }
}