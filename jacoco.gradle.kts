apply(plugin = "jacoco")
jacoco {
    toolVersion = "0.8.10"
}

project.afterEvaluate {
    if (android.hasProperty("applicationVariants")) {
        android.applicationVariants.all { variant ->
            createVariantCoverage(variant)
        }
    } else if (android.hasProperty("libraryVariants")) {
        android.libraryVariants.all { variant ->
            createVariantCoverage(variant)
        }
    }
}

val excludes = listOf(
    "**/databinding/*Binding.*",
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    // butterKnife
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/Lambda\$*.class",
    "**/Lambda.class",
    "**/*Lambda.class",
    "**/*Lambda*.class",
    "**/*_MembersInjector.class",
    "**/Dagger*Component*.*",
    "**/*Module_*Factory.class",
    "**/di/module/*",
    "**/*_Factory*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    // kotlin
    "**/*MapperImpl*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*\$Lambda\$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*"
)


fun createJacocoVariantCoverage(variant: BaseVariant) {
    val variantName = variant.name
    val testTaskName = "test${variantName.capitalize()}UnitTest"

    tasks.register<JacocoReport>("${testTaskName}Coverage") {
        group = "Reporting"
        description = "Generate Jacoco coverage reports for the ${variantName.capitalize()} build."

        reports {
            html.isEnabled = true
        }

        val javaClasses = variant.javaCompileProvider.get().destinationDirectory
        val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/${variantName}") {
            exclude(excludes)
        }

        classDirectories.setFrom(javaClasses, kotlinClasses)

        val sourceDirectories = files(
            "$projectDir/src/main/java",
            "$projectDir/src/${variantName}/java",
            "$projectDir/src/main/kotlin",
            "$projectDir/src/${variantName}/kotlin"
        )
        this.sourceDirectories.setFrom(sourceDirectories)

        val executionData = fileTree("${project.buildDir}/outputs/unit_test_code_coverage/${variantName}UnitTest") {
            include("**/*.exec")
        }
        this.executionData.setFrom(executionData)

        doLast {
            val coverageReportFile = file("${project.buildDir}/reports/jacoco/${testTaskName}Coverage/html/index.html")
            val coverage = coverageReportFile.readText().toRegex("""Total[^%]*>(\d?\d?\d?%)""").find()?.groupValues?.get(1)
            coverage?.let {
                println("Test coverage: $it")
            }
        }
    }

    tasks.register<JacocoCoverageVerification>("${testTaskName}CoverageVerification") {
        group = "Reporting"
        description = "Verifies Jacoco coverage for the ${variantName.capitalize()} build."

        violationRules {
            rule {
                limit {
                    minimum = 0
                }
            }
            rule {
                element = "BUNDLE"
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = 0.30
                }
            }
        }

        val javaClasses = variant.javaCompileProvider.get().destinationDirectory
        val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/${variantName}") {
            exclude(excludes)
        }

        classDirectories.setFrom(javaClasses, kotlinClasses)

        val sourceDirectories = files(
            "$projectDir/src/main/java",
            "$projectDir/src/${variantName}/java",
            "$projectDir/src/main/kotlin",
            "$projectDir/src/${variantName}/kotlin"
        )
        this.sourceDirectories.setFrom(sourceDirectories)

        val executionData = fileTree("${project.buildDir}/outputs/unit_test_code_coverage/${variantName}UnitTest") {
            include("**/*.exec")
        }
        this.executionData.setFrom(executionData)
    }
}

