package net.vivatcreative.plotmanager.plotcount;

import net.vivatcreative.core.players.Users;
import net.vivatcreative.core.players.VivatPlayer;
import net.vivatcreative.core.utils.VivatWorld;
import net.vivatcreative.plotmanager.managers.Config;
import org.bukkit.OfflinePlayer;

public class PlotLimits {
		
	public static int getWorldLimit(OfflinePlayer p, VivatWorld world) {
		return Users.get(p).getPlotcount(world);
	}
	
	public static void setWorldLimit(OfflinePlayer p, VivatWorld world, int amount) {
		Users.get(p).setPlotcount(world, amount);
	}
	
	public static void addWorldLimit(OfflinePlayer p, VivatWorld world, int amount) {
		int current = getWorldLimit(p, world);
		setWorldLimit(p, world, current + amount);
	}
	
	public static int getGlobalLimit(OfflinePlayer p) {
		int sum = 0;
		VivatPlayer player = Users.get(p);
		for (VivatWorld world : Config.getRegisteredWorlds())
			sum += player.getPlotcount(world);
		return Math.min(sum, 999);
	}

}
