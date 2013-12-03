package Goose.Events;

import Goose.Event;
import Goose.Events.WarpEvent;
import Goose.GameWorld;
import Goose.Map;

/**
 * /warp mapid mapx mapy
 * 
 */
public class WarpEvent extends Event {
  public WarpEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new WarpEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.Ready
        && this.getPlayer().getAccess() == Goose.Player.AccessStatus.GameMaster) {
      String[] tokens = ((String) this.getData()).split(" ");
      int mapid = 1;
      int mapx = 50;
      int mapy = 50;
      try {
        mapid = Integer.valueOf(tokens[1]);
        mapx = Integer.valueOf(tokens[2]);
        mapy = Integer.valueOf(tokens[3]);
      } catch (Exception __dummyCatchVar0) {
        return;
      }

      if (tokens.length == 4) {
        Map map = world.getMapHandler().getMap(mapid);
        if (map != null) {
          // invalid coordinates
          if (mapx < 1 || mapx >= map.getWidth() + 1 || mapy < 1 || mapy >= map.getHeight() + 1)
            return;

          this.getPlayer().warpTo(world, map, mapx, mapy);
        }

      }

    }

  }

}
