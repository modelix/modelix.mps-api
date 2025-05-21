package org.modelix.mps.api

import com.intellij.ui.IconManager
import javax.swing.Icon

open class ModelixMpsApiImpl232 : ModelixMpsApiImpl223() {
    override fun loadIcon(resourceName: String, contextClass: Class<*>): Icon {
        return IconManager.getInstance().getIcon(resourceName, contextClass)
    }
}
