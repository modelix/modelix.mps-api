import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    repositories {
        maven { url = uri("https://artifacts.itemis.cloud/repository/maven-mps/") }
        mavenCentral()
        mavenLocal()
    }
}

// https://artifacts.itemis.cloud/service/rest/repository/browse/maven-mps/com/jetbrains/mps/
val mpsVersions = mapOf<Int, String>(
    203 to "2020.3.6",
    211 to "2021.1.4",
    212 to "2021.2.6",
    213 to "2021.3.5",
    222 to "2022.2.4",
    223 to "2022.3.3",
    232 to "2023.2.2",
    233 to "2023.3.2",
    241 to "2024.1.1",
    243 to "2024.3"
)

var previousMajorVersions_: List<Int> = emptyList()
for ((majorVersion, fullVersion) in mpsVersions) {
    val previousMajorVersions = previousMajorVersions_
    val mpsZip = rootProject.configurations.create("mpsZip_$majorVersion")
    rootProject.dependencies {
        mpsZip("com.jetbrains:mps:$fullVersion")
    }
    project("impl$majorVersion") {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        val mpsDir = layout.buildDirectory.dir("mps")
        if (!mpsDir.get().asFile.exists()) {
            sync {
                from(zipTree({ mpsZip.singleFile }))
                into(mpsDir)
            }
        }

        dependencies {
            "implementation"(project(":api"))
            for (previousMajorVersion in previousMajorVersions) {
                "implementation"(project(":impl$previousMajorVersion"))
            }
            "compileOnly"(
                fileTree(mpsDir).matching {
                    include("lib/*.jar")
                }
            )
        }

        plugins.withType<KotlinPlatformJvmPlugin> {
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(11)
            }
        }
    }
    previousMajorVersions_ += majorVersion
}

project(":lib") {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies {
        for (mpsMajorVersion in mpsVersions.keys) {
            "implementation"(project(":impl$mpsMajorVersion"))
        }
        "compileOnly"(
            fileTree(project.project(":impl${mpsVersions.keys.last()}").layout.buildDirectory.dir("mps")).matching {
                include("lib/*.jar")
            }
        )
    }
}

project(":api") {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies {
        "compileOnly"(
            fileTree(project.project(":impl${mpsVersions.keys.last()}").layout.buildDirectory.dir("mps")).matching {
                include("lib/*.jar")
            }
        )
    }
}
