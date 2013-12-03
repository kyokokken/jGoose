package Goose;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Goose.Event;
import Goose.Events.ClearMapItemsEvent;
import Goose.GameSettings;
import Goose.Map;

/**
 * MapHandler
 * 
 * Handles loading and storage of Map objects
 * 
 */
public class MapHandler {
  List<Map> maps;

  /**
   * Constructor, constructs map list
   * 
   */
  public MapHandler() throws Exception {
    this.maps = new ArrayList<Map>();
  }

  public List<Map> getMaps() throws Exception {
    return this.maps;
  }

  /**
   * LoadMaps, loads all maps
   * 
   */
  public void loadMaps(GameWorld world) throws Exception {
    ResultSet resultSet =
        world.getSqlConnection().createStatement().executeQuery("SELECT * FROM maps");
    while (resultSet.next()) {
      int width = resultSet.getInt("map_x");
      int height = resultSet.getInt("map_y");
      Map map = new Map(width, height);
      map.setID(resultSet.getInt("map_id"));
      map.setName(resultSet.getString("map_name"));
      map.setWidth(width);
      map.setHeight(height);
      map.setMinLevel(resultSet.getInt("min_level"));
      map.setMaxLevel(resultSet.getInt("max_level"));
      map.setMinExperience(resultSet.getLong("min_experience"));
      map.setMaxExperience(resultSet.getLong("max_experience"));
      map.setCanAuction(("0".equals(resultSet.getString("auction_enabled")) ? false : true));
      map.setCanPVP(("0".equals(resultSet.getString("pvp_enabled")) ? false : true));
      map.setCanChat(("0".equals(resultSet.getString("chat_enabled")) ? false : true));
      map.setCanShout(("0".equals(resultSet.getString("shout_enabled")) ? false : true));
      map.setCanUseItems(("0".equals(resultSet.getString("items_enabled")) ? false : true));
      map.setCanCast(("0".equals(resultSet.getString("spells_enabled")) ? false : true));
      map.setCanBind(("0".equals(resultSet.getString("bind_enabled")) ? false : true));
      map.setCanSpawnPets(("0".equals(resultSet.getString("pets_enabled")) ? false : true));
      this.maps.add(map);
    }
    resultSet.close();
    for (Map map : this.maps) {
      map.loadData(world);
      Event ev = new ClearMapItemsEvent();
      ev.setTicks(ev.getTicks() + world.getTimerFrequency()
          * GameSettings.getDefault().getItemGroundSweepTime());
      ev.setData(map);
      world.getEventHandler().addEvent(ev);
    }
  }

  /**
   * GetMap, gets map by id
   * 
   */
  public Map getMap(int id) throws Exception {
    for (Map map : this.maps) {
      if (map.getID() == id) return map;

    }
    return null;
  }

  /**
   * Count, returns map count
   * 
   */
  public int getCount() throws Exception {
    return this.maps.size();
  }

}
