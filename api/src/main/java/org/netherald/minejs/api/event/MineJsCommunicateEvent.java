package org.netherald.minejs.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MineJsCommunicateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private JavaPlugin plugin;

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public List<?> getArgs() {
        return args;
    }

    private List<?> args;

    public MineJsCommunicateEvent(JavaPlugin plugin, List<?> args) {
        this.plugin = plugin;
        this.args = args;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
