package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.Pet;
import Goose.Pet.Modes;
import Goose.SpellEffect.EffectTypes;

public class PetMoveEvent extends Event {
    public PetMoveEvent() throws Exception {
        super("PetMoveEvent");
    }

    public void ready(GameWorld world) throws Exception {
        Pet pet = (Pet) this.getPlayer();
        pet.setMoveEvent(null);
        if (pet.getIsAlive()) {
            if (pet.getOwner().getMap() != pet.getMap()) {
                pet.destroy(world);
                return;
            }

            for (Buff b : pet.getBuffs()) {
                // can't move when stunned or rooted
                if (b.getSpellEffect().getEffectType() == EffectTypes.Stun
                        || b.getSpellEffect().getEffectType() == EffectTypes.Root) {
                    pet.addMoveEvent(world);
                    return;
                }

            }
            if (pet.getTarget() != null
                    && (pet.getTarget().getMap() != pet.getMap() || (pet.getTarget() instanceof Goose.NPC && ((Goose.NPC) pet
                    .getTarget()).getState() != Goose.NPC.States.Alive))) {
                pet.setTarget(null);
                if (pet.getMode() == Modes.Attack) {
                    pet.setMode(Modes.Neutral);
                }

            }

            int direction = 1;
            switch (pet.getMode()) {
                case Neutral:
                    direction =
                            pet.nextStepTo(pet.getOwner().getMapX() + world.getRandom().nextInt(4) - 2, pet
                                    .getOwner().getMapY() + world.getRandom().nextInt(4) - 2, world);
                    break;
                case Follow:
                    direction = pet.nextStepTo(pet.getOwner().getMapX(), pet.getOwner().getMapY(), world);
                    break;
                case Defend:
                case Attack:
                    if (pet.getTarget() == null) {
                        direction =
                                pet.nextStepTo(pet.getOwner().getMapX() + world.getRandom().nextInt(4) - 2, pet
                                        .getOwner().getMapY() + world.getRandom().nextInt(4) - 2, world);
                    } else {
                        direction = pet.nextStepTo(pet.getTarget().getMapX(), pet.getTarget().getMapY(), world);
                    }
                    break;

            }
            int ox = pet.getMapX();
            int oy = pet.getMapY();
            int x = ox;
            int y = oy;
            switch (direction) {
                case 1:
                    y--;
                    break;
                case 2:
                    x++;
                    break;
                case 3:
                    y++;
                    break;
                case 4:
                    x--;
                    break;

            }
            if (pet.canMoveTo(x, y)) {
                pet.moveTo(world, x, y);
                pet.setFacing(direction);
            } else {
                pet.faceTo(direction, world);
            }
            pet.addMoveEvent(world);
        }

    }

}
