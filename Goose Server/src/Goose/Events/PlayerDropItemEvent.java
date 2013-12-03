package Goose.Events;

import Goose.Event;
import Goose.Events.PlayerDropItemEvent;
import Goose.GameWorld;
import Goose.ItemSlot;
import Goose.ItemTile;

/**
 * PlayerDropItemEvent, event for "DRP" packet
 * 
 * Packet syntax: DRPinvid,stacksize
 * 
 */
public class PlayerDropItemEvent extends Event {
  public PlayerDropItemEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new PlayerDropItemEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      int id = 0;
      int stack = 0;
      String[] t = ((String) this.getData()).substring(3).split(",");
      try {
        id = Integer.valueOf(t[0]);
        stack = Integer.valueOf(t[1]);
      } catch (Exception __dummyCatchVar0) {
        id = 0;
        stack = 0;
      }

      // log bad drop item stuff
      if (id <= 0 || stack <= 0) return;

      ItemSlot slot = this.getPlayer().getInventory().getSlot(id);
      if (slot == null) return;

      // log bad slot
      if (stack > slot.getStack()) return;

      // log bad stack
      // Can't drop bound item unless gm
      if (slot.getItem().getIsBound()
          && this.getPlayer().getAccess() != Goose.Player.AccessStatus.GameMaster) return;

      ItemSlot drop =
          this.getPlayer().getInventory().removeItem(slot.getItem(), slot.getStack(), world);
      if (drop == null) return;

      ItemTile tile = new ItemTile();
      tile.setItemSlot(drop);
      tile.setX(this.getPlayer().getMapX());
      tile.setY(this.getPlayer().getMapY());
      tile.setOwner(this.getPlayer());
      this.getPlayer().getMap().placeItem(tile);
      // tile can stack
      ItemTile maptile = (ItemTile) this.getPlayer().getMap().getTile(tile.getX(), tile.getY());
      if (maptile != null && maptile instanceof ItemTile) {
        drop.getItem().setDelete(true);
        maptile.getItemSlot().setStack(maptile.getItemSlot().getStack() + drop.getStack());
        world.sendToMap(this.getPlayer().getMap(), maptile.mOBString());
      } else {
        this.getPlayer().getMap().addItem(tile, world);
      }
    }

  }

}
