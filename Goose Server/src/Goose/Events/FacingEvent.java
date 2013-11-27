package Goose.Events;

import java.util.List;

import Goose.Buff;
import Goose.Event;
import Goose.Events.FacingEvent;
import Goose.GameWorld;
import Goose.SpellEffect;
import Goose.SpellEffect.EffectTypes;

/**
 * MoveEvent, event for "F" + 1-4 packet
 * 
 * Called when someone moves Packet format: FDirection Direction being 1-4. For
 * some reason these differ from the moving. direction for facing is as follows.
 * 1,2,3,4 = up,left,right,down
 * 
 * Server responds: CHHLoginID,Facing NOTE: Server remaps the directions 1,2,3,4
 * = 1,3,4,2 Server sends the response to everyone in the area including the
 * player who generated it
 * 
 */
public class FacingEvent extends Event {
	public FacingEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new FacingEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			for (Object __dummyForeachVar0 : this.getPlayer().getBuffs()) {
				Buff b = (Buff) __dummyForeachVar0;
				// can't move when stunned
				if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
					// stunned battletext
					world.send(this.getPlayer(), "BT"
							+ this.getPlayer().getLoginID() + ",50");
					return;
				}

			}
			if (((String) this.getData()).length() == 1)
				return;

			// log bad facing event
			int facing = Integer.valueOf(String.valueOf(((String) this
					.getData()).charAt(1)));
			if (facing <= 0 || facing >= 5)
				return;

			// log bad facing event
			if (facing == 1)
				facing = 1;
			else if (facing == 2)
				facing = 3;
			else if (facing == 3)
				facing = 4;
			else if (facing == 4)
				facing = 2;

			if (this.getPlayer().getFacing() != facing) {
				this.getPlayer().setFacing(facing);
				String packet = "CHH" + this.getPlayer().getLoginID() + ","
						+ this.getPlayer().getFacing();
				world.send(this.getPlayer(), packet);
				List<Goose.Player> range = this.getPlayer().getMap()
						.getPlayersInRange(this.getPlayer());
				for (Goose.Player player : range) {
					world.send(player, packet);
				}
			}

		}

	}

}
