package Goose.Events;

import Goose.Event;
import Goose.Events.LoginContinuedEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Map;

/**
 * LoginContinuedEvent, event for LCNT
 * 
 * Called in response to LOKServername Packet format: LCNT
 * 
 * Server responds: SCMMapId,MapVersion,MapName Send Current Map
 * 
 */
public class LoginContinuedEvent extends Event {
  public LoginContinuedEvent() throws Exception {
    super();
  }

  public static Event create(Goose.Player player, Object data) throws Exception {
    Event e = new LoginContinuedEvent();
    e.setPlayer(player);
    e.setData(data);
    return e;
  }

  public void ready(GameWorld world) throws Exception {
    if (this.getPlayer().getState() == Goose.Player.States.LoadingGame) {
      Map map = world.getMapHandler().getMap(this.getPlayer().getMapID());
      this.getPlayer().setState(Goose.Player.States.LoadingMap);
      world.send(this.getPlayer(), "SCM" + map.getID() + ",1," + map.getName());
      if (GameSettings.getDefault().getMOTD().length() > 0) {
        world.send(this.getPlayer(), "$7" + GameSettings.getDefault().getMOTD());
      }

      world.send(this.getPlayer(), "$7There are currently "
          + world.getPlayerHandler().getPlayerCount() + " players online.");
      if (GameSettings.getDefault().getExperienceModifier() != 1) {
        world.send(this.getPlayer(), "$7Current experience rate is "
            + GameSettings.getDefault().getExperienceModifier() + "x.");
      }

      world.send(this.getPlayer(), this.getPlayer().sNFString());
      this.getPlayer().addRegenEvent(world);
      this.getPlayer().sendInventory(world);
      this.getPlayer().sendSpellbook(world);
      this.getPlayer().sendBuffBar(world);
      if (this.getPlayer().getGuild() != null) {
        this.getPlayer().getGuild().getOnlineMembers().add(this.getPlayer());
        if (!this.getPlayer().getGuild().getMOTD().equals("")) {
          world.send(this.getPlayer(), "$2[guild-notice] MOTD: "
              + this.getPlayer().getGuild().getMOTD());
        }

      }

    }

  }

}
