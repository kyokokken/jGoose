package Goose.Events;

import Goose.Event;
import Goose.Events.GMBroadcastCommandEvent;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

public class GMBroadcastCommandEvent extends Event {
	public GMBroadcastCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GMBroadcastCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready
				&& this.getPlayer().getAccess() == AccessStatus.GameMaster) {
			String data = ((String) this.getData()).substring(11);
			if (data.length() <= 0)
				return;

			world.sendToAll("$7" + data);
		}

	}

}
