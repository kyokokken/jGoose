package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Pet;

public class PetVitaCommandEvent extends Event {
    public PetVitaCommandEvent() throws Exception {
        super("PetVitaCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PetVitaCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String[] data = ((String) this.getData()).split(" ");
            long bought = 0;
            long soldexp = 0;
            int buys = 1;
            int petid = 0;
            try {
                petid = Integer.valueOf(data[1]);
            } catch (Exception __dummyCatchVar0) {
                petid = 0;
            }

            try {
                buys = Integer.valueOf(data[2]);
            } catch (Exception __dummyCatchVar1) {
                buys = 1;
            }

            if (buys <= 0) {
                world.send(this.getPlayer(), "$7Invalid buy amount.");
                return;
            }

            if (petid <= 0) {
                world.send(this.getPlayer(), "$7Invalid pet id.");
                return;
            }

            Pet match = null;
            for (Pet pet : this.getPlayer().getPets()) {
                if (pet.getPetID() == petid) {
                    match = pet;
                    break;
                }

            }
            if (match == null) {
                world.send(this.getPlayer(), "$7Couldn't find pet matching ID.");
                return;
            }

            if (match.getClas().getLevel(match.getLevel()).getExperience() != 0) return;

            match.removeStats(match.getBaseStats(), world);
            double buyrate = 0;
            long expcost;
            for (int i = 1; i <= buys; i++) {
                buyrate =
                        ((match.getBaseStats().getHP() / GameSettings.getDefault().getIncreasePetVitaBuyCost()) * (double) .2) + 1;
                expcost = (long) (GameSettings.getDefault().getPetVitaCost() * buyrate);
                if (match.getExperience() >= expcost) {
                    match.setExperience(match.getExperience() - expcost);
                    match.setExperienceSold(match.getExperienceSold() + expcost);
                    match.getBaseStats().setHP(
                            match.getBaseStats().getHP() + GameSettings.getDefault().getPetVitaBuyAmount());
                    bought += GameSettings.getDefault().getPetVitaBuyAmount();
                    soldexp += expcost;
                } else {
                    break;
                }
            }
            match.addStats(match.getBaseStats(), world);
            if (bought == 0) return;

            world.send(this.getPlayer(), "$7Bought " + bought + " hp for " + soldexp + " experience.");
        }

    }

}
