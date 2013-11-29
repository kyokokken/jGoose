package Goose.Events;

import Goose.Event;
import Goose.Events.GuildAddCommandEvent;
import Goose.GameWorld;
import Goose.Guild.GuildRanks;

public class GuildAddCommandEvent extends Event {
	public GuildAddCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GuildAddCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (this.getPlayer().getGuild() == null)
				return;

			if (this.getPlayer().getGuild().getRank(this.getPlayer())
					.compareTo(GuildRanks.Officer) < 0)
				return;

			String name = ((String) this.getData()).substring(10);
			Goose.Player player = world.getPlayerHandler().getPlayer(name);
			if (player != null
					&& player.getState() == Goose.Player.States.Ready) {
				if (player.getGuild() == null) {
					this.getPlayer().getGuild().joinGuild(player, world);
				}

			} else {
				world.send(this.getPlayer(), "$7Couldn't find player.");
			}
		}

	}

}
