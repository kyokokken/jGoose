package Goose;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * ClassHandler, handles Class objects
 */
public class ClassHandler {
    Hashtable<Integer, Class> classes;

    public ClassHandler() throws Exception {
        this.classes = new Hashtable<Integer, Class>();
    }

    /**
     * GetClass, returns class object from id
     */
    public Class getClass(int id) throws Exception {
        return this.classes.get(id);
    }

    /**
     * LoadClasses, loads classes from database
     */
    public void loadClasses(GameWorld world) throws Exception {
        ResultSet resultSet =
                world.getSqlConnection().createStatement().executeQuery("SELECT * FROM classes");
        while (resultSet.next()) {
            Class c = new Class();
            c.setClassID(resultSet.getInt("class_id"));
            c.setClassName(resultSet.getString("class_name"));
            c.setACMultiplier(Double.parseDouble(resultSet.getString("ac_multiplier")));
            c.setVitaCost(resultSet.getInt("vita_cost"));
            c.setManaCost(resultSet.getInt("mana_cost"));
            this.classes.put(c.getClassID(), c);
        }
        resultSet.close();
        resultSet = world.getSqlConnection().createStatement().executeQuery("SELECT * FROM class_info");
        while (resultSet.next()) {
            ClassLevel c = new ClassLevel();
            c.setClassID(resultSet.getInt("class_id"));
            Class cl = this.getClass(c.getClassID());
            if (cl == null) {
                return;
            }

            // log something wrong
            c.setLevel(resultSet.getInt("level"));
            c.setExperience(resultSet.getInt("level_up_exp"));
            c.setBaseStats(new AttributeSet());
            c.getBaseStats().setHP(resultSet.getInt("player_hp"));
            c.getBaseStats().setMP(resultSet.getInt("player_mp"));
            c.getBaseStats().setSP(resultSet.getInt("player_sp"));
            c.getBaseStats().setAC(resultSet.getInt("stat_ac"));
            c.getBaseStats().setStrength(resultSet.getInt("stat_str"));
            c.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
            c.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
            c.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
            c.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
            c.getBaseStats().setAirResist(resultSet.getInt("res_air"));
            c.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
            c.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
            c.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
            c.getBaseStats().setHPPercentRegen(
                    Double.parseDouble(resultSet.getString("hp_percent_regen")));
            c.getBaseStats().setHPStaticRegen(resultSet.getInt("hp_static_regen"));
            c.getBaseStats().setMPPercentRegen(
                    Double.parseDouble(resultSet.getString("mp_percent_regen")));
            c.getBaseStats().setMPStaticRegen(resultSet.getInt("mp_static_regen"));
            c.getBaseStats().setHaste(Double.parseDouble(resultSet.getString("haste")));
            c.getBaseStats().setSpellDamage(Double.parseDouble(resultSet.getString("spell_damage")));
            c.getBaseStats().setSpellCrit(Double.parseDouble(resultSet.getString("spell_crit")));
            c.getBaseStats().setMeleeDamage(Double.parseDouble(resultSet.getString("melee_damage")));
            c.getBaseStats().setMeleeCrit(Double.parseDouble(resultSet.getString("melee_crit")));
            c.getBaseStats().setDamageReduction(Double.parseDouble(resultSet.getString("damage_reduce")));

            c.setSpells(new ArrayList<Spell>());
            cl.addLevel(c);
        }
        resultSet.close();
        resultSet =
                world.getSqlConnection().createStatement()
                        .executeQuery("SELECT * FROM classes_levelup_spells");
        Class clas;
        ClassLevel level;
        Spell spell;
        while (resultSet.next()) {
            clas = this.getClass(resultSet.getInt("class_id"));
            if (clas == null) {
                continue;
            }

            // log bad class id
            level = clas.getLevel(resultSet.getInt("level"));
            if (level == null) {
                continue;
            }

            // log bad level
            spell = world.getSpellHandler().getSpell(resultSet.getInt("spell_id"));
            if (spell == null) {
                continue;
            }

            // log bad spell
            level.getSpells().add(spell);
        }
        resultSet.close();
    }

    /**
     * Count, returns class count
     */
    public int getCount() throws Exception {
        return this.classes.size();
    }

}
