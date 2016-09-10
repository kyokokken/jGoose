package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.NPC.States;
import Goose.NPCTemplate;
import Goose.NPCTemplate.BehaviourTypes;
import Goose.SpellEffect.EffectTypes;

public class NPCAttackEvent extends Event {
    public NPCAttackEvent() throws Exception {
        super("NPCAttackEvent");
    }

    public void ready(GameWorld world) throws Exception {
        this.getNPC().setAttackEvent(null);
        if (this.getNPC().getState() == States.Dead) return;

        if (this.getNPC().getAggroTarget() == null) return;

        boolean rooted = false;
        for (Object __dummyForeachVar0 : this.getNPC().getBuffs()) {
            Buff b = (Buff) __dummyForeachVar0;
            // can't attack when stunned
            if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
                this.getNPC().setLastAttackTime(world.getTimeNow());
                this.getNPC().addAttackEvent(world);
                return;
            }

            if (b.getSpellEffect().getEffectType() == EffectTypes.Root) {
                rooted = true;
                this.getNPC().setLastAttackTime(world.getTimeNow());
            }

        }
        // Can't tele when rooted
        if (!rooted
                && this.getNPC().getLastAttackTime() + this.getNPC().getBehaviourTimeout()
                * world.getTimerFrequency() < world.getTimeNow()) {
            NPCTemplate.BehaviourTypes __dummyScrutVar0 = this.getNPC().getBehaviour();
            if (__dummyScrutVar0.equals(BehaviourTypes.TeleportAggro)) {
                boolean loseaggro = true;
                this.getNPC()
                        .getAggroTarget()
                        .warpTo(world, this.getNPC().getMap(), this.getNPC().getMapX(),
                                this.getNPC().getMapY(), !loseaggro);
                // reset attack time so doesn't keep teleporting if it can't
                // attack
                this.getNPC().setLastAttackTime(world.getTimeNow());
            } else if (__dummyScrutVar0.equals(BehaviourTypes.TeleportToAggro)) {
                // move off this square so null
                this.getNPC().getMap().setCharacter(null, this.getNPC().getMapX(), this.getNPC().getMapY());
                this.getNPC().setMapX(this.getNPC().getAggroTarget().getMapX());
                this.getNPC().setMapY(this.getNPC().getAggroTarget().getMapY());
                this.getNPC().getMap().placeCharacter(this.getNPC());
                this.getNPC().moveTo(world, this.getNPC().getMapX(), this.getNPC().getMapY());
                // kinda hackish, moveto will set the aggrotarget char to null
                // so have to reset it
                // this.getNPC()
                // .getMap()
                // .setCharacter(this.getNPC().getAggroTarget(), this.getNPC().getAggroTarget().getMapX(),
                // this.getNPC().getAggroTarget().getMapY());
                // reset attack time so doesn't keep teleporting if it can't
                // attack
                this.getNPC().setLastAttackTime(world.getTimeNow());
            }

        }

        Goose.Player aggro = this.getNPC().getAggroTarget();
        // Try to attack main aggro first
        if (this.getNPC().getMap() == aggro.getMap()
                && Math.abs(this.getNPC().getMapX() - aggro.getMapX()) <= this.getNPC().getAttackRange()
                && Math.abs(this.getNPC().getMapY() - aggro.getMapY()) <= this.getNPC().getAttackRange()) {
            this.getNPC().attack(aggro, world);
            this.getNPC().addAttackEvent(world);
            return;
        }

        for (Object __dummyForeachVar1 : this.getNPC().getAggroTargetToValue().keySet()) {
            Goose.Player player = (Goose.Player) __dummyForeachVar1;
            if (player == aggro) continue;

            if (this.getNPC().getMap() == player.getMap()
                    && Math.abs(this.getNPC().getMapX() - player.getMapX()) <= this.getNPC().getAttackRange()
                    && Math.abs(this.getNPC().getMapY() - player.getMapY()) <= this.getNPC().getAttackRange()) {
                this.getNPC().attack(player, world);
                this.getNPC().addAttackEvent(world);
                return;
            }

        }
        this.getNPC().addAttackEvent(world);
    }

}
