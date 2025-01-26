# modelix.mps-api
Single jar library that works in a wide range of MPS versions

In Modelix we switched from the strategy of maintaining a separate branch for each MPS version
to a single release of our plugins that is compatible to a wide range of MPS versions.

This works as long as we only use APIs that didn't change between MPS versions.
Often it's possible to avoid unstable APIs,
but sometimes there is a single API that can't be avoided and would make the plugin incompatible with previous versions.

This library maintains these differences between MPS versions and provides a common API.
For each MPS version there is a different implementation and all of them are packaged into the same JAR.
At runtime the correct implementation is chosen based on the version number provided by 
`com.intellij.openapi.application.ApplicationInfo`.
