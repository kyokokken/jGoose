package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class GuildSaveEvent extends Event {
	public GuildSaveEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void ready(GameWorld world) throws Exception {
		world.getGuildHandler().save(world);
	}

}
