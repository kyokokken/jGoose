package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.Guild.GuildRanks;

public class GuildOfficerCommandEvent extends Event {
    public GuildOfficerCommandEvent() throws Exception {
        super("GuildOfficerCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new GuildOfficerCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            if (this.getPlayer().getGuild() == null) return;

            if (this.getPlayer().getGuild().getRank(this.getPlayer()).compareTo(GuildRanks.Leader) < 0)
                return;

            String name = ((String) this.getData()).substring(14);
            Goose.Player player = world.getPlayerHandler().getPlayer(name);
            if (player != null && player.getState() == Goose.Player.States.Ready) {
                if (player.getGuild() == this.getPlayer().getGuild() && player != this.getPlayer()) {
                    switch (player.getGuild().getRank(player)) {
                        case Officer:
                            player.getGuild().changeRank(player, GuildRanks.Member, world);
                            break;
                        case Member:
                            player.getGuild().changeRank(player, GuildRanks.Officer, world);
                            break;

                    }
                }

            } else {
                world.send(this.getPlayer(), "$7Couldn't find player.");
            }
        }

    }

}
