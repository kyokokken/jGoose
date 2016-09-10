package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;

/**
 * SpellbookSwapEvent, swap two spell in spellbook
 * <p>
 * Packet: SWAP id1, id2
 * <p>
 * Not sure why the spaces are there but k
 */
public class SpellbookSwapEvent extends Event {
    public SpellbookSwapEvent() throws Exception {
        super("SpellbookSwapEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new SpellbookSwapEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            int id1 = 0;
            int id2 = 0;
            String[] ids = ((String) this.getData()).substring(4).split(",");
            try {
                id1 = Integer.valueOf(ids[0]);
                id2 = Integer.valueOf(ids[1]);
            } catch (Exception __dummyCatchVar0) {
                id1 = 0;
                id2 = 0;
            }

            if (id1 <= 0 || id2 <= 0 || id1 > GameSettings.getDefault().getSpellbookSize()
                    || id2 > GameSettings.getDefault().getSpellbookSize()) {
                return;
            }

            // log something bad about packet
            this.getPlayer().getSpellbook().swapSlots(id1, id2, world);
        }

    }

}
