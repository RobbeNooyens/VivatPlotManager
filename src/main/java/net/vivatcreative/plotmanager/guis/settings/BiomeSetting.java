package net.vivatcreative.plotmanager.guis.settings;

import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

import java.util.Objects;

public class BiomeSetting extends BlueprintGui {

	private final Plot plot;
	private Biome enabledBiome;
	private int currentEnabled;

	public BiomeSetting(Player p, String title, Plot plot) {
		super(p);
		this.setTotalPages(2);
		this.setTitle(title);
		this.plot = plot;
		this.setBiomes();
	}

	public static boolean show(Player player, Plot plot, String title) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(plot);
		Objects.requireNonNull(title);
		new BiomeSetting(player, title, plot).open();
		return true;
	}

	private void setBiomes() {
		String biome = plot.getBiome().toUpperCase();
		int i = 0;
		for (Biome b : Biome.values()) {
			boolean enabled = biome.equalsIgnoreCase(b.name());
			if (enabled) {
				enabledBiome = b;
				this.currentEnabled = i;
			}
			this.setItem(i, getState(b, i, enabled));
			i++;
		}
	}

	private GuiItem getState(Biome b, int i, boolean enabled) {
		return new GuiItem.Builder().name(enabled ? "&7Biome: &a" + b.name() : "&7Biome: &c" + b.name())
				.material(Material.STAINED_GLASS_PANE).data(enabled ? 5 : 14)
				.lore(enabled ? "&7This is the current biome." : "&7Click to make this the active biome.").click((p) -> {
					if (i == currentEnabled) return;
					plot.setBiome(b.toString().toUpperCase(), plot::removeRunning);
					this.setItem(i, getState(b, i, !enabled));
					this.setItem(currentEnabled, getState(enabledBiome, currentEnabled, enabled));
					this.currentEnabled = i;
					this.enabledBiome = b;
					this.open();
				}).build();
	}

	@Override
	public void updateGui() {
	}

}
