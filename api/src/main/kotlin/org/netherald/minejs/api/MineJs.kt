package org.netherald.minejs.api

import com.eclipsesource.v8.V8Object
import org.bukkit.plugin.java.JavaPlugin

interface MineJs {
    fun communicate(plugin: JavaPlugin, vararg args: Any)
    fun invokeCustomEvent(name: String, eventArg: V8Object)
}