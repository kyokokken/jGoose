package Goose.Events;

import Goose.Event;
import Goose.Events.GuildRemoveCommandEvent;
import Goose.GameWorld;
import Goose.Guild.GuildRanks;

public class GuildRemoveCommandEvent extends Event {
  public GuildRemoveCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GuildRemoveCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      if (this.getPlayer().getGuild() == null) return;

      String name = ((String) this.getData()).substring(12);
      if (name.length() <= 0) {
        this.getPlayer().getGuild().leaveGuild(this.getPlayer(), world);
      } else {
        if (this.getPlayer().getGuild().getRank(this.getPlayer()).compareTo(GuildRanks.Officer) < 0)
          return;

        name = name.substring(1);
        Goose.Player player = world.getPlayerHandler().getPlayer(name);
        if (player != null && player.getState() == Goose.Player.States.Ready) {
          if (player.getGuild() != null
              && player.getGuild() == this.getPlayer().getGuild()
              && this.getPlayer().getGuild().getRank(this.getPlayer())
                  .compareTo(this.getPlayer().getGuild().getRank(player)) > 0) {
            this.getPlayer().getGuild().leaveGuild(player, world, true);
          }

        } else {
          world.send(this.getPlayer(), "$7Couldn't find player.");
        }
      }
    }

  }

}
