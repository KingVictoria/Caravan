package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop implements Serializable {
	private static final long serialVersionUID = -5465645480327211480L;
	
	public static ArrayList<Shop> shops; // All Shops
	
	private DistributionChest dist;	// Shop Sale Inventory
	private CollectionChest coll;	// Shop Vault Inventory
	
	private ArrayList<ReceiptChest> recps;	// ReceiptChests tied to this Shop
	
	private ItemMetaSerializer metaToSell;	// Item being sold serialized
	private Material matToSell;				// Type of thing sold at this shop
	private int amountToSell;				// Amount of thing to sell per sale
	
	private ItemMetaSerializer metaToBuy;	// Item being bought serialized
	private Material matToBuy;				// Type of thing bought by this shop
	private int amountToBuy;				// Amount of thing to buy per sale
	
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
	 * Creates a Shop with a kind of material to sell and a kind of material to buy with amounts for each per sale
	 * @param player Owner of the shop
	 * @param itemToSell Sold item
	 * @param amountToSell Sold item quantity per sale
	 * @param itemToBuy Bought item
	 * @param amountToBuy Bought item quantity per sale
	 */
	public Shop(Player player, ItemMeta metaToSell, Material matToSell, int amountToSell, ItemMeta metaToBuy, Material matToBuy, int amountToBuy) {
		owner = player.getUniqueId();
		
		this.metaToSell = ItemMetaSerializer.serialize(metaToSell, matToSell);
		this.matToSell = matToSell;
		this.amountToSell = amountToSell;
		
		this.metaToBuy = ItemMetaSerializer.serialize(metaToBuy, matToBuy);
		this.matToBuy = matToBuy;
		this.amountToBuy = amountToBuy;
		
		recps = new ArrayList<>();
		reference = generateReference();
		shops.add(this);
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
		dist = new DistributionChest(sign, chest, owner, reference, ItemMetaSerializer.deSerialize(metaToBuy), matToBuy, amountToBuy);
	}
	
	/**
	 * Makes the CollectionChest for this Shop from a Chest Block and a Sign Block
	 * @param chest Chest Block
	 * @param sign Sign Block
	 */
	public void makeCollectionChest(Chest chest, Sign sign) {
		coll = new CollectionChest(sign, chest, owner, reference, ItemMetaSerializer.deSerialize(metaToSell), matToSell, amountToSell);
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
		return coll.transactable(num) && dist.transactable(num);
	}
	
	public int getReferenceNumber() {
		return reference;
	}
	
	public ItemMeta getSellMeta() {
		return ItemMetaSerializer.deSerialize(metaToSell);
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
	
	public Material getBuyMaterial() {
		return matToBuy;
	}
	
	public int getAmountToBuy() {
		return amountToBuy;
	}
	
	public Material getSellMaterial() {
		return matToSell;
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
