package Goose.Events;

import Goose.Event;
import Goose.Events.GroupAddEvent;
import Goose.GameWorld;
import Goose.Group;

/**
 * GroupAddEvent
 * 
 * /groupadd player Adds player to group.
 * 
 */
public class GroupAddEvent extends Event {
	public GroupAddEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GroupAddEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			String name = ((String) this.getData()).substring(10);
			Goose.Player player = world.getPlayerHandler().getPlayer(name);
			if (player != null
					&& player.getState() == Goose.Player.States.Ready) {
				if (player == this.getPlayer()) {
					world.send(this.getPlayer(),
							"$3You can't group with yourself.");
					return;
				}

				if (player.getGroup() != null) {
					world.send(this.getPlayer(),
							"$3Player is already in a group.");
					return;
				}

				if (!player.getGroupInvitesEnabled()) {
					world.send(this.getPlayer(),
							"$3Player is not accepting group invitations.");
					return;
				}

				if (this.getPlayer().getGroup() == null) {
					this.getPlayer().setGroup(new Group());
					this.getPlayer().getGroup().getPlayers()
							.add(this.getPlayer());
				}

				this.getPlayer().getGroup()
						.addPlayer(player, world, this.getPlayer());
			} else {
				world.send(this.getPlayer(), "$3Couldn't find player.");
			}
		}

	}

}
