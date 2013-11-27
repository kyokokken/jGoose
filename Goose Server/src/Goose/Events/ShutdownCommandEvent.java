package Goose.Events;

import Goose.Event;
import Goose.Events.ShutdownCommandEvent;
import Goose.GameWorld;

/**
 * Called when GM types /shutdown
 * 
 */
public class ShutdownCommandEvent extends Event {
	public ShutdownCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new ShutdownCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
				world.setRunning(false);
			}

		}

	}

}
