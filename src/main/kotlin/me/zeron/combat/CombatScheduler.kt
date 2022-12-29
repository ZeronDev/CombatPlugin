package me.zeron.combat

import me.zeron.combat.MainCore.Companion.bossbarMap
import me.zeron.combat.MainCore.Companion.combatPlayer
import me.zeron.combat.MainCore.Companion.combatTaskId
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CombatScheduler(val p: Player) : Runnable {
    private var progress = 1.0
    private val combatTime = progress / (8.toDouble() * 20)
    private var second = 8.0
    private var secondCounter = 20
    private var bossbar = BossBar.bossBar(text("§c§l☠전투 ${second.toInt()}초☠"), progress.toFloat(), BossBar.Color.RED, BossBar.Overlay.NOTCHED_10)
    override fun run() {
        bossbarMap.put(p, bossbar)
        if (progress < 0.0) {
            Bukkit.getScheduler().cancelTask(combatTaskId[p]!!)
            p.hideBossBar(bossbar)
            combatTaskId.remove(p)
            combatPlayer.remove(p)
            bossbarMap.remove(p)
            return
        }
        if (secondCounter <= 0) {
            secondCounter = 20
            second -= 1
            bossbar.name(text("§c§l☠전투 ${second.toInt()}초☠"))
        }
        secondCounter -= 1
        bossbar.progress(progress.toFloat())
        p.showBossBar(bossbar)
        progress -= combatTime
    }
}