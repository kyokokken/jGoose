package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class ItemSaveEvent extends Event {
	public ItemSaveEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void ready(GameWorld world) throws Exception {
		world.getItemHandler().save(world);
	}

}
