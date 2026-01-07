package io.papermc.voidWorld.mobs.helper

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.spongepowered.configurate.ConfigurationNode
import java.util.*
import kotlin.math.ceil

@JvmRecord
data class RItemStackConfiguration(
    val material: Material,
    val damage: Int,
    val name: String?,
    val lore: MutableList<String?>?,
    val enchants: MutableMap<String?, Int?>?,
    val attributes: MutableMap<Attribute?, Double?>?
) {
    companion object {
        @JvmStatic
        fun parseItem(node: ConfigurationNode?): RItemStackConfiguration? {
            if (node == null || node.empty()) return null

            val materialStr = node.node("material").string ?: return null

            val material = Material.matchMaterial(materialStr.uppercase(Locale.getDefault())) ?: return null

            val name = node.node("name").string

            val damage = node.node("damage").getInt(0)

            val lore: MutableList<String?> = ArrayList<String?>()
            for (line in node.node("lore").childrenList()) {
                val string = line.string
                if (string != null) lore.add(string)
            }

            val enchants: MutableMap<String?, Int?> = HashMap<String?, Int?>()
            for (enchant in node.node("enchants").childrenMap().entries) {
                val enchantment = enchant.key.toString()
                val level = enchant.value.getInt(0)
                enchants[enchantment] = level
            }

            val attributes: MutableMap<Attribute?, Double?> = HashMap<Attribute?, Double?>()
            val attributesNode = node.node("attributes")

            for (entry in attributesNode.childrenMap().entries) {
                val key = entry.key.toString()
                val value = entry.value.double

                val attribute: Attribute? = RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.ATTRIBUTE)
                    .get(NamespacedKey.minecraft(key.lowercase(Locale.getDefault())))

                if (attribute != null) {
                    attributes[attribute] = value
                }
            }

            return RItemStackConfiguration(material, damage, name, lore, enchants, attributes)
        }

        fun build(itemStackConfiguration: RItemStackConfiguration?): ItemStack? {
            if (itemStackConfiguration == null) return null

            val material = itemStackConfiguration.material

            val item = ItemStack(material)

            val meta = item.itemMeta

            if (itemStackConfiguration.name != null) {
                meta.displayName(MiniMessage.miniMessage().deserialize(itemStackConfiguration.name))
            }

            if (itemStackConfiguration.lore != null) {
                meta.lore(
                    itemStackConfiguration.lore.stream()
                        .map { line: String? -> MiniMessage.miniMessage().deserialize(line!!) }
                        .toList()
                )
            }

            itemStackConfiguration.enchants?.forEach { (key: String?, level: Int?) ->
                val enchantment: Enchantment? = RegistryAccess.registryAccess().getRegistry(
                    RegistryKey.ENCHANTMENT
                )[NamespacedKey.minecraft(key!!.lowercase(Locale.getDefault()))]
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level!!, true)
                }
            }

            itemStackConfiguration.attributes?.forEach { (attribute: Attribute?, value: Double?) ->
                val modifier = AttributeModifier(
                    NamespacedKey.minecraft(attribute!!.key().value()),
                    value!!,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.ANY
                )
                meta.addAttributeModifier(attribute, modifier)
            }

            if (itemStackConfiguration.damage != 0) {
                if (meta is Damageable) {
                    val maxDurability = item.type.getMaxDurability().toInt()
                    if (maxDurability > 0) {
                        val currentDamage = meta.damage

                        val damageToAdd = ceil(maxDurability * (itemStackConfiguration.damage / 100.0)).toInt()

                        val newDamage = currentDamage + damageToAdd

                        if (newDamage >= maxDurability) {
                            return null
                        } else {
                            meta.damage = newDamage
                            item.setItemMeta(meta)
                            return item
                        }
                    }
                }
            }

            item.setItemMeta(meta)
            return item
        }
    }
}
