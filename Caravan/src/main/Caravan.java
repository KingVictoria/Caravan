package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Caravan extends JavaPlugin {
	
	/* TODO:
	 * - Create "Shop" which stores one collection chest and one distribution chest
	 * - 
	 */
	
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
