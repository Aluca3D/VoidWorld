package io.papermc.voidWorld

import org.bukkit.configuration.file.YamlConfiguration

class YamlSettings(
  config: YamlConfiguration,
) {
  val hide = HideSettings(config)

  class HideSettings(
    config: YamlConfiguration,
  ) {
    val radius: Double = config.getDouble("hideTag.radius", 10.0).coerceAtLeast(1.0)
    val period: Long = config.getLong("hideTag.period", 40L).coerceAtLeast(1)
  }
}
