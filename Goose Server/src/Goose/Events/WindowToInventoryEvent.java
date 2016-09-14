package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Window;

/**
 * WindowToInventoryEvent
 */
public class WindowToInventoryEvent extends Event {
    public WindowToInventoryEvent() throws Exception {
        super("WindowToInventoryEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new WindowToInventoryEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            int invslot, windowid, windowslot;
            String data = ((String) this.getData()).substring(3);
            try {
                String[] t = data.split(",");
                windowid = Integer.valueOf(t[0]);
                windowslot = Integer.valueOf(t[1]);
                invslot = Integer.valueOf(t[2]);
            } catch (Exception __dummyCatchVar0) {
                invslot = 0;
                windowid = 0;
                windowslot = 0;
            }

            if (invslot <= 0 || invslot > GameSettings.getDefault().getInventorySize()) return;

            if (windowid <= 0) return;

            for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar0;
                if (window.getID() == windowid) {
                    window.windowToInventory(this.getPlayer(), windowslot, invslot, world);
                    return;
                }

            }
        }

    }

}
