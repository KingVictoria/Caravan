package main;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Signs {
	/**
	 * Types:
	 * 
	 * Distribution = DIST
	 * Collection   = COLL
	 * Receipt      = RECP
	 */
	
	/**
	 * TradeChest Sign Layout:
	 * 
	 *   TYPE
	 * Reference#
	 *   info
	 *   info
	 */
	
	private String type;
	private int reference;
	
	private Sign sign;
	
	/**
	 * Use the Signs interpreter with given existing Sign Block
	 * @param sign Sign Block
	 * @return Signs
	 */
	public static Signs with(Sign sign) {
		return new Signs(sign);
	}
	
	private Signs(Sign sign) {
		this.sign = sign;
	}
	
	public boolean validate(Player player) {
		if	   (sign.getLine(0).equalsIgnoreCase("DIST")) {
				sign.setLine(0, ChatColor.GREEN + "" + ChatColor.BOLD + "DIST");
				type = "DIST";
		}
		else if(sign.getLine(0).equalsIgnoreCase("COLL")) {
				sign.setLine(0, ChatColor.GREEN + "" + ChatColor.BOLD + "COLL");
				type = "COLL";
		}
		else if(sign.getLine(0).equalsIgnoreCase("RECP")) {
			    sign.setLine(0, ChatColor.GREEN + "" + ChatColor.BOLD + "RECP");
			    type = "RECP";
		}
		else return false;
		
		try {
			reference = Integer.parseInt(sign.getLine(1));
		} catch (Exception e) {
			return false;
		}
		
		sign.setLine(1, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "REF: " + ChatColor.WHITE + sign.getLine(1));
		
		sign.setLine(2, ChatColor.AQUA + "" + ChatColor.BOLD + player.getName());
		sign.setLine(3, ChatColor.AQUA + "" + ChatColor.BOLD + "Validated");
		
		for(TradeChest tc: Caravan.tradeChests) if(tc.reference == reference) { sign.update(); return false; }
		
		sign.update();
		return true;
	}
	
	public String type() {
		return type;
	}
	
	public int reference() {
		return reference;
	}
	
	/**
	 * Clears the sign
	 * @return Signs
	 */
	public Signs clear() {
		for(int i = 0; i < sign.getLines().length; i++) sign.setLine(i, "");
		sign.update();
		return this;
	}
	
	/**
	 * Logs the sign to console (for testing purposes)
	 * @return Signs
	 */
	public Signs logToConsole() {
		for(String line : sign.getLines()) Bukkit.getLogger().info(line);
		return this;
	}
	
	

}
