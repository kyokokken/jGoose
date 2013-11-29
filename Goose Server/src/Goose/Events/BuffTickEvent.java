package Goose.Events;

import java.util.List;

import Goose.Buff;
import Goose.Event;
import Goose.Events.BuffTickEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.NPC;
import Goose.NPC.States;
import Goose.Player;
import Goose.SpellEffect.EffectTypes;

public class BuffTickEvent extends Event {
	public BuffTickEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void ready(GameWorld world) throws Exception {
		Buff buff = (Buff) this.getData();
		// Basically only item buffs are allowed to not expire
		if (!buff.getItemBuff() && buff.getBuffExpireEvent() == null)
			return;

		if (buff.getTarget() instanceof NPC
				&& ((NPC) buff.getTarget()).getState() != States.Alive)
			return;

		if (buff.getTarget() instanceof Player
				&& ((Player) buff.getTarget()).getState() != Player.States.Ready)
			return;

		if (buff.getSpellEffect().getEffectType() == EffectTypes.Tick) {
			buff.getSpellEffect().castFormulaSpell(buff.getCaster(),
					buff.getTarget(), world);
		} else if (buff.getSpellEffect().getEffectType() == EffectTypes.TickBuff
				|| buff.getSpellEffect().getEffectType() == EffectTypes.Stun
				|| buff.getSpellEffect().getEffectType() == EffectTypes.Root) {
			if (buff.getSpellEffect().getAnimation() != 0) {
				List<Player> range = buff.getTarget().getMap()
						.getPlayersInRange(buff.getTarget());
				String packet = "SPP" + buff.getTarget().getLoginID() + ","
						+ buff.getSpellEffect().getAnimation();
				if (buff.getTarget() instanceof Player)
					world.send((Player) buff.getTarget(), packet);

				for (Player player : range) {
					world.send(player, packet);
				}
			}

		} else if (buff.getSpellEffect().getEffectType() == EffectTypes.Viral) {
			buff.getSpellEffect().castFormulaSpell(buff.getCaster(),
					buff.getTarget(), world);
			buff.getSpellEffect().cast(buff.getTarget(), buff.getTarget(),
					world);
		}

		if (!buff.getItemBuff() && buff.getBuffExpireEvent() != null) {
			// buff will expire before next tick
			if (buff.getBuffExpireEvent().getTicks() - world.getTimeNow() < GameSettings
					.getDefault().getSpellEffectPeriod()
					* world.getTimerFrequency()) {
				return;
			}

		}

		BuffTickEvent ev = new BuffTickEvent();
		ev.setData(buff);
		ev.setPlayer(this.getPlayer());
		ev.setNPC(this.getNPC());
		ev.setTicks(ev.getTicks()
				+ (long) (GameSettings.getDefault().getSpellEffectPeriod() * world
						.getTimerFrequency()));
		world.getEventHandler().addEvent(ev);
	}

}
