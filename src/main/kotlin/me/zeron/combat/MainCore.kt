package me.zeron.combat

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MainCore : JavaPlugin() {
    companion object {
        lateinit var plugin: Plugin
        val combatPlayer: MutableList<Player> = mutableListOf()
        val combatTaskId: MutableMap<Player, Int> = mutableMapOf()
        val bossbarMap: MutableMap<Player, BossBar> = mutableMapOf()
        val prefix = Component.text("[ ").append(
            Component.text("N").decorate(TextDecoration.BOLD).color(TextColor.color(0x01FC7A)).
        append(Component.text("E").decorate(TextDecoration.BOLD).color(TextColor.color(0x03FCBE))).
        append(Component.text("O").decorate(TextDecoration.BOLD).color(TextColor.color(0x02FAEA))).
        append(Component.text("N").decorate(TextDecoration.BOLD).color(TextColor.color(0x03DCFD))).
        append(Component.text(" §f] ")))
        val leavingInCombat: MutableList<UUID> = mutableListOf()
    }

    override fun onEnable() {
        plugin = this
        logger.info("플러그인이 활성화 중입니다")

        server.pluginManager.registerEvents(CombatListener, this)
    }

    override fun onDisable() {
        logger.info("플러그인이 비활성화 중입니다")
    }
}