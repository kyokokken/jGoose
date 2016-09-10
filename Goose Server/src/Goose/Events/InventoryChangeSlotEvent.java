package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

/**
 * InventoryChangeSlotEvent, event for "CHANGE" packet
 * <p>
 * Called when someone moves an item in their inventory Packet format: CHANGEslotid1,slotid2
 */
public class InventoryChangeSlotEvent extends Event {
    public InventoryChangeSlotEvent() throws Exception {
        super("InventoryChangeSlotEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new InventoryChangeSlotEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            int id1 = 0;
            int id2 = 0;
            String[] ids = ((String) this.getData()).substring(6).split(",");
            try {
                id1 = Integer.valueOf(ids[0]);
                id2 = Integer.valueOf(ids[1]);
            } catch (Exception __dummyCatchVar0) {
                id1 = 0;
                id2 = 0;
            }

            if (id1 <= 0 || id2 <= 0) {
                return;
            }

            // log something bad about packet
            this.getPlayer().getInventory().swapSlots(id1, id2, world);
        }
    }

}
