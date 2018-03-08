package main;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A type of TradeChest which receives items. Can be part of a selling pair or a buyer's receipt chest.
 * @author KingVictoria
 */
public class CollectionChest extends TradeChest implements Serializable {
	private static final long serialVersionUID = -7617422746413144920L;
	
	boolean selling;	// Determines whether this collection chest is part of a selling pair or is a buyer's receipt

	public CollectionChest(Sign sign, Chest chest, Player owner, int reference, Material mat, int amount, boolean selling) {
		super(sign, chest, owner, reference, mat, amount);
		this.selling = selling;
	}

	@Override
	public boolean transactable(int num) {
		int requiredSlots = (amount * num) / mat.getMaxStackSize();
		if(amount * num % mat.getMaxStackSize() > 0) requiredSlots++;
		
		int availibleSlots = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content == null) availibleSlots++;
		
		return availibleSlots >= requiredSlots;
	}

}
