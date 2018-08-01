package me.Jyumpp.CustomWorldBorder;

import com.google.common.collect.Lists;
import de.craftstuebchen.api.worldborder.WorldBorderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
//import org.json.simple.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin implements Listener
{
	public static JSONObject cwbJSON;
	public static List<String> jailedPlayers = new ArrayList<>();
	public static List<OfflinePlayer> offlinePlayers = new ArrayList<>();
	public static Map<String, UUID> players = new HashMap<>();

	public static Location getCenter(Location loc) {
		return new Location(loc.getWorld(),
				getRelativeCoord(loc.getBlockX()),
				loc.getY(),
				getRelativeCoord(loc.getBlockZ()));
	}

	private static double getRelativeCoord(int i) {
		double d = i;
		d = d < 0 ? d - .5 : d + .5;
		return d;
	}

	public static void roundPlayPos(Player player){
		Location playerLocation = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		playerLocation = getCenter(playerLocation);
		player.teleport(playerLocation);
	}

	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent event)
	{
		if (jailedPlayers.contains(event.getPlayer().getUniqueId().toString()))
		{
			Location borderLocation = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();

			WorldBorderAPI.inst().setBorder(event.getPlayer(), 1, event.getPlayer().getLocation());
			event.getPlayer().setFlying(false);

			if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
				Location newLoc = event.getFrom();
				newLoc.setY(event.getTo().getY());
				event.getPlayer().teleport(newLoc.setDirection(event.getTo().getDirection()));
			}

			//Bukkit.getServer().broadcastMessage("test!");
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 100), false);
		}
	}

	@Override
	public void onEnable()
	{
		offlinePlayers.addAll(new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers())));

		for (OfflinePlayer o : offlinePlayers) players.put(o.getName(), o.getUniqueId());

		Bukkit.getPluginManager().registerEvents(this, this);

		cwbJSON = getJSON();
		JSONArray jsonArray = new JSONArray();
		if ((JSONArray) cwbJSON.get("jailed") instanceof JSONArray) jsonArray = (JSONArray) cwbJSON.get("jailed");
		if (jsonArray.size() > 0)
			for (Object j : jsonArray)
			{
				jailedPlayers.add((String) j);
			}

		Bukkit.getServer().broadcastMessage("test!" + jailedPlayers.toString());
		this.getCommand("cwb").setExecutor(new cwbCommand());
		this.getCommand("wbjail").setExecutor(new WBJailCommand());
		//this.getCommand("mgw").setTabCompleter(new MGWCommand());
	}

	@Override
	public void onDisable()
	{

	}

	public static JSONObject getJSON()
	{

		JSONParser parser = new JSONParser();

		try
		{

			Object obj = parser.parse(new FileReader("custom_worldborder.json"));

			return (JSONObject) obj;


		} catch (Exception e)
		{
			return new JSONObject();
		}

	}

	public static void putJSON()
	{
		JSONArray jsonArray = new JSONArray();
		for (String s : jailedPlayers) jsonArray.add(s);


		cwbJSON.clear();
		cwbJSON.put("jailed", jsonArray);

		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("custom_worldborder.json"));
			writer.write(cwbJSON.toJSONString());

			writer.close();
		} catch (IOException e)
		{
			e.getStackTrace();
		}
	}

}
