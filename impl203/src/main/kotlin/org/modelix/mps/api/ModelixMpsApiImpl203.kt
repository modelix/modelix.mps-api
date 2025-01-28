package org.modelix.mps.api

import com.intellij.openapi.wm.IdeFrame
import com.intellij.ui.ComponentUtil
import jetbrains.mps.ide.MPSCoreComponents
import jetbrains.mps.ide.project.ProjectHelper
import jetbrains.mps.lang.migration.runtime.base.VersionFixer
import jetbrains.mps.openapi.editor.EditorContext
import jetbrains.mps.project.MPSProject
import jetbrains.mps.project.ProjectManager
import jetbrains.mps.smodel.MPSModuleRepository
import jetbrains.mps.smodel.SReference
import org.jetbrains.mps.openapi.language.SReferenceLink
import org.jetbrains.mps.openapi.model.EditableSModel
import org.jetbrains.mps.openapi.model.SModel
import org.jetbrains.mps.openapi.model.SNode
import org.jetbrains.mps.openapi.model.SNodeReference
import org.jetbrains.mps.openapi.model.SaveOptions
import org.jetbrains.mps.openapi.module.SModule
import org.jetbrains.mps.openapi.module.SRepository
import org.jetbrains.mps.openapi.project.Project
import java.awt.Component

open class ModelixMpsApiImpl203 : IModelixMpsApi {
    override fun getMPSProjects(): List<Project> {
        return ProjectManager.getInstance().openedProjects
    }

    override fun getVirtualFolder(
        project: Project,
        module: SModule,
    ): String? {
        return (project as MPSProject).getPath(module)?.virtualFolder?.takeIf { it.isNotEmpty() }
    }

    override fun getGlobalRepository(): SRepository {
        return checkNotNull(MPSCoreComponents.getInstance()?.platform?.findComponent(MPSModuleRepository::class.java)) {
            "MPSModuleRepository not available"
        }
    }

    override fun getProjectRepository(): SRepository? {
        return getMPSProjects().map { it.repository }.firstOrNull()
    }

    override fun getRepository(project: Project): SRepository {
        return project.repository
    }

    override fun getRepository(project: com.intellij.openapi.project.Project): SRepository {
        return getRepository(checkNotNull(ProjectHelper.fromIdeaProject(project)) { "No MPS project found in $project" })
    }

    override fun getRepository(editorContext: EditorContext): SRepository {
        return editorContext.repository
    }

    override fun getRepository(awtComponent: Component): SRepository {
        val project = generateSequence(ComponentUtil.getWindow(awtComponent)) { it.owner }
            .filterIsInstance<IdeFrame>()
            .mapNotNull { it.project }
            .firstOrNull()
        requireNotNull(project) { "Not part of a project: $awtComponent" }
        return getRepository(project)
    }

    override fun fixVersions(project: Project, module: SModule) {
        VersionFixer(project as jetbrains.mps.project.Project, module, true).updateImportVersions()
    }

    override fun setReference(node: SNode, link: SReferenceLink, target: SNodeReference) {
        SReference.create(link, node, target, null)
    }

    override fun forceSave(model: SModel) {
        if (model !is EditableSModel) return
        model.save(SaveOptions.FORCE)
    }
}
