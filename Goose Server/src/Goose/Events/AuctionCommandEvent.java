package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.AccessStatus;
import Goose.Player.States;

import java.util.List;

public class AuctionCommandEvent extends Event {
    public AuctionCommandEvent() throws Exception {
        super("AuctionCommandEvent");
    }

    public static Event create(Player player, Object data) throws Exception {
        Event e = new AuctionCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == States.Ready) {
            String data = ((String) this.getData()).substring(9);
            if (data.length() <= 0) return;

            if (!this.getPlayer().getMap().getCanAuction()
                    && this.getPlayer().getAccess() != AccessStatus.GameMaster) {
                world.send(this.getPlayer(), "#Auction is disabled in this map.");
                return;
            }

            String packet = "$7<Auction> " + this.getPlayer().getName() + ": " + data;
            String filteredpacket = "$7<Auction> " + this.getPlayer().getName() + ": ";
            boolean filtered = false;
            List<Player> range = this.getPlayer().getMap().getPlayers();
            for (Player player : range) {
                if (player.getChatFilterEnabled()) {
                    if (!filtered) {
                        filteredpacket += world.getChatFilter().filter(data);
                        filtered = true;
                    }

                    world.send(player, filteredpacket);
                } else {
                    world.send(player, packet);
                }
            }
        }

    }

}
