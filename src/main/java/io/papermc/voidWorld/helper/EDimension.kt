package io.papermc.voidWorld.helper

import org.bukkit.World
import java.util.*

enum class EDimension(val environment: World.Environment) {
    OVERWORLD(World.Environment.NORMAL),
    NETHER(World.Environment.NETHER),
    END(World.Environment.THE_END),
    ;

    companion object {
        @JvmStatic
        fun fromString(name: String?): EDimension? =
            name
                ?.uppercase(Locale.ROOT)
                ?.let {
                    runCatching { valueOf(it) }.getOrNull()
                }
    }
}
