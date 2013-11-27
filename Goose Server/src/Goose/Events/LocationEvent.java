package Goose.Events;

import Goose.Event;
import Goose.Events.LocationEvent;
import Goose.GameWorld;

/**
 * Event for /location command
 * 
 */
public class LocationEvent extends Event {
	public LocationEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new LocationEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			world.send(this.getPlayer(), "$7You are in "
					+ this.getPlayer().getMap().getName() + " at "
					+ this.getPlayer().getMapX() + ","
					+ this.getPlayer().getMapY() + ".");
		}

	}

}
