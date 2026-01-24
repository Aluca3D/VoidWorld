package io.papermc.voidWorld

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader
import java.io.File
import java.io.IOException
import java.nio.file.Files

class ConfigLoader(
  private val plugin: JavaPlugin,
) {
  fun loadJson(fileName: String): ConfigurationNode {
    val configFile = createFile(fileName)

    val loader = JacksonConfigurationLoader
      .builder()
      .file(configFile)
      .build()

    return try {
      loader.load()
    } catch (e: IOException) {
      plugin.logger.warning("Unable to load config file $fileName: ${e.message}")
      loader.createNode()
    }
  }

  fun loadYamlConfig(): YamlConfiguration {
    val configFile = createFile("config/config.yml")
    return YamlConfiguration.loadConfiguration(configFile)
  }

  private fun createFile(fileName: String): File {
    val dataFolder = plugin.dataFolder
    if (!dataFolder.exists() && !dataFolder.mkdirs()) {
      plugin.logger.warning("Failed to create plugin data folder: ${dataFolder.path}")
    }

    val file = File(dataFolder, fileName)

    file.parentFile?.let {
      if (!it.exists() && !it.mkdirs()) {
        plugin.logger.warning("Failed to create config directories: ${it.path}")
      }
    }

    if (!file.exists()) {
      try {
        plugin.getResource(fileName).use { input ->
          if (input != null) {
            Files.copy(input, file.toPath())
          } else {
            file.createNewFile()
          }
        }
      } catch (e: IOException) {
        plugin.logger.warning("Unable to create config file $fileName: ${e.message}")
      }
    }

    return file
  }
}
