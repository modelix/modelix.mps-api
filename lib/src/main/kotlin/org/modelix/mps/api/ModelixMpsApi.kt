package org.modelix.mps.api

import com.intellij.openapi.application.ApplicationInfo

private fun detectMpsVersion(): Int {
    val info = ApplicationInfo.getInstance()
    return (info.majorVersion.toInt() - 2000) * 10 + info.minorVersionMainPart.toInt()
}

private fun resolveInstance(): IModelixMpsApi {
    val mpsVersion = detectMpsVersion()
    when (mpsVersion) {
        203 -> ModelixMpsApiImpl203()
        211 -> ModelixMpsApiImpl211()
        212 -> ModelixMpsApiImpl212()
        213 -> ModelixMpsApiImpl213()
        222 -> ModelixMpsApiImpl222()
        223 -> ModelixMpsApiImpl223()
        232 -> ModelixMpsApiImpl232()
        233 -> ModelixMpsApiImpl233()
        241 -> ModelixMpsApiImpl241()
        243 -> ModelixMpsApiImpl243()
        else -> throw UnsupportedOperationException("Unsupported MPS version: $mpsVersion")
    }
    return Class.forName("org.modelix.mps.api.ModelixMpsApiImpl${detectMpsVersion()}")
        .constructors.single().newInstance() as IModelixMpsApi
}

object ModelixMpsApi : IModelixMpsApi by resolveInstance()
