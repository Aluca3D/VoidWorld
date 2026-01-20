package io.papermc.voidWorld

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.voidWorld.commands.SitCommand
import java.io.IOException
import java.net.URISyntaxException

@Suppress("UNUSED_PARAMETER")
class VoidWorldBootstrap : PluginBootstrap {
  override fun bootstrap(context: BootstrapContext) {
    context.lifecycleManager.registerEventHandler(
      LifecycleEvents.DATAPACK_DISCOVERY.newHandler { event ->
        try {
          val uri = requireNotNull(javaClass.getResource("/void_world_pack")).toURI()
          event.registrar().discoverPack(uri, "provided")
        } catch (e: URISyntaxException) {
          throw RuntimeException(e)
        } catch (e: IOException) {
          throw RuntimeException(e)
        }
      },
    )

    context.lifecycleManager.registerEventHandler(
      LifecycleEvents.COMMANDS.newHandler { commands ->
        commands.registrar().register(SitCommand().sitCommand())
      },
    )
  }
}
