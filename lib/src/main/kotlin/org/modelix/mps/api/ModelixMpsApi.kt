package org.modelix.mps.api

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.extensions.PluginId

private fun detectMpsVersion(): Int {
    PluginManagerCore.getPlugin(PluginId.getId("jetbrains.mps.core"))
        ?.version
        ?.substringBefore(".")
        ?.toInt()
        ?.let { return it }

    val info = ApplicationInfo.getInstance()
    return (info.majorVersion.toInt() - 2000) * 10 + info.minorVersionMainPart.toInt()
}

private fun resolveInstance(): IModelixMpsApi {
    val mpsVersion = detectMpsVersion()
    return when (mpsVersion) {
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
}

object ModelixMpsApi : IModelixMpsApi by resolveInstance()
