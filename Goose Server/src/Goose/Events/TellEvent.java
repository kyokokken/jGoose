package Goose.Events;

import Goose.Event;
import Goose.Events.TellEvent;
import Goose.GameWorld;
import Goose.Player.ToggleSetting;

/**
 * TellEvent, event for "/tell " packet
 * 
 * Called when someone types /tell name message Packet format: /tell name message
 * 
 * Server responds: $6[tell to] name: message, to sender and $6[tell from] sender: message, to name
 * 
 * 
 */
public class TellEvent extends Event {
  public TellEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new TellEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      String info = ((String) this.getData()).substring(6);
      int index = info.indexOf(' ');
      if (index != -1) {
        String name = info.substring(0, (0) + (info.indexOf(' ')));
        String message = info.substring(name.length() + 1);
        if (message.length() > 0) {
          Goose.Player recipient = world.getPlayerHandler().getPlayer(name);
          if (recipient != null
              && recipient.getState().compareTo(Goose.Player.States.LoadingGame) > 0) {
            if ((recipient.getToggleSettings().getValue() & ToggleSetting.Tell.getValue()) == 0) {
              world.send(this.getPlayer(), "$6[tell to] " + recipient.getName() + ": " + message);
              if (recipient.getChatFilterEnabled())
                message = world.getChatFilter().filter(message);

              world.send(recipient, "$6[tell from] " + this.getPlayer().getName() + ": " + message);
            } else {
              world.send(this.getPlayer(), "$7" + recipient.getName() + " has tells disabled.");
            }
          } else {
            world.send(this.getPlayer(), "$6" + name + " is not online.");
          }
        }

      }

    }

  }

}
