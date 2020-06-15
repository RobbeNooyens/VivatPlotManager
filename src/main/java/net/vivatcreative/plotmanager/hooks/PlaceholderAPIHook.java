package net.vivatcreative.plotmanager.hooks;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.vivatcreative.core.exceptions.WorldNotFoundException;
import net.vivatcreative.core.utils.Logger;
import net.vivatcreative.core.utils.PlotUtil;
import net.vivatcreative.core.utils.TextUtil;
import net.vivatcreative.core.utils.VivatWorld;
import net.vivatcreative.plotmanager.VivatPlotManager;
import net.vivatcreative.plotmanager.plotcount.PlotCounter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private final VivatPlotManager plugin;

    public PlaceholderAPIHook(VivatPlotManager plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "plotcount";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null)
            return null;
        // placeholder: %plotcount_staff_count%
        if (identifier.equals("limit_global"))
            return String.valueOf(PlotCounter.getGlobalLimit(p));
        try {
            if (identifier.equals("limit_world"))
                return String.valueOf(PlotCounter.getWorldLimit(p, VivatWorld.fromString(p.getWorld().getName())));
            if (identifier.startsWith("limit_world_"))
                return String.valueOf(PlotCounter.getWorldLimit(p, VivatWorld.fromString(identifier.replaceAll("limit_world_", ""))));
        } catch(WorldNotFoundException e){
            Logger.exception(e);
        }

        if (identifier.equals("claimed_global"))
            return String.valueOf(PlotCounter.getPlotCount(p));
        if (identifier.equals("claimedmerged_global"))
            return String.valueOf(PlotCounter.getMergedPlotCount(p));
        if (identifier.equals("claimedmerged_world"))
            return String.valueOf(PlotCounter.getMergedWorldPlotCount(p, p.getWorld().getName()));
        if (identifier.startsWith("claimedmerged_world_"))
            return String.valueOf(PlotCounter.getMergedWorldPlotCount(p, identifier.replaceAll("claimedmerged_world_", "")));

        if (identifier.equals("claimed_world"))
            return String.valueOf(PlotCounter.getWorldPlotCount(p, p.getWorld().getName()));
        if (identifier.startsWith("claimed_world_"))
            return String.valueOf(PlotCounter.getWorldPlotCount(p, identifier.replaceAll("claimed_world_", "")));

        if (identifier.equals("canclaim_global"))
            return PlotCounter.canClaimGlobal(p) ? "true" : "false";
        if (identifier.equals("canclaim_world"))
            return PlotCounter.canClaim(p, p.getWorld().getName()) ? "true" : "false";
        if (identifier.startsWith("canclaim_world_"))
            return PlotCounter.canClaim(p, identifier.replaceAll("canclaim_world_", "")) ? "true" : "false";
        if (identifier.equals("plotowner"))
            return (getPlotOwner(p, false));
        if (identifier.equals("plotownercolored"))
            return (getPlotOwner(p, true));

        if (identifier.equals("plotchat"))
            return (isPlotChatEnabled(p) ? "true" : "false");

        // anything else someone types is invalid because we never defined
        // %customplaceholder_<what they want a value for>%
        // we can just return null so the placeholder they specified is not replaced.
        return null;
    }

    public String getPlotOwner(Player p, boolean color) {
        Plot currentPlot = PlotUtil.getCurrentPlot(p);
        if (currentPlot == null)
            return color("None", color);
        Set<UUID> list = currentPlot.getOwners();
        if (list == null)
            return color("Unknown", color);
        if (list.isEmpty())
            return color("Unclaimed", color);
        UUID uuid = (UUID) list.toArray()[0];
        if (uuid == null)
            return color("None", color);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null)
            return color("Unknown", color);
        if (player.getName() == null)
            return color("Unknown", color);
        if (player.getName().equalsIgnoreCase("<none>"))
            return color("Unknown", color);
        return Bukkit.getOfflinePlayer((UUID) list.toArray()[0]).getName();
    }

    public boolean isPlotChatEnabled(Player p) {
        return PlotPlayer.wrap(p).getAttribute("chat");
    }

    public String color(String s, boolean color) {
        return TextUtil.toColor(color ? "&7" + s : s);
    }
}
