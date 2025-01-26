package org.modelix.mps.api

import jetbrains.mps.ide.MPSCoreComponents
import jetbrains.mps.project.Project
import jetbrains.mps.project.ProjectManager

open class ModelixMpsApiImpl243 : ModelixMpsApiImpl241() {
    override fun getMPSProjects(): List<Project> {
        val manager = MPSCoreComponents.getInstance()?.platform?.findComponent(ProjectManager::class.java) ?: return emptyList()
        return manager.openedProjects
    }
}