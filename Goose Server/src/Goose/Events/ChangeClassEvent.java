package Goose.Events;

import Goose.Event;
import Goose.Events.ChangeClassEvent;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.States;

/**
 * ChangeClassEvent, changes a players class if they're a commoner
 * 
 * Syntax: /changeclass [rogue|magus|warrior|priest]
 * 
 */
public class ChangeClassEvent extends Event {
  public ChangeClassEvent() throws Exception {
    super();
  }

  public static Event create(Player player, Object data) throws Exception {
    Event e = new ChangeClassEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == States.Ready) {
      if (!this.getPlayer().getClas().getClassName().equals("Commoner")) {
        world.send(this.getPlayer(), "$7/changeclass: You need to be a Commoner to change class.");
        return;
      }

      String packet = (String) this.getData();
      String[] tokens = packet.split(" ");
      if (tokens.length < 2) return;

      String cl = tokens[1];
      String __dummyScrutVar0 = cl.toLowerCase();
      if (__dummyScrutVar0.equals("rogue")) {
        this.getPlayer().changeClass(2, world);
      } else if (__dummyScrutVar0.equals("warrior")) {
        this.getPlayer().changeClass(3, world);
      } else if (__dummyScrutVar0.equals("magus")) {
        this.getPlayer().changeClass(4, world);
      } else if (__dummyScrutVar0.equals("priest")) {
        this.getPlayer().changeClass(5, world);
      } else {
        world.send(this.getPlayer(), "$7/changeclass [rogue|magus|warrior|priest]");
      }
    }

  }

}
