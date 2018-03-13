package main;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A type of TradChest with sells items
 * @author KingVictoria
 */
public class DistributionChest extends TradeChest implements Serializable {
	private static final long serialVersionUID = 6802928152779620567L;

	public DistributionChest(Sign sign, Chest chest, UUID owner, int reference, ItemMeta meta, Material mat, int amount) {
		super(sign, chest, owner, reference, meta, mat, amount);
		
		sign.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[DIST]");
		sign.setLine(1, ChatColor.WHITE + "" + ChatColor.BOLD + reference);
		sign.setLine(2, mat.name() + " " + amount);
		sign.setLine(3, ChatColor.DARK_RED + "" + ChatColor.BOLD + "inactive");
		sign.update();
	}

	@Override
	public boolean transactable(int num) {
		int desiredAmount = num * amount;
		
		int availibleAmount = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content != null && content.getType().equals(mat))
			if(content.getItemMeta().equals(ItemMetaSerializer.deSerialize(meta))) availibleAmount += content.getAmount();
		
		return availibleAmount >= desiredAmount;
	}

}
