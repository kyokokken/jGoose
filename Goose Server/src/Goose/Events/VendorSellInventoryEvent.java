package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.ItemSlot;
import Goose.Map;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * Player sold item to vendor
 * <p>
 * Format: VSInpcid,invslotid,stack
 */
public class VendorSellInventoryEvent extends Event {
    public VendorSellInventoryEvent() throws Exception {
        super("VendorSellInventoryEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new VendorSellInventoryEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            int npcid = 0;
            int slotid = 0;
            int stack = 0;
            String[] t = ((String) this.getData()).substring(3).split(",");
            // log bad packet
            if (t.length != 3) return;

            try {
                npcid = Integer.valueOf(t[0]);
                slotid = Integer.valueOf(t[1]);
                stack = Integer.valueOf(t[2]);
            } catch (Exception __dummyCatchVar0) {
                npcid = 0;
                slotid = 0;
                stack = 0;
            }

            // log bad npc/slot
            if (npcid <= 0 || slotid <= 0 || slotid > GameSettings.getDefault().getInventorySize())
                return;

            Goose.NPC npc = null;
            for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar0;
                if (window.getType() == WindowTypes.Vendor && window.getNPC().getLoginID() == npcid) {
                    npc = window.getNPC();
                    break;
                }

            }
            // log bad npc
            if (npc == null) return;

            if (npc.getState() != Goose.NPC.States.Alive || npc.getMap() != this.getPlayer().getMap()
                    || Math.abs(npc.getMapX() - this.getPlayer().getMapX()) > Map.RANGE_X
                    || Math.abs(npc.getMapY() - this.getPlayer().getMapY()) > Map.RANGE_Y) {
                return;
            }

            ItemSlot slot = this.getPlayer().getInventory().getSlot(slotid);
            // log bad slot
            if (slot == null) return;

            // log bad stack size
            if (stack <= 0 || stack > slot.getStack()) return;

            if (slot.getItem().getValue() == 0) {
                world.send(this.getPlayer(), "$7I have no interest in purchasing " + slot.getItem().getName() + ".");
                return;
            }

            ItemSlot sellslot = this.getPlayer().getInventory().removeItem(slot.getItem(), stack, world);
            this.getPlayer().addGold(sellslot.getStack() * slot.getItem().getValue() / 2, world);
            world.send(this.getPlayer(), "$7Sold " + sellslot.getItem().getName() + (sellslot.getStack() > 1 ? " (" + sellslot.getStack() + ")" : "") + " for "
                            + sellslot.getStack() * sellslot.getItem().getValue() / 2 + " gold.");
        }

    }

}
