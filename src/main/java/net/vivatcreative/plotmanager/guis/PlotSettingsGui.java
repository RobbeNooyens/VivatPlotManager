package net.vivatcreative.plotmanager.guis;

import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import net.vivatcreative.plotmanager.guis.settings.BiomeSetting;
import net.vivatcreative.plotmanager.guis.settings.GamemodeSetting;
import net.vivatcreative.plotmanager.guis.settings.TeleportSetting;
import net.vivatcreative.plotmanager.guis.settings.TimeSettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.base.Optional;
import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.PlotWeather;

import java.util.Objects;

public class PlotSettingsGui extends BlueprintGui {

	private final Plot plot;

	public PlotSettingsGui(Player p, Plot plot) {
		super(p);
		this.setTotalPages(3);
		this.setTitle("&8Settings for Plot [" + plot.getId().toCommaSeparatedString() + "]");
		this.plot = plot;
		this.booleanSettings();
		this.otherSettings();
	}

	public static boolean show(Player p, Plot plot) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(plot);
		new PlotSettingsGui(p, plot).open();
		return true;
	}

	private void booleanSettings() {
		// Boolean settings
		int i = 0;
		int[] slots = { 10, 19, 11, 20, 12, 21, 13, 22, 14, 23, 15, 24, 16, 25, 46, 55, 47, 56, 48, 57, 49, 58, 50, 59,
				51, 60, 52, 61 };
		for (BooleanPlotSetting setting : BooleanPlotSetting.values()) {
			this.setItem(slots[i], getBooleanItem(setting));
			i++;
			this.setItem(slots[i], getState(setting, slots[i]));
			i++;
		}
	}
	
	public void otherSettings() {
		// Other settings
		this.setItem((2 * 36) + 11, getGamemode());
		this.setItem((2 * 36) + 12, getTime());
		this.setItem((2 * 36) + 14, getTeleport());
		this.setItem((2 * 36) + 15, getBiome());
	}

	private GuiItem getBooleanItem(BooleanPlotSetting setting) {
		return new GuiItem.Builder().name("&b" + setting.getName()).lore(setting.getDesc())
				.material(setting.getMaterial()).build();
	}

	private boolean enabled(BooleanPlotSetting setting) {
		Optional<?> option = plot.getFlag(setting.getFlag());
		if (!option.isPresent())
			return setting.getDefaultVal();
		else if (setting == BooleanPlotSetting.RAIN)
			return option.get().equals(PlotWeather.RAIN);
		else
			return Boolean.parseBoolean(option.get().toString());
	}

	private GuiItem getState(BooleanPlotSetting setting, int i) {
		boolean enabled = enabled(setting);
		return new GuiItem.Builder().name(enabled ? "&8[&aEnabled&8]" : "&8[&cDisabled&8]")
				.lore("&7Click here to set &f" + setting.getName().toLowerCase() + " &7to " + !enabled + ".")
				.material(Material.STAINED_GLASS_PANE).data(enabled ? 5 : 14).click((p) -> {
					if (setting != BooleanPlotSetting.RAIN)
						plot.setFlag(setting.getFlag(), !enabled);
					else
						plot.setFlag(setting.getFlag(), !enabled ? PlotWeather.RAIN : PlotWeather.CLEAR);
					this.setItem(i, getState(setting, i));
					this.open();
				}).build();
	}

	private GuiItem getGamemode() {
		if (plot.getFlag(Flags.GAMEMODE).isPresent())
			return new GuiItem.Builder().name("&7Gamemode: &b" + plot.getFlag(Flags.GAMEMODE).get().getName())
					.lore("&7Click to change gamemeode.").hideFlags().click((p) -> GamemodeSetting.show(getPlayer(), plot, getTitle())).material(Material.WOOD_PICKAXE).build();
		else
			return new GuiItem.Builder().name("&7Gamemode: &bUnset").lore("&7Click to change gamemeode.").hideFlags()
					.click((p) -> GamemodeSetting.show(getPlayer(), plot, getTitle())).material(Material.WOOD_PICKAXE).build();
	}

	private GuiItem getTime() {
		if (plot.getFlag(Flags.TIME).isPresent())
			return new GuiItem.Builder().name("&7Time: &b" + plot.getFlag(Flags.TIME).get())
					.lore("&7Click to change time.").hideFlags().click((p) -> TimeSettings.show(getPlayer(), plot, getTitle())).material(Material.DOUBLE_PLANT).build();
		else
			return new GuiItem.Builder().name("&7Time: &bUnset").lore("&7Click to change time.").hideFlags()
					.click((p) -> TimeSettings.show(getPlayer(), plot, getTitle())).material(Material.DOUBLE_PLANT).build();
	}

	private GuiItem getTeleport() {
		if (plot.getFlag(Flags.DENY_TELEPORT).isPresent())
			return new GuiItem.Builder().name("&7Deny teleport access: &b" + plot.getFlag(Flags.DENY_TELEPORT).get())
					.lore("&7Click to change denied group.").hideFlags().click((p) -> TeleportSetting.show(getPlayer(), plot, getTitle())).material(Material.ENDER_PEARL).build();
		else
			return new GuiItem.Builder().name("&7Deny teleport access: &bUnset").lore("&7Click to change denied group.")
					.hideFlags().click((p) -> TeleportSetting.show(getPlayer(), plot, getTitle())).material(Material.ENDER_PEARL).build();
	}

	private GuiItem getBiome() {
		return new GuiItem.Builder().name("&7Biome: &b" + plot.getBiome()).lore("&7Click to change biome.").hideFlags()
				.click((p) -> BiomeSetting.show(getPlayer(), plot, getTitle())).material(Material.SAPLING).build();
	}

	@Override
	public void updateGui() {
	}

}
