package arthessia.hearthstone.objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocationHearth {

	private String localDateTime;
	private String world;
	private double x;
	private double y;
	private double z;
	
	public LocationHearth(
			String localDateTime, 
			String world, 
			double x, 
			double y, 
			double z) {
		this.localDateTime = localDateTime;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public String getLocalDateTime() {
		return localDateTime;
	}
	public void setLocalDateTime(String localDateTime) {
		this.localDateTime = localDateTime;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	public boolean getCooldown(Integer cooldown) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(this.getLocalDateTime(), formatter);

		LocalDateTime l = LocalDateTime.now();
		
		if(l.isAfter(dateTime.plusSeconds(cooldown)))
			return true;

		return false;
	}
	
	public long getCooldownLong(Integer cooldown) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(this.getLocalDateTime(), formatter);

		LocalDateTime l = LocalDateTime.now();
		
		if(l.isAfter(dateTime.plusSeconds(cooldown)))
			return 0;

		return ChronoUnit.SECONDS.between(l, dateTime.plusSeconds(cooldown));
	}
}
