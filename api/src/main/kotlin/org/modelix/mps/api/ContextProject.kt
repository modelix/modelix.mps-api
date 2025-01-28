package org.modelix.mps.api

import org.jetbrains.mps.openapi.project.Project

object ContextProject {
    private val threadLocal = ThreadLocal<Project>()

    fun <R> runWith(project: Project, body: () -> R): R {
        val oldValue = threadLocal.get()
        try {
            threadLocal.set(project)
            return body()
        } finally {
            threadLocal.set(oldValue)
        }
    }

    fun getProject(): Project? = threadLocal.get()
}
