plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "modelix.mps-api"
include("api")
include("lib")

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
    243 to "2024.3",
)

for (majorVersion in mpsVersions.keys) {
    include("impl$majorVersion")
}
