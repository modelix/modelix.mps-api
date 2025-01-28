package org.modelix.mps.api

import jetbrains.mps.ide.MPSCoreComponents
import jetbrains.mps.project.ProjectManager
import org.jetbrains.mps.openapi.project.Project

open class ModelixMpsApiImpl223 : ModelixMpsApiImpl222() {
    override fun getMPSProjects(): List<Project> {
        val manager = MPSCoreComponents.getInstance()?.platform?.findComponent(ProjectManager::class.java) ?: return emptyList()
        return manager.openedProjects
    }
}
