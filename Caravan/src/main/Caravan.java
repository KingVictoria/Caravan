package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
		
	}
	
	/**
	 * Enabled - Now have safe access to everything
	 */
	public void onEnable() {
		ConfigurationSerialization.registerClass(Shop.class);
		ConfigurationSerialization.registerClass(TradeChest.class);
		ConfigurationSerialization.registerClass(DistributionChest.class);
		ConfigurationSerialization.registerClass(CollectionChest.class);
		ConfigurationSerialization.registerClass(ReceiptChest.class);
		
		loadShops();
		
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		
		// TODO test commands
	}
	
	/**
	 * Disabled - Ensure it closes safely
	 */
	public void onDisable() {
		saveShops();
	}
	
	/**
	 * Loads the shop data
	 */
	@SuppressWarnings("unchecked")
	public void loadShops() {
		checkData();
		FileConfiguration myConfig = this.getConfig();	
		
		if(!myConfig.contains("shops")) Shop.shops = new ArrayList<Shop>();
		
		Shop.shops = (ArrayList<Shop>) myConfig.get("shops");
	}
	
	/**
	 * Ensures the configuration folder exists
	 */
	private void checkData() {
		if(!getDataFolder().exists()) {
			getLogger().info("[" + getDescription().getName() + "] data folders not found, creating!");
			getDataFolder().mkdirs();
		}
		
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			getLogger().info("config.yml not found, creating!");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			saveDefaultConfig();
		} else getLogger().info("config.yml found, loading!");
	}

	/**
	 * Saves the shop data
	 */
	public void saveShops() {
		FileConfiguration myConfig = this.getConfig();
		
		myConfig.set("shops", Shop.shops);
	}

}