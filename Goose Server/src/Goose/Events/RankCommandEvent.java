package Goose.Events;

import Goose.Event;
import Goose.Events.RankCommandEvent;
import Goose.GameWorld;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * RankCommandEvent, handles /rank
 * 
 * Packet: /rank [all, gold, magus, priest, warrior, rogue]
 * 
 */
public class RankCommandEvent extends Event {
	public RankCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new RankCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			String data = ((String) this.getData()).substring(5);
			if (data.length() <= 0) {
				world.send(this.getPlayer(),
						"$7Usage: /rank [all, gold, magus, priest, warrior, rogue]");
				return;
			}

			data = data.substring(1);
			Window window;
			String rankArgument = data.toLowerCase();
			if (rankArgument.equals("all")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("All Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getAll());
			} else if (rankArgument.equals("gold")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("Gold Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getGold());
			} else if (rankArgument.equals("magus")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("Magus Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getMagus());
			} else if (rankArgument.equals("priest")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("Priest Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getPriest());
			} else if (rankArgument.equals("warrior")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("Warrior Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getWarrior());
			} else if (rankArgument.equals("rogue")) {
				window = new Window();
				window.setType(WindowTypes.Rank);
				window.setTitle("Rogue Ranks");
				window.setButtons("0,0,0,0,0");
				window.setData(world.getRankHandler().getRogue());
			} else {
				world.send(this.getPlayer(),
						"$7Usage: /rank [all, gold, magus, priest, warrior, rogue]");
				return;
			}
			this.getPlayer().getWindows().add(window);
			window.create(this.getPlayer(), world);
		}

	}

}
