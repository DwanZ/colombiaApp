
plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
