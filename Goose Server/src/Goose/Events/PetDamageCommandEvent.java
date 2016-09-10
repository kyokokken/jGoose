package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Pet;

public class PetDamageCommandEvent extends Event {
    public PetDamageCommandEvent() throws Exception {
        super("PetDamageCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PetDamageCommandEvent();
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
            for (Object __dummyForeachVar0 : this.getPlayer().getPets()) {
                Pet pet = (Pet) __dummyForeachVar0;
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

            double buyrate = 0;
            long expcost;
            for (int i = 1; i <= buys; i++) {
                buyrate =
                        ((match.getWeaponDamage() / GameSettings.getDefault().getIncreasePetDamageBuyCost()) * (double) .2) + 1;
                expcost = (long) (GameSettings.getDefault().getPetDamageCost() * buyrate);
                if (match.getExperience() >= expcost) {
                    match.setExperience(match.getExperience() - expcost);
                    match.setExperienceSold(match.getExperienceSold() + expcost);
                    match.setWeaponDamage(match.getWeaponDamage()
                            + GameSettings.getDefault().getPetDamageBuyAmount());
                    bought += GameSettings.getDefault().getPetDamageBuyAmount();
                    soldexp += expcost;
                } else {
                    break;
                }
            }
            if (bought == 0) return;

            world
                    .send(this.getPlayer(), "$7Bought " + bought + " damage for " + soldexp + " experience.");
        }

    }

}
