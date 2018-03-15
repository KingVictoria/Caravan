package main;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * Abstract TradeChest class from which the two types (Collection and Distribution) extend
 * @author KingVictoria
 */
public class TradeChest implements ConfigurationSerializable {

	private Sign sign;		// Sign block
	private Chest chest;	// Chest block
	
	private int reference;	// Reference number of the shop this TradeChest is associated with
	private UUID owner;		// Unique Identification of owner
	
	private ItemStack item;	// Item to trade
	private int amount;		// Amount of item to exchange
	
	/**
	 * Goes through all the TradeChests to see if a given Chest Block matches
	 * @param chest
	 * @return
	 */
	public static boolean isTradeChest(Chest chest) {
		for(Shop shop: Shop.shops) {
			if(shop.getDistributionChest().getChest().equals(chest)) return true;
			if(shop.getCollectionChest().getChest().equals(chest)) return true;
			for(ReceiptChest rc: shop.getReceiptChests()) if(rc.getChest().equals(chest)) return true;
		}
		
		return false;
	}
	
	/**
	 * Goes through all the TradeChests to see if a given Chest Block matches and returns the TradeChest if so
	 * @param chest
	 * @return
	 */
	public static TradeChest getTradeChest(Chest chest) {
		for(Shop shop: Shop.shops) {
			if(shop.getDistributionChest().getChest().equals(chest)) return shop.getDistributionChest();
			if(shop.getCollectionChest().getChest().equals(chest)) return shop.getCollectionChest();
			for(ReceiptChest rc: shop.getReceiptChests()) if(rc.getChest().equals(chest)) return rc;
		}
		
		return null;
	}
	
	/**
	 * Creates a generic TradeChest
	 * @param sign Sign Block
	 * @param chest Chest Block
	 * @param owner Owner UUID
	 * @param reference Reference Number of the shop
	 * @param item ItemStack item to trade
	 * @param amount Amount of item to exchange
	 */
	public TradeChest(Sign sign, Chest chest, UUID owner, int reference, ItemStack item, int amount) {
		this.sign 		= sign;
		this.chest 		= chest;
		this.reference 	= reference;
		this.owner 		= owner;
		this.item 		= item;
		this.amount 	= amount;
	}
	
	/**
	 * Creates a TradeChest from serial
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public TradeChest(Map<String, Object> map) {
		sign 		= 	(Sign) 	Location.deserialize((Map<String, Object>) map.get("signloc")).getBlock().getState();
		chest 		= 	(Chest) Location.deserialize((Map<String, Object>) map.get("chestloc")).getBlock().getState();
		reference	=	(int)	map.get("reference");
		owner		=	UUID.fromString((String) map.get("owner"));
		item		=	ItemStack.deserialize((Map<String, Object>) map.get("item"));
		if(map.containsKey("meta")) item.setItemMeta(ItemMetaSerializer.deserialize((Map<String, Object>) map.get("meta")));
		amount		=	(int) map.get("amount");
	}
	
	public int getReference() {
		return reference;
	}
	
	/**
	 * Gets the Chest Block of this TradeChest
	 * @return
	 */
	public Chest getChest() {
		return chest;
	}
	
	/**
	 * Gets the Sign Block of this TradeChest
	 * @return
	 */
	public Sign getSign() {
		return sign;
	}
	
	/**
	 * Gets the Item to be traded here
	 * @return ItemStack item
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Gets amount of an item to be traded per transaction
	 * @return int amount
	 */
	public int getAmount() {
		return amount;
	}
	
	public int getContent() {
		int content = 0;
		
		for(ItemStack item: chest.getInventory().getContents()) if(item != null && item.isSimilar(getItem())) content += item.getAmount();
		
		return content;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		
		map.put("signloc", sign.getLocation().serialize());
		map.put("chestloc", chest.getLocation().serialize());
		map.put("reference", reference);
		map.put("owner", owner.toString());
		map.put("item", item.serialize());
		if(item.hasItemMeta()) map.put("meta", item.getItemMeta().serialize());
		map.put("amount", amount);
		
		return map;
	}
	
	/**
	 * Determines whether this TradeChest is available for a given trade
	 * @param num Number of transactions desired
	 * @return True if able to be used for trade
	 */
	public boolean transactable(int num) { return true; }
	
	/**
	 * Updates the TradeChest Sign
	 */
	public void update() { 
		sign.setLine(1, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + getItem().getType().name() + ": " + ChatColor.LIGHT_PURPLE + getAmount());
		if(transactable(1)) sign.setLine(2, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "In Store: " + ChatColor.GREEN + getContent());
		else sign.setLine(2, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "In Store: " + ChatColor.GREEN + getContent());
		if(Shop.getShop(reference).transactable(1)) sign.setLine(3, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Shop Active");
		else sign.setLine(3, ChatColor.GRAY + "" + ChatColor.BOLD + "Shop Inactive");
		
		sign.update();
	}

}
