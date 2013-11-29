package Goose.Events;

import Goose.Event;
import Goose.Events.SaveConfigCommandEvent;
import Goose.GameWorld;

public class SaveConfigCommandEvent  extends Event 
{
    public SaveConfigCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new SaveConfigCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster)
        {
//            GameSettings.getDefault().save();
            world.send(this.getPlayer(), "$7Game Settings Saved.");
        }
         
    }

}


