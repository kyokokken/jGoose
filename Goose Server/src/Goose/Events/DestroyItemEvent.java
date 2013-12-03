package Goose.Events;

import Goose.Event;
import Goose.Events.DestroyItemEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.ItemSlot;

/**
 * DestroyItemEvent, destroy item
 * 
 * Packet: DITM<slot>
 * 
 * Slot can be inventory or an equipped item
 * 
 */
public class DestroyItemEvent extends Event {
  public DestroyItemEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new DestroyItemEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      int id = 0;
      String data = ((String) this.getData()).substring(4);
      try {
        id = Integer.valueOf(data);
      } catch (Exception __dummyCatchVar0) {
        id = 0;
      }

      if (id <= 0
          || id > GameSettings.getDefault().getInventorySize()
              + GameSettings.getDefault().getEquippedSize()) return;

      if (id <= GameSettings.getDefault().getInventorySize()) {
        ItemSlot slot = this.getPlayer().getInventory().getSlot(id);
        if (slot == null) return;

        slot = this.getPlayer().getInventory().removeItem(slot.getItem(), slot.getStack(), world);
        if (slot == null) return;

        slot.getItem().setDelete(true);
      } else {
        ItemSlot slot = this.getPlayer().getInventory().getEquippedSlot(id);
        if (slot == null) return;

        this.getPlayer().getInventory().unequip(id, world);
        slot = this.getPlayer().getInventory().removeItem(slot.getItem(), slot.getStack(), world);
        if (slot == null) return;

        slot.getItem().setDelete(true);
      }
    }

  }

}
