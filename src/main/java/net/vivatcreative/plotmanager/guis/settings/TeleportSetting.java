package net.vivatcreative.plotmanager.guis.settings;

import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;

import java.util.Objects;

public class TeleportSetting extends BlueprintGui {

	//private final int[] times = {0,2,4,6,8,10,12,14,16,18,20,22,24};
	private final int[] slots = { 10, 19, 11, 20, 12, 21, 14, 23, 15, 24, 16, 25, 17, 26 };
	private final String[] modes = { null, "trusted", "members", "nonmembers", "nontrusted", "nonowners" };
	private Plot plot;
	private String current;
	private int currentSlot;

	public TeleportSetting(Player p, String title, Plot plot) {
		super(p);
		this.setTitle(title);
		this.plot = plot;
		this.setTeleportOptions();
	}

	public static boolean show(Player player, Plot plot, String title) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(plot);
		Objects.requireNonNull(title);
		new TeleportSetting(player, title, plot).open();
		return true;
	}

	private void setTeleportOptions() {
		if (!plot.getFlag(Flags.DENY_TELEPORT).isPresent())
			current = null;
		else
			current = plot.getFlag(Flags.DENY_TELEPORT).get();
		for (int i = 0; i < modes.length; i++) {
			boolean enabled = current == modes[i];
			if (enabled) this.currentSlot = slots[2 * i + 1];
			this.setItem(slots[2 * i], getTeleportSetting(modes[i]));
			this.setItem(slots[2 * i + 1], getState(modes[i], slots[2 * i + 1], enabled));
		}
	}

	private GuiItem getTeleportSetting(String state) {
		String name = state == null ? "unset" : state;
		return new GuiItem.Builder().name("&7Denied access for: &b" + name).material(getMaterial(state)).hideFlags()
				.lore("&7If enabled, this group won't be able", "&7to teleport to your plot.").build();
	}

	private GuiItem getState(String state, int slot, boolean enabled) {
		return new GuiItem.Builder().name(enabled ? "&8[&aEnabled&8]" : "&8[&cDisabled&8]")
				.material(Material.STAINED_GLASS_PANE).data(enabled ? 5 : 14)
				.lore(enabled ? "&7This group can't teleport to your plot."
						: "&7Click to block visiting for &f" + state)
				.click((p) -> {
					if (enabled) return;
					int pSlot = currentSlot;
					String pState = current;
					this.currentSlot = slot;
					this.current = state;
					this.setItem(pSlot, getState(pState, pSlot, false));
					this.setItem(slot, getState(state, slot, true));
					if (state == null)
						plot.removeFlag(Flags.DENY_TELEPORT);
					else
						plot.setFlag(Flags.DENY_TELEPORT, state);
					open();
				}).build();
	}

	private Material getMaterial(String state) {
		if (state == null) return Material.GOLD_ORE;
		try {
			switch (state) {
			case "trusted":
				return Material.GOLD_PLATE;
			case "members":
				return Material.GOLD_NUGGET;
			case "nonmembers":
				return Material.GOLD_INGOT;
			case "nontrusted":
				return Material.GOLDEN_APPLE;
			case "nonowners":
				return Material.GOLD_BLOCK;
			default:
				return Material.GOLD_ORE;
			}
		} catch (IllegalStateException e) {
			return Material.GOLD_ORE;
		}
	}

	@Override
	public void updateGui() {}

}