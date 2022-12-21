package arthessia.hearthstone.Commands.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import arthessia.hearthstone.Plugin;
import arthessia.hearthstone.objects.LocationHearth;

public class HearthstoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
    	if(!(sender instanceof Player))
    		return true;
    	
    	Player player = (Player) sender;
    	LocationHearth loc = Plugin.getLocationHearth(player);
    	if(loc == null)
    		return true;
    	
    	long seconds = loc.getCooldownLong(Plugin.getCooldown());
    	if(seconds == 0) {
    		sender.sendMessage("The potion looks at the right temperature.");
    	} else {
    		sender.sendMessage(seconds + " seconds remaining...");
    	}
        return true;
    }
}
