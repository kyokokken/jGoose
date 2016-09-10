package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

/**
 * GroupChatEvent
 * <p>
 * /group message
 * <p>
 * Sends message to everyone in group.
 */
public class GroupChatEvent extends Event {
    public GroupChatEvent() throws Exception {
        super("GroupChatEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new GroupChatEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            if (this.getPlayer().getGroup() == null) return;

            String message = ((String) this.getData()).substring(7);
            if (message.length() >= 1) {
                this.getPlayer().getGroup().chat(this.getPlayer(), message, world);
            }

        }

    }

}
