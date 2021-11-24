package org.netherald.minejs.bukkit.impl

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import org.bukkit.Bukkit
import org.bukkit.Location
import org.netherald.minejs.bukkit.utils.ObjectUtils
import org.netherald.minejs.bukkit.utils.ObjectUtils.createPlayerObject
import org.netherald.minejs.bukkit.utils.ObjectUtils.fromV8ItemStack
import org.netherald.minejs.common.PlayerManager
import java.lang.UnsupportedOperationException
import java.util.*

class PlayerManagerImpl : PlayerManager {

    override fun getPlayers(runtime: V8): V8Array {
        val array = V8Array(runtime)
        for (player in Bukkit.getOnlinePlayers()) {
            val playerArray = V8Object(runtime).apply(createPlayerObject(player, runtime))
            array.push(playerArray)
            playerArray.release()
        }

        return array
    }

    override fun getPlayersOnServer(runtime: V8, server: String): V8Array {
        throw UnsupportedOperationException("Only bungeecord can do it!")
    }

    override fun playerOf(runtime: V8, name: String): V8Object {
        val player = Bukkit.getPlayer(name)
        return if(player != null) {
            V8Object(runtime).apply(createPlayerObject(player, runtime))
        } else {
            val player2 = Bukkit.getPlayer(UUID.fromString(name))
            if(player2 != null) {
                V8Object(runtime).apply(createPlayerObject(player2, runtime))
            } else {
                V8Object(runtime)
            }
        }
    }

    override fun worldOf(runtime: V8, name: String): V8Object {
        val world = Bukkit.getWorld(name)
        return if(world != null) V8Object(runtime).apply(ObjectUtils.createWorldObject(world, runtime)) else V8Object(runtime)
    }

    override fun locationOf(runtime: V8, parameters: V8Array): V8Object {
        // world, x, y, z, yaw, pitch
        if(parameters.length() >= 6) {
            val world = Bukkit.getWorld((parameters[0] as V8Object).getString("name"))
            val x = parameters[1] as Double
            val y = parameters[2] as Double
            val z = parameters[3] as Double
            val yaw = parameters[4] as Double
            val pitch = parameters[5] as Double

            return V8Object(runtime).apply(ObjectUtils.createLocationObject(Location(world, x, y, z, yaw.toFloat(), pitch.toFloat()), runtime))
        } else if(parameters.length() >= 4) {
            val world = Bukkit.getWorld((parameters[0] as V8Object).getString("name"))
            val x = parameters[1] as Double
            val y = parameters[2] as Double
            val z = parameters[3] as Double
            return V8Object(runtime).apply(ObjectUtils.createLocationObject(Location(world, x, y, z), runtime))
        } else {
            return V8Object(runtime)
        }
    }
}