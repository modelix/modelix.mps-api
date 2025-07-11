package org.modelix.mps.api

import com.intellij.openapi.wm.IdeFrame
import com.intellij.ui.ComponentUtil
import com.intellij.ui.IconManager
import jetbrains.mps.ide.MPSCoreComponents
import jetbrains.mps.ide.project.ProjectHelper
import jetbrains.mps.lang.migration.runtime.base.VersionFixer
import jetbrains.mps.openapi.editor.EditorContext
import jetbrains.mps.project.ProjectBase
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
import javax.swing.Icon

open class ModelixMpsApiImpl203 : IModelixMpsApi {
    override fun getMPSProjects(): List<Project> {
        return (listOfNotNull(ContextProject.getProject()) + ProjectManager.getInstance().openedProjects).distinct()
    }

    override fun getVirtualFolder(
        project: Project,
        module: SModule,
    ): String? {
        return (project as ProjectBase).getPath(module)?.virtualFolder?.takeIf { it.isNotEmpty() }
    }

    override fun setVirtualFolder(project: Project, module: SModule, folder: String?) {
        (project as ProjectBase).setVirtualFolder(module, folder)
    }

    override fun setVirtualFolder(module: SModule, folder: String?) {
        getMPSProjects().filter { it.projectModules.contains(module) }.forEach { setVirtualFolder(it, module, folder) }
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
        return getRepository(getMPSProject(project))
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

    override fun getIdeaProject(project: Project): com.intellij.openapi.project.Project {
        return ProjectHelper.toIdeaProject(project as jetbrains.mps.project.Project)
    }

    override fun getMPSProject(project: com.intellij.openapi.project.Project): Project {
        return checkNotNull(ProjectHelper.fromIdeaProject(project)) {
            "No MPS project found in $project"
        }
    }

    override fun loadIcon(resourceName: String, contextClass: Class<*>): Icon {
        return IconManager.getInstance().getIcon(resourceName, contextClass)
    }
}
