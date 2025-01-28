import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.intellij2)
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    intellijPlatform {
        defaultRepositories()
        localPlatformArtifacts()
    }
}

intellijPlatform {
}

dependencies {
    intellijPlatform {
        if (Os.isOs(Os.FAMILY_MAC, null, "aarch64", null)) {
            local(System.getProperty("user.home") + "/Applications/MPS 2024.3.app/Contents")
        } else {
            local(project.project(":impl243").layout.buildDirectory.dir("mps").map { it.asFile.absolutePath }.get())
        }
        // intellijIdeaCommunity("2024.3")
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation(project(":lib"))
    testImplementation("junit:junit:4.13.2")
}
