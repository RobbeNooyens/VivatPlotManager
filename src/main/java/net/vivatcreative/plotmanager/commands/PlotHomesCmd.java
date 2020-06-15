package net.vivatcreative.plotmanager.commands;

import net.vivatcreative.core.messages.CoreMessage;
import net.vivatcreative.core.messages.Message;
import net.vivatcreative.core.utils.PlayerUtil;
import net.vivatcreative.plotmanager.api.PlotsPermission;
import net.vivatcreative.plotmanager.guis.PlotHomesGui;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotHomesCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return Message.send(sender, CoreMessage.SHOULD_BE_PLAYER);
		Player p = (Player) sender;

		if (args.length == 0) return ((!PlotsPermission.PLOTHOMES_VIEW_SELF.hasAndWarn(p)) || PlotHomesGui.show(p, p));

		if (!PlotsPermission.PLOTHOMES_VIEW_OTHERS.hasAndWarn(p)) return true;
		OfflinePlayer target = PlayerUtil.getOfflinePlayer(args[0]);
		return (target != null) ? PlotHomesGui.show(p, target) : Message.send(p, CoreMessage.TARGET_NOT_FOUND);
	}

}
