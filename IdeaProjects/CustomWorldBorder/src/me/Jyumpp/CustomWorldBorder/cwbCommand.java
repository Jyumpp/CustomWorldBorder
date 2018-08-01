package me.Jyumpp.CustomWorldBorder;


import net.minecraft.server.v1_13_R1.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.craftstuebchen.api.worldborder.WorldBorderAPI;
import org.bukkit.entity.Player;


import java.lang.reflect.Field;

public class cwbCommand implements CommandExecutor
{
	WorldBorder worldBorder = new WorldBorder();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{

		if (sender.hasPermission("CustomWorldBorder.setborder"))
		{
			if (args.length > 0)
				WorldBorderAPI.inst().setBorder((Player) sender, Integer.valueOf(args[0]), ((Player) sender).getLocation());
			else WorldBorderAPI.inst().resetBorder((Player) sender);
			Bukkit.getServer().broadcastMessage("test!" + Main.jailedPlayers.toString() + " " + ((Player) sender).getUniqueId().toString());
		} else sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
		return true;
	}
}
