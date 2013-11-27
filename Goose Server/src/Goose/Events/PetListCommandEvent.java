package Goose.Events;

import Goose.Event;
import Goose.Events.PetListCommandEvent;
import Goose.GameWorld;
import Goose.Pet;

/**
 * Command that lists all of the player's current pets
 */
public class PetListCommandEvent extends Event {
	public PetListCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new PetListCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			world.send(this.getPlayer(), "$7Listing Pets: <ID> <Name> <Level>");
			for (Object __dummyForeachVar0 : this.getPlayer().getPets()) {
				Pet pet = (Pet) __dummyForeachVar0;
				world.send(
						this.getPlayer(),
						"$7" + pet.getPetID() + " " + pet.getName() + " "
								+ pet.getLevel());
			}
		}

	}

}
