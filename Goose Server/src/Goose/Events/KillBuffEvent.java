package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;

/**
 * KillBuffEvent, KBUFid event
 * <p>
 * When a player clicks a buff in buff bar to remove it.
 */
public class KillBuffEvent extends Event {
    public KillBuffEvent() throws Exception {
        super("KillBuffEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new KillBuffEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String idstr = ((String) this.getData()).substring(4);
            if (idstr.length() <= 0) return;

            // log bad packet
            int id = 0;
            try {
                id = Integer.valueOf(idstr);
            } catch (Exception e) {
                id = 0;
            }

            if (id <= 0 || id > this.getPlayer().getBuffs().size()) return;

            // log bad packet
            Buff buff = this.getPlayer().getBuffs().get(id - 1);
            // I dunno if I should make item buffs able to be removed or not..
            if (!buff.getSpellEffect().getBuffCanBeRemoved()) return;

            this.getPlayer().removeBuff(buff, world, true);
        }

    }

}
