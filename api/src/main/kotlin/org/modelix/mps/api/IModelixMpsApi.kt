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
    fun getRepository(): SRepository = ContextRepository.getRepository() ?: getProjectRepository() ?: getGlobalRepository()
    fun getGlobalRepository(): SRepository
    fun getProjectRepository(): SRepository?
    fun getRepository(project: Project): SRepository
    fun getRepository(project: com.intellij.openapi.project.Project): SRepository
    fun getRepository(editorContext: EditorContext): SRepository
    fun getRepository(awtComponent: Component): SRepository

    fun getMPSProjects(): List<Project>
    fun getMPSProject(): Project = getMPSProjects().first()
    fun getVirtualFolder(project: Project, module: SModule): String?
    fun getVirtualFolders(module: SModule): List<String> = getMPSProjects().mapNotNull {
        getVirtualFolder(it, module).takeIf { !it.isNullOrEmpty() }
    }
    fun getVirtualFolder(module: SModule): String? = getVirtualFolders(module).firstOrNull()
    fun setVirtualFolder(project: Project, module: SModule, folder: String?)
    fun setVirtualFolder(module: SModule, folder: String?)

    fun fixVersions(project: Project, module: SModule)

    fun setReference(node: SNode, link: SReferenceLink, target: SNodeReference)

    fun forceSave(model: SModel)

    fun getIdeaProject(project: Project): com.intellij.openapi.project.Project
    fun getMPSProject(project: com.intellij.openapi.project.Project): Project
    fun <R> runWithProject(project: Project, body: () -> R): R =
        ContextProject.runWith(project, body)
    fun <R> runWithProject(project: com.intellij.openapi.project.Project, body: () -> R): R = runWithProject(getMPSProject(project), body)
    fun <R> runWithRepository(repository: SRepository, body: () -> R): R = ContextRepository.runWith(repository, body)
}
