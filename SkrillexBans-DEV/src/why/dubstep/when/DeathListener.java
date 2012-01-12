package why.dubstep.when;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class DeathListener extends EntityListener {

	public Main mai;

	public DeathListener(Main ma) {
		mai = ma;
	}

	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player pl = (Player) event.getEntity();
			if (mai.isExempt(pl) || mai.canUseCommand(pl)) {
				return;
			}
			String playersname = pl.getName();
			if (playersname.contains("PvPLogger")) {
				// most likely the combat tag plugin.
				// there was a bug, so this should fix it
				// i get it, it's horrible but what-fucking-evar.
				return;
			}
			if (mai.banned.containsKey(playersname)) {
				// hopefully to fix this multi-death issue.
				return;
			}
			Long banlen = System.currentTimeMillis() + mai.banl;
			// add the ban.
			mai.addBan(playersname, banlen);

			// now for the epic lulz.
			String deathmessage = ChatColor.BLUE + playersname + " ";
			if (mai.parseMessages(mai.getConfigValue("messages", "default"))
					.equalsIgnoreCase("default")) {
				Random rdn = new Random();
				int rnd = rdn.nextInt(11);

				// now my famed words of wisdom.
				switch (rnd) {
				case 1:
					deathmessage += "got wubbed by dubstep.";
					break;
				case 2:
					deathmessage += "fell into Klaypex's bassdrops.";
					break;
				case 3:
					deathmessage += "didn't survive basscode's airwaves."; // fanboy
																			// alert
																			// :3
					break;
				case 4:
					deathmessage += "has fallen into the hands of dubstep.";
					break;
				case 5:
					deathmessage += "didn't bother to look.";
					break;
				case 6:
					deathmessage += "got rh4p3d by JohnSmithStep.";
					break;
				case 7:
					deathmessage += "got PWN4G3DZ0RS";
					break;
				case 8:
					deathmessage += "balls shrunk.";
					break;
				case 9:
					deathmessage += "got caught in the rye."; // book reference
					break;
				case 10:
					deathmessage += "caught the case of a broken neck.";
					break;
				case 11:
					deathmessage += "once changed the world, now dead.";
					break;
				default:
					deathmessage += "got fucked.";
					break;
				}
			}
			mai.getServer().broadcastMessage(deathmessage);
			if (mai.parseBoolean(mai.getConfigValue("lightning-on-death",
					"enabled"))) {
				pl.getWorld().strikeLightning(pl.getLocation());
			}
			pl.kickPlayer("You have died! See you tomorrow.");

		} else {
			// mai.log.info("Something went wrong :O");
		}
	}

}
