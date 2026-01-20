package io.papermc.voidWorld.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.voidWorld.commands.helper.OCommandHelper
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class SitCommand {
  fun sitCommand(): LiteralCommandNode<CommandSourceStack> {
    val sitCommand: LiteralArgumentBuilder<CommandSourceStack> =
      Commands
        .literal("sit")
        .executes { ctx ->
          val sender = ctx.source.sender
          if (sender !is Player) return@executes OCommandHelper.fail()

          val seat = EntityType.ARMOR_STAND
          val newSeat = summonSeat(sender, seat)
          newSeat.addPassenger(sender)

          OCommandHelper.success()
        }.then(
          Commands
            .literal("on")
            .executes { ctx ->
              val sender = ctx.source.sender
              if (sender !is Player) return@executes OCommandHelper.fail()

              val target = sender.rayTraceEntities(2)?.hitEntity

              if (target != null) {
                target.addPassenger(sender)
                sender.sendMessage("You are now sitting on ${target.type.name.lowercase()}")
              } else {
                sender.sendMessage("No entity in range to sit on!")
              }

              OCommandHelper.success()
            },
        )

    val buildSitCommand: LiteralCommandNode<CommandSourceStack> = sitCommand.build()

    return buildSitCommand
  }

  private fun summonSeat(
    sender: Player,
    seat: EntityType,
  ): Entity {
    val pos = sender.location
    val spawnedSeat = sender.world.spawnEntity(pos, seat)

    spawnedSeat.isInvulnerable = true
    spawnedSeat.isInvisible = true
    spawnedSeat.addScoreboardTag("sit_seat")
    spawnedSeat.setRotation(sender.location.yaw, 0f)

    if (spawnedSeat is ArmorStand) {
      spawnedSeat.isSmall = true
      spawnedSeat.isMarker = true
    }

    return spawnedSeat
  }
}
