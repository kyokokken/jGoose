package Goose.Events;

import Goose.Event;
import Goose.Events.RefreshPositionEvent;
import Goose.GameWorld;

/**
 * Called when someone types /refresh
 * 
 */
public class RefreshPositionEvent extends Event {
  public RefreshPositionEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new RefreshPositionEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      // Fix the clients position
      world.send(this.getPlayer(), "SUP" + this.getPlayer().getMapX() + ","
          + this.getPlayer().getMapY());
    }

  }

}
