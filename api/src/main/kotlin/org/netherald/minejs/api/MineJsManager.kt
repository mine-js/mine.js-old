package org.netherald.minejs.api

import com.eclipsesource.v8.V8Object
import org.bukkit.plugin.java.JavaPlugin

object MineJsManager {

    lateinit var impl: MineJs

    fun communicate(plugin: JavaPlugin, vararg other: Any) {
        impl.communicate(plugin, other)
    }

    fun invokeCustomEvent(name: String, eventArg: V8Object) {
        impl.invokeCustomEvent(name, eventArg)
    }

}