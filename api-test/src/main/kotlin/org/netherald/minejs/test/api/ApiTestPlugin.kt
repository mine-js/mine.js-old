package org.netherald.minejs.test.api

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.netherald.minejs.api.MineJsManager
import org.netherald.minejs.api.event.MineJsCommunicateEvent

class ApiTestPlugin: JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun communicate(event: MineJsCommunicateEvent) {
        val tag = event.args[0]
        println("Tag: $tag")
        if(tag == "GiveMoney") {
            println("GiveMoney: " + event.args.joinToString())
        }
        println("Callbak!")
        MineJsManager.communicate(this, "Callback", 123456)
    }

}