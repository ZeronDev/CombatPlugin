package me.zeron.combat

import me.zeron.combat.MainCore.Companion.combatPlayer
import me.zeron.combat.MainCore.Companion.combatTaskId
import me.zeron.combat.MainCore.Companion.plugin
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent

object CombatListener : Listener {
    @EventHandler
    fun playerAttack(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Player) { //PVP 상황이라면
            val p = e.entity as Player
            if (!combatPlayer.contains(p)) {
                combatPlayer.add(p)
                val task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(p), 0L, 1L)
                combatTaskId[p] = task.taskId
            } else {
                Bukkit.getScheduler().cancelTask(combatTaskId[p]!!)
                combatTaskId.remove(p)
                val task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(p), 0L, 1L)
                combatTaskId[p] = task.taskId
            }
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (combatPlayer.contains(e.player)) {
            e.player.health = 0.toDouble()
            Bukkit.broadcast(text("${e.player.name}가 콤뱃 상태에서 나가서 사망하였습니다"))
        }
    }

     @EventHandler
     fun onCommand(e: PlayerCommandPreprocessEvent) {
         if (combatPlayer.contains(e.player)) {
             e.isCancelled = true
             e.player.sendMessage("§c콤뱃 상태에서는 명령어를 이용할 수 없습니다")
         }
     }
}