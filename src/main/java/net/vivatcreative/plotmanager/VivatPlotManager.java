package net.vivatcreative.plotmanager;

import net.vivatcreative.core.connections.ConnectionManager;
import net.vivatcreative.core.files.FileManager;
import net.vivatcreative.core.messages.MessageHelper;
import net.vivatcreative.plotmanager.api.PlotManagerConnection;
import net.vivatcreative.plotmanager.commands.PlotCountCommand;
import net.vivatcreative.plotmanager.commands.PlotHomesCmd;
import net.vivatcreative.plotmanager.commands.PlotManagerCommand;
import net.vivatcreative.plotmanager.hooks.PlaceholderAPIHook;
import net.vivatcreative.plotmanager.listeners.CommandPreProcessListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VivatPlotManager extends JavaPlugin {

    public void onEnable() {
        // Events
        Bukkit.getPluginManager().registerEvents(new CommandPreProcessListener(), this);

        // Hooks
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            (new PlaceholderAPIHook(this)).register();

        // Vivat API
        PlotManagerConnection connection = new PlotManagerConnection();
        connection.registerCommand("plotcount", new PlotCountCommand());
        connection.registerCommand("plothomes", new PlotHomesCmd());
        connection.registerCommand("plotmanager", new PlotManagerCommand());
        ConnectionManager.register(connection);

        MessageHelper.register(FileManager.getFile(this, "messages.yml", false));
    }

    public static VivatPlotManager get() {
        return JavaPlugin.getPlugin(VivatPlotManager.class);
    }

    public void onDisable() {
        FileManager.removeFile(this, "messages.yml", null);
    }
}
