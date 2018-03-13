package main;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;

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
