package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Window;
import Goose.Window.WindowTypes;

public class CharacterInfoCommandEvent extends Event {
    public CharacterInfoCommandEvent() throws Exception {
        super("CharacterInfoCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new CharacterInfoCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            for (Object __dummyForeachVar0 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar0;
                if (window.getType() == WindowTypes.CharInfo) {
                    window.refresh(this.getPlayer(), world);
                    return;
                }

            }
            Window w = new Window();
            w.setTitle("Character Info:");
            w.setType(WindowTypes.CharInfo);
            w.setButtons("0,0,0,0,0");
            this.getPlayer().getWindows().add(w);
            w.create(this.getPlayer(), world);
        }

    }

}
