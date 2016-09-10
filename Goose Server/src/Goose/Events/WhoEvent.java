package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * WhoEvent, event for "who" packet
 * <p>
 * Called when someone types /who [all|guild] [name] Packet format: /who [all|guild] [name]
 * <p>
 * Server responds: #[Mapname] Playername (Level lvl class)
 */
public class WhoEvent extends Event {
    public WhoEvent() throws Exception {
        super("WhoEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new WhoEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String packet = (String) this.getData();
            List<Goose.Player> players;
            int matches = 0;
            if (packet.equals("/who")) {
                players = this.getPlayer().getMap().getPlayers();
            } else {
                String[] search = packet.split(" ");
                if (search.length > 1) {
                    if (search[1].equals("all")) {
                        players = world.getPlayerHandler().getPlayers();
                    } else if (search[1].equals("guild") && this.getPlayer().getGuild() != null) {
                        players = this.getPlayer().getGuild().getOnlineMembers();
                    } else {
                        players = new ArrayList<Goose.Player>();
                    }
                } else {
                    players = this.getPlayer().getMap().getPlayers();
                }
            }
            for (Goose.Player player : players) {
                if (player instanceof Pet) continue;

                if (player.getState() == Goose.Player.States.Ready) {
                    world.send(
                            this.getPlayer(),
                            "#[" + player.getMap().getName() + "] "
                                    + (isNullOrEmpty(player.getTitle()) ? player.getTitle() + " " : "")
                                    + player.getName()
                                    + (isNullOrEmpty(player.getSurname()) ? " " + player.getSurname() : "")
                                    + " (Level " + player.getLevel() + " " + player.getClas().getClassName() + ")");
                    matches++;
                }

            }
            world.send(this.getPlayer(), "#[Matched " + matches + " players]");
        }

    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.equals(""));
    }

}
