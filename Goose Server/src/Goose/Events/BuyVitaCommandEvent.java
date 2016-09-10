package Goose.Events;

import Goose.Event;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Player;
import Goose.Player.States;

public class BuyVitaCommandEvent extends Event {
    public BuyVitaCommandEvent() throws Exception {
        super("BuyVitaCommandEvent");
    }

    public static Event create(Player player, Object data) throws Exception {
        Event e = new BuyVitaCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == States.Ready) {
            // can't sell exp when not max level
            // this enables commoners to sell exp but uh so what
            if (this.getPlayer().getClas().getLevel(this.getPlayer().getLevel()).getExperience() != 0)
                return;

            long bought = 0;
            long soldexp = 0;
            int buys = 1;
            try {
                buys = Integer.valueOf(((String) this.getData()).split(" ")[1]);
            } catch (Exception __dummyCatchVar0) {
                buys = 1;
            }

            if (buys <= 0) return;

            this.getPlayer().removeStats(this.getPlayer().getBaseStats(), world);
            double buyrate = 0;
            for (int i = 1; i <= buys; i++) {
                buyrate =
                        ((this.getPlayer().getBaseStats().getHP() / GameSettings.getDefault()
                                .getIncreaseVitaBuyAmount()) * (double) .2) + 1;
                if (this.getPlayer().getExperience() >= this.getPlayer().getClas().getVitaCost() * buyrate) {
                    this.getPlayer().setExperience(
                            this.getPlayer().getExperience()
                                    - (long) (this.getPlayer().getClas().getVitaCost() * buyrate));
                    this.getPlayer().setExperienceSold(
                            this.getPlayer().getExperienceSold()
                                    + (long) (this.getPlayer().getClas().getVitaCost() * buyrate));
                    this.getPlayer()
                            .getBaseStats()
                            .setHP(
                                    this.getPlayer().getBaseStats().getHP()
                                            + GameSettings.getDefault().getVitaBuyAmount());
                    bought += GameSettings.getDefault().getVitaBuyAmount();
                    soldexp += (long) (this.getPlayer().getClas().getVitaCost() * buyrate);
                } else {
                    break;
                }
            }
            this.getPlayer().addStats(this.getPlayer().getBaseStats(), world);
            if (bought == 0) return;

            world.send(this.getPlayer(), "$7Bought " + bought + " hp for " + soldexp + " experience.");
            world.send(this.getPlayer(), this.getPlayer().tNLString());
        }

    }

}
