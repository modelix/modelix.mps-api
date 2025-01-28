package org.modelix.mps.api

import org.jetbrains.mps.openapi.model.EditableSModel
import org.jetbrains.mps.openapi.model.SModel
import org.jetbrains.mps.openapi.model.SaveOptions

open class ModelixMpsApiImpl212 : ModelixMpsApiImpl211() {
    override fun forceSave(model: SModel) {
        if (model !is EditableSModel) return
        model.save(SaveOptions.FORCE_SAVE)
    }
}
