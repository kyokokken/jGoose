package Goose.Events;

import Goose.Event;
import Goose.Events.DestroySpellEvent;
import Goose.GameSettings;
import Goose.GameWorld;

/**
 * DestroySpellEvent, delete spell from spellbook
 * 
 */
public class DestroySpellEvent extends Event {
	public DestroySpellEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new DestroySpellEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			int id = 0;
			String data = ((String) this.getData()).substring(4);
			try {
				id = Integer.valueOf(data);
			} catch (Exception __dummyCatchVar0) {
				id = 0;
			}

			if (id <= 0 || id > GameSettings.getDefault().getSpellbookSize())
				return;

			this.getPlayer().getSpellbook().removeSpell(id, world);
		}

	}

}
