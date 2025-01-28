import com.intellij.ide.impl.OpenProjectTask
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.util.Disposer
import com.intellij.testFramework.TestApplicationManager
import com.intellij.testFramework.UsefulTestCase
import com.intellij.util.io.delete
import jetbrains.mps.ide.ThreadUtils
import jetbrains.mps.ide.project.ProjectHelper
import jetbrains.mps.project.MPSProject
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Based on org.jetbrains.uast.test.env.AbstractLargeProjectTest
 */
@Suppress("removal")
abstract class TestBase(val testDataName: String?) : UsefulTestCase() {
    init {
        // workaround for MPS 2023.3 failing to start in test mode
        System.setProperty("intellij.platform.load.app.info.from.resources", "true")
    }

    protected lateinit var project: Project

    override fun runInDispatchThread() = false

    override fun setUp() {
        super.setUp()
        TestApplicationManager.getInstance()
        project = openTestProject()
    }

    override fun tearDown() {
        super.tearDown()
    }

    private fun openTestProject(): Project {
        val projectDirParent = Path.of("build", "test-projects").toAbsolutePath()
        projectDirParent.toFile().mkdirs()
        val projectDir = Files.createTempDirectory(projectDirParent, "mps-project")
        projectDir.delete(recursively = true)
        projectDir.toFile().mkdirs()
        projectDir.toFile().deleteOnExit()
        val project = if (testDataName != null) {
            val sourceDir = File("testdata/$testDataName")
            sourceDir.copyRecursively(projectDir.toFile(), overwrite = true)
            ProjectManagerEx.getInstanceEx().openProject(projectDir, OpenProjectTask())!!
        } else {
            ProjectManagerEx.getInstanceEx().newProject(projectDir, OpenProjectTask())!!
        }

        disposeOnTearDownInEdt { runCatching { ProjectManager.getInstance().closeAndDispose(project) } }

        ApplicationManager.getApplication().invokeAndWait {
            // empty - openTestProject executed not in EDT, so, invokeAndWait just forces
            // processing of all events that were queued during project opening
        }

        return project
    }

    private fun disposeOnTearDownInEdt(runnable: Runnable) {
        Disposer.register(
            testRootDisposable,
            Disposable {
                ApplicationManager.getApplication().invokeAndWait(runnable)
            },
        )
    }

    protected val mpsProject: MPSProject get() {
        return checkNotNull(ProjectHelper.fromIdeaProject(project)) { "MPS project not loaded" }
    }

    protected fun <R> writeAction(body: () -> R): R {
        return mpsProject.modelAccess.computeWriteAction(body)
    }

    protected fun <R> writeActionOnEdt(body: () -> R): R {
        return onEdt { writeAction { body() } }
    }

    protected fun <R> onEdt(body: () -> R): R {
        var result: R? = null
        ThreadUtils.runInUIThreadAndWait {
            result = body()
        }
        return result as R
    }

    protected fun <R> readAction(body: () -> R): R {
        var result: R? = null
        mpsProject.modelAccess.runReadAction {
            result = body()
        }
        return result as R
    }
}
