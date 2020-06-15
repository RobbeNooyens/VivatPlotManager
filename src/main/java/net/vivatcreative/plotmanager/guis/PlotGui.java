package net.vivatcreative.plotmanager.guis;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import net.vivatcreative.core.players.Users;
import net.vivatcreative.core.utils.PlotUtil;
import net.vivatcreative.core.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

public class PlotGui extends BlueprintGui {

	private final Plot plot;
	private OfflinePlayer plotOwner;

	public PlotGui(Player p, Plot plot) {
		super(p);
		this.setTitle(String.format("&8Plot: [%s] - %s", plot.getId().toCommaSeparatedString(), plot.getWorldName().replaceAll("world_", "")));
		this.plot = plot;
		if (plot.getOwners() != null && plot.getOwners().size() > 0) {
			boolean isOwner = plot.getOwners().contains(p.getUniqueId());
			plotOwner = isOwner ? p : Bukkit.getOfflinePlayer(plot.getOwners().iterator().next());
			this.setItem(4, getOwnerHead());
			this.setItem(20, getTeleport());
			this.setItem(24, isOwner ? getSettings() : null);
		}
	}

	public static boolean show(Player player, Plot plot) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(plot);
		if(plot.getOwners() == null || plot.getOwners().size() == 0) return true;
		new PlotGui(player, plot).open();
		return true;
	}

	private GuiItem getOwnerHead() {
		return new GuiItem.Builder().playerSkull(plotOwner.getName())
				.name(plot.getOwners().size() == 1 ? "&7Plot Owner:" : "&7Plot Owners:").lore(getLore()).hideFlags().build();
	}

	private GuiItem getTeleport() {
		return new GuiItem.Builder().material(Material.ENDER_PEARL).name("&bTeleport")
				.lore("&7Click to teleport to this plot.")
				.click((p) -> PlotPlayer.wrap(p).teleport(plot.getHome())).hideFlags().build();
	}

	private GuiItem getSettings() {
		return new GuiItem.Builder().material(Material.BOOK_AND_QUILL).name("&bPlot Settings")
				.click((p) -> Bukkit.dispatchCommand(p, "plotmanager settings " + PlotUtil.formatPlot(plot)))
				.hideFlags().build();
	}

	private ArrayList<String> getLore() {
		ArrayList<String> lore = new ArrayList<>();
		for (UUID uuid : plot.getOwners()) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			lore.add(TextUtil.toColor("&7|  " + Users.get(player).getBuildRank().getName(true) + " &f" + player.getName()));
		}
		if (plot.getMembers() == null || plot.getMembers().size() == 0) return lore;
		lore.add("&7Members:");
		for (UUID uuid : plot.getMembers()) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			lore.add(TextUtil.toColor("&7|  " + Users.get(player).getBuildRank().getName(true) + " &f" + player.getName()));
		}
		return lore;
	}

	@Override
	public void updateGui() {}

}
