package net.vivatcreative.plotmanager.guis.settings;

import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.PlotGameMode;
import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GamemodeSetting extends BlueprintGui {

	//private final int[] times = {0,2,4,6,8,10,12,14,16,18,20,22,24};
	private final int[] slots = { 11, 20, 12, 21, 13, 22, 14, 23, 15, 24 };
	private final PlotGameMode[] modes = { PlotGameMode.SURVIVAL, PlotGameMode.CREATIVE, PlotGameMode.ADVENTURE,
			PlotGameMode.SPECTATOR, PlotGameMode.NOT_SET };
	private Plot plot;
	private PlotGameMode current;
	int currentSlot;

	public GamemodeSetting(Player p, String title, Plot plot) {
		super(p);
		this.setTitle(title);
		this.plot = plot;
		this.setGamemodes();
	}

	public static boolean show(Player player, Plot plot, String title) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(plot);
		Objects.requireNonNull(title);
		new GamemodeSetting(player, title, plot).open();
		return true;
	}

	private void setGamemodes() {
		if (!plot.getFlag(Flags.GAMEMODE).isPresent())
			current = PlotGameMode.NOT_SET;
		else
			current = plot.getFlag(Flags.GAMEMODE).get();
		for (int i = 0; i < modes.length; i++) {
			boolean enabled = current == modes[i];
			if (enabled) this.currentSlot = slots[2 * i + 1];
			this.setItem(slots[2 * i], getGamemode(modes[i]));
			this.setItem(slots[2 * i + 1], getState(modes[i], slots[2 * i + 1], enabled));
		}
	}

	private GuiItem getGamemode(PlotGameMode mode) {
		return new GuiItem.Builder().name("&7Gamemode: &b" + mode.getName()).material(getMaterial(mode)).hideFlags()
				.lore("&7If enabled, players will be put in",
						"&7gamemode &f" + mode.getName() + "&7 when they enter your plot.")
				.build();
	}

	private GuiItem getState(PlotGameMode mode, int slot, boolean enabled) {
		return new GuiItem.Builder().name(enabled ? "&8[&aEnabled&8]" : "&8[&cDisabled&8]")
				.material(Material.STAINED_GLASS_PANE).data(enabled ? 5 : 14)
				.lore(enabled ? "&7This is the current Gamemode."
						: "&7Click to set Gamemode to &f" + mode.getName() + "&7")
				.click((p) -> {
					if (enabled) return;
					int pSlot = currentSlot;
					PlotGameMode pMode = current;
					currentSlot = slot;
					current = mode;
					this.setItem(pSlot, getState(pMode, pSlot, false));
					this.setItem(slot, getState(mode, slot, true));
					if (mode.equals(PlotGameMode.NOT_SET))
						plot.removeFlag(Flags.GAMEMODE);
					else
						plot.setFlag(Flags.GAMEMODE, mode);
					open();
				}).build();
	}

	private Material getMaterial(PlotGameMode mode) {
		try {
			switch (mode) {
			case ADVENTURE:
				return Material.WOOD_SWORD;
			case CREATIVE:
				return Material.BRICK;
			case NOT_SET:
				return Material.STRUCTURE_VOID;
			case SPECTATOR:
				return Material.COMPASS;
			case SURVIVAL:
				return Material.WOOD_PICKAXE;
			default:
				return null;
			}
		} catch (IllegalStateException e) {
			return Material.STRUCTURE_VOID;
		}
	}

	@Override
	public void updateGui() {}

}
