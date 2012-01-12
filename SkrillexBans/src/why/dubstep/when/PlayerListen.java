package why.dubstep.when;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListen extends PlayerListener {

	public Main mai;

	public PlayerListen(Main ma) {
		mai = ma;
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player pl = event.getPlayer();
		if (mai.isExempt(pl) || mai.canUseCommand(pl)) {
			return;
		}
		try {
			if (mai.banned.containsKey(pl.getName())) {
				long timenow = System.currentTimeMillis();
				long banExpiry = mai.banned.get(pl.getName());
				if (timenow > banExpiry) {
					// user is allowed to join because the ban has expired.
					mai.removeBan(pl.getName());
					pl.teleport(pl.getWorld().getSpawnLocation());
					pl.setHealth(20);
				} else {
					// user is still banned.
					// algebra?
					int d = (int) (banExpiry - timenow);
					int timeleft = (int) (d / 60 / 60 / 1000);
					mai.log.info(Long.toString(timenow));
					mai.log.info(Long.toString(banExpiry));
					event.disallow(
							PlayerLoginEvent.Result.KICK_BANNED,
							"You are banned, Expires in "
									+ Integer.toString(timeleft) + " hours");
				}
			}
		} catch (Exception e) {
			mai.log.info("Exception: " + e.getMessage());
		}

	}

	@SuppressWarnings("rawtypes")
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player pl = event.getPlayer();
		String ipaddr = pl.getAddress().toString().split(":")[0];

		if (mai.ipLogCheck(pl)) {
			if (mai.canUseCommand(pl)) {
				Player[] online = mai.getServer().getOnlinePlayers();
				int o = 0;
				for (int i = 0; i < online.length; i++) {
					if (!mai.violators.containsKey(online[i].getName())) {
						continue;
					}
					String ipd = mai.violators.get(online[i].getName());
					
					// one of dem violators.
					o += 1;
					if (o == 1) {
						pl.sendMessage(ChatColor.RED
								+ "Oh noes! We got multiple account violators on the loose!");

					}
					mai.notifyAdmins(ChatColor.RED + "Player: "
							+ online[i].getName() + " with IP: " + ipd);
					mai.notifyAdmins(ChatColor.RED + "Real Player's name is: " + mai.getRealPlayersName(ipd));
				}
			}
			return;
		}

		try {
			if (mai.ip.containsValue(ipaddr)) {
				Iterator list = mai.ip.entrySet().iterator();
				while (list.hasNext()) {
					Entry l = (Entry) list.next();
					String ip = (String) l.getValue();
					String player = (String) l.getKey();
					if (!ip.equals(ipaddr)) {
						continue;
					}
					// so it does match... what about playername.

					if (!player.equals(pl.getName())) {
						// uh oh. multiaccounter.
						mai.violators.put(pl.getName(), ipaddr);
						mai.notifyAdmins(ChatColor.RED
								+ "Oh noes! We got a multi accounter on the loose!");
						mai.notifyAdmins(ChatColor.RED + "Player: "
								+ pl.getName() + " with IP: " + ipaddr);
						mai.notifyAdmins(ChatColor.RED + "Real Player's name is: " + player);
						mai.notifyAdmins(ChatColor.RED
								+ "To excuse: do /sban excuse " + pl.getName()
								+ " or ban with rBans.");
					}

				}
			} else {
				mai.addIp(pl.getName(), ipaddr);
			}
		} catch (Exception e) {
			mai.log.info("Exception: " + e.getMessage());
		}

	}

}
