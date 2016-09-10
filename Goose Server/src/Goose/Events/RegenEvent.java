package Goose.Events;

import Goose.Event;
import Goose.GameWorld;

import java.util.List;

/**
 * Character regen Event
 * <p>
 * Sends VCid,hp %, mp % to everyone in range, including the player themselves
 */
public class RegenEvent extends Event {
    public RegenEvent() throws Exception {
        super("RegenEvent");
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getNPC() != null) {
            if (this.getNPC().getState() == Goose.NPC.States.Alive) {
                if (this.getNPC().getAggroTarget() != null) {
                    this.getNPC().setCurrentHP(
                            this.getNPC().getCurrentHP()
                                    + (int) Math.round(this.getNPC().getMaxStats().getHP()
                                    * this.getNPC().getMaxStats().getHPPercentRegen()));
                    this.getNPC().setCurrentHP(
                            this.getNPC().getCurrentHP() + this.getNPC().getMaxStats().getHPStaticRegen());
                    this.getNPC().setCurrentMP(
                            this.getNPC().getCurrentMP()
                                    + (int) Math.round(this.getNPC().getMaxStats().getMP()
                                    * this.getNPC().getMaxStats().getMPPercentRegen()));
                    this.getNPC().setCurrentMP(
                            this.getNPC().getCurrentMP() + this.getNPC().getMaxStats().getMPStaticRegen());
                } else {
                    this.getNPC().setCurrentHP(
                            this.getNPC().getCurrentHP()
                                    + (int) Math.round(this.getNPC().getMaxStats().getHP() * 0.10));
                }
                if (this.getNPC().getCurrentHP() <= 0) this.getNPC().setCurrentHP(1);

                if (this.getNPC().getCurrentMP() <= 0) this.getNPC().setCurrentMP(1);

                String packet = this.getNPC().vCString();
                List<Goose.Player> range = this.getNPC().getMap().getPlayersInRange(this.getNPC());
                for (Goose.Player p : range) {
                    world.send(p, packet);
                }
                this.getNPC().setRegenEventExists(false);
                this.getNPC().addRegenEvent(world);
                return;
            }

            this.getNPC().setRegenEventExists(false);
        } else {
            Goose.Player player = (Goose.Player) this.getData();
            if (player.getState() == Goose.Player.States.Ready) {
                player.setCurrentHP(player.getCurrentHP()
                        + (int) Math.round(player.getMaxStats().getHP()
                        * player.getMaxStats().getHPPercentRegen()));
                player.setCurrentHP(player.getCurrentHP() + player.getMaxStats().getHPStaticRegen());
                player.setCurrentMP(player.getCurrentMP()
                        + (int) Math.round(player.getMaxStats().getMP()
                        * player.getMaxStats().getMPPercentRegen()));
                player.setCurrentMP(player.getCurrentMP() + player.getMaxStats().getMPStaticRegen());
                if (player.getCurrentHP() <= 0) player.setCurrentHP(1);

                if (player.getCurrentMP() <= 0) player.setCurrentMP(1);

                String packet = player.vCString();
                List<Goose.Player> range = player.getMap().getPlayersInRange(player);
                world.send(player, packet);
                world.send(player, player.sNFString());
                for (Goose.Player p : range) {
                    world.send(p, packet);
                }
                player.setRegenEventExists(false);
                player.addRegenEvent(world);
                return;
            }

            player.setRegenEventExists(false);
        }
    }
}
