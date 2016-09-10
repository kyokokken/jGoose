package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.ItemSlot;
import Goose.ItemTile;

/**
 * PlayerDropGoldEvent, event for "/dropgold " packet
 * <p>
 * Packet syntax: /dropgold amount
 */
public class PlayerDropGoldEvent extends Event {
    public PlayerDropGoldEvent() throws Exception {
        super("PlayerDropGoldEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PlayerDropGoldEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            long gold = 0;
            try {
                gold = Integer.valueOf(((String) this.getData()).split(" ")[1]);
            } catch (Exception __dummyCatchVar0) {
                gold = 0;
            }

            // log bad drop gold stuff
            if (gold == 0) return;

            if (gold > this.getPlayer().getGold()) return;

            if (this.getPlayer().getLevel() < 10) {
                world.send(this.getPlayer(), "$7You need to be level 10 or higher to drop gold.");
                return;
            }

            this.getPlayer().removeGold(gold, world);
            ItemTile tile = new ItemTile();
            tile.setItemSlot(new ItemSlot());
            tile.getItemSlot().setItem(world.getItemHandler().getGold());
            tile.getItemSlot().setStack(gold);
            tile.setX(this.getPlayer().getMapX());
            tile.setY(this.getPlayer().getMapY());
            tile.setOwner(this.getPlayer());
            this.getPlayer().getMap().placeItem(tile);
            // tile can stack
            ItemTile maptile = (ItemTile) this.getPlayer().getMap().getTile(tile.getX(), tile.getY());
            if (maptile != null && maptile instanceof ItemTile) {
                maptile.getItemSlot().setStack(
                        maptile.getItemSlot().getStack() + tile.getItemSlot().getStack());
                world.sendToMap(this.getPlayer().getMap(), maptile.mOBString());
            } else {
                this.getPlayer().getMap().addItem(tile, world);
            }
        }

    }
}
