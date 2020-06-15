package net.vivatcreative.plotmanager.plotcount;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import net.vivatcreative.core.exceptions.WorldNotFoundException;
import net.vivatcreative.core.utils.VivatWorld;
import net.vivatcreative.plotmanager.managers.Config;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlotCounter {

	/**
	 * Returns the sum of the maximum plots of each registered world for an
	 * OfflinePlayer
	 */
	public static int getGlobalLimit(OfflinePlayer p) {
		return PlotLimits.getGlobalLimit(p);
	}

	/**
	 * Returns the maximum amount of plots for an OfflinePlayer
	 */
	public static int getWorldLimit(OfflinePlayer p, VivatWorld world) {
		return PlotLimits.getWorldLimit(p, world);
	}

	/**
	 * Returns the sum of the plots in the registered worlds owned by an
	 * OfflinePlayer
	 */
	public static int getPlotCount(OfflinePlayer p) {
		Set<Plot> plots = PlotPlayer.wrap(p).getPlots();
		if (plots == null)
			return 0;
		int count = 0;
		for (Plot plot : plots)
			if (Config.pluginAllowed(plot.getWorldName()))
				count++;
		return count;
	}

	/**
	 * Returns the amount of plots an OfflinePlayer has in a specific world
	 */
	public static int getWorldPlotCount(OfflinePlayer p, String world) {
		if(!Config.pluginAllowed(world)) return 0;
		Set<Plot> plots = PlotPlayer.wrap(p).getPlots(world);
		return (plots == null ? 0 : plots.size());
	}

	/**
	 * Returns sum of all plots (merged plots count as 1)
	 */
	public static int getMergedPlotCount(OfflinePlayer p) {
		List<Plot> plots = getAllPlots(p);
		return (plots != null) ? plots.size() : 0;
	}

	/**
	 * Returns the amount of all plots (merged plots count as 1) in a world
	 */
	public static int getMergedWorldPlotCount(OfflinePlayer p, String world) {
		Set<Plot> plots = getAllWorldPlots(world, p);
		return plots.size();
	}

	public static List<Plot> getAllPlots(OfflinePlayer p) {
		PlotPlayer player = PlotPlayer.wrap(p);
		Set<Plot> plots = player.getPlots();
		plots.removeIf(plot -> !plot.isBasePlot());
		//ArrayList<Plot> sorted = PS.get().sortPlots(plots, PS.SortType.CREATION_DATE, null);
		return PS.get().sortPlotsByTemp(plots);
	}

	/**
	 * Returns all plots in a world (merged plots count as 1)
	 */
	public static Set<Plot> getAllWorldPlots(String world, OfflinePlayer p) {
		List<Plot> plots = getAllPlots(p);
		Set<Plot> result = new HashSet<>();
		for (Plot plot : plots) {
			if (plot.getWorldName().equalsIgnoreCase(world) && Config.pluginAllowed(plot.getWorldName()))
				result.add(plot);
		}			
		return result;
	}

	/**
	 * Whether a player has currently enough free plots
	 */
	public static boolean canClaim(final Player p, final String world) {
		VivatWorld w;
		try {
			w = VivatWorld.fromString(world);
		} catch (WorldNotFoundException e) {
			return false;
		}
		int currentPlots = getWorldPlotCount(p, world);
		int plotLimit = getWorldLimit(p, w);
		return currentPlots < plotLimit;
	}
	
	public static boolean canClaimGlobal(final Player p) {
		int currentPlots = getPlotCount(p);
		int max = getGlobalLimit(p);
		return currentPlots < max;
	}

}
