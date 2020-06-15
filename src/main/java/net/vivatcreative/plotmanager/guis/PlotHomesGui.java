package net.vivatcreative.plotmanager.guis;

import com.intellectualcrafters.plot.object.Plot;
import net.vivatcreative.core.exceptions.WorldNotFoundException;
import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import net.vivatcreative.core.utils.Logger;
import net.vivatcreative.core.utils.PlotUtil;
import net.vivatcreative.core.utils.VivatWorld;
import net.vivatcreative.plotmanager.plotcount.PlotCounter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class PlotHomesGui extends BlueprintGui {

	private final OfflinePlayer target;
	private Plot currentPlot;

	private PlotHomesGui(Player p, OfflinePlayer t) {
		super(p);
		this.target = t;
		GuiItem[] itemList = getItems();
		this.setTitle("&8Plothomes of &3" + t.getName() + " &8[" + itemList.length + "]");
		this.setItems(itemList);
	}

	public static boolean show(Player p, OfflinePlayer target) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(target);
		new PlotHomesGui(p, target).open();
		return true;
	}

	private GuiItem[] getItems() {
		List<Plot> plots = PlotCounter.getAllPlots(target);
		currentPlot = PlotUtil.getCurrentPlot(getPlayer());
		GuiItem[] items = new GuiItem[plots.size()];
		for(int i = 0; i < items.length; i++)
			items[i] = getPlotItem(plots.get(i), i);
		return items;
	}

	private GuiItem getPlotItem(Plot plot, int slot) {
		try {
			return new GuiItem.Builder().material(Material.CONCRETE)
					.data(VivatWorld.fromString(plot.getWorldName()).blockData).name(getName(slot))
					.lore("&7World: &f" + plot.getWorldName().replaceAll("world_", ""),
							"&7ID: &f" + plot.getId().toString(), " ", "&7Click for more actions")
					.hideFlags()
					.click((p) -> Bukkit.dispatchCommand(p, "plotmanager plot " + PlotUtil.formatPlot(plot)))
					.glowing(currentPlot != null && currentPlot.equals(plot)).build();
		} catch (WorldNotFoundException e) {
			Logger.exception(e);
			return new GuiItem.Builder().material(Material.BARRIER).name("&4Error: &cPlease report this issue").build();
		}
	}

	public static String getName(int i) {
		return "&3Plot &f" + (i + 1);
	}

	@Override
	public void updateGui() {}

}
