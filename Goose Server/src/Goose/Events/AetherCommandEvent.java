package Goose.Events;

import Goose.Event;
import Goose.Events.AetherCommandEvent;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.States;

public class AetherCommandEvent extends Event {
	public AetherCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Player player, Object data) throws Exception {
		Event e = new AetherCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == States.Ready) {
			String data = ((String) this.getData()).substring(8);
			if (data.length() <= 0)
				return;

			double thres = 0;
			try {
				thres = Double.parseDouble(data);
			} catch (Exception __dummyCatchVar0) {
				thres = 0;
			}

			this.getPlayer().setAetherThreshold(thres);
		}

	}

}
