package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.Logger;
import Goose.NPC;
import Goose.Pet;
import Goose.Player;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LogoutEvent extends Event {
    public LogoutEvent() throws Exception {
        super("LogoutEvent");
    }

    /*
     * If null they never made it into the system so just disconnect them
     *
     * If their state is not logged in we remove them from the player list and disconnect them, since
     * they're not actually in the game yet
     *
     * If their state is loading game we have already sent a "Player has entered world" message but
     * they're not in the game properly yet, so we just send a "Player left the world" message, remove
     * from player handler and disconnect them
     *
     * Else they're properly in the game and we have to remove them from maps, etc
     */
    public void ready(GameWorld world) throws Exception {
        Socket sock = (Socket) this.getData();
        Goose.Player player = world.getPlayerHandler().getPlayer(sock);
        if (player == null) {
        } else if (player.getState() == Goose.Player.States.NotLoggedIn) {
            world.getPlayerHandler().removePlayer(player);
        } else if (player.getState() == Goose.Player.States.LoadingGame) {
            player.setState(Goose.Player.States.NotLoggedIn);
            world.getPlayerHandler().removePlayer(player);
            world.sendToAll("$7" + player.getName() + " has left the world.");
        } else {
            if (player.getMap() != null) {
                List<Player> range = player.getMap().getPlayersInRange(player);
                for (Player p : range) {
                    world.send(p, "ERC" + player.getLoginID());
                }
                for (NPC npc : player.getMap().getNPCsInRange(player)) {
                    npc.removeAggro(player);
                }
                player.getMap().removePlayer(player);
                player.getMap().setCharacter(null, player.getMapX(), player.getMapY());
            }

            for (Pet pet : player.getPets()) {
                if (pet.getIsAlive()) pet.destroy(world);

            }
            player.saveToDatabase(world).start();
            if (player.getGroup() != null) {
                player.getGroup().removePlayer(player, world, false, this.getPlayer());
            }

            if (player.getGuild() != null) {
                player.getGuild().getOnlineMembers().remove(player);
            }

            player.setState(Goose.Player.States.NotLoggedIn);
            // Remove all buffs on logout
            List<Buff> removebuff = new ArrayList<Buff>();
            for (Buff b : player.getBuffs()) {
                if (!b.getItemBuff()) removebuff.add(b);

            }
            for (Buff b : removebuff) {
                player.removeBuff(b, world, false);
            }
            world.getPlayerHandler().removePlayer(player);
            world.sendToAll("$7" + player.getName() + " has left the world.");
            Logger.INSTANCE.connect(player.getName() + " logged out.");
        }
    }

}
