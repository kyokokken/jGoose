package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.Map;
import Goose.NPC.Aggro;
import Goose.NPC.States;
import Goose.Player;
import Goose.SpellEffect.EffectTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * NPCMoveEvent
 */
public class NPCMoveEvent extends Event {
    public NPCMoveEvent() throws Exception {
        super("NPCMoveEvent");
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getNPC().getState() == States.Alive) {
            this.getNPC().setMoveEvent(null);
            for (Object __dummyForeachVar0 : this.getNPC().getBuffs()) {
                Buff b = (Buff) __dummyForeachVar0;
                // can't move when stunned or rooted
                if (b.getSpellEffect().getEffectType() == EffectTypes.Stun
                        || b.getSpellEffect().getEffectType() == EffectTypes.Root) {
                    this.getNPC().addMoveEvent(world);
                    return;
                }

            }
            int direction;
            if (this.getNPC().getAggroTarget() == null) {
                if (this.getNPC().getMoveSpeed() <= 0.0) return;

                if (!this.getNPC().getCanMove()) {
                    // Return to spawn point if stationary npc
                    if (this.getNPC().getMapX() != this.getNPC().getSpawnX()
                            || this.getNPC().getMapY() != this.getNPC().getSpawnY()) {
                        direction =
                                this.getNPC().nextStepTo(this.getNPC().getSpawnX(), this.getNPC().getSpawnY(),
                                        world);
                    } else {
                        return;
                    }
                } else {
                    direction = world.getRandom().nextInt(4) + 1;
                }
            } else {
                // Fix being hit from across map bug.
                // If the player isn't on same map/in screen range then lose
                // aggro
                if (this.getNPC().getAggroTarget().getMap() == this.getNPC().getMap()
                        && Math.abs(this.getNPC().getAggroTarget().getMapX() - this.getNPC().getMapX()) < Map.RANGE_X
                        && Math.abs(this.getNPC().getAggroTarget().getMapY() - this.getNPC().getMapY()) < Map.RANGE_Y) {
                    direction =
                            this.getNPC().nextStepTo(this.getNPC().getAggroTarget().getMapX(),
                                    this.getNPC().getAggroTarget().getMapY(), world);
                } else {
                    this.getNPC().getAggroTargetToValue().remove(this.getNPC().getAggroTarget());
                    this.getNPC().setAggroTarget(null);
                    List<Player> remove = new ArrayList<Player>();
                    Goose.Player highest = null;
                    Goose.NPC.Aggro aggro = new Goose.NPC.Aggro(0, 0);
                    for (Entry<Player, Aggro> p : this.getNPC().getAggroTargetToValue().entrySet()) {
                        if (p.getValue().compareTo(aggro) > 0) {
                            if (p.getKey().getMap() == this.getNPC().getMap()
                                    && Math.abs(p.getKey().getMapX() - this.getNPC().getMapX()) < Map.RANGE_X
                                    && Math.abs(p.getKey().getMapY() - this.getNPC().getMapY()) < Map.RANGE_Y) {
                                aggro = p.getValue();
                                highest = p.getKey();
                            } else {
                                remove.add(p.getKey());
                            }
                        }


                    }
                    for (Goose.Player p : remove) {
                        this.getNPC().getAggroTargetToValue().remove(p);
                    }
                    if (highest == null) {
                        this.getNPC().addMoveEvent(world);
                        return;
                    } else {
                        this.getNPC().setAggroTarget(highest);
                        this.getNPC().setAggroValue(aggro);
                        direction =
                                this.getNPC().nextStepTo(this.getNPC().getAggroTarget().getMapX(),
                                        this.getNPC().getAggroTarget().getMapY(), world);
                    }
                }
            }
            int ox = this.getNPC().getMapX();
            int oy = this.getNPC().getMapY();
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
            if (this.getNPC().canMoveTo(x, y)) {
                this.getNPC().moveTo(world, x, y);
                this.getNPC().setFacing(direction);
            } else {
                this.getNPC().faceTo(direction, world);
            }
            this.getNPC().addMoveEvent(world);
        }

    }

}
