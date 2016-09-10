package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.ItemTile;
import Goose.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Clears all items that have existed longer than GameSettings.Default.ItemGroundExistTime seconds.
 * Checks every GameSettings.Default.ItemGroundSweepTime seconds.
 */
public class ClearMapItemsEvent extends Event {
    public ClearMapItemsEvent() throws Exception {
        super("ClearMapItemsEvent");
    }

    public void ready(GameWorld world) throws Exception {
        Map map = (Map) this.getData();
        long existedfor;
        // how long the item has been on the map in seconds
        List<ItemTile> remove = new ArrayList<ItemTile>();
        for (ItemTile item : map.getItems()) {
            existedfor = ((world.getTimeNow() - item.getDroppedTime()) / world.getTimerFrequency());
            if (existedfor < GameSettings.getDefault().getItemGroundExistTime()) continue;

            if (item.getItemSlot().getItem().getItemID() != GameSettings.getDefault().getItemIDStartpoint() + GameSettings.getDefault().getGoldItemID()) {
                item.getItemSlot().getItem().setDelete(true);
            }

            remove.add(item);
        }
        for (ItemTile item : remove) {
            map.removeItem(item, world);
        }
        this.setTicks(world.getTimeNow() + world.getTimerFrequency() * GameSettings.getDefault().getItemGroundSweepTime());
        world.getEventHandler().addEvent(this);
    }

}
