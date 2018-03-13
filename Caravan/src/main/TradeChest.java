package main;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Abstract TradeChest class from which the two types (Collection and Distribution) extend
 * @author KingVictoria
 */
public abstract class TradeChest implements Serializable {
	private static final long serialVersionUID = 7521932815577470025L;
	
	/**
	 * Serializable Location for Location Serialization
	 * @author KingVictoria
	 */
	class SerializableLocation implements Serializable {
		private static final long serialVersionUID = -4669465984456536290L;

		int x, y, z;	// Coordinates
		String world;	// World Name
		
		SerializableLocation(Location loc) {
			x = loc.getBlockX();
			y = loc.getBlockY();
			z = loc.getBlockZ();
			world = loc.getWorld().getName();
		}
		
		Location getLocation() {
			return new Location(Bukkit.getWorld(world), x, y, z);
		}
	}
	
	SerializableLocation signLocation;	// Location of the Chest Block
	SerializableLocation chestLocation;	// Location of the Sign Block
	
	int reference;	// Reference number of the shop this TradeChest is associated with
	UUID owner;		// Unique Identification of owner
	
	ItemMetaSerializer meta;	// Serialzed meta of item
	Material mat;				// Material of item
	int amount;					// Amount of item to exchange
	
	/**
	 * Creates a generic TradeChest
	 * @param sign Sign Block
	 * @param chest Chest Block
	 * @param owner Owner of the TradeChest as Entity Player
	 * @param reference Reference Number of TradeChest
	 */
	public TradeChest(Sign sign, Chest chest, UUID owner, int reference, ItemMeta meta, Material mat, int amount) {
		signLocation = new SerializableLocation(sign.getLocation());
		chestLocation = new SerializableLocation(chest.getLocation());
		
		this.owner = owner;
		this.reference = reference;
		
		this.meta = ItemMetaSerializer.serialize(meta, mat);
		this.mat = mat;
		this.amount = amount;
	}
	
	/**
	 * Gets the Chest Block of this TradeChest
	 * @return
	 */
	public Chest getChest() {
		return (Chest) chestLocation.getLocation().getBlock().getState();
	}
	
	/**
	 * Gets the Sign Block of this TradeChest
	 * @return
	 */
	public Sign getSign() {
		return (Sign) signLocation.getLocation().getBlock().getState();
	}
	
	/**
	 * Gets the ItemMeta of the traded item
	 * @return ItemMeta
	 */
	public ItemMeta getItemMeta() {
		return ItemMetaSerializer.deSerialize(meta);
	}
	
	/**
	 * Determines whether this TradeChest is available for a given trade
	 * @param num Number of transactions desired
	 * @return True if able to be used for trade
	 */
	public abstract boolean transactable(int num);

}
