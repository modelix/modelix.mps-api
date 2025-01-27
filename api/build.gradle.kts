plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)
}
