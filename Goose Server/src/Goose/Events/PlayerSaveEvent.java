package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class PlayerSaveEvent extends Event {
  public PlayerSaveEvent() throws Exception {
    super();
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() != Goose.Player.States.NotLoggedIn) {
      this.getPlayer().saveToDatabase(world);
      this.getPlayer().addSaveEvent(world);
    }

  }

}
