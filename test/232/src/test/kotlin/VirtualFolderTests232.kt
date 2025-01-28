import org.modelix.mps.api.ModelixMpsApi

class VirtualFolderTests232 : TestBase("SimpleProject") {

    fun `test getVirtualFolder`() {
        val module = ModelixMpsApi.getMPSProjects().single().projectModules.single()
        val folder = ModelixMpsApi.getVirtualFolder(module)
        assertEquals("myFolder", folder)
    }
}
