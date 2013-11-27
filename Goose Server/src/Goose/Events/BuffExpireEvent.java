package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.Events.BuffExpireEvent;
import Goose.GameWorld;
import Goose.NPC;
import Goose.NPC.States;
import Goose.Player;

public class BuffExpireEvent extends Event {
	public BuffExpireEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void ready(GameWorld world) throws Exception {
		Buff buff = (Buff) this.getData();
		if (buff.getBuffExpireEvent() == null)
			return;

		if (buff.getTarget() instanceof NPC
				&& ((NPC) buff.getTarget()).getState() == States.Dead)
			return;
		else if (buff.getTarget() instanceof Player
				&& ((Player) buff.getTarget()).getState() == Player.States.NotLoggedIn)
			return;

		if (world.getTimeNow() - buff.getTimeCast() >= buff.getSpellEffect()
				.getDuration() * world.getTimerFrequency()) {
			buff.setBuffExpireEvent(null);
			if (buff.getTarget() instanceof NPC)
				this.getNPC().removeBuff(buff, world);
			else
				this.getPlayer().removeBuff(buff, world);
		} else {
			BuffExpireEvent ev = new BuffExpireEvent();
			ev.setData(buff);
			ev.setPlayer(this.getPlayer());
			ev.setNPC(this.getNPC());
			ev.setTicks(buff.getTimeCast()
					+ buff.getSpellEffect().getDuration()
					* world.getTimerFrequency());
			world.getEventHandler().addEvent(ev);
			buff.setBuffExpireEvent(ev);
		}
	}

}
