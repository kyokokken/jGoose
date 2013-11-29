package Goose.Events;

import Goose.Event;
import Goose.Events.SetConfigCommandEvent;
import Goose.GameWorld;

public class SetConfigCommandEvent  extends Event 
{
    public SetConfigCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new SetConfigCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster)
        {
        	/*
            String data = ((String)this.getData()).substring(11);
            String[] tokens = data.split(" ");
            // Reflection.. fun
            // Get GameSettings type
            Class gs = GameSettings.getDefault().getClass();
            // Try to get the property specified
            PropertyInfo prop = gs.getProperty(tokens[0]);
            // Couldn't find property.. error and return
            if (prop == null)
            {
                world.send(this.getPlayer(), "$7Couldn't find Game Setting: " + tokens[0] + ".");
                return ;
            }
             
            // Get Setter/Getter
            MethodInfo setter = prop.GetSetMethod();
            MethodInfo getter = prop.GetGetMethod();
            // If string we can just set directly
            if (getter.ReturnType == String.class)
            {
                setter.Invoke(GameSettings.getDefault(), new Object[]{ tokens[1] });
            }
            else
            {
                // Else we have to get a parser from the return type of the getter
                // And Set the value to the parsed value
                MethodInfo parser = getter.ReturnType.GetMethod("Parse", new Class[]{ String.class });
                setter.Invoke(GameSettings.getDefault(), new Object[]{ parser.Invoke(null, new Object[]{ tokens[1] }) });
            } 
            world.sendToAll("$7[GM] Set Game Setting " + tokens[0] + " to: " + tokens[1]);
            */
        }
         
    }

}


