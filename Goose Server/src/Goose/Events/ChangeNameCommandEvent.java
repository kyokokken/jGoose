package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.AccessStatus;
import Goose.Player.States;

import java.util.List;

public class ChangeNameCommandEvent extends Event {
    public ChangeNameCommandEvent() throws Exception {
        super("ChangeNameCommandEvent");
    }

    public static Event create(Player player, Object data) throws Exception {
        Event e = new ChangeNameCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == States.Ready
                && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
            String[] tokens = ((String) this.getData()).split(" ");
            if (tokens.length < 3) {
                world.send(this.getPlayer(), "$7/changename <oldname> <newname>: not enough parameters.");
                return;
            }

            String oldname = tokens[1];
            String newname = tokens[2];
            Player playerCheck = world.getPlayerHandler().getPlayerFromData(newname);
            if (playerCheck != null) {
                world.send(this.getPlayer(), "$7New name " + newname + " is already used.");
                return;
            }

            Player player = world.getPlayerHandler().getPlayerFromData(oldname);
            if (player == null) {
                world.send(this.getPlayer(), "$7Old name " + oldname + " doesn't exist.");
                return;
            }

            world.getPlayerHandler().removePlayerFromData(player);
            player.setName(newname);
            world.getPlayerHandler().addPlayerToData(player);
            world.send(this.getPlayer(), "$7Changed name successfully.");
            if (player.getState() != Goose.Player.States.NotLoggedIn) {
                world.send(player, player.sNFString());
                if (player.getMap() != null) {
                    List<Player> range = player.getMap().getPlayersInRange(player);
                    String packet = "ERC" + player.getLoginID();
                    String packet2 = player.mKCString();
                    for (Goose.Player p : range) {
                        world.send(p, packet);
                        world.send(p, packet2);
                    }
                }

            } else {
                player.saveToDatabase(world).start();
            }
        }

    }

}
