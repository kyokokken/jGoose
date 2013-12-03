package Goose.Events;

import Goose.Event;
import Goose.Events.GMGetItemCommandEvent;
import Goose.GameWorld;
import Goose.Item;
import Goose.ItemTemplate;
import Goose.Player.AccessStatus;

/**
 * /getitem templateid stack
 * 
 */
public class GMGetItemCommandEvent extends Event {
  public GMGetItemCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GMGetItemCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      if (this.getPlayer().getAccess() != AccessStatus.GameMaster) return;

      int id = 0;
      int stack = 1;
      String[] t = ((String) this.getData()).split(" ");
      if (t.length <= 2) {
        try {
          id = Integer.valueOf(t[1]);
        } catch (Exception __dummyCatchVar0) {
          return;
        }

      }

      if (t.length >= 3) {
        try {
          id = Integer.valueOf(t[1]);
          stack = Integer.valueOf(t[2]);
        } catch (Exception __dummyCatchVar1) {
          stack = 1;
        }

      }

      if (id <= 0 || stack <= 0) return;

      ItemTemplate template = world.getItemHandler().getTemplate(id);
      if (template == null) return;

      Item item = new Item();
      item.loadFromTemplate(template);
      world.getItemHandler().addItem(item);
      this.getPlayer().getInventory().addItem(item, stack, world);
    }

  }

}
