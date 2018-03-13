package main;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

public class ReceiptChest extends CollectionChest implements Serializable {
	private static final long serialVersionUID = 8356995416122273029L;

	public ReceiptChest(Sign sign, Chest chest, UUID owner, Shop shop) {
		super(sign, chest, owner, shop.getReferenceNumber(), shop.getSellMeta(), shop.getSellMaterial(), shop.getAmountToSell());
		
		// TODO format sign
	}
	
	/**
	 * Checks to see if the owner of this ReceiptChest has put in enough for the trade to go through
	 * @param num Number of desired transactions
	 * @return
	 */
	public boolean enoughDosh(int num) {
		Shop shop = Shop.getShop(reference);
		
		int desiredAmount = num * shop.getAmountToBuy();
		
		int availibleAmount = 0;
		for(ItemStack content: getChest().getInventory().getContents()) if(content != null && content.getType().equals(shop.getBuyMaterial())) availibleAmount += content.getAmount();
		
		return availibleAmount >= desiredAmount;
	}

}
