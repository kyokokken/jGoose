package Goose.Events;

import Goose.Event;
import Goose.Events.WindowButtonClickEvent;
import Goose.GameWorld;
import Goose.Window;

/**
 * WindowButtonClickEvent
 * 
 * Player clicked a button in a window
 * 
 * Format: WBCbuttonid,windowid,npcid,0,0
 * 
 * 0s are currently unknown
 * 
 */
public class WindowButtonClickEvent extends Event {
  public WindowButtonClickEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new WindowButtonClickEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      int buttonid = 0;
      int windowid = 0;
      int npcid = 0;
      int id2 = 0;
      int id3 = 0;
      String[] t = ((String) this.getData()).substring(3).split(",");
      // log bad packet
      if (t.length != 5) return;

      try {
        buttonid = Integer.valueOf(t[0]);
        windowid = Integer.valueOf(t[1]);
        npcid = Integer.valueOf(t[2]);
        id2 = Integer.valueOf(t[3]);
        id3 = Integer.valueOf(t[4]);
      } catch (Exception __dummyCatchVar0) {
        buttonid = -1;
      }

      if (buttonid <= -1 || buttonid >= Goose.Window.ButtonTypes.values().length) return;

      for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
        Window window = (Window) __dummyForeachVar0;
        if (window.getID() == windowid) {
          switch (Goose.Window.ButtonTypes.values()[buttonid]) {
            case Exit:
            case Close:
              switch (window.getType()) {
                case Vendor:
                  window.getNPC().closeVendorWindow(window, this.getPlayer(), world);
                  break;
                case CombineBag:
                  this.getPlayer().getWindows().remove(window);
                  break;
                case ItemInfo:
                case CharInfo:
                case Rank:
                case PetInfo:
                  this.getPlayer().getWindows().remove(window);
                  break;

              }
              break;
            case Combine:
              switch (window.getType()) {
                case CombineBag:
                  this.getPlayer().getInventory().combine(world);
                  break;

              }
              break;

          }
          return;
        }

      }
    }

  }

}
