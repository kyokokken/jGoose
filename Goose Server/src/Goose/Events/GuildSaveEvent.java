package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

public class GuildSaveEvent extends Event {
  public GuildSaveEvent() throws Exception {
    super();
  }

  public void ready(GameWorld world) throws Exception {
    world.getGuildHandler().save(world);
  }

}
