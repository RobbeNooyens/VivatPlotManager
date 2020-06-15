package net.vivatcreative.plotmanager.api;

import net.vivatcreative.core.connections.VivatConnection;
import net.vivatcreative.plotmanager.VivatPlotManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class PlotManagerConnection implements VivatConnection {

    private final Set<String> commands = new HashSet<>();

    @Override
    public Object get(String arg0, Object... arg1) {return null;}

    @Override
    public JavaPlugin getPlugin() {
        return VivatPlotManager.get();
    }

    @Override
    public void set(String key, Object... args) {}

    @Override
    public void onReload() {}

    @Override
    public void addCommand(String s) {commands.add(s);}

    @Override
    public Set<String> getRegisteredCommands() {
        return commands;
    }

}
