package main;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class ReceiptChest extends CollectionChest implements ConfigurationSerializable {

	public ReceiptChest(Sign sign, Chest chest, UUID owner, Shop shop) {
		super(sign, chest, owner, shop.getReferenceNumber(), shop.getSellItem(), shop.getAmountToSell());
		
		sign.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[RECP]");
		update();
	}
	
	/**
	 * Checks to see if the owner of this ReceiptChest has put in enough for the trade to go through
	 * @param num Number of desired transactions
	 * @return
	 */
	public boolean enoughDosh(int num) {
		Shop shop = Shop.getShop(getReference());
		
		int desiredAmount = num * shop.getAmountToBuy();
		
		int availibleAmount = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content != null && content.isSimilar(shop.getBuyItem())) availibleAmount += content.getAmount();
		
		return availibleAmount >= desiredAmount;
	}
	
	@Override
	public void update() {
		super.update();
		
		if(enoughDosh(1) && transactable(1) && Shop.getShop(getReference()).transactable(1)) 
			getSign().setLine(3, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Can Trade");
		else getSign().setLine(3, ChatColor.GRAY + "" + ChatColor.BOLD + "Cannot Trade");
		
		getSign().update();
	}

}
