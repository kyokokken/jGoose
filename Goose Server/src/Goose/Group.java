package Goose;

import java.util.ArrayList;
import java.util.List;

import Goose.GameSettings;
import Goose.ItemSlot;
import Goose.Map;
import Goose.Player.ExperienceMessage;

/**
 * Group, holds group related information
 * 
 */
public class Group {
	private List<Goose.Player> __Players;

	public List<Goose.Player> getPlayers() {
		return __Players;
	}

	public void setPlayers(List<Goose.Player> value) {
		__Players = value;
	}

	/**
	 * Constructor
	 */
	public Group() throws Exception {
		this.setPlayers(new ArrayList<Goose.Player>());
	}

	/**
	 * AddPlayer, adds player to group
	 * 
	 * Updates everyone about new member
	 * 
	 */
	public void addPlayer(Goose.Player player, GameWorld world,
			Goose.Player adder) throws Exception {
		if (this.getPlayers().contains(player))
			return;

		String packet = "$3" + player.getName() + " has joined your group. ("
				+ adder.getName() + ")";
		for (Goose.Player p : this.getPlayers()) {
			if (p.getState() != Goose.Player.States.Ready)
				continue;

			world.send(p, packet);
		}
		world.send(player, "$3You have joined a group.");
		player.setGroup(this);
		this.getPlayers().add(player);
		for (Goose.Player p : this.getPlayers()) {
			this.sendPartyWindow(p, world);
		}
	}

	/**
	 * RemovePlayer, removes player from group
	 * 
	 * Updates everyone about member leaving
	 * 
	 */
	public void removePlayer(Goose.Player player, GameWorld world,
			boolean kicked, Goose.Player kicker) throws Exception {
		if (!this.getPlayers().contains(player))
			return;

		player.setGroup(null);
		this.getPlayers().remove(player);
		this.sendPartyWindow(player, world);
		String packet;
		if (kicked) {
			packet = "$3" + player.getName() + " was kicked from your group. ("
					+ kicker.getName() + ")";
		} else {
			packet = "$3" + player.getName() + " has left your group.";
		}
		for (Goose.Player p : this.getPlayers()) {
			if (p.getState() != Goose.Player.States.Ready)
				continue;

			world.send(p, packet);
			this.sendPartyWindow(p, world);
		}
		if (kicked) {
			world.send(player, "$3" + kicker.getName()
					+ " kicked you from the group.");
		} else {
			world.send(player, "$3You have left the group.");
		}
		if (this.getPlayers().size() == 1) {
			this.getPlayers().get(0).setGroup(null);
			world.send(this.getPlayers().get(0),
					"$3Your group has been disbanded.");
		}

	}

	/**
	 * SendPartyWindow, sends party window to player
	 * 
	 */
	public void sendPartyWindow(Goose.Player player, GameWorld world)
			throws Exception {
		int i = 1;
		// send players in party window if player is in party
		if (this.getPlayers().contains(player)) {
			for (Goose.Player p : this.getPlayers()) {
				// Can only display PartyWindowMax players in list
				if (i > GameSettings.getDefault().getPartyWindowMax())
					return;

				if (p == player)
					continue;

				world.send(player,
						"GUD" + i + "," + p.getLoginID() + "," + p.getName()
								+ "," + p.getLevel()
								+ p.getClas().getClassName());
				i++;
			}
		}

		while (i <= GameSettings.getDefault().getPartyWindowMax()) {
			// blank out the rest of the party window
			world.send(player, "GUD" + i + ",0,,0,");
			i++;
		}
	}

	/**
	 * Chat, player talked in group chat
	 * 
	 */
	public void chat(Goose.Player player, String message, GameWorld world)
			throws Exception {
		// string packet = "$3[group] " + player.Name + ": " + message;
		// foreach (Player p in this.Players)
		// {
		// world.Send(p, packet);
		// }
		String packet = "$3[group] " + player.getName() + ": " + message;
		String filteredpacket = "$3[group] " + player.getName() + ": ";
		boolean filtered = false;
		for (Goose.Player p : this.getPlayers()) {
			if (p.getChatFilterEnabled()) {
				if (!filtered) {
					filteredpacket += world.getChatFilter().filter(message);
					filtered = true;
				}

				world.send(p, filteredpacket);
			} else {
				world.send(p, packet);
			}
		}
	}

	/**
	 * ItemPickup, player picked up an item
	 * 
	 */
	public void itemPickup(Goose.Player player, ItemSlot itemslot,
			GameWorld world) throws Exception {
		String packet = "$3" + player.getName() + " picked up "
				+ itemslot.getItem().getName() + " (" + itemslot.getStack()
				+ ").";
		for (Goose.Player p : this.getPlayers()) {
			world.send(p, packet);
		}
	}

	/**
	 * GainExperience, distributes experience to the group
	 * 
	 */
	public void gainExperience(Goose.NPC npc, GameWorld world) throws Exception {
		long exp = npc.getExperience();
		double groupexp = exp / this.getPlayers().size() * 1.3
				* (this.getPlayers().size() * 0.1 + 1);
		int highest = 0;
		for (Goose.Player player : this.getPlayers()) {
			if (player.getLevel() > highest)
				highest = player.getLevel();

		}
		for (Goose.Player player : this.getPlayers()) {
			// no exp too far away
			if (player.getMap() != npc.getMap()
					|| Math.abs(player.getMapX() - npc.getMapX()) > Map.RANGE_X
					|| Math.abs(player.getMapY() - npc.getMapY()) > Map.RANGE_Y) {
				player.addExperience(0, world, ExperienceMessage.TooFarAway);
			} else // no exp since group member is too high
			if (player.getLevel() + 9 < highest) {
				player.addExperience(0, world, ExperienceMessage.TooLow);
			} else if (npc.getLevel() + 9 < player.getLevel()) {
				player.addExperience((long) groupexp / 10, world,
						ExperienceMessage.TooHigh);
			} else {
				player.addExperience((long) groupexp, world,
						ExperienceMessage.Normal);
			}
		}
	}

}
