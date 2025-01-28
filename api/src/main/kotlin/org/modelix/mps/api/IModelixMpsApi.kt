package org.modelix.mps.api

import jetbrains.mps.openapi.editor.EditorContext
import org.jetbrains.mps.openapi.language.SReferenceLink
import org.jetbrains.mps.openapi.model.SModel
import org.jetbrains.mps.openapi.model.SNode
import org.jetbrains.mps.openapi.model.SNodeReference
import org.jetbrains.mps.openapi.module.SModule
import org.jetbrains.mps.openapi.module.SRepository
import org.jetbrains.mps.openapi.project.Project
import java.awt.Component

interface IModelixMpsApi {
    fun getRepository(): SRepository = getProjectRepository() ?: getGlobalRepository()
    fun getGlobalRepository(): SRepository
    fun getProjectRepository(): SRepository?
    fun getRepository(project: Project): SRepository
    fun getRepository(project: com.intellij.openapi.project.Project): SRepository
    fun getRepository(editorContext: EditorContext): SRepository
    fun getRepository(awtComponent: Component): SRepository

    fun getMPSProjects(): List<Project>
    fun getVirtualFolder(project: Project, module: SModule): String?
    fun getVirtualFolders(module: SModule): List<String> = getMPSProjects().mapNotNull {
        getVirtualFolder(it, module).takeIf { !it.isNullOrEmpty() }
    }
    fun getVirtualFolder(module: SModule): String? = getVirtualFolders(module).firstOrNull()

    fun fixVersions(project: Project, module: SModule)

    fun setReference(node: SNode, link: SReferenceLink, target: SNodeReference)

    fun forceSave(model: SModel)
}
