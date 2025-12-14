package io.papermc.voidWorld

import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader
import java.io.File
import java.io.IOException
import java.nio.file.Files

object VWConfigLoader {
    @JvmStatic
    fun loadConfig(plugin: JavaPlugin, fileName: String): ConfigurationNode {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                plugin.logger.warning("Failed to create plugin data folder: ${dataFolder.path}")
            }
        }

        val configFile = File(dataFolder, fileName)

        if (!configFile.exists()) {
            try {
                plugin.getResource(fileName).use { `in` ->
                    if (`in` != null) {
                        Files.copy(`in`, configFile.toPath())
                    } else {
                        if (!configFile.createNewFile()) {
                            plugin.logger.warning("Failed to create config file: $fileName")
                        }
                    }
                }
            } catch (e: IOException) {
                plugin.logger.warning("Unable to create config file $fileName: ${e.message}")
            }
        }

        val loader = JacksonConfigurationLoader.builder()
            .file(configFile)
            .build()

        try {
            return loader.load()
        } catch (e: IOException) {
            plugin.logger.warning("Unable to create config file $fileName: ${e.message}")
            return loader.createNode()
        }
    }
}
