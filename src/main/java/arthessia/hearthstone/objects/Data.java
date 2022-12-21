package arthessia.hearthstone.objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class Data {
	private HashMap<String, LocationHearth> homes = new HashMap<>();

	public HashMap<String, LocationHearth> getHomes() {
		return homes;
	}

	public void setHomes(HashMap<String, LocationHearth> homes) {
		this.homes = homes;
	}

	public void setLocation(Player player) {
		if(this.getHomes().containsKey(player.getUniqueId().toString())) {
	        LocationHearth l = this.getHomes().get(player.getUniqueId().toString());

	        l.setWorld(player.getLocation().getWorld().getName());
	        l.setX(player.getLocation().getX());
	        l.setY(player.getLocation().getY());
	        l.setZ(player.getLocation().getZ());
			this.getHomes().put(
					player.getUniqueId().toString(), 
					l);
		} else {
	        // Get current LocalDateTime
	        LocalDateTime currentLocalDateTime = LocalDateTime.now().minusYears(30);
	 
	        // Create DateTimeFormatter instance with specified format
	        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	 
	        // Format LocalDateTime to String
	        String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);

			this.getHomes().put(
					player.getUniqueId().toString(), 
					new LocationHearth(
							formattedDateTime, 
							player.getWorld().getName(), 
							player.getLocation().getX(), 
							player.getLocation().getY(), 
							player.getLocation().getZ()));
		}
	}
	
	public void setTime(Player player) {
		if(this.getHomes().containsKey(player.getUniqueId().toString())) {
	        // Get current LocalDateTime
	        LocalDateTime currentLocalDateTime = LocalDateTime.now();
	 
	        // Create DateTimeFormatter instance with specified format
	        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	 
	        // Format LocalDateTime to String
	        String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);

	        LocationHearth l = this.getHomes().get(player.getUniqueId().toString());

	        l.setLocalDateTime(formattedDateTime);
	        
			this.getHomes().put(
					player.getUniqueId().toString(), 
					l);
		}
	}
	
	public LocationHearth getLocation(Player player) {
		if(!this.getHomes().containsKey(player.getUniqueId().toString()))
			return null;
		
		return this.getHomes().get(player.getUniqueId().toString());
	}
}
