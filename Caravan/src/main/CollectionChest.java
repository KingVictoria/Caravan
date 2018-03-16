package main;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * A type of TradeChest which receives items. Can be part of a selling pair or a buyer's receipt chest.
 * @author KingVictoria
 */
public class CollectionChest extends TradeChest implements ConfigurationSerializable {

	public CollectionChest(Sign sign, Chest chest, UUID owner, int reference, ItemStack item, int amount) {
		super(sign, chest, owner, reference, item, amount);
		
		sign.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[COLL]");
		update();
	}
	
	public CollectionChest(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean transactable(int num) {		
		int requiredSlots = (getAmount() * num) / getItem().getType().getMaxStackSize();
		if(getAmount() * num % getItem().getType().getMaxStackSize() > 0) requiredSlots++;
		
		int availibleSlots = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content == null) availibleSlots++;
		
		return availibleSlots >= requiredSlots;
	}

}
