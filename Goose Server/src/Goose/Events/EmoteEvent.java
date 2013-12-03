package Goose.Events;

import Goose.Event;
import Goose.Events.EmoteEvent;
import Goose.GameWorld;

/**
 * EmoteEvent
 * 
 * Format: EMOTn
 * 
 * n is 1-12
 * 
 */
public class EmoteEvent extends Event {

  final int MAX_EMOTES = 12;

  public EmoteEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new EmoteEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      String data = ((String) this.getData()).substring(4);
      if (data.length() <= 0) return;

      int emot = 0;
      try {
        emot = Integer.valueOf(data);
      } catch (Exception __dummyCatchVar0) {
        emot = 0;
      }

      if (emot <= 0 || emot > MAX_EMOTES) return;

      String packet = "EMOT" + this.getPlayer().getLoginID() + "," + emot;
      world.send(this.getPlayer(), packet);
      for (Object __dummyForeachVar0 : this.getPlayer().getMap()
          .getPlayersInRange(this.getPlayer())) {
        Goose.Player player = (Goose.Player) __dummyForeachVar0;
        world.send(player, packet);
      }
    }

  }

}
