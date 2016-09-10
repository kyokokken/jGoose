package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.ITile;
import Goose.ItemTile;

/**
 * PickupItemEvent, event for "GET" packet
 * <p>
 * Called when someone presses comma
 */
public class PickupItemEvent extends Event {
    public PickupItemEvent() throws Exception {
        super("PickupItemEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PickupItemEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            int x = this.getPlayer().getMapX();
            int y = this.getPlayer().getMapY();
            // check tile at player x,y
            ITile itile = this.getPlayer().getMap().getTile(x, y);
            ItemTile tile;
            if (itile == null) {
                // check tile 1 square in front of player
                int __dummyScrutVar0 = this.getPlayer().getFacing();
                if (__dummyScrutVar0 == 1) {
                    y--;
                } else if (__dummyScrutVar0 == 2) {
                    x++;
                } else if (__dummyScrutVar0 == 3) {
                    y++;
                } else if (__dummyScrutVar0 == 4) {
                    x--;
                }

                itile = this.getPlayer().getMap().getTile(x, y);
                if (itile == null) {
                    return;
                } else {
                    // no items
                    // log no items, i don't think the client sends get unless
                    // there's an item
                    if (itile instanceof ItemTile) {
                        tile = (ItemTile) itile;
                    } else {
                        return;
                    }
                    // So players can only pick up 1 tile in front if it's their
                    // item
                    // and within time limit
                    if (!(tile.getOwner() == this.getPlayer() || (tile.getOwner().getGroup() != null && tile
                            .getOwner().getGroup().getPlayers().contains(this.getPlayer())))) {
                        return;
                    }

                }
            }

            if (itile instanceof ItemTile) {
                tile = (ItemTile) itile;
            } else {
                return;
            }
            // Can't pick up cause not owner and it's not past the time limit
            if (!(tile.getPickupTime() < world.getTimeNow() || (tile.getOwner() == this.getPlayer() || (tile
                    .getOwner().getGroup() != null && tile.getOwner().getGroup().getPlayers()
                    .contains(this.getPlayer()))))) {
                return;
            }

            // picked up gold
            if (tile.getItemSlot().getItem().getItemID() == GameSettings.getDefault()
                    .getItemIDStartpoint() + GameSettings.getDefault().getGoldItemID()) {
                this.getPlayer().addGold(tile.getItemSlot().getStack(), world);
                this.getPlayer().getMap().removeItem(tile, world);
            } else {
                if (tile.getItemSlot().getItem().getIsLore()
                        && this.getPlayer().hasItem(tile.getItemSlot().getItem().getTemplate().getID()))
                    return;

                if (this.getPlayer().getInventory()
                        .addItem(tile.getItemSlot().getItem(), tile.getItemSlot().getStack(), world)) {
                    this.getPlayer().getMap().removeItem(tile, world);
                    if (tile.getItemSlot().getItem().getIsBindOnPickup()) {
                        tile.getItemSlot().getItem().setIsBound(true);
                        tile.getItemSlot().getItem().setDirty(true);
                    }

                } else {
                    return;
                }
            }
            if (this.getPlayer().getGroup() != null) {
                this.getPlayer().getGroup().itemPickup(this.getPlayer(), tile.getItemSlot(), world);
            }

        }

    }

}
