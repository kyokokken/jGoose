package Goose.Events;

import Goose.AttributeSet;
import Goose.Event;
import Goose.GameWorld;
import Goose.Player.AccessStatus;
import Goose.Spell;

public class GMClassChangeCommandEvent extends Event {
    public GMClassChangeCommandEvent() throws Exception {
        super("GMClassChangeCommandEvent");
    }

    public static Event create(Goose.Player player, Object data) throws Exception {
        Event e = new GMClassChangeCommandEvent();
        e.setPlayer(player);
        e.setData(data);
        return e;
    }

    public void ready(GameWorld world) throws Exception {
        if (this.getPlayer().getState() == Goose.Player.States.Ready
                && this.getPlayer().getAccess() == AccessStatus.GameMaster) {
            String packet = (String) this.getData();
            String[] tokens = packet.split(" ");
            if (tokens.length < 4) return;

            String name = tokens[1];
            String cl = tokens[2];
            double modifier = 1;
            try {
                modifier = Double.parseDouble(tokens[3]);
            } catch (Exception __dummyCatchVar0) {
                modifier = 1;
            }

            Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
            if (player == null) {
                world.send(this.getPlayer(), "$7Player " + name + " doesn't exist.");
                return;
            }

            String __dummyScrutVar0 = cl.toLowerCase();
            if (__dummyScrutVar0.equals("rogue")) {
                player.setClassID(2);
            } else if (__dummyScrutVar0.equals("warrior")) {
                player.setClassID(3);
            } else if (__dummyScrutVar0.equals("magus")) {
                player.setClassID(4);
            } else if (__dummyScrutVar0.equals("priest")) {
                player.setClassID(5);
            } else {
                world.send(this.getPlayer(), "$7Invalid class name.");
                return;
            }
            player.removeStats(player.getBaseStats(), world);
            player.setMaxStats(AttributeSet.subtract(player.getMaxStats(),
                    player.getClas().getLevel(player.getLevel()).getBaseStats()));
            player.setExperience(player.getExperience() + player.getExperienceSold());
            player.setExperience((long) (player.getExperience() * modifier));
            player.setExperienceSold(0);
            player.getBaseStats().setHP(0);
            player.getBaseStats().setMP(0);
            player.addStats(player.getBaseStats(), world);
            player.setClass(world.getClassHandler().getClass(player.getClassID()));
            player.addStats(player.getClas().getLevel(player.getLevel()).getBaseStats(), world);
            world.send(player, player.sNFString());
            world.send(player, player.tNLString());
            player.getSpellbook().removeNonClassSpells(world);
            for (int level = 1; level <= player.getLevel(); level++) {
                if (level > player.getClas().getMaxLevel()) break;

                for (Spell spell : player.getClas().getLevel(level).getSpells()) {
                    player.learnSpell(spell.getID(), world);
                }
            }
            world.send(this.getPlayer(), "$7Changed class successfully.");
            if (player.getState() != Goose.Player.States.NotLoggedIn) {
                world.send(player, player.sNFString());
            } else {
                player.saveToDatabase(world).start();
            }
        }

    }

}
