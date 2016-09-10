package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.GameWorld;
import Goose.ICharacter;
import Goose.SpellEffect.EffectTypes;
import Goose.Window;
import Goose.Window.WindowTypes;

import java.util.List;

/**
 * PlayerAttackEvent, event for "ATT" packet
 * <p>
 * Packet syntax: ATT
 */
public class PlayerAttackEvent extends Event {
    public PlayerAttackEvent() throws Exception {
        super("PlayerAttackEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new PlayerAttackEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready) {
            for (Object __dummyForeachVar0 : this.getPlayer().getBuffs()) {
                Buff b = (Buff) __dummyForeachVar0;
                // can't attack when stunned
                if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
                    world.send(this.getPlayer(), "BT" + this.getPlayer().getLoginID() + ",50");
                    return;
                }

            }
            for (Object __dummyForeachVar1 : this.getPlayer().getWindows()) {
                Window window = (Window) __dummyForeachVar1;
                if (window.getType() == WindowTypes.Vendor) {
                    world.send(this.getPlayer(), "$7You can't attack while with a vendor.");
                    return;
                }

            }
            long delay =
                    (long) (((double) (this.getPlayer().getWeaponDelay() / 10.0) * (1 - this.getPlayer()
                            .getMaxStats().getHaste())) * world.getTimerFrequency());
            long now = world.getTimeNow();
            if (now - this.getPlayer().getLastAttack() >= delay) {
                List<Goose.Player> range = this.getPlayer().getMap().getPlayersInRange(this.getPlayer());
                String packet = "ATT" + this.getPlayer().getLoginID();
                for (Goose.Player player : range) {
                    world.send(player, packet);
                }
                this.getPlayer().setLastAttack(now);
                int x = this.getPlayer().getMapX();
                int y = this.getPlayer().getMapY();
                // check tile 1 square in front of player
                int __dummyScrutVar0 = this.getPlayer().getFacing();
                if (__dummyScrutVar0 == 1) {
                    y--;
                } else if (__dummyScrutVar0 == 2) {
                    x++;
                } else if (__dummyScrutVar0 == 3) {
                    y++;
                } else if (__dummyScrutVar0 == 4) {
                    x--;
                }

                ICharacter character = this.getPlayer().getMap().getCharacterAt(x, y);
                if (character != null) this.getPlayer().attack(character, world);

            }

        }

    }

}
