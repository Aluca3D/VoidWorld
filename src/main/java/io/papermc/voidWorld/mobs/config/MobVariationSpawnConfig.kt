package io.papermc.voidWorld.mobs.config

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.voidWorld.helper.EDimension
import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration.Companion.parseItem
import io.papermc.voidWorld.mobs.helper.RMobEquipment
import io.papermc.voidWorld.mobs.helper.RMobVariation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import org.spongepowered.configurate.ConfigurationNode
import java.util.EnumMap
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.text.isNotBlank

class MobVariationSpawnConfig(
  private val plugin: JavaPlugin,
  private val root: ConfigurationNode,
) {
  private val variations: MutableMap<NamespacedKey, MutableList<RMobVariation>> = HashMap()
  private val keysByEntity: MutableMap<EntityType, MutableList<NamespacedKey>> = EnumMap(EntityType::class.java)

  fun loadConfig() {
    // plugin.logger.info("<##> Begin of VWMobVariationSpawnConfig <##>")

    if (root.empty()) {
      plugin.logger.warning("No $root found!")
      return
    }

    loop@ for ((key, mobNode) in root.childrenMap()) {
      val entityType =
        runCatching {
          EntityType.valueOf(key.toString().uppercase())
        }.getOrElse {
          plugin.logger.warning("Invalid entity type: $key")
          continue
        }

      // plugin.logger.info("Variation: $entityType")

      mobNode.childrenList().forEachIndexed { index, replacementNode ->

        val namespacedKey =
          NamespacedKey(
            plugin,
            "${entityType.name.lowercase()}-$index",
          )

        val list = variations.computeIfAbsent(namespacedKey) { mutableListOf() }
        keysByEntity.computeIfAbsent(entityType) { mutableListOf() }.add(namespacedKey)

        val variation = createVariation(replacementNode) ?: continue@loop

        list.add(variation)
        // plugin.logger.info(" -> $replacement ($min-$max) Key: $namespacedKey")
      }
    }

    // plugin.logger.info("<##> End of VWMobVariationSpawnConfig <##>")
  }

  private fun createVariation(node: ConfigurationNode): RMobVariation? {
    val replacementStr = node.node("replacement").string ?: return null
    val replacement = EntityType.valueOf(replacementStr.uppercase())

    val min = node.node("interval", "min").getInt(0)
    val max = node.node("interval", "max").getInt(min)

    val isBurning = node.node("isBurning").getBoolean(false)
    val isHitByLightning = node.node("isHitByLightning").getBoolean(false)

    val setBaby = node.node("setBaby").getBoolean(false)

    val useDimension = node.node("useDimension").getBoolean(false)
    val dimensionStr = node.node("inDimension").getString("OVERWORLD")
    val dimension = EDimension.fromString(dimensionStr)

    val standingOn =
      parseBlock(
        node.node("standingOn").getString("NONE"),
      )

    val hasEffect =
      parseEffect(
        node.node("hasEffect").getString("NONE"),
      )

    val nameString = node.node("name").getString("NONE")
    val name: Component? =
      if (nameString != null && nameString != "NONE" && nameString.isNotBlank()) {
        MiniMessage.miniMessage().deserialize(nameString)
      } else {
        null
      }

    val hasTags =
      node
        .node("hasTags")
        .childrenList()
        .mapNotNull { it.string }

    val giveTags =
      node
        .node("giveTags")
        .childrenList()
        .mapNotNull { it.string }

    val attributes = mutableMapOf<Attribute, Double>()

    for ((attrKey, attrNode) in node.node("attributes").childrenMap()) {
      val attribute =
        RegistryAccess
          .registryAccess()
          .getRegistry(RegistryKey.ATTRIBUTE)
          .get(NamespacedKey.minecraft(attrKey.toString().lowercase()))

      attribute?.let {
        attributes[it] = attrNode.getDouble(0.0)
      }
    }

    val equipmentNode = node.node("equipment")
    val equipment =
      RMobEquipment(
        parseItem(equipmentNode.node("mainhand")),
        parseItem(equipmentNode.node("offhand")),
        parseItem(equipmentNode.node("helmet")),
        parseItem(equipmentNode.node("chestplate")),
        parseItem(equipmentNode.node("leggings")),
        parseItem(equipmentNode.node("boots")),
      )

    val passenger = createPassenger(node.node("passenger"))

    return RMobVariation(
      replacement,
      passenger,
      min,
      max,
      setBaby,
      isBurning,
      isHitByLightning,
      standingOn,
      hasEffect,
      useDimension,
      dimension,
      name,
      attributes,
      hasTags,
      giveTags,
      equipment,
    )
  }

  private fun createPassenger(node: ConfigurationNode): RMobVariation? {
    val entityStr = node.node("entity").string ?: return null
    val entity = EntityType.valueOf(entityStr.uppercase())

    val setBaby = node.node("setBaby").getBoolean(false)

    val nameString = node.node("name").getString("NONE")
    val name: Component? =
      if (nameString != null && nameString != "NONE" && nameString.isNotBlank()) {
        MiniMessage.miniMessage().deserialize(nameString)
      } else {
        null
      }

    val giveTags =
      node
        .node("giveTags")
        .childrenList()
        .mapNotNull { it.string }

    val attributes = mutableMapOf<Attribute, Double>()

    for ((attrKey, attrNode) in node.node("attributes").childrenMap()) {
      val attribute =
        RegistryAccess
          .registryAccess()
          .getRegistry(RegistryKey.ATTRIBUTE)
          .get(NamespacedKey.minecraft(attrKey.toString().lowercase()))

      attribute?.let {
        attributes[it] = attrNode.getDouble(0.0)
      }
    }

    val equipmentNode = node.node("equipment")
    val equipment =
      RMobEquipment(
        parseItem(equipmentNode.node("mainhand")),
        parseItem(equipmentNode.node("offhand")),
        parseItem(equipmentNode.node("helmet")),
        parseItem(equipmentNode.node("chestplate")),
        parseItem(equipmentNode.node("leggings")),
        parseItem(equipmentNode.node("boots")),
      )

    val passenger = createPassenger(node.node("passenger"))

    return RMobVariation(
      entity,
      passenger,
      0,
      0,
      setBaby,
      null,
      null,
      null,
      null,
      false,
      null,
      name,
      attributes,
      null,
      giveTags,
      equipment,
    )
  }

  fun hasVariation(type: EntityType): Boolean = getAllVariations(type).isNotEmpty()

  fun getVariation(key: NamespacedKey): RMobVariation? = variations[key]?.firstOrNull()

  fun getReplacement(key: NamespacedKey): EntityType? {
    val list = variations[key].orEmpty()
    if (list.isEmpty()) return null
    return list[ThreadLocalRandom.current().nextInt(list.size)].replacement
  }

  fun getRandomInterval(key: NamespacedKey): Int {
    val list = variations[key].orEmpty()
    if (list.isEmpty()) return 0

    val variation = list[ThreadLocalRandom.current().nextInt(list.size)]
    return ThreadLocalRandom
      .current()
      .nextInt(variation.intervalMin, variation.intervalMax + 1)
  }

  fun getKeysForEntity(type: EntityType): List<NamespacedKey> = keysByEntity[type].orEmpty()

  private fun getAllVariations(type: EntityType): List<RMobVariation> =
    keysByEntity[type]
      ?.flatMap { variations[it].orEmpty() }
      .orEmpty()

  private fun parseBlock(blockName: String?): Material? =
    blockName
      ?.takeUnless { it == "NONE" }
      ?.let { runCatching { Material.valueOf(it.uppercase()) }.getOrNull() }

  private fun parseEffect(effectName: String?): PotionEffectType? =
    effectName
      ?.takeUnless { it.equals("NONE", true) }
      ?.let { Registry.MOB_EFFECT.get(NamespacedKey.minecraft(it.lowercase())) }
}
