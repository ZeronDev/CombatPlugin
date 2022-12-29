package me.zeron.combat

import me.zeron.combat.MainCore.Companion.bossbarMap
import me.zeron.combat.MainCore.Companion.combatPlayer
import me.zeron.combat.MainCore.Companion.combatTaskId
import me.zeron.combat.MainCore.Companion.leavingInCombat
import me.zeron.combat.MainCore.Companion.plugin
import me.zeron.combat.MainCore.Companion.prefix
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent

object CombatListener : Listener {
    @EventHandler
    fun playerAttack(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Player) { //PVP 상황이라면
            val p = e.entity as Player
            val damager = e.damager as Player
            if (!combatPlayer.contains(p)) {
                combatPlayer.add(p)
                val task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(p), 0L, 1L)
                combatTaskId[p] = task.taskId
            } else {
                Bukkit.getScheduler().cancelTask(combatTaskId[p]!!)
                p.hideBossBar(bossbarMap[p]!!)
                val task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(p), 0L, 1L)
                combatTaskId[p] = task.taskId
            }
            if (combatPlayer.contains(damager)) {
                Bukkit.getScheduler().cancelTask(combatTaskId[damager]!!)
                damager.hideBossBar(bossbarMap[damager]!!)
                val damatask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(damager), 0L, 1L)
                combatTaskId[damager] = damatask.taskId
            } else {
                combatPlayer.add(damager)
                val damatask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CombatScheduler(damager), 0L, 1L)
                combatTaskId[damager] = damatask.taskId
            }
        }
    }

    @EventHandler
    fun onKill(e: PlayerDeathEvent) {
        if (leavingInCombat.contains(e.player.uniqueId)) {
            e.deathMessage(prefix.append(text("§c${e.player.name}가 전투 상태에서 나가서 사망하였습니다")))
            leavingInCombat.remove(e.player.uniqueId)
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (combatPlayer.contains(e.player)) {
            Bukkit.getScheduler().cancelTask(combatTaskId[e.player]!!)
            e.player.hideBossBar(bossbarMap.remove(e.player)!!)
            leavingInCombat.add(e.player.uniqueId)
            combatPlayer.remove(e.player)
            combatTaskId.remove(e.player)
            e.player.health = 0.toDouble()
        }
    }

     @EventHandler
     fun onCommand(e: PlayerCommandPreprocessEvent) {
         if (combatPlayer.contains(e.player)) {
             e.isCancelled = true
             e.player.sendMessage(prefix.append(text("§c전투 상태에서는 명령어를 이용할 수 없습니다")))
         }
     }
}