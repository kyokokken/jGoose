package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Pet;
import Goose.Window;
import Goose.Window.WindowTypes;

public class PetInfoCommandEvent extends Event {
    public PetInfoCommandEvent() throws Exception {
        super("PetInfoCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PetInfoCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String data = ((String) this.getData()).substring(9);
            int id = 0;
            try {
                id = Integer.valueOf(data);
            } catch (Exception __dummyCatchVar0) {
                id = 0;
            }

            if (id <= 0) {
                world.send(this.getPlayer(), "$7Invalid pet ID.");
                return;
            }

            Pet match = null;
            for (Object __dummyForeachVar0 : this.getPlayer().getPets()) {
                Pet pet = (Pet) __dummyForeachVar0;
                if (pet.getPetID() == id) {
                    match = pet;
                    break;
                }

            }
            if (match == null) {
                world.send(this.getPlayer(), "$7Couldn't find pet matching ID.");
                return;
            }

            for (Object __dummyForeachVar1 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar1;
                if (window.getType() == WindowTypes.PetInfo && window.getData() == match) {
                    window.refresh(this.getPlayer(), world);
                    return;
                }

            }
            Window w = new Window();
            w.setTitle("Pet Info For ID " + match.getPetID());
            w.setType(WindowTypes.PetInfo);
            w.setButtons("0,0,0,0,0");
            w.setData(match);
            this.getPlayer().getWindows().add(w);
            w.create(this.getPlayer(), world);
        }

    }

}
