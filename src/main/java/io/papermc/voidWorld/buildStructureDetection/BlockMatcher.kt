package io.papermc.voidWorld.buildStructureDetection

import org.bukkit.Material

data class BlockMatcher(
    val dx: Int,
    val dz: Int,
    val types: Set<Material>
) {
    constructor(dx: Int, dz: Int, type: Material) :
            this(dx, dz, setOf(type))

    fun matches(blockType: Material): Boolean =
        blockType in types
}
