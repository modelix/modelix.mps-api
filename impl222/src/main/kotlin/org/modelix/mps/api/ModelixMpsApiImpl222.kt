package org.modelix.mps.api

import jetbrains.mps.project.MPSProject
import jetbrains.mps.project.Project
import org.jetbrains.mps.openapi.module.SModule

open class ModelixMpsApiImpl222 : ModelixMpsApiImpl213() {
    override fun getVirtualFolder(
        project: Project,
        module: SModule,
    ): String? {
        return (project as MPSProject).getVirtualFolder(module).takeIf { it.isNotEmpty() }
    }
}
