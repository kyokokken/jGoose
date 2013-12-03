package Goose.Events;

import Goose.Event;
import Goose.Events.GuildCreateCommandEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Guild;
import Goose.Guild.GuildRanks;

/**
 * GuildCreateCommandEvent
 * 
 * Event for /guildcreate Packet: /guildcreate <guild name here>
 * 
 * 
 */
public class GuildCreateCommandEvent extends Event {
  public GuildCreateCommandEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new GuildCreateCommandEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready) {
      if (this.getPlayer().getGold() < GameSettings.getDefault().getGuildCreationCost()) {
        world.send(this.getPlayer(), "$7You need "
            + GameSettings.getDefault().getGuildCreationCost() + "gp to create a guild.");
        return;
      }

      String name = ((String) this.getData()).substring(13);
      if (name.length() <= 3 || name.length() > 64) {
        world.send(this.getPlayer(), "$7Your guild name needs to be between 3 and 64 characters.");
        return;
      }

      if (this.getPlayer().getGuild() != null) {
        this.getPlayer().getGuild().leaveGuild(this.getPlayer(), world);
      }

      this.getPlayer().removeGold(GameSettings.getDefault().getGuildCreationCost(), world);
      this.getPlayer().setGuild(new Guild());
      this.getPlayer().getGuild().setID(0);
      this.getPlayer().getGuild().setMOTD(GameSettings.getDefault().getDefaultGuildMOTD());
      this.getPlayer().getGuild().setName(name);
      this.getPlayer().getGuild()
          .addMember(this.getPlayer().getPlayerID(), GuildRanks.Leader, true, true);
      this.getPlayer().getGuild().getOnlineMembers().add(this.getPlayer());
      this.getPlayer()
          .getGuild()
          .sendToGuild("$2[guild-notice] MOTD: " + GameSettings.getDefault().getDefaultGuildMOTD(),
              world);
      world.getGuildHandler().addGuild(this.getPlayer().getGuild());
    }

  }

}
