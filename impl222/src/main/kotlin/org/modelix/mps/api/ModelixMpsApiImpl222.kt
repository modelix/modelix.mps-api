package org.modelix.mps.api

import com.intellij.ide.impl.ProjectUtil
import jetbrains.mps.project.MPSProject
import org.jetbrains.mps.openapi.module.SModule
import org.jetbrains.mps.openapi.module.SRepository
import org.jetbrains.mps.openapi.project.Project
import java.awt.Component

open class ModelixMpsApiImpl222 : ModelixMpsApiImpl213() {
    override fun getVirtualFolder(
        project: Project,
        module: SModule,
    ): String? {
        return (project as MPSProject).getVirtualFolder(module).takeIf { it.isNotEmpty() }
    }

    override fun getRepository(awtComponent: Component): SRepository {
        ProjectUtil.getProjectForComponent(awtComponent)
        val project = ProjectUtil.getProjectForComponent(awtComponent)
        requireNotNull(project) { "Not part of a project: $awtComponent" }
        return getRepository(project)
    }
}
