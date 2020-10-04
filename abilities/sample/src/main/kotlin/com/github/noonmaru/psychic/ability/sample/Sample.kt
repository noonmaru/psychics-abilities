package com.github.noonmaru.psychic.ability.sample

import com.github.noonmaru.psychics.Ability
import com.github.noonmaru.psychics.AbilityConcept
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent

class SampleConcept: AbilityConcept() {
    init {
        displayName = "Ability예제"
        levelRequirement = 10
        cooldownTicks = 200 // 10초
        cost = 20.0
    }
}

class Sample: Ability<SampleConcept>(), Listener, Runnable {
    override fun onEnable() {
        psychic.registerEvents(this)
        psychic.runTaskTimer(this, 0L, 1L)
    }

    @EventHandler
    fun onPlayerSneak(event: PlayerToggleSneakEvent) {
        event.player.sendMessage("Sneak event")
    }

    override fun run() {
        esper.player.sendMessage("Task running")
    }
}