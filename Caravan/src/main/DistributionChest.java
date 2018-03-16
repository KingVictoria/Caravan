package main;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * A type of TradChest with sells items
 * @author KingVictoria
 */
public class DistributionChest extends TradeChest implements ConfigurationSerializable {

	public DistributionChest(Sign sign, Chest chest, UUID owner, int reference, ItemStack item, int amount) {
		super(sign, chest, owner, reference, item, amount);
		
		sign.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[DIST]");
		update();
	}

	public DistributionChest(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean transactable(int num) {
		int desiredAmount = num * getAmount();
		
		int availibleAmount = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content != null && content.isSimilar(getItem())) {
			availibleAmount += content.getAmount();
		}
		return availibleAmount >= desiredAmount;
	}

}
