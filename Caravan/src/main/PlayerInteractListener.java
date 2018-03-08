package main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.material.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();	// The Player
		
		if(!e.getClickedBlock().getType().equals(Material.WALL_SIGN)) return; // If not a sign, return
		
		org.bukkit.block.Sign signBlock = (org.bukkit.block.Sign) e.getClickedBlock().getState();	// The sign block
		Sign signData = (Sign) e.getClickedBlock().getState().getData(); 							// The data of the sign that was clicked
		Block block = e.getClickedBlock().getRelative(signData.getAttachedFace()); 					// Reference attached block
		
		if(!(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST))) return;	// If not a chest, return
		
		Chest chest = (Chest) block.getState();	// The Chest
		Inventory inv = chest.getInventory();	// The Chest's Inventory
		
		/** TEST CODE
		Signs signs = Signs.with(signBlock);
		if(signs.validate(player)) {
			if(signs.type().equals("DIST")) Caravan.tradeChests.add(new DistributionChest(signBlock, chest, player, signs.reference(), 
					Material.PAPER, 64));
			if(signs.type().equals("COLL")) Caravan.tradeChests.add(new CollectionChest(signBlock, chest, player, signs.reference(), 
					Material.PAPER, 64, true));
			if(signs.type().equals("RECP")) Caravan.tradeChests.add(new CollectionChest(signBlock, chest, player, signs.reference(), 
					Material.PAPER, 64, false));
			
			Bukkit.getLogger().info("transactable: " + Caravan.tradeChests.get(0).transactable(1));
		}
		*/
		
		// TODO Check sign for type and valid id
		// TODO Check valid id against player
		// TODO Create type from chest
		
	}

}
