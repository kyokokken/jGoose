package Goose.Events;

import java.util.List;

import Goose.Event;
import Goose.Events.GuildChatCommandEvent;
import Goose.GameWorld;

public class GuildChatCommandEvent extends Event {
	public GuildChatCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GuildChatCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (this.getPlayer().getGuild() == null)
				return;

			String message = ((String) this.getData()).substring(7);
			if (message.length() <= 0)
				return;

			// this.Player.Guild.SendToGuild("$2[guild] " + this.Player.Name +
			// ": " + message, world);
			String packet = "$2[guild] " + this.getPlayer().getName() + ": "
					+ message;
			String filteredpacket = "$2[guild] " + this.getPlayer().getName()
					+ ": ";
			boolean filtered = false;
			List<Goose.Player> range = this.getPlayer().getGuild()
					.getOnlineMembers();
			for (Goose.Player player : range) {
				if (player.getChatFilterEnabled()) {
					if (!filtered) {
						filteredpacket += world.getChatFilter().filter(message);
						filtered = true;
					}

					world.send(player, filteredpacket);
				} else {
					world.send(player, packet);
				}
			}
		}

	}

}
