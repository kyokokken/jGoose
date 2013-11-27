package Goose.Events;

import Goose.Event;
import Goose.Events.ToggleGroupCommandEvent;
import Goose.GameWorld;

/**
 * ToggleGroupCommandEvent
 * 
 * Event for /togglegroup command
 * 
 * /togglegroup enables/disables allowing players to add you to a group
 * 
 */
public class ToggleGroupCommandEvent extends Event {
	public ToggleGroupCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new ToggleGroupCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			this.getPlayer().setGroupInvitesEnabled(
					!this.getPlayer().getGroupInvitesEnabled());
			if (this.getPlayer().getGroupInvitesEnabled()) {
				world.send(this.getPlayer(),
						"$3Group invitations are now enabled.");
			} else {
				world.send(this.getPlayer(),
						"$3Group invitations have been disabled.");
			}
		}

	}

}
