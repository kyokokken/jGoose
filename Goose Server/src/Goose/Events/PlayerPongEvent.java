package Goose.Events;

import Goose.Event;
import Goose.Events.PlayerPongEvent;
import Goose.GameWorld;

/**
 * PlayerPongEvent, event for PONG packet
 * 
 */
public class PlayerPongEvent extends Event {
	public PlayerPongEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new PlayerPongEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() != Goose.Player.States.NotLoggedIn) {
			this.getPlayer().setLastPing(world.getTimeNow());
		}

	}

}