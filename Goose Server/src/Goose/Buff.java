package Goose;

import Goose.Event;
import Goose.ICharacter;
import Goose.SpellEffect;

/**
 * Buff holds info about a buff on a player
 * 
 */
public class Buff {
	private ICharacter __Caster;

	public ICharacter getCaster() {
		return __Caster;
	}

	public void setCaster(ICharacter value) {
		__Caster = value;
	}

	private ICharacter __Target;

	public ICharacter getTarget() {
		return __Target;
	}

	public void setTarget(ICharacter value) {
		__Target = value;
	}

	private SpellEffect __SpellEffect;

	public SpellEffect getSpellEffect() {
		return __SpellEffect;
	}

	public void setSpellEffect(SpellEffect value) {
		__SpellEffect = value;
	}

	private long __TimeCast;

	public long getTimeCast() {
		return __TimeCast;
	}

	public void setTimeCast(long value) {
		__TimeCast = value;
	}

	private boolean __ItemBuff;

	public boolean getItemBuff() {
		return __ItemBuff;
	}

	public void setItemBuff(boolean value) {
		__ItemBuff = value;
	}

	private Event __BuffExpireEvent;

	public Event getBuffExpireEvent() {
		return __BuffExpireEvent;
	}

	public void setBuffExpireEvent(Event value) {
		__BuffExpireEvent = value;
	}

}
