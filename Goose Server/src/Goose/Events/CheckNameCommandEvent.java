package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

public class CheckNameCommandEvent extends Event {
    public CheckNameCommandEvent() throws Exception {
        super("CheckNameCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new CheckNameCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready
                && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
            String name = ((String) this.getData()).substring(11);
            Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
            if (player == null) {
                world.send(this.getPlayer(), "$7" + name + " is currently unused.");
            } else {
                world.send(this.getPlayer(), "$7" + name + " is used.");
            }
        }

    }

}
