package Goose.Events;

import Goose.Event;
import Goose.Events.GroupRemoveEvent;
import Goose.GameWorld;

/**
 * GroupRemoveEvent
 * 
 * /groupremove - removes themselves from group /groupremove player - removes player from group
 * 
 */
public class GroupRemoveEvent extends Event {
  public GroupRemoveEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GroupRemoveEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      String name = ((String) this.getData()).substring(12);
      if (name.equals("")) {
        if (this.getPlayer().getGroup() == null) {
          world.send(this.getPlayer(), "$3You are not in a group.");
          return;
        } else {
          this.getPlayer().getGroup()
              .removePlayer(this.getPlayer(), world, false, this.getPlayer());
          return;
        }
      }

      name = name.substring(1);
      if (name.length() <= 0) return;

      Goose.Player player = world.getPlayerHandler().getPlayer(name);
      if (player != null) {
        if (player.getGroup() == null) {
          world.send(this.getPlayer(), "$3Player is not in a group.");
          return;
        }

        if (this.getPlayer().getGroup() == player.getGroup()) {
          this.getPlayer().getGroup()
              .removePlayer(player, world, (this.getPlayer() != player), this.getPlayer());
        } else {
          world.send(this.getPlayer(), "$3Player isn't in your group.");
          return;
        }
      } else {
        world.send(this.getPlayer(), "$3Couldn't find player.");
      }
    }

  }

}
