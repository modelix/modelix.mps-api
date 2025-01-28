plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.binaryCompatibility)
    `maven-publish`
}

dependencies {
    api(project(":api"))
}

tasks.shadowJar {
    archiveClassifier = ""
}

java {
    withSourcesJar()
}

version = rootProject.version

publishing {
    repositories {
        if (project.hasProperty("artifacts.itemis.cloud.user")) {
            maven {
                name = "itemis"
                url = if (version.toString().contains("SNAPSHOT")) {
                    uri("https://artifacts.itemis.cloud/repository/maven-mps-snapshots/")
                } else {
                    uri("https://artifacts.itemis.cloud/repository/maven-mps-releases/")
                }
                credentials {
                    username = project.findProperty("artifacts.itemis.cloud.user").toString()
                    password = project.findProperty("artifacts.itemis.cloud.pw").toString()
                }
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            group = "org.modelix.mps"
            artifactId = "stable-api"
            from(components["shadow"])
        }
    }
}

tasks.named("generateMetadataFileForMavenPublication") {
    dependsOn(tasks.jar)
}
