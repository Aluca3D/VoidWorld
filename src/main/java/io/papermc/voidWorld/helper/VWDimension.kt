package io.papermc.voidWorld.helper

import org.bukkit.World
import java.util.*

enum class VWDimension(val environment: World.Environment) {
    OVERWORLD(World.Environment.NORMAL),
    NETHER(World.Environment.NETHER),
    END(World.Environment.THE_END),
    ;

    companion object {
        @JvmStatic
        fun fromString(name: String?): VWDimension? =
            name
                ?.uppercase(Locale.ROOT)
                ?.let {
                    runCatching { valueOf(it) }.getOrNull()
                }
    }
}
