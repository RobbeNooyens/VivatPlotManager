package net.vivatcreative.plotmanager.guis.settings;

import net.vivatcreative.core.gui.BlueprintGui;
import net.vivatcreative.core.gui.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;

import java.util.Objects;

public class TimeSettings extends BlueprintGui {

	private final Plot plot;
	private final int[] slots = {10,11,12,13,14,15,16,20,21,22,23,24};
	private final int[] times = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22 };

	public TimeSettings(Player p, String title, Plot plot) {
		super(p);
		this.setTitle(title);
		this.plot = plot;
		this.setTimeItems();
	}

	public static boolean show(Player player, Plot plot, String title) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(plot);
		Objects.requireNonNull(title);
		new TimeSettings(player, title, plot).open();
		return true;
	}

	private void setTimeItems() {
		long current = plot.getFlag(Flags.TIME).isPresent() ? plot.getFlag(Flags.TIME).get() : 18000;
		for (int i = 0; i < times.length; i++) {
			long mcTime = (18000 + times[i] * 1000);
			this.setItem(slots[i], (mcTime == current) ? getTime(times[i], mcTime, true) : getTime(times[i], mcTime, false));
		}
	}

	private GuiItem getTime(int hour, long mcTime, boolean current) {
		int color = current ? 5 : 14;
		String chatColor = current ? "&a" : "&c";
		return new GuiItem.Builder().name(chatColor + hour + ":00").click((p) -> {
			plot.setFlag(Flags.TIME, mcTime);
			this.setTimeItems();
			open();
		}).material(Material.STAINED_GLASS_PANE).data(color).build();

	}

	@Override
	public void updateGui() {
		
	}

}
