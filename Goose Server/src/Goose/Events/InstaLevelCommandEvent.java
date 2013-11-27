package Goose.Events;

import Goose.Event;
import Goose.Events.InstaLevelCommandEvent;
import Goose.GameWorld;
import Goose.Player.ExperienceMessage;

public class InstaLevelCommandEvent extends Event {
	public InstaLevelCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new InstaLevelCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (this.getPlayer().getLevel() < 50
					&& !this.getPlayer().getClas().getClassName()
							.equals("Commoner")) {
				long exp = 4500000 - this.getPlayer().getExperience();
				this.getPlayer().addExperience(exp, world,
						ExperienceMessage.Normal);
			}

		}

	}

}