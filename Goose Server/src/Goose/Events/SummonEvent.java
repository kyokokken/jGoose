package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

/**
 * /summon playername
 */
public class SummonEvent extends Event {
    public SummonEvent() throws Exception {
        super("SummonEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new SummonEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready
                && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
            String name = ((String) this.getData()).substring(8);
            Goose.Player player = world.getPlayerHandler().getPlayer(name);
            if (player != null) {
                if (player.getState() != Goose.Player.States.Ready) {
                    world.send(this.getPlayer(), "$7Player is still loading a map.");
                    return;
                }

                player.warpTo(world, this.getPlayer().getMap(), this.getPlayer().getMapX(), this
                        .getPlayer().getMapY());
            } else {
                world.send(this.getPlayer(), "$7Couldn't find player.");
            }
        }

    }

}
