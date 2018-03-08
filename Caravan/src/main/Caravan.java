package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Caravan extends JavaPlugin {
	
	public static ArrayList<TradeChest> tradeChests = new ArrayList<>();
	
	/**
	 * Initialization
	 */
	public void onLoad() {
		
	}
	
	/**
	 * Enabled - Now have safe access to everything
	 */
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
	}
	
	/**
	 * Disabled - Ensure it closes safely
	 */
	public void onDisable() {
		
	}

}
