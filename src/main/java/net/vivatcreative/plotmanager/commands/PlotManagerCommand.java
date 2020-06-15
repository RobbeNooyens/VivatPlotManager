package net.vivatcreative.plotmanager.commands;

import net.vivatcreative.core.messages.CoreMessage;
import net.vivatcreative.core.messages.Message;
import net.vivatcreative.core.utils.PlotUtil;
import net.vivatcreative.plotmanager.api.PlotsMessage;
import net.vivatcreative.plotmanager.api.PlotsPermission;
import net.vivatcreative.plotmanager.guis.PlotGui;
import net.vivatcreative.plotmanager.guis.PlotSettingsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

public class PlotManagerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("plotmanager")) return true;
		if (!(sender instanceof Player)) return Message.send(sender, CoreMessage.SHOULD_BE_PLAYER);
		Player p = (Player) sender;

		if (args.length == 0) return Message.send(sender, CoreMessage.COMMAND_USAGE, "%usage%", "/plotmanager");
		Plot plot;
		switch (args[0].toLowerCase()) {
		case "plot":
			if (args.length < 2) {
				plot = PlotUtil.getCurrentPlot(p);
				if (plot == null) return Message.send(sender, PlotsMessage.NOT_ON_PLOT);
			} else {
				plot = PlotUtil.getPlot(args[1]);
				if (plot == null) return Message.send(sender, CoreMessage.INVALID_ARGUMENT);
			}
			return PlotGui.show(p, plot);
		case "settings":
			if (args.length < 2) {
				plot = PlotUtil.getCurrentPlot(p);
				if (plot == null) return Message.send(sender, PlotsMessage.NOT_ON_PLOT);
			} else {
				plot = PlotUtil.getPlot(args[1]);
				if (plot == null) return Message.send(sender, CoreMessage.INVALID_ARGUMENT);
			}
			if (plot.getOwners().contains(p.getUniqueId()) || PlotsPermission.PLOTMANAGER_SETTINGS_OTHERS.hasAndWarn(p))
				return PlotSettingsGui.show(p, plot);
			return true;
		case "help":
			return Message.send(sender, PlotsMessage.PLOTMANAGER_HELP);
		default:
			return Message.send(sender, CoreMessage.COMMAND_USAGE, "%usage%", "/plotmanager help");
		}
	}

}
