package io.papermc.voidWorld.commands.helper

import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack

interface ICommand {
  val success: Int
    get() = 1

  val fail: Int
    get() = 0

  fun command(): LiteralCommandNode<CommandSourceStack>
}
