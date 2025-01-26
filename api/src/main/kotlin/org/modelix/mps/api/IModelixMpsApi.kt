package org.modelix.mps.api

import com.intellij.openapi.application.ApplicationInfo
import jetbrains.mps.project.Project
import org.jetbrains.mps.openapi.module.SModule

interface IModelixMpsApi {
    fun getMPSProjects(): List<Project>
    fun getVirtualFolder(project: Project, module: SModule): String?
    fun getVirtualFolders(module: SModule): List<String> = getMPSProjects().mapNotNull {
        getVirtualFolder(it, module).takeIf { !it.isNullOrEmpty() }
    }

    fun fixVersions(project: Project, module: SModule)
}
