package arthessia.hearthstone;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

import arthessia.hearthstone.Initializers.CommandInit;
import arthessia.hearthstone.Initializers.EventInit;
import arthessia.hearthstone.objects.Data;
import arthessia.hearthstone.objects.LocationHearth;


public class Plugin extends JavaPlugin implements Listener {
    public static JavaPlugin plugin;
	private static File datad = new File("plugins/hearthstone/data.json");
	private static Data data = new Data();
	private static Integer cooldown = 0;

    @Override
    public void onEnable() {
        Plugin.plugin = this;
        getLogger().info("HearthStone loading...");
        this.saveDefaultConfig();
        getLogger().info("Config loaded...");
        CommandInit.init();
        getLogger().info("Registered commands...");
        EventInit.init();
        getLogger().info("Registered Events...");
        
        /* HEARTSTONE POTION
         * Check following RECIPE
         */
        ItemStack heartStoneStack = new ItemStack(Material.POTION, 1);
        PotionMeta heartStoneMeta = (PotionMeta) heartStoneStack.getItemMeta();
        heartStoneMeta.setColor(Color.fromRGB(0, 187, 157));
        heartStoneMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        heartStoneMeta.setDisplayName("HearthStone");
        heartStoneMeta.setLore(List.of("This potion doesn't inspire confidence..."));
        heartStoneStack.setItemMeta(heartStoneMeta);
        
        ShapedRecipe heartstone = new ShapedRecipe(new NamespacedKey(this, "hearthstone"), heartStoneStack);
        heartstone.shape("VWX", " Y ", " Z ");
        heartstone.setIngredient('V', Material.ENDER_PEARL);
        heartstone.setIngredient('W', Material.BLAZE_POWDER);
        heartstone.setIngredient('X', Material.REDSTONE);
        heartstone.setIngredient('Y', Material.WATER_BUCKET);
        heartstone.setIngredient('Z', Material.GLASS_BOTTLE);

        /* HOMESTONE POTION
         * Check following RECIPE
         */
        ItemStack homeStoneStack = new ItemStack(Material.POTION, 1);
        PotionMeta homeStoneMeta = (PotionMeta) homeStoneStack.getItemMeta();
        homeStoneMeta.setColor(Color.fromRGB(238, 0, 95));
        homeStoneMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        homeStoneMeta.setDisplayName("HomeStone");
        homeStoneMeta.setLore(List.of("This potion looks familiar to you."));
        homeStoneStack.setItemMeta(homeStoneMeta);
        
        ShapedRecipe homeStone = new ShapedRecipe(new NamespacedKey(this, "enderhearthstone"), homeStoneStack);
        homeStone.shape("VWX", " Y ", " Z ");
        homeStone.setIngredient('V', Material.NETHER_WART);
        homeStone.setIngredient('W', Material.BLAZE_POWDER);
        homeStone.setIngredient('X', Material.REDSTONE);
        homeStone.setIngredient('Y', Material.WATER_BUCKET);
        homeStone.setIngredient('Z', Material.GLASS_BOTTLE);

        Bukkit.addRecipe(heartstone);
        Bukkit.addRecipe(homeStone);
        
        cooldown = this.getConfig().getInt("cooldown-hearthstone");
        
		File dir = new File("plugins/hearthstone/");
		dir.mkdir();

		if (datad.exists()) {
			Gson gson = new Gson();
			try(FileReader fileReader = new FileReader(datad)) {
				
				StringBuilder sb = new StringBuilder();
				int c;
			    while((c = fileReader.read()) !=-1) {
			    	sb.append((char) c);
			    }

				data = (Data) gson.fromJson(sb.toString(), Data.class);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    
	public static void save() {
		Gson gson = new Gson();

		try(FileWriter fileWriter = new FileWriter(datad)) {
			
			fileWriter.write(gson.toJson(data));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public void onDisable() {
    	save();
        getLogger().info("HearthStone shutdown...");
    }
    
	public static void link(Player player) {
		Plugin.data.setLocation(player);
    	save();
	}
	
	public static Integer getCooldown() {
		return cooldown;
	}
	
	public static LocationHearth getLocationHearth(Player player) {
		return data.getLocation(player);
	}
	
	@EventHandler
    public void onUsage(PlayerItemConsumeEvent event) {
    	if(event.getItem() == null ||
    			event.getItem().getItemMeta() == null ||
    			event.getItem().getItemMeta().getDisplayName() == null)
    		return;
    	
    	ItemStack is = event.getItem();
    	
        if(is.getType().equals(Material.POTION) &&
        		event.getItem().getItemMeta().getDisplayName().equals("HearthStone")) {

            LocationHearth location = data.getLocation(event.getPlayer());
            
            if(location == null)
            	return;
            
            Location loc = new Location(
            		Bukkit.getWorld(location.getWorld()), 
            		location.getX(), 
            		location.getY(), 
            		location.getZ(), 
            		0, 
            		0);
			Player player = event.getPlayer();
            if (location.getCooldown(this.getConfig().getInt("cooldown-hearthstone"))) {
            	Location tempLoc = player.getLocation();
				player.teleport(loc);
				data.setTime(player);
		    	save();
            	tempLoc.getWorld().strikeLightning(tempLoc);
			} else {
				event.setCancelled(true);
				player.sendMessage("The potion is too hot! Wait a bit...");
			}
        } else if(is.getType().equals(Material.POTION) &&
        		event.getItem().getItemMeta().getDisplayName().equals("HomeStone")) {
    		Plugin.data.setLocation(event.getPlayer());
    		event.getPlayer().sendMessage("You feel fine here...");
        	save();
        }
    }
}
