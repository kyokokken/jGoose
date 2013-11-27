package Goose.Events;

import Goose.Event;
import Goose.Events.RandomCommandEvent;
import Goose.GameWorld;

public class RandomCommandEvent extends Event {
	public RandomCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new RandomCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (!this.getPlayer().getMap().getCanChat()
					&& this.getPlayer().getAccess() != Goose.Player.AccessStatus.GameMaster) {
				world.send(this.getPlayer(), "#Chat is disabled in this map.");
				return;
			}

			int max = 0;
			String data = ((String) this.getData()).substring(7);
			if (data.length() > 0) {
				try {
					max = Integer.valueOf(data) + 1;
				} catch (Exception __dummyCatchVar0) {
					max = 0;
				}

			}

			if (max <= 0)
				max = 1000;

			int rnd = world.getRandom().nextInt(max) + 1;
			String packet = "$7" + this.getPlayer().getName() + " rolls " + rnd
					+ " out of " + max + ".";
			world.send(this.getPlayer(), packet);
			for (Object __dummyForeachVar0 : this.getPlayer().getMap()
					.getPlayersInRange(this.getPlayer())) {
				Goose.Player player = (Goose.Player) __dummyForeachVar0;
				world.send(player, packet);
			}
		}

	}

}
