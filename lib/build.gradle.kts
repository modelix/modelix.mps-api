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

publishing {
    publications {
        create<MavenPublication>("maven") {
            group = "org.modelix.mps"
            artifactId = "stable-api"
            version = "0.0.3"
            //artifact(tasks.shadowJar)
            artifact(project(":api").tasks.named("sourcesJar"))
            from(components["shadow"])
        }
    }
}
