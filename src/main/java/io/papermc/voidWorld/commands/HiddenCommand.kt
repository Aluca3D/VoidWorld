package io.papermc.voidWorld.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver
import io.papermc.voidWorld.commands.helper.ICommand
import io.papermc.voidWorld.helper.OHidden
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class HiddenCommand : ICommand {
  private val hiddenTag: String = OHidden.HIDDEN_TAG
  private val seeingTag: String = OHidden.SEEING_TAG

  override fun command(): LiteralCommandNode<CommandSourceStack> {
    val root: LiteralArgumentBuilder<CommandSourceStack> =
      Commands
        .literal("hidden")
        .requires { src -> src.sender.hasPermission("voidworld.hidden") }
        .then(
          Commands
            .argument("entity", ArgumentTypes.entity())
            .then(
              Commands
                .literal("hidden")
                .executes { ctx ->
                  val sender = ctx.source.sender
                  val entityResolver: EntitySelectorArgumentResolver =
                    ctx.getArgument("entity", EntitySelectorArgumentResolver::class.java)

                  val entity = entityResolver.resolve(ctx.source).first()

                  if (entity.scoreboardTags.contains(hiddenTag)) {
                    sender.sendMessage(
                      Component
                        .text("Removed the hidden tag from ${entity.name}")
                        .color(NamedTextColor.GREEN),
                    )
                    entity.scoreboardTags.remove(hiddenTag)
                  } else {
                    sender.sendMessage(
                      Component
                        .text("Added the hidden tag to ${entity.name}")
                        .color(NamedTextColor.GREEN),
                    )
                    entity.scoreboardTags.add(hiddenTag)
                  }

                  success
                },
            ).then(
              Commands
                .literal("seeing")
                .executes { ctx ->
                  val sender = ctx.source.sender
                  val entityResolver: EntitySelectorArgumentResolver =
                    ctx.getArgument("entity", EntitySelectorArgumentResolver::class.java)
                  val player = entityResolver.resolve(ctx.source).first()

                  if (player !is Player) {
                    sender.sendMessage(
                      Component
                        .text("${player.name} is not a player")
                        .color(NamedTextColor.RED),
                    )
                    return@executes fail
                  }

                  if (player.scoreboardTags.contains(seeingTag)) {
                    sender.sendMessage(
                      Component
                        .text("Removed the seeing tag from ${player.name}")
                        .color(NamedTextColor.GREEN),
                    )
                    player.scoreboardTags.remove(seeingTag)
                  } else {
                    sender.sendMessage(
                      Component
                        .text("Added the seeing tag to ${player.name}")
                        .color(NamedTextColor.GREEN),
                    )
                    player.scoreboardTags.add(seeingTag)
                  }

                  success
                },
            ),
        )
    return root.build()
  }
}
