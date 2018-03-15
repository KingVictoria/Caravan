package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop implements ConfigurationSerializable {
	
	public static ArrayList<Shop> shops; // All Shops
	
	private DistributionChest dist;	// Shop Sale Inventory
	private CollectionChest coll;	// Shop Vault Inventory
	
	private ArrayList<ReceiptChest> recps;	// ReceiptChests tied to this Shop
	
	private ItemStack toSell;	// Item to sell
	private int amountToSell;	// Amount of item to sell per sale
	
	private ItemStack toBuy;	// Item to buy
	private int amountToBuy;	// Amount of item to buy per sale
	
	private UUID owner;		// Owner's Unique Id
	private int reference; 	// Shop reference id
	
	/**
	 * Gets a shop by its reference number
	 * @param reference
	 * @return null if there is not shop with that reference number
	 */
	public static Shop getShop(int reference) {
		for(Shop shop: shops) if(shop.reference == reference) return shop;
		return null;
	}
	
	/**
	 * Creates a Shop
	 * @param player Player
	 * @param toSell ItemStack item to sell
	 * @param amountToSell Amount of item to sell
	 * @param toBuy ItemStack item to buy
	 * @param amountToBuy Amount of item to buy
	 */
	public Shop(Player player, ItemStack toSell, int amountToSell, ItemStack toBuy, int amountToBuy) {
		owner = player.getUniqueId();
		
		this.toSell 		= toSell;
		this.amountToSell 	= amountToSell;
		this.toBuy			= toBuy;
		this.amountToBuy 	= amountToBuy;
		
		recps = new ArrayList<>();
		reference = generateReference();
		shops.add(this);
	}
	
	@SuppressWarnings("unchecked")
	public Shop(Map<String, Object> map) {
		
		dist = (DistributionChest) 	new TradeChest((Map<String, Object>) map.get("dist"));
		coll = (CollectionChest) 	new TradeChest((Map<String, Object>) map.get("coll"));
		recps = new ArrayList<ReceiptChest>();
		for(Map<String, Object> recpMap: ((ArrayList<Map<String, Object>>) map.get("recps")))
			recps.add((ReceiptChest) new TradeChest(recpMap));
		
		toSell = ItemStack.deserialize((Map<String, Object>) map.get("tosell"));
		if(map.containsKey("sellmeta")) toSell.setItemMeta(ItemMetaSerializer.deserialize((Map<String, Object>) map.get("sellmeta")));
		amountToSell = (int) map.get("amounttosell");
		
		toBuy = ItemStack.deserialize((Map<String, Object>) map.get("tobuy"));
		if(map.containsKey("buymeta")) toBuy.setItemMeta(ItemMetaSerializer.deserialize((Map<String, Object>) map.get("buymeta")));
		amountToBuy = (int) map.get("amounttobuy");
		
		owner = UUID.fromString((String) map.get("owner"));
		reference = (int) map.get("reference");
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		
		map.put("dist", dist.serialize());
		map.put("coll", coll.serialize());
		ArrayList<Map<String, Object>> serializedRecps = new ArrayList<>();
		for(TradeChest tradeChest: recps) serializedRecps.add(tradeChest.serialize());
		map.put("recps", serializedRecps);
		map.put("tosell", toSell.serialize());
		if(toSell.hasItemMeta()) map.put("sellmeta", ItemMetaSerializer.serialize(toSell.getItemMeta()));
		map.put("amounttosell", amountToSell);
		map.put("tobuy", toBuy.serialize());
		if(toBuy.hasItemMeta()) map.put("buymeta", ItemMetaSerializer.serialize(toBuy.getItemMeta()));
		map.put("amounttobuy", amountToBuy);
		map.put("owner", owner.toString());
		map.put("reference", reference);
		
		return map;
	}
	
	/**
	 * Tells whether a given player is the owner of this shop
	 * @param player Player to test with
	 * @return True if the UUID of the player equals the UUID of this shop's owner
	 */
	public boolean isOwner(Player player) {
		return player.getUniqueId().equals(owner);
	}
	
	/**
	 * Generates a distribution key in format:
	 * Distribution Key
	 * <reference#>
	 * @param player Player owner
	 * @return False if player is not owner
	 */
	public boolean generateDistributionKey(Player player) {
		if(isOwner(player)) { 
			ItemStack key = new ItemStack(Material.PAPER);
			ItemMeta keyMeta = key.getItemMeta();
			keyMeta.setDisplayName("Distribution Key");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(""+reference);
			keyMeta.setLore(lore);
			key.setItemMeta(keyMeta);
			player.getInventory().addItem(key);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generates a collection key in format:
	 * Collection Key
	 * <reference#>
	 * @param player Player owner
	 * @return False if player is not owner
	 */
	public boolean generateCollectionKey(Player player) {
		if(isOwner(player)) { 
			ItemStack key = new ItemStack(Material.PAPER);
			ItemMeta keyMeta = key.getItemMeta();
			keyMeta.setDisplayName("Collection Key");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(""+reference);
			keyMeta.setLore(lore);
			key.setItemMeta(keyMeta);
			player.getInventory().addItem(key);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generates a receipt key in format:
	 * Receipt Key
	 * <reference#>
	 * <#transactions>
	 * @param player Player to generate key for
	 * @param num Number of transactions
	 * @return False if player is owner
	 */
	public boolean generateReceiptKey(Player player, int num) {
		if(!isOwner(player)) { 
			ItemStack key = new ItemStack(Material.PAPER);
			ItemMeta keyMeta = key.getItemMeta();
			keyMeta.setDisplayName("Receipt Key");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(""+reference);
			lore.add(""+num);
			keyMeta.setLore(lore);
			key.setItemMeta(keyMeta);
			player.getInventory().addItem(key);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Makes the DistributionChest for this Shop from a Chest Block and a Sign Block
	 * @param chest Chest Block
	 * @param sign Sign Block
	 */
	public void makeDistributionChest(Chest chest, Sign sign) {
		dist = new DistributionChest(sign, chest, owner, reference, toBuy, amountToBuy);
	}
	
	/**
	 * Makes the CollectionChest for this Shop from a Chest Block and a Sign Block
	 * @param chest Chest Block
	 * @param sign Sign Block
	 */
	public void makeCollectionChest(Chest chest, Sign sign) {
		coll = new CollectionChest(sign, chest, owner, reference, toSell, amountToSell);
	}
	
	/**
	 * Adds a ReceiptChest to this Shop from a Chest Block and a Sign Block
	 * @param chest Chest Block
	 * @param sign Sign Block
	 * @param customer Player who owns the ReceiptChest
	 */
	public void addReceiptChest(Chest chest, Sign sign, Player customer) {
		recps.add(new ReceiptChest(sign, chest, customer.getUniqueId(), this));
	}
	
	/**
	 * Determines whether this shop is available for a trade
	 * @param num Number of Transactions Desired
	 * @return 
	 */
	public boolean transactable(int num) {
		if(coll == null || dist == null) return false;
		
		return coll.transactable(num) && dist.transactable(num);
	}
	
	public int getReferenceNumber() {
		return reference;
	}
	
	/**
	 * Gets the DistributionChest
	 * @return DistributionChest
	 */
	public DistributionChest getDistributionChest() {
		return dist;
	}
	
	/**
	 * Gets the CollectionChest
	 * @return CollectionChest
	 */
	public CollectionChest getCollectionChest() {
		return coll;
	}
	
	/**
	 * Gets the ArrayList of ReceiptChests tied to this shop
	 * @return ArrayList<ReceiptChest>
	 */
	public ArrayList<ReceiptChest> getReceiptChests() {
		return recps;
	}
	
	public ItemStack getBuyItem() {
		return toBuy;
	}
	
	public ItemStack getSellItem() {
		return toSell;
	}
	
	public int getAmountToBuy() {
		return amountToBuy;
	}
	
	public int getAmountToSell() {
		return amountToSell;
	}
	
	/**
	 * Returns true if the player and the reference# inputted are the same as the owner of this shop and their reference#
	 * @param player Player
	 * @param reference Reference number int
	 * @return true if they are the same
	 */
	public boolean validate(Player player, int reference) {
		return player.getUniqueId().equals(owner) && reference == this.reference;
	}

	/**
	 * Generates a unique reference number for this shop
	 * @return Reference Number
	 */
	private int generateReference() {
		int ref = 0;
		for(int i = 0; i < shops.size(); i++) if(ref == shops.get(i).reference) { ref++; i = 0; }
		return ref;
	}
	
	

}
