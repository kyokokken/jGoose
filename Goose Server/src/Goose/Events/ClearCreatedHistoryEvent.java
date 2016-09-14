package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

/**
 * Clears the account limits once a day
 */
public class ClearCreatedHistoryEvent extends Event {
    public ClearCreatedHistoryEvent() throws Exception {
        super("ClearCreatedHistoryEvent");
    }

    public void ready(GameWorld world) throws Exception {
        world.getCharactersCreatedPerIP().clear();
        this.setTicks(this.getTicks() + world.getTimerFrequency() * 1_000_000_000 * 24 * 60 * 60);
        world.getEventHandler().addEvent(this);
    }

}
