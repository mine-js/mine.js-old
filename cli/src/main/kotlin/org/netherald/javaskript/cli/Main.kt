package org.netherald.javaskript.cli

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import org.netherald.javaskript.common.Platform
import org.netherald.javaskript.common.PlayerManager
import org.netherald.javaskript.common.ScriptLoader
import java.io.File
import java.lang.UnsupportedOperationException
import java.util.*

fun main() {
    println("Loading scripts...")
    ScriptLoader.load(File(System.getProperty("user.dir") + File.pathSeparator + "scripts"), Platform.CLI, PlayerManagerImpl(), ConsoleImpl())
    var exited = false
    val scanner = Scanner(System.`in`)

    while(!exited) {
        val read = scanner.next()
        if(read.startsWith("move_event")) {
            ScriptLoader.invokeEvent("onPlayerMove", ScriptLoader.createV8Object {
                add("from", ScriptLoader.createV8Object {
                    add("x", 1)
                    add("y", 1)
                    add("z", 1)
                })
                add("to", ScriptLoader.createV8Object {
                    add("x", 2)
                    add("y", 2)
                    add("z", 2)
                })
                add("player", createObjectForPlayer(Player("netherald"), runtime))
            })
        }
    }
}

fun createObjectForPlayer(player: Player, runtime: V8) : V8Object {
    val res = V8Object(runtime)
    res.add("name", player.name)
    res.registerJavaMethod({ receiver, parameters ->
        if(parameters.length() > 0) {
            player.sendMessage(parameters[0] as String)
        }
    }, "send")
}

class ConsoleImpl : org.netherald.javaskript.common.Console() {
    override fun log(content: Any, fileName: String) {
        println("[LOG] [$fileName] $content")
    }
}

class PlayerManagerImpl : PlayerManager {
    override fun getPlayers(runtime: V8): V8Array {
        return V8Array(runtime).push(createObjectForPlayer(Player("test"), runtime))
    }

    override fun getPlayersOnServer(runtime: V8, server: String): V8Array {
        throw UnsupportedOperationException("This is not bungee!")
    }

    override fun playerOf(runtime: V8, name: String): V8Object {
        return createObjectForPlayer(Player("netherald"), runtime)
    }
}