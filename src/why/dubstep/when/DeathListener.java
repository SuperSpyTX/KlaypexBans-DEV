package why.dubstep.when;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathListener implements Listener {

	public Main mai;

	public DeathListener(Main ma) {
		mai = ma;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player pl = (Player) event.getEntity();
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
				int rnd = rdn.nextInt(20);

				// now my famed words of wisdom.
				switch (rnd) {
				case 1:
					deathmessage += "got wubbed by dubstep.";
					break;
				case 2:
					deathmessage += "fell into Klaypex's bassdrops.";
					break;
				case 3:
					deathmessage += "didn't survive supah's airwaves.";
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
					deathmessage += "got Pun'd";
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
				case 12:
					deathmessage += "took a huge arrow in the knee.";
					break;
				case 13:
					deathmessage += "got trolled by Klaypex.";
					break;
				case 14:
					deathmessage += "didn't survive the hunger games.";
					break;
				case 15:
					deathmessage += "got eatened by spawned tigers.";
					break;
				case 16:
					deathmessage += "heart shattered by girl.";
					break;
				case 17:
					deathmessage += "bitchslapped by GF.";
					break;
				case 18:
					deathmessage += "got tbagged.";
					break;
				case 19:
					deathmessage += "is madbro.";
					break;
				case 20:
					deathmessage += "is now going to use alt accounts.";
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
			Thread nt = new Thread() {
				public void run() {
					try {
						Thread.sleep(Integer.parseInt(mai.getConfigValue("delay-death", "2000")));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pl.kickPlayer(mai.getConfigValue("death-kickmsg", "Oh noes! u dead! See you tomorrow."));
				}
			}; nt.start();
			

		} else {
			// mai.log.info("Something went wrong :O");
		}
	}

}
