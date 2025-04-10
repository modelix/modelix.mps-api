package org.modelix.mps.api

import org.jetbrains.mps.openapi.module.SRepository

object ContextRepository {
    private val threadLocal = ThreadLocal<SRepository>()

    fun <R> runWith(repository: SRepository, body: () -> R): R {
        val oldValue = threadLocal.get()
        try {
            threadLocal.set(repository)
            return body()
        } finally {
            threadLocal.set(oldValue)
        }
    }

    fun getRepository(): SRepository? = threadLocal.get()
}
