package io.papermc.voidWorld.mobs.config

import io.papermc.voidWorld.helper.EDimension
import io.papermc.voidWorld.mobs.helper.RDropDefinition
import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import java.util.*

class MobLootDropConfig(plugin: JavaPlugin, root: ConfigurationNode) {

    private val lootTable: MutableMap<EntityType, List<RDropDefinition>> = EnumMap(EntityType::class.java)

    init {
        if (root.empty()) {
            plugin.logger.warning("No $root section found!")
        } else {
            loadConfig(plugin, root)
        }
    }

    private fun loadConfig(plugin: JavaPlugin, root: ConfigurationNode) {
        plugin.logger.info("<##> Begin of VWMobLootDropConfig <##>")

        for ((key, mobNode) in root.childrenMap()) {

            val mobName = key.toString()
            val type = runCatching {
                EntityType.valueOf(mobName.uppercase())
            }.getOrElse {
                plugin.logger.warning("Invalid mob: $mobName")
                continue
            }

            val drops = mobNode.node("drops")
                .childrenList()
                .mapNotNull { dropNode ->

                    val itemConfig = RItemStackConfiguration.parseItem(dropNode)
                        ?: run {
                            plugin.logger.warning("Item is null, skipping drop")
                            return@mapNotNull null
                        }

                    val min = dropNode.node("amount", "min").getInt(1)
                    val max = dropNode.node("amount", "max").getInt(1)
                    val chance = dropNode.node("chance").getDouble(1.0)

                    val useDimension = dropNode.node("useDimension").getBoolean(false)
                    val dimensionStr = dropNode.node("inDimension").getString("OVERWORLD")
                    val dimension = EDimension.fromString(dimensionStr)

                    val tags = dropNode.node("tags")
                        .childrenList()
                        .mapNotNull { it.string }

                    val lootingNode = dropNode.node("looting")
                    val lootingEnabled = lootingNode.node("enabled").getBoolean(false)
                    val extraChance = lootingNode.node("extra-chance-per-level").getDouble(0.0)
                    val extraAmount = lootingNode.node("extra-amount-per-level").getInt(0)

                    RDropDefinition(
                        itemConfig,
                        min, max, chance,
                        lootingEnabled,
                        extraChance, extraAmount,
                        useDimension, dimension,
                        tags
                    )
                }

            lootTable[type] = drops

            plugin.logger.info("Loaded ${drops.size} drops for $type")
            drops.forEach {
                plugin.logger.info(" -> Item:${it.itemStackConfiguration?.material}")
            }
        }

        plugin.logger.info("<##> End of VWMobLootDropConfig <##>")
    }

    fun getDrops(type: EntityType): List<RDropDefinition> =
        lootTable[type].orEmpty()
}
