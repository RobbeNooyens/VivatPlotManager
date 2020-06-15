package net.vivatcreative.plotmanager.commands;

import net.vivatcreative.core.exceptions.WorldNotFoundException;
import net.vivatcreative.core.messages.CoreMessage;
import net.vivatcreative.core.messages.Message;
import net.vivatcreative.core.messages.MessageHelper;
import net.vivatcreative.core.utils.PlayerUtil;
import net.vivatcreative.core.utils.VivatWorld;
import net.vivatcreative.plotmanager.api.PlotsMessage;
import net.vivatcreative.plotmanager.api.PlotsPermission;
import net.vivatcreative.plotmanager.managers.Config;
import net.vivatcreative.plotmanager.plotcount.PlotCounter;
import net.vivatcreative.plotmanager.plotcount.PlotLimits;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlotCountCommand implements CommandExecutor {

    private static final int INFO_ARG = 0, NAME_ARG = 1, WORLD_ARG = 2, AMPLIFIER_ARG = 3;
    private static final String BETWEEN_CUR_MAX = " / ";


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return Message.send(sender, CoreMessage.SHOULD_BE_PLAYER);
        if (!cmd.getName().equalsIgnoreCase("plotcount")) return true;
        if (args.length < 2 || args[0].equalsIgnoreCase("help")) return Message.send(sender, "plotmanager_help");
        Player p = (Player) sender;

        Player target = PlayerUtil.getOnlinePlayer(args[NAME_ARG]);
        if (target == null) return Message.send(sender, CoreMessage.TARGET_NOT_FOUND);

        String info = args[INFO_ARG].toLowerCase();

        if (info.equals("list")) return listPlotcount(p, target);

        if (args.length <= AMPLIFIER_ARG) return Message.send(sender, PlotsMessage.PLOTMANAGER_HELP);

        String worldName = args[WORLD_ARG];
        if (worldName.equalsIgnoreCase("this")) worldName = p.getWorld().getName();
        VivatWorld world;
        try {
            world = VivatWorld.fromString(worldName);
        } catch (WorldNotFoundException e) {
            return Message.send(sender, "invalid_argument");
        }
        // TODO: check if plugin is allowed in world
        //if (!Config.pluginAllowed(worldName)) return Message.send(sender, "invalid_argument");

        int amplifier;
        if (!StringUtils.isNumeric(args[AMPLIFIER_ARG])) return Message.send(sender, CoreMessage.INVALID_ARGUMENT);
        amplifier = Integer.parseInt(args[AMPLIFIER_ARG]);

        if (info.equals("add")) {
            if (!PlotsPermission.PLOTCOUNT_ADD.hasAndWarn(p)) return true;
            PlotLimits.addWorldLimit(target, world, Math.abs(amplifier));
            return Message.send(sender, PlotsMessage.ADDED_PLOTS, "%amount%", String.valueOf(amplifier), "%player%",
                    target.getName(), "%world%", world.coloured);
        }

        if (info.equals("remove")) {
            if (!PlotsPermission.PLOTCOUNT_REMOVE.hasAndWarn(p)) return true;
            PlotLimits.addWorldLimit(target, world, -Math.abs(amplifier));
            return Message.send(sender, PlotsMessage.REMOVED_PLOTS, "%amount%", String.valueOf(amplifier), "%player%",
                    target.getName(), "%world%", world.coloured);
        }
        return Message.send(sender, CoreMessage.COMMAND_USAGE, "%usage%", "/plotcount <list/add/remove>");
    }

    private boolean listPlotcount(Player p, Player target) {
        if (!PlotsPermission.PLOTCOUNT_LIST.hasAndWarn(p)) return true;
        List<String> m = new ArrayList<>();
        m.add(" ");
        m.add("&8&m-------------------&7[ &6&lPlot Limits &7]&8&m---------------------");
        m.add("&7Plots of &b" + target.getName());
        m.add(" ");
        m.add("&8- &7Total Plots: &a" + PlotCounter.getPlotCount(target) + "/" + PlotCounter.getGlobalLimit(target));
        m.add("&8- &7Total Extra: &a" + PlotLimits.getGlobalLimit(target));
        m.add("&8- &7Total Plots (merged): &a" + PlotCounter.getMergedPlotCount(target));
        m.add(" ");
        for (VivatWorld world : Config.getRegisteredWorlds())
            m.add(String.format("&8- &7%s: &7%s", world.coloured,
                    getWorldClaimedPerLimit(target, world)));
        m.add("&8&m-----------------------------------------------------");
        MessageHelper.sendMessage(p, m);

        return true;
    }

    private String getWorldClaimedPerLimit(OfflinePlayer p, VivatWorld world) {
        return PlotCounter.getWorldPlotCount(p, world.officialName) + BETWEEN_CUR_MAX + PlotCounter.getWorldLimit(p, world);
    }

    public String getGlobalClaimedPerLimit(OfflinePlayer p) {
        return PlotCounter.getPlotCount(p) + BETWEEN_CUR_MAX + PlotCounter.getGlobalLimit(p);
    }
}
