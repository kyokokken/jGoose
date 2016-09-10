package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class UnbanCommandEvent extends Event {
    public UnbanCommandEvent() throws Exception {
        super("UnbanCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new UnbanCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready
                && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
            String name = ((String) this.getData()).substring(7);
            Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
            if (player != null) {
                player.setAccess(Goose.Player.AccessStatus.Normal);
                world.send(this.getPlayer(), "$7Unbanned " + name + ".");
                if (player.getState() == Goose.Player.States.NotLoggedIn) {
                    player.saveToDatabase(world).start();
                }

            } else {
                world.send(this.getPlayer(), "$7Couldn't find player.");
            }
        }

    }

}
