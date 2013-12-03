package Goose.Events;

import Goose.Event;
import Goose.Events.HaxCommandEvent;
import Goose.GameWorld;

public class HaxCommandEvent extends Event {
  public HaxCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new HaxCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      world.send(this.getPlayer(), ((String) this.getData()).substring(5));
    }

  }

}
