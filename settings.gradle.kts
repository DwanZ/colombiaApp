pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "colombia"

include(":app")
include(":core:common")
include(":core:domain")
include(":core:data")
include(":feature:country")
include(":feature:attractions")
include(":feature:presidents")
include(":feature:map")
