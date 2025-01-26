package org.modelix.mps.api

import jetbrains.mps.lang.migration.runtime.base.VersionFixer
import jetbrains.mps.project.MPSProject
import jetbrains.mps.project.Project
import jetbrains.mps.project.ProjectManager
import org.jetbrains.mps.openapi.module.SModule

open class ModelixMpsApiImpl203 : IModelixMpsApi {
    override fun getMPSProjects(): List<Project> {
        return ProjectManager.getInstance().openedProjects
    }

    override fun getVirtualFolder(
        project: Project,
        module: SModule
    ): String? {
        return (project as MPSProject).getPath(module)?.virtualFolder
    }

    override fun fixVersions(project: Project, module: SModule) {
        VersionFixer(project, module, true).updateImportVersions()
    }
}