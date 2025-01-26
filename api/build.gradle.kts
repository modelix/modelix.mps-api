plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {

}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)
}
