package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class Shop implements Serializable {
	private static final long serialVersionUID = -5465645480327211480L;
	
	public static ArrayList<Shop> shops; // All Shops
	
	private DistributionChest dist;	// Shop Sale Inventory
	private CollectionChest coll;	// Shop Vault Inventory
	
	private Material toSell;	// Type of thing sold at this shop
	private int amountToSell;	// Amount of thing to sell per sale
	
	private Material toBuy;		// Type of thing bought by this shop
	private int amountToBuy;	// Amount of thing to buy per sale
	
	private UUID owner;		// Owner's Unique Id
	private int reference; 	// Shop reference id
	
	/**
	 * Creates a Shop with a kind of material to sell and a kind of material to buy with amounts for each per sale
	 * @param player Owner of the shop
	 * @param toSell Sold item material
	 * @param amountToSell Sold item quantity per sale
	 * @param toBuy Bought item material
	 * @param amountToBuy Bought item quantity per sale
	 */
	public Shop(Player player, Material toSell, int amountToSell, Material toBuy, int amountToBuy) {
		owner = player.getUniqueId();
		this.toSell = toSell;
		this.amountToSell = amountToSell;
		this.toBuy = toBuy;
		this.amountToBuy = amountToBuy;
		
		reference = generateReference();
		shops.add(this);
	}
	
	public void makeDistributionChest(Chest chest, Sign sign) {
		dist = new DistributionChest(sign, chest, owner, reference, toSell, amountToSell);
	}
	
	public void makeCollectionChest(Chest chest, Sign sign) {
		coll = new CollectionChest(sign, chest, owner, reference, toBuy, amountToBuy);
	}
	
	public DistributionChest getDistributionChest() {
		return dist;
	}
	
	public CollectionChest getCollectionChest() {
		return coll;
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
