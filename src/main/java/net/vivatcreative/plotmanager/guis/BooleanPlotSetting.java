package net.vivatcreative.plotmanager.guis;

import org.bukkit.Material;

import com.intellectualcrafters.plot.flag.Flag;
import com.intellectualcrafters.plot.flag.Flags;

public enum BooleanPlotSetting implements PlotSetting {
	
	NOTIFY_ENTER("Notify Enter", false, Material.GOLD_NUGGET, Flags.NOTIFY_ENTER, "&7If enabled, you'll get notified whenever", "&7someone enters your plot."),
	RAIN("Rain", false, Material.WATER_BUCKET, Flags.WEATHER, "&7If enabled, rain will fall down", "&7from the sky on your plot."),
	ITEM_DROP("Item Drop", true, Material.DROPPER, Flags.ITEM_DROP, "&7If enabled, players will be able to", "&7drop items on your plot."),
	PLAYER_INTERACT("Player Interact", false, Material.DIAMOND_HOE, Flags.PLAYER_INTERACT, "&7If enabled, players can interact with", "&7items on your plot."),
	REDSTONE("Redstone", true, Material.REDSTONE, Flags.REDSTONE, "&7If enabled, complex redstone will be possible", "�7on your plot."),
	DEVICE_INTERACT("Device Interact", false, Material.GOLD_PLATE, Flags.DEVICE_INTERACT, "&7If enabled, players will be able to use", "&7devices like pressure plates."),
	LIQUID_FLOW("Liquid Flow", true, Material.LAVA_BUCKET, Flags.LIQUID_FLOW, "&7If enabled, water and lava will flow."),
	FIRE_SPREAD("Fire Spread", false, Material.FLINT_AND_STEEL, Flags.FIRE_SPREAD, "&7If enabled, fire can spread."),
	FORCE_FIELD("Forcefield", false, Material.SHIELD, Flags.FORCEFIELD, "&7If enabled, players not added to the plot will be", "&7thrown back when they get close to a player", "&7who's added to the plot."),
	NO_WORLDEDIT("No Worldedit", false, Material.GRASS, Flags.NO_WORLDEDIT, "&7If enabled, even owners and members won't", "&7be able to use worldedit on your plot."),
	PLACE("Place", false, Material.DISPENSER, Flags.PLACE, "&7If enabled, players will be able to place", "&7blocks on your plot."),
	BREAK("Break", false, Material.DIAMOND_PICKAXE, Flags.BREAK, "&7If enabled, players will be able to break", "&7blocks on your plot."),
	PVP("PvP", false, Material.DIAMOND_SWORD, Flags.PVP, "&7If enabled, PvP will be turned on."),
	DROP_PROTECTION("Drop Protection", false, Material.HOPPER, Flags.DROP_PROTECTION, "&7If enabled, players without permission won't", "&7be able to pickup items on your plot.");
	
	private final String name;
	private final String[] description;
	private final boolean val;
	private final Material mat;
	private final Flag<?> flag;
	
	BooleanPlotSetting(String name, boolean defaultVal, Material mat, Flag<?> flag, String... desc){
		this.description = desc;
		this.name = name;
		this.val = defaultVal;
		this.mat = mat;
		this.flag = flag;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getDesc() {
		return description;
	}
	
	public boolean getDefaultVal() {
		return val;
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public Flag<?> getFlag(){
		return flag;
	}

}
