package Goose.Events;

import java.util.List;

import Goose.Event;
import Goose.Events.PlayerRightClickEvent;
import Goose.GameWorld;

/**
 * PlayerRightClickEvent, Player right clicked
 * 
 * Packet format: RCx,y
 * 
 */
public class PlayerRightClickEvent extends Event {
	public PlayerRightClickEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new PlayerRightClickEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			int x = 0;
			int y = 0;
			String data = ((String) this.getData()).substring(2);
			if (data.length() >= 3) {
				String[] t = data.split(",");
				if (t.length == 2) {
					try {
						x = Integer.valueOf(t[0]);
						y = Integer.valueOf(t[1]);
					} catch (Exception __dummyCatchVar0) {
						x = 0;
						y = 0;
					}

				}

			}

			if (x <= 0 || y <= 0 || x > this.getPlayer().getMap().getWidth()
					|| y > this.getPlayer().getMap().getHeight()) {
				return;
			}

			// log bad right click
			// Look for any vendors
			List<Goose.NPC> range = this.getPlayer().getMap()
					.getNPCsInRange(this.getPlayer());
			for (Goose.NPC npc : range) {
				if (npc.getMapX() == x && npc.getMapY() == y
						&& npc.getVendorItems() != null) {
					npc.openVendorWindow(this.getPlayer(), world);
					return;
				}

			}
		}

	}

}
