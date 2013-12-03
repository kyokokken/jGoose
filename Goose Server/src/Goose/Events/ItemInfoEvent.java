package Goose.Events;

import Goose.Event;
import Goose.Events.ItemInfoEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.NPCVendorSlot;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * ItemInfoEvent
 * 
 * Packet: GIDitemid
 * 
 * Note: if itemid is lessthan GameSettings.Default.ItemIDStartpoint then it's a template id else
 * it's an item id
 * 
 */
public class ItemInfoEvent extends Event {
  public ItemInfoEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new ItemInfoEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      int itemid = 0;
      String data = ((String) this.getData()).substring(3);
      // log bad id
      if (data.length() <= 0) return;

      try {
        itemid = Integer.valueOf(data);
      } catch (Exception __dummyCatchVar0) {
        itemid = 0;
      }

      // log bad item id
      if (itemid <= 0) return;

      if (itemid >= GameSettings.getDefault().getItemIDStartpoint()) {
        this.getPlayer().getInventory().showStatsWindow(itemid, world);
      } else {
        for (Window window : this.getPlayer().getWindows()) {
          if (window.getType() != WindowTypes.Vendor) continue;

          for (NPCVendorSlot slot : window.getNPC().getVendorItems()) {
            if (slot == null) continue;

            if (slot.getItemTemplate().getID() == itemid) {
              this.getPlayer().showStatsWindow(itemid, world);
              return;
            }

          }
        }
      }
    }
  }
}
