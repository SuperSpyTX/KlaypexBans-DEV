package why.dubstep.when;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	public Main mai;

	public Commands(Main ma) {
		mai = ma;
	}

	public boolean processCmd(String[] arg3, CommandSender arg0,
			boolean permscheck) {
		if (permscheck) {
			if (!mai.canUseCommand(((Player) arg0).getPlayer())) {
				arg0.sendMessage("nope.avi");
				return true;
			}
		}
		// if (arg3.length == 2) {
		if (arg3[0].equalsIgnoreCase("ban")) {
			try {
				Player pl = mai.getServer().getPlayer(arg3[1]);
				if (mai.isExempt(pl)) {
					arg0.sendMessage("Player is exempt from being striked. Cancelling.");
					return true;
				}
				pl.damage(1000);
				arg0.sendMessage("Player striked.");
			} catch (Exception e) {
				arg0.sendMessage("Player not found.");
				return true;
			}
		} else if (arg3[0].equalsIgnoreCase("unban")) {
			try {
				if (mai.banned.containsKey(arg3[1])) {
					mai.removeBan(arg3[1]);
					arg0.sendMessage("Player removed from banned list.");
					return true;
				} else {
					arg0.sendMessage("Player not found");
					return true;
				}
			} catch (Exception e) {
				arg0.sendMessage("Player not found?");
			}
		} else if (arg3[0].equalsIgnoreCase("excuse")) {
			try {
				Player pl = mai.getServer().getPlayer(arg3[1]);
				String ip = mai.ip.get(pl.getName());
				mai.removeIp(pl.getName());
				mai.addIp(pl.getName(), ip);
				arg0.sendMessage("Excused player.");
			} catch (Exception e) {
				arg0.sendMessage("Player not found.");
				return true;
			}
		} else if (arg3[0].equalsIgnoreCase("vlist")) {
			if (mai.ipLogCheck((Player) arg0)) {
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
						arg0.sendMessage(ChatColor.RED
								+ "Oh noes! We got multiple account violators on the loose!");

					}
					arg0.sendMessage(ChatColor.RED + "Player: "
							+ online[i].getName() + " with IP: " + ipd);
					arg0.sendMessage(ChatColor.RED + "Real Player's name is: "
							+ mai.getRealPlayersName(ipd));
				}
				if (o == 0) {
					arg0.sendMessage(ChatColor.GREEN
							+ "No violators are online! You're safe.");
				}
			} else {
				arg0.sendMessage("Chuck Testa.");
			}
		} else if (arg3[0].equalsIgnoreCase("reload")) {
			mai.log.info("Reloading configuration..");
			mai.banned.clear();
			mai.violators.clear();
			mai.config.clear();
			mai.ip.clear();

			mai.loadConfiguration();

			arg0.sendMessage("Reloaded configuration");
			return true;
		}
		return false;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if ((arg0 instanceof Player)) {
			return processCmd(arg3, arg0, true);
		} else {
			return processCmd(arg3, arg0, false);
		}

		// return true;
	}

}
