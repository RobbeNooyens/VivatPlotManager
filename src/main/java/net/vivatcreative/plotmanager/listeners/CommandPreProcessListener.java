package net.vivatcreative.plotmanager.listeners;

import net.vivatcreative.core.messages.Message;
import net.vivatcreative.plotmanager.api.PlotsMessage;
import net.vivatcreative.plotmanager.plotcount.PlotCounter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CommandPreProcessListener implements Listener {

    private static final List<String> cmdAliases = Arrays.asList("plot", "p", "2", "plots", "plotsquared", "plot2",
            "p2", "ps", "plotme");
    private static final List<String> subCmd = Arrays.asList("auto", "claim", "a", "c");

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage() == null) return;
        String cmd = e.getMessage().toLowerCase();

        // Check for /plothomes aliases
        if (cmd.equals("plots") || cmd.equals("homes")) {
            e.setCancelled(true);
            Bukkit.dispatchCommand(p, "plothomes");
            return;
        }

        String[] cmdPieces = cmd.split(" ");
        if (cmdPieces.length <= 1) return;
        if (!cmdAliases.contains(cmdPieces[0].replaceAll("/", ""))) return;

        // Check for PlotSquared aliases
        if (subCmd.contains(cmdPieces[1])) {
            if (!PlotCounter.canClaim(p, p.getWorld().getName())) {
                e.setCancelled(true);
                Message.send(p, PlotsMessage.NO_SPARE_PLOTS);
                Bukkit.dispatchCommand(p, "plotcount list " + p.getName());
            }
        } else if (cmdPieces[1].equals("settings")) {
            e.setCancelled(true);
            Bukkit.dispatchCommand(p, "plotmanager settings");
        }
    }
}

