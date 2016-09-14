package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Pet;

// / <summary>
// / Spawns given pet
// /
// / Syntax: /petspawn <id>
// / </summary>
public class PetSpawnCommandEvent extends Event {
    public PetSpawnCommandEvent() throws Exception {
        super("PetSpawnCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PetSpawnCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            if (!this.getPlayer().getMap().getCanSpawnPets()) {
                world.send(this.getPlayer(), "$7Pets are disabled in this map.");
                return;
            }

            String data = ((String) this.getData()).substring(10);
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

            if (match.getNextRespawnTime() > world.getTimeNow()) {
                double wait =
                        ((double) (world.getTimeNow() - match.getNextRespawnTime()) / world.getTimerFrequency());
                wait = Math.round(wait);
                world.send(this.getPlayer(), "$7You must wait " + System.out.printf("%f2", wait)
                        + " seconds to spawn this pet.");
                return;
            }

            match.spawn(world);
        }

    }

}
