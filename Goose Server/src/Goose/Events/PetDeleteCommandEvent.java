package Goose.Events;

import Goose.Event;
import Goose.Events.PetDeleteCommandEvent;
import Goose.GameWorld;
import Goose.Pet;

public class PetDeleteCommandEvent extends Event {
  public PetDeleteCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new PetDeleteCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      String data = ((String) this.getData()).substring(11);
      int id = 0;
      try {
        id = Integer.valueOf(data);
      } catch (Exception __dummyCatchVar0) {
        id = 0;
      }

      if (id <= 0) {
        world.send(this.getPlayer(), "$7Invalid pet ID.");
        return;
      }

      Pet match = null;
      for (Object __dummyForeachVar0 : this.getPlayer().getPets()) {
        Pet pet = (Pet) __dummyForeachVar0;
        if (pet.getPetID() == id) {
          match = pet;
          break;
        }

      }
      if (match == null) {
        world.send(this.getPlayer(), "$7Couldn't find pet matching ID.");
        return;
      }

      match.destroy(world);
      this.getPlayer().getPets().remove(match);
      match.setDelete(true);
      match.saveToDatabase(world);
    }

  }

}
