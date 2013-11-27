package Goose.Events;

import Goose.Event;
import Goose.Events.RespawnMapCommandEvent;
import Goose.GameWorld;

public class RespawnMapCommandEvent extends Event {
	public RespawnMapCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new RespawnMapCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready
				&& this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
			for (Object __dummyForeachVar0 : this.getPlayer().getMap()
					.getNPCs()) {
				Goose.NPC npc = (Goose.NPC) __dummyForeachVar0;
				if (npc.getState() == Goose.NPC.States.Dead) {
					npc.spawn(world);
				}

			}
			world.sendToMap(this.getPlayer().getMap(), "$7Respawned all NPCs.");
		}

	}

}
