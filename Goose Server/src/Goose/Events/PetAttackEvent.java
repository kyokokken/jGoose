package Goose.Events;

import java.util.List;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.NPC;
import Goose.Pet;
import Goose.Pet.Modes;
import Goose.SpellEffect;
import Goose.SpellEffect.EffectTypes;

public class PetAttackEvent extends Event {
	public PetAttackEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void ready(GameWorld world) throws Exception {
		Pet pet = (Pet) this.getPlayer();
		pet.setAttackEvent(null);
		if (!pet.getIsAlive())
			return;

		if (pet.getTarget() == null || pet.getTarget().getMap() != pet.getMap()) {
			if (pet.getMode() == Modes.Attack) {
				pet.setMode(Modes.Neutral);
				return;
			}

			pet.setTarget(null);
			pet.addAttackEvent(world);
			return;
		}

		if (pet.getTarget() instanceof NPC
				&& ((NPC) pet.getTarget()).getState() == Goose.NPC.States.Dead) {
			pet.setMode(Modes.Neutral);
			pet.setTarget(null);
			return;
		}

		for (Buff b : pet.getBuffs()) {
			// can't attack when stunned
			if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
				pet.setLastAttack(world.getTimeNow());
				pet.addAttackEvent(world);
				return;
			}

		}
		if (Math.abs(pet.getMapX() - pet.getTarget().getMapX()) <= pet
				.getAttackRange()
				&& Math.abs(pet.getMapY() - pet.getTarget().getMapY()) <= pet
						.getAttackRange()) {
			List<Goose.Player> range = pet.getMap().getPlayersInRange(pet);
			String packet = "ATT" + pet.getLoginID();
			for (Goose.Player player : range) {
				world.send(player, packet);
			}
			pet.attack(pet.getTarget(), world);
			pet.addAttackEvent(world);
			return;
		}

		pet.addAttackEvent(world);
	}

}
