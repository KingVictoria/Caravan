package main;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A type of TradChest with sells items
 * @author KingVictoria
 */
public class DistributionChest extends TradeChest implements Serializable {
	private static final long serialVersionUID = 6802928152779620567L;

	public DistributionChest(Sign sign, Chest chest, Player owner, int reference, Material mat, int amount) {
		super(sign, chest, owner, reference, mat, amount);
	}

	@Override
	public boolean transactable(int num) {
		int desiredAmount = num * amount;
		
		int availibleAmount = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content != null && content.getType().equals(mat)) availibleAmount += content.getAmount();
		
		return availibleAmount >= desiredAmount;
	}

}
