package Goose.Events;

import Goose.Event;
import Goose.Events.ToggleCommandEvent;
import Goose.GameWorld;
import Goose.Player.ToggleSetting;

public class ToggleCommandEvent extends Event {
	public ToggleCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new ToggleCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			String packet = (String) this.getData();
			String[] tokens = packet.split(" ");
			if (tokens.length < 2)
				return;

			String cl = tokens[1];
			String __dummyScrutVar0 = cl.toLowerCase();
			if (__dummyScrutVar0.equals("exp")
					|| __dummyScrutVar0.equals("experience")) {
				this.getPlayer()
						.setToggleSettings(
								ToggleSetting.values()[(this.getPlayer()
										.getToggleSettings().getValue() ^ ToggleSetting.Experience
										.getValue())]);
				if ((this.getPlayer().getToggleSettings().getValue() & ToggleSetting.Experience
						.getValue()) == 0) {
					world.send(this.getPlayer(),
							"$7Experience display is enabled.");
				} else {
					world.send(this.getPlayer(),
							"$7Experience display is disabled.");
				}
			} else if (__dummyScrutVar0.equals("tell")) {
				this.getPlayer()
				.setToggleSettings(
						ToggleSetting.values()[(this.getPlayer()
								.getToggleSettings().getValue() ^ ToggleSetting.Tell
								.getValue())]);
				if ((this.getPlayer().getToggleSettings().getValue() & ToggleSetting.Tell.getValue()) == 0) {
					world.send(this.getPlayer(), "$7Tells are enabled.");
				} else {
					world.send(this.getPlayer(), "$7Tells are disabled.");
				}
			} else if (__dummyScrutVar0.equals("swear")
					|| __dummyScrutVar0.equals("word")
					|| __dummyScrutVar0.equals("curse")) {
				this.getPlayer()
				.setToggleSettings(
						ToggleSetting.values()[(this.getPlayer()
								.getToggleSettings().getValue() ^ ToggleSetting.WordFilter
								.getValue())]);
				if ((this.getPlayer().getToggleSettings().getValue() & ToggleSetting.WordFilter.getValue()) == 0) {
					world.send(this.getPlayer(), "$7Word filter is enabled.");
				} else {
					world.send(this.getPlayer(), "$7Word filter is disabled.");
				}
			} else {
				world.send(this.getPlayer(),
						"$7/toggle [experience|tell|curse]");
			}
		}

	}

}
