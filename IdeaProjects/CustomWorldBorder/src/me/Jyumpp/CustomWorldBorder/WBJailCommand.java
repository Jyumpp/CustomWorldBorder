package me.Jyumpp.CustomWorldBorder;

import de.craftstuebchen.api.worldborder.WorldBorderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class WBJailCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{

		if (sender.hasPermission("CustomWorldBorder.freejail"))
		{
			if (args.length == 2 && args[0].equalsIgnoreCase("free"))
			{
				try
				{
					Bukkit.broadcastMessage(Main.players.keySet().toString());
					if (Main.players.containsKey(args[1]))
						Main.jailedPlayers.remove(Main.players.get(args[1]).toString());
					Main.putJSON();
					WorldBorderAPI.inst().resetBorder(Bukkit.getPlayer(args[1]));
					Bukkit.getPlayer((args[1])).removePotionEffect(PotionEffectType.SLOW);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}

		if (sender.hasPermission("CustomWorldBorder.setjail"))
		{
			if (args.length == 1 && Bukkit.getServer().getPlayer(args[0]) instanceof Player)
			{
				try
				{
					Player player = Bukkit.getServer().getPlayer(args[0]);

					Main.jailedPlayers.add(player.getUniqueId().toString());
					Main.putJSON();
					Main.roundPlayPos(player);
					player.setBedSpawnLocation(player.getLocation(), true);
					player.kickPlayer("You have been jailed.");
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}
		return true;
	}
}
