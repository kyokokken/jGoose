package Goose;

import Goose.Event;
import Goose.Events.GuildSaveEvent;
import Goose.GameSettings;
import Goose.Guild;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * GuildHandler, handles loading/saving of guilds
 * 
 */
public class GuildHandler {
  Hashtable<Integer, Guild> guilds;
  List<Guild> newguilds;

  /**
   * Constructor
   */
  public GuildHandler() throws Exception {
    guilds = new Hashtable<Integer, Guild>();
    newguilds = new ArrayList<Guild>();
  }

  /**
   * LoadGuilds, loads all guild data
   * 
   */
  public void loadGuilds(GameWorld world) throws Exception {
    ResultSet resultSet =
        world.getSqlConnection().createStatement().executeQuery("SELECT * FROM guilds");
    while (resultSet.next()) {
      Guild guild = new Guild();
      guild.setID(resultSet.getInt("guild_id"));
      guild.setName(resultSet.getString("guild_name"));
      guild.setMOTD(resultSet.getString("guild_motd"));
      guilds.put(guild.getID(), guild);
    }
    resultSet.close();
    int playerid;
    Goose.Guild.GuildRanks rank = Goose.Guild.GuildRanks.Deleted;
    for (Object __dummyForeachVar0 : this.guilds.values()) {
      Guild guild = (Guild) __dummyForeachVar0;
      world.getSqlConnection().createStatement()
          .executeQuery("SELECT * FROM guild_members WHERE guild_id=" + guild.getID());
      while (resultSet.next()) {
        playerid = resultSet.getInt("player_id");
        rank = Goose.Guild.GuildRanks.values()[resultSet.getInt("guild_rank")];
        guild.addMember(playerid, rank);
      }
      resultSet.close();
    }
  }

  /**
   * Count, returns the number of guilds
   * 
   */
  public int getCount() throws Exception {
    return this.guilds.size();
  }

  /**
   * GetGuild, returns guild for id if it exists, else null
   * 
   */
  public Guild getGuild(int id) throws Exception {
    return (Guild) this.guilds.get(id);
  }

  /**
   * AddGuild, adds a guild to the temporary new guilds buffer until saved
   * 
   */
  public void addGuild(Guild guild) throws Exception {
    this.newguilds.add(guild);
  }

  /**
   * Save, saves all guilds that are marked as dirty
   * 
   */
  public void save(GameWorld world) throws Exception {
    for (Guild guild : this.newguilds) {
      guild.save(world);
      this.guilds.put(guild.getID(), guild);
    }
    for (Object __dummyForeachVar2 : this.guilds.values()) {
      Guild guild = (Guild) __dummyForeachVar2;
      if (guild.getDirty()) guild.save(world);

    }
    this.newguilds.clear();
    this.addSaveEvent(world);
  }

  /**
   * AddSaveEvent, adds save event to the event handler
   * 
   */
  public void addSaveEvent(GameWorld world) throws Exception {
    Event ev = new GuildSaveEvent();
    ev.setTicks(ev.getTicks()
        + (long) (GameSettings.getDefault().getGuildSavePeriod() * world.getTimerFrequency()));
    world.getEventHandler().addEvent(ev);
  }

}
