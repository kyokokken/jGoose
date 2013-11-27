package Goose.Events;

import Goose.Event;
import Goose.Events.GMShutdownServer;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

public class GMShutdownServer extends Event {
	public GMShutdownServer() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GMShutdownServer();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready
				&& this.getPlayer().getAccess() == AccessStatus.GameMaster) {
			String packet = (String) this.getData();
			String[] tokens = packet.split(" ");
			world.send(this.getPlayer(), "$7Server restarting.");
			world.stop();
		}

	}

}
