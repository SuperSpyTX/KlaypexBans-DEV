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
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public Logger log;
	public Long banl = 24L;
	public File cfgdir;
	public boolean iplogging = false;
	public DeathListener deathListener = new DeathListener(this);
	public PlayerListen playerListener = new PlayerListen(this);

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		this.log = Logger.getLogger("Minecraft");
		this.log.info("About to enable SkrillexBans.");
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		loadConfiguration();

		// register dem events

		this.getServer().getPluginManager().registerEvents(deathListener, this);
		this.getServer().getPluginManager()
				.registerEvents(playerListener, this);
		this.log.info("SkrillexBans Enabled!");
	}

	public HashMap<String, String> config = new HashMap<String, String>();

	public HashMap<String, Long> banned = new HashMap<String, Long>();

	public HashMap<String, String> ip = new HashMap<String, String>();

	public HashMap<String, String> violators = new HashMap<String, String>();

	public void defaultValues() {
		config.put("messages", "default");
		config.put("ip-logging", "disabled");
		config.put("ban-length", "1d");
		config.put("lightning-on-death", "enabled");
	}

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
				writefile += pl.toString() + "=" + Long.toString(ban) + "\n";
			}

			var0.write(writefile);
			var0.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getConfigValue(String cfg, String value) {
		try {
			String ret = this.config.get(cfg);
			return ret;
		} catch (Exception e) {
			return value;
		}
	}

	public Long parseTime(String time) {
		Long ti = Long.parseLong(time.replace("m", "").replace("h", "")
				.replace("d", ""));
		if (time.endsWith("m")) {
			// minutes.
			return ti * 60000; // Change to milliseconds.
		} else if (time.endsWith("h")) {
			// hours
			return ti * 1000 * 60 * 60;
		} else if (time.endsWith("d")) {
			// days
			return ti * 24 * 1000 * 60 * 60;
		}

		this.log.info("Unable to read time in configuration. Setting to default.");
		return (long) (24 * 1000 * 60 * 60);
	}

	public Boolean parseBoolean(String cfg) {
		if (cfg.equalsIgnoreCase("enabled")) {
			return true;
		} else if (cfg.equalsIgnoreCase("disabled")) {
			return false;
		}

		// unable to read the statement, returning true.
		return true;
	}

	public String parseMessages(String cfg) {
		if (cfg.equalsIgnoreCase("default")) {
			return cfg;
		}

		return "default";
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
				writefile += pl.toString() + "=" + ipo.toString() + "\n";
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

	public void loadConfig() {
		try {
			BufferedReader var0 = new BufferedReader(new FileReader(
					getDataFolder().getAbsolutePath() + File.separator
							+ "config.txt"));
			String var4;
			try {
				while ((var4 = var0.readLine()) != null) {
					String cfg = var4.split("=")[0];
					String val = var4.split("=")[1];
					config.put(cfg, val);
				}
			} catch (Exception var9) {
				var9.printStackTrace();
			}

			var0.close();

			// now parse the new shit :P
			this.banl = parseTime(getConfigValue("ban-length", "1d"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void newConfig() {
		try {
			PrintWriter var0 = new PrintWriter(new FileWriter(getDataFolder()
					.getAbsolutePath() + File.separator + "config.txt"));
			defaultValues();
			Iterator var1 = config.entrySet().iterator();
			String writefile = "";
			while (var1.hasNext()) {
				Entry var2 = (Entry) var1.next();
				String cfg = (String) var2.getKey();
				String value = (String) var2.getValue();
				writefile += cfg.toString() + "=" + value.toString() + "\n";
			}

			var0.write(writefile);
			var0.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadConfiguration() {
		// load the real configuration.
		final File blocks = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "config.txt");
		if (blocks.exists()) {
			this.log.info("Loading configuration...");
			loadConfig();
			this.log.info("Configuration loaded!");
		} else {
			try {
				this.log.info("No configuration! Creating.");
				blocks.createNewFile();
				newConfig();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		// load banned users
		final File blocks1 = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "bannedplayers.txt");
		if (blocks1.exists()) {
			this.log.info("Loading previous bans...");
			loadBannedPlayers();
			this.log.info(banned.size() + " bans loaded!");
		} else {
			try {
				this.log.info("No ban file! Creating.");
				blocks1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// load ip logs
		final File blocks11 = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "ipdata.txt");
		if (blocks11.exists()) {
			this.log.info("Loading IP data...");
			loadIPPlayers();
			this.log.info(ip.size() + " players loaded!");
		} else {
			try {
				this.log.info("No IP file! Creating.");
				blocks11.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// set commands CPU
		this.getCommand("sban").setExecutor(new Commands(this));
	}

	@SuppressWarnings("rawtypes")
	public String getRealPlayersName(String ip) {
		Iterator v = this.ip.entrySet().iterator();
		while (v.hasNext()) {
			Entry line = (Entry) v.next();
			String player = (String) line.getKey();
			String rip = (String) line.getValue();
			if (!rip.equals(ip)) {
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
		return parseBoolean(getConfigValue("ip-logging", "enabled")) ? player
				.hasPermission("sban.iplog") : false;
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

}
