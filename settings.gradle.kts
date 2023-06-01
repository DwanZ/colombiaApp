pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.name = "colombia"
include(":app")
include(":domain")
include(":data")
include(":common")
