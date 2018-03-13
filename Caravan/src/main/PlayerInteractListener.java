package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractListener implements Listener {
	
	/*
	 * PROCESS: 
	 * 1. Get Generated Key(s) (Shop -> Dist and Coll) (Buy -> Recp)
	 * 2. Right click on sign on chest with appropriate key (will eventually need to check for reinforcement)
	 * 3. IF SHOP -> Fill with necessary materials and right click to activate
	 * 4. IF RECP -> Fill with transaction requirement and right click to send trade
	 */
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		
		/* TEST CODE */
		
		if(e.getClickedBlock().getType().equals(Material.GRASS)) {
			ItemStack succ = new ItemStack(Material.PAPER);
			ItemMeta succMeta = succ.getItemMeta();
			ArrayList<String> lore = new ArrayList<>();
			lore.add("succ");
			succMeta.setLore(lore);
			succMeta = ItemMetaSerializer.deSerialize(ItemMetaSerializer.serialize(succMeta, Material.PAPER));
			Bukkit.getLogger().info(""+succMeta.hasLore());
			// **AHEM** PRAISE TITOS
			succ.setItemMeta(succMeta);
			player.getInventory().addItem(succ);
		}
		
		/* TEST CODE */
		
		if(!player.getInventory().getItemInMainHand().getType().equals(Material.PAPER)) return;	// Make sure item in hand is paper
		
		ItemStack key = player.getInventory().getItemInMainHand();
		
		if(!e.getClickedBlock().getType().equals(Material.WALL_SIGN)) return; // If not a wall sign, return
		
		Sign sign = (Sign) e.getClickedBlock().getState();
		Block block = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace());
		
		if(!(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST))) return;	// If not a chest, return
		
		Chest chest = (Chest) block.getState();
		
		ItemMeta keyMeta = key.getItemMeta();
		try { if(Shop.getShop(Integer.parseInt(keyMeta.getLore().get(0))) == null) return; } catch (Exception ex) { return; }
		Shop shop = Shop.getShop(Integer.parseInt(keyMeta.getLore().get(0)));
		
		if(keyMeta.getDisplayName().equals("Distribution Key")) {
			if(shop.getDistributionChest() != null) return;	// If the DistributionChest already exists, return
			shop.makeDistributionChest(chest, sign);
		} // Distribution
		
		else if(keyMeta.getDisplayName().equals("Collection Key")) {
			if(shop.getDistributionChest() != null) return;	// If the DistributionChest already exists, return
			shop.makeCollectionChest(chest, sign);
		} // Collection
		
		else if(keyMeta.getDisplayName().equals("Receipt Key")) {
			shop.addReceiptChest(chest, sign, player);
		} // Receipt
		
		else return;
		
		player.getInventory().getItemInMainHand().setAmount(0); // Remove item from hand
		
	}

}
