package me.zeron.combat

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class MainCore : JavaPlugin() {
    companion object {
        lateinit var plugin: Plugin
        val combatPlayer: MutableList<Player> = mutableListOf()
        val combatTaskId: MutableMap<Player, Int> = mutableMapOf()
    }

    override fun onEnable() {
        plugin = this
        logger.info("§a플러그인이 활성화 중입니다")

        server.pluginManager.registerEvents(CombatListener, this)
    }

    override fun onDisable() {
        logger.info("§c플러그인이 비활성화 중입니다")
    }
}