package Goose.Events;

import Goose.Event;
import Goose.Events.GMKickCommandEvent;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

/**
 * /kick player
 * 
 * kicks player from server
 * 
 */
public class GMKickCommandEvent extends Event {
  public GMKickCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GMKickCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready
        && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
      String name = ((String) this.getData()).substring(6);
      Goose.Player player = world.getPlayerHandler().getPlayer(name);
      if (player != null) {
        world.lostConnection(player.getSock());
      } else {
        world.send(this.getPlayer(), "$7Couldn't find player.");
      }
    }

  }

}
