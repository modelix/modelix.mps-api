plugins {
    alias(libs.plugins.intellij)
    alias(libs.plugins.kotlin.jvm)
}

intellij {
    localPath = project(":impl222").layout.buildDirectory.dir("mps").map { it.asFile.absolutePath }
    instrumentCode = false
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
}

dependencies {
    testImplementation(project(":lib"))
}
