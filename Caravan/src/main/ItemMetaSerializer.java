package main;

import java.util.Map;

import org.bukkit.inventory.meta.ItemMeta;

public class ItemMetaSerializer {
	
	public static Map<String, Object> serialize(ItemMeta im) {
		return im.serialize();
	}
	
	public static ItemMeta deserialize(Map<String, Object> map) {
		Object raw = map;
		if(raw instanceof ItemMeta) return (ItemMeta) raw;
		return null;
	}

}
