package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Caravan extends JavaPlugin {
	
	private static Caravan instance;
	
	public static Caravan getInstance() {
		return instance;
	}
	
	/**
	 * Initialization
	 */
	public void onLoad() {
		instance = this;
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
		
		getCommand("cvn_make_shop").setExecutor((sender, cmd, label, args) -> {
			if(args.length != 2 || !(sender instanceof Player)) return false;
			int amountToSell, amountToBuy;
			Player player = (Player) sender;
			if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getAmount() == 0) return false;
			if(player.getInventory().getItemInOffHand()  == null || player.getInventory().getItemInOffHand().getAmount()  == 0) return false;
			try { amountToSell = Integer.parseInt(args[0]); amountToBuy = Integer.parseInt(args[1]); } catch (Exception e) { return false; }
			Shop shop = new Shop(player, player.getInventory().getItemInMainHand(), amountToSell, 
									  	 player.getInventory().getItemInOffHand(), amountToBuy);
			shop.generateDistributionKey(player);
			shop.generateCollectionKey(player);
			return true;
		});
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
		
		if(!myConfig.contains("shops")) {
			Shop.shops = new ArrayList<Shop>();
			saveShops();
			return;
		}
		
		Shop.shops = new ArrayList<Shop>();
		ArrayList<Map<String, Object>> maps =  (ArrayList<Map<String, Object>>) myConfig.getList("shops");
		for(Map map: maps) {
			Shop shop = new Shop(map);
			shop.shops.add(shop);
		}
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
			try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
			saveDefaultConfig();
		} else getLogger().info("config.yml found, loading!");
	}

	/**
	 * Saves the shop data
	 */
	public void saveShops() {
		FileConfiguration myConfig = this.getConfig();
		
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		for(Shop shop: Shop.shops)
			maps.add(shop.serialize());
		
		myConfig.set("shops", maps);
		
		File file = new File(getDataFolder(), "config.yml");
		try {
			myConfig.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}