package Goose.Events;

import Goose.Event;
import Goose.Events.GMBanCommandEvent;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

/**
 * /ban player
 * 
 * Bans player from server
 * 
 */
public class GMBanCommandEvent extends Event {
  public GMBanCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GMBanCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready
        && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
      String name = ((String) this.getData()).substring(5);
      Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
      if (player != null) {
        player.setAccess(AccessStatus.Banned);
        world.send(this.getPlayer(), "$7Banned " + name + ".");
        if (player.getState() != Goose.Player.States.NotLoggedIn) {
          world.lostConnection(player.getSock());
        } else {
          player.saveToDatabase(world);
        }
      } else {
        world.send(this.getPlayer(), "$7Couldn't find player.");
      }
    }

  }

}
