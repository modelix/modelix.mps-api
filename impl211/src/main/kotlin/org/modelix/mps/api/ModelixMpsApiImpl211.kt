package org.modelix.mps.api

import jetbrains.mps.project.Project
import jetbrains.mps.smodel.ModuleDependencyVersions
import jetbrains.mps.smodel.language.LanguageRegistry
import org.jetbrains.mps.openapi.module.SModule

open class ModelixMpsApiImpl211 : ModelixMpsApiImpl203() {
    override fun fixVersions(project: Project, module: SModule) {
        ModuleDependencyVersions(
            LanguageRegistry.getInstance(project.repository),
            project.repository,
        ).update(module)
    }
}