@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    jacoco
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
allprojects {
    //plugins.apply("$project.rootDir/jacoco.gradle.kts")
    repositories {
        google()
        mavenCentral()
    }

    tasks.register<Copy>("installGitHook") {
        from(File(rootProject.rootDir, "scripts/pre-commit"))
        into(File(rootProject.rootDir, ".git/hooks/"))
        fileMode = 777
    }

    tasks.getByPath(":app:preBuild").dependsOn("installGitHook")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    debug.set(true)
    android.set(false)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    reporters {
        /*reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)*/
    }
    filter {
        exclude("**/style-violations.kt")
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}


