package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;

/**
 * PlayerCastSpellEvent, event for "CAST" packet
 * <p>
 * Syntax: CASTspellbookid,targetid
 */
public class PlayerCastSpellEvent extends Event {
    public PlayerCastSpellEvent() throws Exception {
        super("PlayerCastSpellEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PlayerCastSpellEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            String packet = ((String) this.getData()).substring(4);
            String[] t = packet.split(",");
            int spellid = 0;
            int target = 0;
            if (t.length == 2) {
                try {
                    spellid = Integer.valueOf(t[0]);
                    target = Integer.valueOf(t[1]);
                } catch (Exception __dummyCatchVar0) {
                    spellid = 0;
                    target = 0;
                }

                if (spellid == 0 || target == 0) return;

                if (spellid >= 1 && spellid <= GameSettings.getDefault().getSpellbookSize()) {
                    if (this.getPlayer().getLoginID() == target) {
                        this.getPlayer().castSpell(spellid, this.getPlayer(), world);
                        return;
                    } else {
                        for (Goose.Player player : this.getPlayer().getMap().getPlayersInRange(this.getPlayer())) {
                            if (player.getLoginID() == target) {
                                this.getPlayer().castSpell(spellid, player, world);
                                return;
                            }
                        }
                        for (Goose.NPC npc : this.getPlayer().getMap().getNPCsInRange(this.getPlayer())) {
                            if (npc.getLoginID() == target) {
                                this.getPlayer().castSpell(spellid, npc, world);
                                return;
                            }
                        }
                    }
//                    Logger.INSTANCE.println("SUSPECTED MACRO: " + this.getPlayer().getName());
                }
            }
        }
    }
}
