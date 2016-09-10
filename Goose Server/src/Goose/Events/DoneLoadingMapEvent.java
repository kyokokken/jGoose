package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.ItemTile;
import Goose.Map;
import Goose.NPC;

import java.util.List;

/**
 * DoneLoadingMapEvent, event for DLM
 * 
 * Called in response to SCMMapId,MapVersion,MapName Packet format: DLM
 * 
 * Server responds: - SMNMapname - Send Map Name, I'm unsure why it sends it twice or if it's even
 * needed - DSM - Done Sending Map - MKC - See Player.MKCString() for full syntax. This is sent to
 * all players in the range of the player. Including the player himself. - SUCId - Set Your
 * Character (I think) - Tells the client which character is the player
 * 
 */
public class DoneLoadingMapEvent extends Event {
  public DoneLoadingMapEvent() throws Exception {
    super("DoneLoadingMapEvent");
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new DoneLoadingMapEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.LoadingMap) {
      Map map = world.getMapHandler().getMap(this.getPlayer().getMapID());
      map.placeCharacter(this.getPlayer());
      map.setCharacter(this.getPlayer(), this.getPlayer().getMapX(), this.getPlayer().getMapY());
      this.getPlayer().setState(Goose.Player.States.Ready);
      world.send(this.getPlayer(), "SMN" + map.getName());
      world.send(this.getPlayer(), "DSM");
      world.send(this.getPlayer(), this.getPlayer().mKCString());
      world.send(this.getPlayer(), "SUC" + String.valueOf(this.getPlayer().getLoginID()));
      // Notify people in range that someone joined
      // Notify person that joined about people in range
      List<Goose.Player> range = map.getPlayersInRange(this.getPlayer());
      for (Goose.Player player : range) {
        world.send(player, this.getPlayer().mKCString());
        world.send(this.getPlayer(), player.mKCString());
      }
      List<NPC> npcrange = map.getNPCsInRange(this.getPlayer());
      for (NPC npc : npcrange) {
        world.send(this.getPlayer(), npc.mKCString());
        npc.aggroIfInRange(this.getPlayer(), world);
      }
      this.getPlayer().setMap(map);
      this.getPlayer().getMap().addPlayer(this.getPlayer());
      this.getPlayer().addRegenEvent(world);
      world.send(this.getPlayer(), this.getPlayer().tNLString());
      world.send(this.getPlayer(), this.getPlayer().wPSString());
      this.getPlayer().sendBuffBar(world);
      for (Object __dummyForeachVar2 : this.getPlayer().getMap().getItems()) {
        ItemTile tile = (ItemTile) __dummyForeachVar2;
        world.send(this.getPlayer(), tile.mOBString());
      }
      if (this.getPlayer().getGroup() != null) {
        this.getPlayer().getGroup().sendPartyWindow(this.getPlayer(), world);
      }

    }

  }

}
