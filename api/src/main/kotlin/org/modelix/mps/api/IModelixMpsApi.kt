package org.modelix.mps.api

import org.jetbrains.mps.openapi.module.SModule
import org.jetbrains.mps.openapi.project.Project

interface IModelixMpsApi {
    fun getMPSProjects(): List<Project>
    fun getVirtualFolder(project: Project, module: SModule): String?
    fun fixVersions(project: Project, module: SModule)

    fun getVirtualFolders(module: SModule): List<String> = getMPSProjects().mapNotNull {
        getVirtualFolder(it, module).takeIf { !it.isNullOrEmpty() }
    }
    fun getVirtualFolder(module: SModule): String? = getVirtualFolders(module).firstOrNull()
}
