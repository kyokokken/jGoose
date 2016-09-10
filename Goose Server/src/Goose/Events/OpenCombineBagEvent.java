package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * OpenCombineBagEvent, opens combine bag
 * <p>
 * Packet: OCB
 */
public class OpenCombineBagEvent extends Event {
    public OpenCombineBagEvent() throws Exception {
        super("OpenCombineBagEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new OpenCombineBagEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar0;
                if (window.getType() == WindowTypes.CombineBag) {
                    window.refresh(this.getPlayer(), world);
                    return;
                }

            }
            Window w = new Window();
            w.setTitle("Combine Bag");
            w.setButtons("1,0,0,0,0");
            w.setType(WindowTypes.CombineBag);
            this.getPlayer().getWindows().add(w);
            w.create(this.getPlayer(), world);
        }

    }

}
