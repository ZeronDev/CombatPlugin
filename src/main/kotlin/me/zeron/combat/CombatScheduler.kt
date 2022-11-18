package me.zeron.combat

import me.zeron.combat.MainCore.Companion.combatTaskId
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CombatScheduler(val p: Player) : Runnable {
    private var progress = 1.0
    private val combatTime = progress / (8.toDouble() * 20)
    private var bossbar = BossBar.bossBar(text("§c전투 시간"), progress.toFloat(), BossBar.Color.RED, BossBar.Overlay.PROGRESS)


    override fun run() {
        if (progress < 0) {
            Bukkit.getScheduler().cancelTask(combatTaskId[p]!!)
            p.hideBossBar(bossbar)
            combatTaskId.remove(p)
        }
        bossbar.progress(progress.toFloat())
        p.showBossBar(bossbar)
        progress -= combatTime
    }
}