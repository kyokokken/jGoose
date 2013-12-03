package Goose.Events;

import Goose.Event;
import Goose.Events.VendorPurchaseInventoryEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Item;
import Goose.Map;
import Goose.NPCVendorSlot;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * Player bought item from vendor
 * 
 * Format: VPInpcid,slotid
 * 
 */
public class VendorPurchaseInventoryEvent extends Event {
  public VendorPurchaseInventoryEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new VendorPurchaseInventoryEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      int npcid = 0;
      int slotid = 0;
      String[] t = ((String) this.getData()).substring(3).split(",");
      // log bad packet
      if (t.length != 2) return;

      try {
        npcid = Integer.valueOf(t[0]);
        slotid = Integer.valueOf(t[1]);
      } catch (Exception __dummyCatchVar0) {
        npcid = 0;
        slotid = 0;
      }

      if (npcid <= 0 || slotid <= 0 || slotid > GameSettings.getDefault().getVendorSlotSize())
        return;

      Goose.NPC npc = null;
      for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
        Window window = (Window) __dummyForeachVar0;
        if (window.getType() == WindowTypes.Vendor && window.getNPC().getLoginID() == npcid) {
          npc = window.getNPC();
          break;
        }

      }
      if (npc == null) return;

      if (npc.getState() != Goose.NPC.States.Alive || npc.getMap() != this.getPlayer().getMap()
          || Math.abs(npc.getMapX() - this.getPlayer().getMapX()) > Map.RANGE_X
          || Math.abs(npc.getMapY() - this.getPlayer().getMapY()) > Map.RANGE_Y) {
        return;
      }

      NPCVendorSlot slot = npc.getVendorItems()[slotid];
      // log bad slot purchase
      if (slot == null) return;

      if (slot.getItemTemplate().getIsLore()
          && this.getPlayer().hasItem(slot.getItemTemplate().getID())) {
        world.send(this.getPlayer(), "$7Can't purchase " + slot.getItemTemplate().getName()
            + " as it is LORE and you already have this item.");
        return;
      }

      if (slot.getItemTemplate().getValue() * slot.getStack() > this.getPlayer().getGold()) {
        world.send(this.getPlayer(),
            "$7Can't purchase " + slot.getItemTemplate().getName()
                + (slot.getStack() > 1 ? " (" + slot.getStack() + ")" : "")
                + " as you don't have enough gold.");
        return;
      }

      Item item = new Item();
      item.loadFromTemplate(slot.getItemTemplate());
      world.getItemHandler().addItem(item);
      if (this.getPlayer().getInventory().addItem(item, slot.getStack(), world)) {
        this.getPlayer().removeGold(slot.getItemTemplate().getValue() * slot.getStack(), world);
        if (item.getIsBindOnPickup()) {
          item.setIsBound(true);
        }

        world.send(
            this.getPlayer(),
            "$7Purchased " + slot.getItemTemplate().getName()
                + (slot.getStack() > 1 ? " (" + slot.getStack() + ")" : "") + " for "
                + slot.getItemTemplate().getValue() * slot.getStack() + " gold.");
        return;
      } else {
        item.setDelete(true);
        world.send(this.getPlayer(), "$7Can't purchase " + slot.getItemTemplate().getName()
            + " as your inventory is full.");
        return;
      }
    }

  }

}
