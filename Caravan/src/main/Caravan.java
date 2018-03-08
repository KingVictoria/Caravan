package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Caravan extends JavaPlugin {
	
	/* TODO:
	 * ==Phase One==
	 * - Create "Shop" which stores one collection chest and one distribution chest
	 * - Create ReceiptChest extension of CollectionChest with methods related to that
	 * - Set up rudimentary test which sends items from Shop to ReceiptChest
	 * ==Phase Two==
	 * - Create TradeStation and Terminal
	 * - Create Trade Lanes
	 * - Create UI and Search Function
	 * - Create Receipt Generation
	 * - Set up test
	 * ==Phase Three==
	 * - Add costs for everything
	 * - Create settings document to easily toy with values and costs
	 * - Bug test for glitches
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
