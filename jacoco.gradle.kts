project.afterEvaluate {
    setupAndroidReporting()
}

fun setupAndroidReporting() {
    tasks.withType<Test> {
        jacoco.includeNoLocationClasses = true
        jacoco.excludes = listOf("jdk.internal.*")
    }

    val buildTypes = android.buildTypes.map { type ->
        type.name
    }
    val productFlavors = android.productFlavors.map { flavor ->
        flavor.name
    }
    if (productFlavors.isEmpty()) productFlavors.add("")
    productFlavors.forEach { productFlavorName ->
        buildTypes.forEach { buildTypeName ->
            val sourceName: String
            val sourcePath: String
            if (productFlavorName.isEmpty()) {
                sourceName = sourcePath = buildTypeName
            } else {
                sourceName = "${productFlavorName}${buildTypeName.capitalize()}"
                sourcePath = "${productFlavorName}/${buildTypeName}"
            }
            val testTaskName = "test${sourceName.capitalize()}UnitTest"
            println("Task -> $testTaskName")

            tasks.register<JacocoReport>("${testTaskName}Coverage") {
                group = "Reporting"
                description = "Generate Jacoco coverage reports on the ${sourceName.capitalize()} build."

                val fileFilter = listOf(
                    "android/databinding/**/*.class",
                    "**/android/databinding/*Binding.class",
                    "**/android/databinding/*",
                    "**/androidx/databinding/*",
                    "**/BR.*",
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "**/*MapperImpl*.*",
                    "**/*\$ViewInjector*.*",
                    "**/*\$ViewBinder*.*",
                    "**/BuildConfig.*",
                    "**/*Component*.*",
                    "**/*BR*.*",
                    "**/Manifest*.*",
                    "**/*\$Lambda$*.*",
                    "**/*Companion*.*",
                    "**/*Module*.*",
                    "**/*Dagger*.*",
                    "**/*Hilt*.*",
                    "**/*MembersInjector*.*",
                    "**/*_MembersInjector.class",
                    "**/*_Factory*.*",
                    "**/*_Provide*Factory*.*",
                    "**/*Extensions*.*",
                    "**/*\$Result.*",
                    "**/*\$Result$*.*",
                    "**/*JsonAdapter.*"
                )

                val javaTree = fileTree("${project.buildDir}/intermediates/javac/$sourceName/classes") {
                    exclude(fileFilter)
                }
                val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/$sourceName") {
                    exclude(fileFilter)
                }
                classDirectories.from(javaTree, kotlinTree)
                executionData.from(file("${project.buildDir}/jacoco/$testTaskName.exec"))
                val coverageSourceDirs = listOf(
                    "src/main/java",
                    "src/$productFlavorName/java",
                    "src/$buildTypeName/java"
                )

                sourceDirectories.setFrom(files(coverageSourceDirs))
                additionalSourceDirs.setFrom(files(coverageSourceDirs))

                reports {
                    csv.isEnabled = false
                    xml.isEnabled = false
                    html.isEnabled = true
                    html.destination = file("${buildDir}/coverage-report")
                }
            }

            tasks.named("preBuild") {
                dependsOn("${testTaskName}Coverage")
            }
        }
    }
}

android {
    buildTypes {
        named("debug") {
            isTestCoverageEnabled = true
        }
    }
}
