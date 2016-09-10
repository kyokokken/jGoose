package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.AccessStatus;
import Goose.Player.States;

/**
 * /approach playername
 */
public class ApproachEvent extends Event {
    public ApproachEvent() throws Exception {
        super("ApproachEvent");
    }

    public static Event create(Player player, Object data) throws Exception {
        Event e = new ApproachEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == States.Ready
                && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
            String name = ((String) this.getData()).substring(10);
            Player player = world.getPlayerHandler().getPlayer(name);
            if (player != null) {
                if (player.getState() != States.Ready) {
                    world.send(this.getPlayer(), "$7Player is still loading a map.");
                    return;
                }

                this.getPlayer().warpTo(world, player.getMap(), player.getMapX(), player.getMapY());
            } else {
                world.send(this.getPlayer(), "$7Couldn't find player.");
            }
        }

    }

}
