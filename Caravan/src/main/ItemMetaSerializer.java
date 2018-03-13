package main;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Serializes ItemMeta. This is a surprisingly difficult task as each item has a different thing it needs to serialize in its metadata.
 * It required some serious fanangling is what I'm saying.
 * @author KingVictoria
 */
public class ItemMetaSerializer implements Serializable {
	private static final long serialVersionUID = -951369083430771147L;
	
	Map<String, Object> map;	// Serialized Data
	Material mat;				// Type of item
    
    /**
     * Gets the class
     * @return The CraftMetaItem Class
     * @throws ClassNotFoundException Don't worry about that
     */
    public Class<?> whatClass() throws ClassNotFoundException {
    	return Class.forName("org.bukkit.craftbukkit." + getVersion() + "inventory.CraftMetaItem").getClasses()[0];
    }
    
    /**
     * Gets the Bukkit version and shit
     * @return A String Version Thing
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }
    /**
     * Serializes an ItemMeta into an ItemMetaSerializer
     * @param im ItemMeta
     * @param mat Material
     * @return ItemMetaSerializer
     */
    public static ItemMetaSerializer serialize(ItemMeta im, Material mat) {
    	ItemMetaSerializer ims = new ItemMetaSerializer();
    	ims.map = im.serialize();
    	ims.mat = mat;
    	return ims;
    }
    
    /**
     * De-serializes and ItemMetaSerializer into an ItemMeta
     * @param ims ItemMetaSerializer
     * @return ItemMeta
     */
    public static ItemMeta deSerialize(ItemMetaSerializer ims) {
    	ItemMeta im = null;
		try {
			im = (ItemMeta) Class.forName("org.bukkit.craftbukkit." + getVersion() + "inventory.CraftMetaItem").getClasses()[0].getMethod("deserialize", 
					new Class[] {Map.class}).invoke(new ItemStack(ims.mat), ims.map);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return im;
    }
}
