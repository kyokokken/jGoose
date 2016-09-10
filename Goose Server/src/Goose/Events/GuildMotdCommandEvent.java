package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Guild.GuildRanks;

public class GuildMotdCommandEvent extends Event {
    public GuildMotdCommandEvent() throws Exception {
        super("GuildMotdCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new GuildMotdCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            if (this.getPlayer().getGuild() == null) return;

            if (this.getPlayer().getGuild().getRank(this.getPlayer()).compareTo(GuildRanks.Officer) < 0)
                return;

            String motd = ((String) this.getData()).substring(10);
            if (motd.length() <= 1) {
                this.getPlayer().getGuild().setMOTD("");
                this.getPlayer().getGuild().setDirty(true);
            } else {
                this.getPlayer().getGuild().setMOTD(motd.substring(1));
                this.getPlayer().getGuild().setDirty(true);
            }
            this.getPlayer().getGuild()
                    .sendToGuild("$2[guild-notice] MOTD: " + this.getPlayer().getGuild().getMOTD(), world);
        }

    }

}
