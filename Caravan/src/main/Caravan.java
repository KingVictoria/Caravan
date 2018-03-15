package main;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class Caravan extends JavaPlugin {
	
	/* TODO:
	 * ==Phase One==
	 * DONE - Create "Shop" which stores one collection chest and one distribution chest
	 * DONE - Create ReceiptChest extension of CollectionChest with methods related to that
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
		loadShops();
	}
	
	/**
	 * Enabled - Now have safe access to everything
	 */
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		
		// TODO test commands
	}
	
	/**
	 * Disabled - Ensure it closes safely
	 */
	public void onDisable() {
		saveShops();
	}
	
	public void loadShops() {
		
		// TODO load shops
		// ArrayList<Shop> shops = new ArrayList<>();
		
		Shop.shops = new ArrayList<Shop>();
	}
	
	public void saveShops() {
		// TODO save shops
	}

}
