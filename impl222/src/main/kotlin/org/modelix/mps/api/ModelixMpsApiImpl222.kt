package org.modelix.mps.api

import jetbrains.mps.project.MPSProject
import org.jetbrains.mps.openapi.module.SModule
import org.jetbrains.mps.openapi.project.Project

open class ModelixMpsApiImpl222 : ModelixMpsApiImpl213() {
    override fun getVirtualFolder(
        project: Project,
        module: SModule,
    ): String? {
        return (project as MPSProject).getVirtualFolder(module).takeIf { it.isNotEmpty() }
    }
}
