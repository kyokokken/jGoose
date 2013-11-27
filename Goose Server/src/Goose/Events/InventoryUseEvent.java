package Goose.Events;

import Goose.Event;
import Goose.Events.InventoryUseEvent;
import Goose.GameSettings;
import Goose.GameWorld;

/**
 * InventoryUseEvent, event for "USE" packet
 * 
 * Called when someone uses an item in their inventory Packet format: USEslotid
 * 
 */
public class InventoryUseEvent extends Event {
	public InventoryUseEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new InventoryUseEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			int id = 0;
			try {
				id = Integer.valueOf(((String) this.getData()).substring(3));
			} catch (Exception __dummyCatchVar0) {
				id = 0;
			}

			if (id <= 0) {
				return;
			}

			// log bad id
			int INVSIZE = GameSettings.getDefault().getInventorySize();
			if (id > 0 && id <= INVSIZE) {
				this.getPlayer().getInventory().use(id, world);
			} else if (id > INVSIZE
					&& id <= INVSIZE
							+ GameSettings.getDefault().getEquippedSize() + 1) {
				this.getPlayer().getInventory().unequip(id, world);
			}

		}

	}

}
