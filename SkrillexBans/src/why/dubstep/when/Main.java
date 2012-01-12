package why.dubstep.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public Logger log;
	public Long banl = 24L;
	public File cfgdir;
	public DeathListener deathListener = new DeathListener(this);
	public PlayerListen playerListener = new PlayerListen(this);

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	public HashMap<String, Long> banned = new HashMap<String, Long>();

	public HashMap<String, String> ip = new HashMap<String, String>();
	
	public HashMap<String, String> violators = new HashMap<String, String>();

	public void removeBan(String player) {
		changeBan(player, (Long) banned.get(player), true);
	}

	public void addBan(String player, long banlen) {
		changeBan(player, banlen, false);
	}

	public void removeIp(String player) {
		changeIp(player, (String) ip.get(player), true);
	}

	public void addIp(String player, String ipl) {
		changeIp(player, ipl, false);
	}

	@SuppressWarnings("rawtypes")
	public void changeBan(String player, Long banlength, boolean remove) {
		try {
			if (!remove) {
				banned.put(player, banlength);
			} else {
				banned.remove(player);
			}
			PrintWriter var0 = new PrintWriter(new FileWriter(getDataFolder()
					.getAbsolutePath() + File.separator + "bannedplayers.txt"));
			Iterator var1 = banned.entrySet().iterator();
			String writefile = "";
			while (var1.hasNext()) {
				Entry var2 = (Entry) var1.next();
				String pl = (String) var2.getKey();
				Long ban = (Long) var2.getValue();
				writefile += pl.toString() + "=" + Long.toString(ban) + "\n"; // manual
																				// linebreaks
																				// -.-
			}

			var0.write(writefile);
			var0.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void changeIp(String player, String ips, boolean remove) {
		try {
			if (!remove) {
				ip.put(player, ips);
			} else {
				ip.remove(player);
			}
			PrintWriter var0 = new PrintWriter(new FileWriter(getDataFolder()
					.getAbsolutePath() + File.separator + "ipdata.txt"));
			Iterator var1 = ip.entrySet().iterator();
			String writefile = "";
			while (var1.hasNext()) {
				Entry var2 = (Entry) var1.next();
				String pl = (String) var2.getKey();
				String ipo = (String) var2.getValue();
				writefile += pl.toString() + "=" + ipo.toString() + "\n"; // manual
																			// linebreaks
																			// -.-
			}

			var0.write(writefile);
			var0.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadBannedPlayers() {
		try {
			BufferedReader var0 = new BufferedReader(new FileReader(
					getDataFolder().getAbsolutePath() + File.separator
							+ "bannedplayers.txt"));

			String var4;
			try {
				while ((var4 = var0.readLine()) != null) {
					String pl = var4.split("=")[0];
					Long banlen = Long.parseLong(var4.split("=")[1]);
					banned.put(pl, banlen);
				}
			} catch (Exception var9) {
				var9.printStackTrace();
			}

			var0.close();
		} catch (Exception var10) {
			var10.printStackTrace();
		}
	}

	public void loadIPPlayers() {
		try {
			BufferedReader var0 = new BufferedReader(new FileReader(
					getDataFolder().getAbsolutePath() + File.separator
							+ "ipdata.txt"));

			String var4;
			try {
				while ((var4 = var0.readLine()) != null) {
					String pl = var4.split("=")[0];
					String ips = var4.split("=")[1];
					ip.put(pl, ips);
				}
			} catch (Exception var9) {
				var9.printStackTrace();
			}

			var0.close();
		} catch (Exception var10) {
			var10.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String getRealPlayersName(String ip) {
		Iterator v = this.ip.entrySet().iterator();
		while(v.hasNext()) {
			Entry line = (Entry) v.next();
			String player = (String) line.getKey();
			String rip = (String) line.getValue();
			if(!rip.equals(ip)) {
				continue;
			}
			
			return player;
		}
		
		return "";
	}

	public boolean isExempt(Player player) {
		return player.hasPermission("sban.exempt");
	}

	public boolean canUseCommand(Player player) {
		return player.hasPermission("sban.commands");
	}
	
	public boolean ipLogCheck(Player player) {
		return player.hasPermission("sban.iplog");
	}

	public void notifyAdmins(String msg) {
		this.log.info(msg);
		Player[] players = this.getServer().getOnlinePlayers();
		for (int i = 0; i < players.length; i++) {
			Player pl = players[i];
			if (!canUseCommand(pl)) {
				continue;
			}
			pl.sendMessage(msg);
		}
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		this.log = Logger.getLogger("Minecraft");
		this.log.info("About to enable SkrillexBans.");
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		// load banned users
		final File blocks = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "bannedplayers.txt");
		if (blocks.exists()) {
			this.log.info("Loading previous bans...");
			loadBannedPlayers();
			this.log.info(banned.size() + " bans loaded!");
		} else {
			try {
				this.log.info("No ban file! Creating.");
				blocks.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// load ip logs.
		final File blocks1 = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "ipdata.txt");
		if (blocks1.exists()) {
			this.log.info("Loading IP data...");
			loadIPPlayers();
			this.log.info(ip.size() + " players loaded!");
		} else {
			try {
				this.log.info("No IP file! Creating.");
				blocks1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// todo: function to convert to milliseconds
		banl = banl * 1000 * 60 * 60; // Change to milliseconds.

		// set commands CPU
		this.getCommand("sban").setExecutor(new Commands(this));

		// register dem events
		this.getServer()
				.getPluginManager()
				.registerEvent(Event.Type.ENTITY_DEATH, this.deathListener,
						Priority.Normal, this);
		this.getServer()
				.getPluginManager()
				.registerEvent(Event.Type.PLAYER_LOGIN, this.playerListener,
						Priority.Normal, this);
		this.getServer()
				.getPluginManager()
				.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener,
						Priority.Normal, this);
		this.log.info("SkrillexBans Enabled!");
	}

}
