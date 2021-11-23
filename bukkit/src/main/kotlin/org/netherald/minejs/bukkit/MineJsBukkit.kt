package org.netherald.minejs.bukkit

import com.comphenix.protocol.ProtocolManager
import com.eclipsesource.v8.JavaCallback
import com.eclipsesource.v8.JavaVoidCallback
import com.eclipsesource.v8.V8Object
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.netherald.minejs.api.MineJs
import org.netherald.minejs.api.MineJsManager
import org.netherald.minejs.api.event.MineJsCommunicateEvent
import org.netherald.minejs.bukkit.command.MineJSCommand
import org.netherald.minejs.bukkit.command.MineJSTabCompleter
import org.netherald.minejs.bukkit.event.BlockListener
import org.netherald.minejs.bukkit.event.EntityListener
import org.netherald.minejs.bukkit.event.MiscListener
import org.netherald.minejs.bukkit.event.PlayerListener
import org.netherald.minejs.bukkit.impl.*
import org.netherald.minejs.bukkit.utils.ProtocolUtil
import org.netherald.minejs.common.CommunicationManager
import org.netherald.minejs.common.Platform
import org.netherald.minejs.common.ScriptLoader
import java.io.File

var protocolEnabled = false

class MineJsBukkit : JavaPlugin(), MineJs, CommunicationManager {

    val scriptsDir = File("plugins${File.separator}scripts")

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerListener(this), this)
        Bukkit.getPluginManager().registerEvents(EntityListener(), this)
        Bukkit.getPluginManager().registerEvents(BlockListener(), this)
        Bukkit.getPluginManager().registerEvents(MiscListener(this), this)

        getCommand("minejs")!!.setExecutor(MineJSCommand(this))
        getCommand("minejs")!!.tabCompleter = MineJSTabCompleter()

        protocolEnabled = Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")
        if(protocolEnabled)
            ProtocolUtil.init()
        else
            logger.warning("You may not use packet feature. Install ProtocolLib to use packet feature!")

        logger.info("Loading scripts...")
        if(!scriptsDir.exists())
            scriptsDir.mkdir()
        load()

        MineJsManager.impl = this
    }

    override fun onDisable() {
        ScriptLoader.unload()
    }

    fun load() {
        Bukkit.getScheduler().cancelTasks(this)
        ScriptLoader.load(scriptsDir, File(scriptsDir, "storage.json"), Platform.BUKKIT, PlayerManagerImpl(), ItemManagerImpl(), this, ConsoleImpl(this), CommandManagerImpl(this), TimeoutImpl(this))
    }

    override fun communicate(plugin: JavaPlugin, vararg args: Any) {
        println("Invoke event")
        ScriptLoader.invokeEvent("onCommunicationReceive") {
            add("plugin", V8Object(runtime).apply {
                add("name", plugin.name)
                registerJavaMethod({ _, parameters ->
                    val list = ArrayList<Any>()
                    for(i in 0 until parameters.length()) {
                        list.add(parameters[i])
                    }
                    Bukkit.getPluginManager().callEvent(MineJsCommunicateEvent(plugin, list))
                }, "communicate")
            })

            registerJavaMethod(JavaCallback { _, parameters ->
                return@JavaCallback args[parameters[0] as Int]
            }, "readArg")
        }
    }

    override fun invokeCustomEvent(name: String, eventArg: V8Object) {
        ScriptLoader.invokeEvent(name, eventArg)
    }

    override fun tryCommunicate(plugin: String, args: ArrayList<Any>) {
        Bukkit.getPluginManager().callEvent(MineJsCommunicateEvent(server.pluginManager.getPlugin(plugin) as JavaPlugin, args))
    }

}