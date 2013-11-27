package Goose.Events;

import Goose.Event;
import Goose.Events.InventorySplitEvent;
import Goose.GameSettings;
import Goose.GameWorld;

/**
 * InventorySplitEvent, event for "SPLIT" packet
 * 
 * Called when someone splits an item stack in their inventory Packet format:
 * SPLITslotid1,slotid2
 * 
 */
public class InventorySplitEvent extends Event {
	public InventorySplitEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new InventorySplitEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			int id1 = 0;
			int id2 = 0;
			String[] ids = ((String) this.getData()).substring(5).split(",");
			try {
				id1 = Integer.valueOf(ids[0]);
				id2 = Integer.valueOf(ids[1]);
			} catch (Exception __dummyCatchVar0) {
				id1 = 0;
				id2 = 0;
			}

			if (id1 <= 0 || id2 <= 0
					|| id1 > GameSettings.getDefault().getInventorySize()
					|| id2 > GameSettings.getDefault().getInventorySize()) {
				return;
			}

			// log something bad about packet
			this.getPlayer().getInventory().splitSlots(id1, id2, world);
		}

	}

}
