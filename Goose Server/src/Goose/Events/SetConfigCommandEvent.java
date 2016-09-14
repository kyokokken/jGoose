package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class SetConfigCommandEvent extends Event {
    public SetConfigCommandEvent() throws Exception {
        super("SetConfigCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new SetConfigCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
            //TODO: Remove this.
        }

    }

}
