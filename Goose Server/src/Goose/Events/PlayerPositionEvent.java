package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player;

/**
 * Created by justin on 9/4/2016.
 */
public class PlayerPositionEvent extends Event {

    public PlayerPositionEvent() throws Exception {
        super("PlayerPositionEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PlayerPositionEvent();
        e.setPlayer(player);
        e.setData(data);
        e.setTicks(60000);
        return e;
    }

    public void ready(GameWorld world){
        try {
            for(Goose.Map map : world.getMapHandler().getMaps()){
                for(Player player : map.getPlayers()) {
                    String erc = "ERC" + player.getLoginID();
                    for (Goose.Player p : map.getPlayersInRange(player)) {
                        world.send(p, erc);
                        world.send(player, "ERC" + p.getLoginID());
                    }

                    String mkc = player.mKCString();
                    for (Goose.Player p : map.getPlayersInRange(player)) {
                        world.send(p, mkc);
                        world.send(player, p.mKCString());
                    }

                    //Send Update Position? SUP
                    world.send(this.getPlayer(), "SUP" + this.getPlayer().getMapX() + "," + this.getPlayer().getMapY());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
