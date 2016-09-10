package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Player.AccessStatus;

import java.util.List;

/**
 * ChatEvent, event for ";" packet
 * <p>
 * Called when someone types a message Packet format: ;Message
 * <p>
 * Server responds: ^LoginID, Name: Message Server sends the response to everyone in the area
 * including the player
 */
public class ChatEvent extends Event {
    public ChatEvent() throws Exception {
        super("ChatEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new ChatEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String message = (String) this.getData();
            if (message.length() == 1) return;

            // log bad chat event
            message = message.substring(1, (1) + (message.length() - 1));
            if (!this.getPlayer().getMap().getCanChat() && this.getPlayer().getAccess() != AccessStatus.GameMaster) {
                world.send(this.getPlayer(), "#Chat is disabled in this map.");
                return;
            }

            String packet = "^" + this.getPlayer().getLoginID() + "," + this.getPlayer().getName() + ": " + message;
            String filteredpacket = "^" + this.getPlayer().getLoginID() + "," + this.getPlayer().getName() + ": ";
            boolean filtered = false;
            world.send(this.getPlayer(), packet);
            List<Goose.Player> range = this.getPlayer().getMap().getPlayersInRange(this.getPlayer());
            for (Goose.Player player : range) {
                if (player.getChatFilterEnabled()) {
                    if (!filtered) {
                        filteredpacket += world.getChatFilter().filter(message);
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
