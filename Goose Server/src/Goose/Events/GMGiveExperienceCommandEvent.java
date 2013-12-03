package Goose.Events;

import Goose.Event;
import Goose.Events.GMGiveExperienceCommandEvent;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

public class GMGiveExperienceCommandEvent extends Event {
  public GMGiveExperienceCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GMGiveExperienceCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready
        && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
      String packet = (String) this.getData();
      String[] tokens = packet.split(" ");
      if (tokens.length < 3) return;

      String name = tokens[1];
      long exp = 0;
      try {
        exp = Long.valueOf(tokens[2]);
      } catch (Exception __dummyCatchVar0) {
        exp = 0;
      }

      Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
      if (player == null) {
        world.send(this.getPlayer(), "$7Player " + name + " doesn't exist.");
        return;
      }

      player.setExperience(player.getExperience() + exp);
      world.send(this.getPlayer(), "$7Added experience successfully.");
      if (player.getState() != Goose.Player.States.NotLoggedIn) {
        world.send(player, player.sNFString());
        world.send(player, player.tNLString());
      } else {
        player.saveToDatabase(world);
      }
    }

  }

}
