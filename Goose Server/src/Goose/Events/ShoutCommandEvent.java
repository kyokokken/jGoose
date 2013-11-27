package Goose.Events;

import java.util.List;

import Goose.Event;
import Goose.Events.ShoutCommandEvent;
import Goose.GameWorld;

public class ShoutCommandEvent extends Event {
	public ShoutCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new ShoutCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			String data = ((String) this.getData()).substring(7);
			if (data.length() <= 0)
				return;

			if (!this.getPlayer().getMap().getCanShout()
					&& this.getPlayer().getAccess() != Goose.Player.AccessStatus.GameMaster) {
				world.send(this.getPlayer(),
						"#Shouting is disabled in this map.");
				return;
			}

			// world.SendToMap(this.Player.Map, "#" + this.Player.Name +
			// " shouts: " + data);
			String packet = "#" + this.getPlayer().getName() + " shouts: "
					+ data;
			String filteredpacket = "#" + this.getPlayer().getName()
					+ " shouts: ";
			boolean filtered = false;
			List<Goose.Player> range = this.getPlayer().getMap().getPlayers();
			for (Goose.Player player : range) {
				if (player.getChatFilterEnabled()) {
					if (!filtered) {
						filteredpacket += world.getChatFilter().filter(data);
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
