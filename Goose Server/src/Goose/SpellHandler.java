package Goose;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Manages Spell/SpellEffect objects
 */
public class SpellHandler {
    Hashtable<Integer, SpellEffect> effects;
    Hashtable<Integer, Spell> spells;

    public SpellHandler() throws Exception {
        this.effects = new Hashtable<>();
        this.spells = new Hashtable<>();
    }

    /**
     * LoadSpellEffects, loads all spell effects
     */
    public void loadSpellEffects(GameWorld world) throws Exception {
        try {
            String query = "SELECT * FROM spell_effects";
            ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
            while (resultSet.next()) {
                SpellEffect effect = new SpellEffect();
                effect.setID(resultSet.getInt("spell_effect_id"));
                effect.setName(resultSet.getString("spell_effect_name"));
                effect.setAnimation(resultSet.getInt("spell_animation"));
                effect.setDisplay(Goose.SpellEffect.SpellDisplays.values()[resultSet.getInt("spell_display")]);
                effect.setTargetType(Goose.SpellEffect.TargetTypes.values()[resultSet.getInt("target_type")]);
                effect.setTargetSize(resultSet.getInt("target_size"));
                effect.setEffected(Goose.SpellEffect.SpellEffected.values()[resultSet.getInt("spell_effected")]);
                effect.setMinimumLevelEffected(resultSet.getInt("min_level_effected"));
                effect.setEffectType(Goose.SpellEffect.EffectTypes.values()[resultSet.getInt("effect_type")]);
                effect.setDuration(resultSet.getLong("effect_duration"));
                effect.setDoAttackAnimation(("0".equals(resultSet.getString("do_attack_animation")) ? false : true));
                effect.setSpellDamageEffects(("0".equals(resultSet.getString("spell_damage_effects")) ? false : true));
                effect.setEnergyType(resultSet.getInt("spell_energy_type"));
                effect.setHPFormula(resultSet.getString("hp_change_formula"));
                effect.setMPFormula(resultSet.getString("mp_change_formula"));
                effect.setSPFormula(resultSet.getString("sp_change_formula"));
                effect.setOnEffectText(resultSet.getString("oneffect_text"));
                effect.setOffEffectText(resultSet.getString("offeffect_text"));
                effect.setTauntAggro(resultSet.getLong("taunt_aggro"));
                effect.setTeleportMapID(resultSet.getInt("teleport_map"));
                effect.setTeleportMapX(resultSet.getInt("teleport_x"));
                effect.setTeleportMapY(resultSet.getInt("teleport_y"));
                effect.setBodyID(resultSet.getInt("body_id"));
                effect.setFaceID(resultSet.getInt("face_id"));
                effect.setHairID(resultSet.getInt("hair_id"));
                effect.setHairR(resultSet.getInt("hair_r"));
                effect.setHairG(resultSet.getInt("hair_g"));
                effect.setHairB(resultSet.getInt("hair_b"));
                effect.setHairA(resultSet.getInt("hair_a"));
                effect.setStats(new AttributeSet());
                effect.getStats().setHP(resultSet.getInt("hp"));
                effect.getStats().setMP(resultSet.getInt("mp"));
                effect.getStats().setSP(resultSet.getInt("sp"));
                effect.getStats().setAC(resultSet.getInt("stat_ac"));
                effect.getStats().setStrength(resultSet.getInt("stat_str"));
                effect.getStats().setStamina(resultSet.getInt("stat_sta"));
                effect.getStats().setIntelligence(resultSet.getInt("stat_int"));
                effect.getStats().setDexterity(resultSet.getInt("stat_dex"));
                effect.getStats().setFireResist(resultSet.getInt("res_fire"));
                effect.getStats().setAirResist(resultSet.getInt("res_air"));
                effect.getStats().setEarthResist(resultSet.getInt("res_earth"));
                effect.getStats().setSpiritResist(resultSet.getInt("res_spirit"));
                effect.getStats().setWaterResist(resultSet.getInt("res_water"));
                effect.getStats().setHPPercentRegen(Double.parseDouble(resultSet.getString("hp_percent_regen")));
                effect.getStats().setHPStaticRegen(resultSet.getInt("hp_static_regen"));
                effect.getStats().setMPPercentRegen(Double.parseDouble(resultSet.getString("mp_percent_regen")));
                effect.getStats().setMPStaticRegen(resultSet.getInt("mp_static_regen"));
                effect.getStats().setDamageReduction(Double.parseDouble(resultSet.getString("damage_reduce")));
                effect.getStats().setHaste(Double.parseDouble(resultSet.getString("haste")));
                effect.getStats().setMeleeCrit(Double.parseDouble(resultSet.getString("melee_crit")));
                effect.getStats().setMeleeDamage(Double.parseDouble(resultSet.getString("melee_damage")));
                effect.getStats().setSpellCrit(Double.parseDouble(resultSet.getString("spell_crit")));
                effect.getStats().setSpellDamage(Double.parseDouble(resultSet.getString("spell_damage")));
                effect.setWorksInPVP(("0".equals(resultSet.getString("works_in_pvp")) ? false : true));
                effect.setWorksNotInPVP(("0".equals(resultSet.getString("works_not_in_pvp")) ? false : true));
                effect.setBuffCanBeRemoved(("0".equals(resultSet.getString("buff_removable")) ? false : true));
                effect.setBuffGraphic(resultSet.getInt("buff_graphic"));
                effect.setRandomJoinChance(Double.parseDouble(resultSet.getString("random_join_chance")));
                effect.setOnMeleeAttackSpellID(resultSet.getInt("on_attack_spell_effect_id"));
                effect.setOnMeleeAttackSpellChance(Double.parseDouble(resultSet.getString("on_attack_spell_chance")));
                effect.setOnMeleeHitSpellID(resultSet.getInt("on_hit_spell_effect_id"));
                effect.setOnMeleeHitSpellChance(Double.parseDouble(resultSet.getString("on_hit_spell_chance")));
                effect.setSnarePercent(Double.parseDouble(resultSet.getString("snare_percent")));
                effect.setBuffStacksOverString(resultSet.getString("buff_stacks_over"));
                effect.setBuffDoesntStackOverString(resultSet.getString("buff_doesnt_stack_over"));
                effect.setBuffStacksOver(new ArrayList<>());
                effect.setBuffDoesntStackOver(new ArrayList<>());
                effect.setOnlyHitsOneNPC(("0".equals(resultSet.getString("only_hits_one_npc")) ? false : true));
                this.effects.put(effect.getID(), effect);
            }
            resultSet.close();
            for (SpellEffect s : this.effects.values()) {
                s.setOnMeleeAttackSpell(this.getSpellEffect(s.getOnMeleeAttackSpellID()));
                s.setOnMeleeHitSpell(this.getSpellEffect(s.getOnMeleeHitSpellID()));

                if (s.getBuffStacksOverString() == null)
                    s.setBuffStacksOverString("");
                if (s.getBuffDoesntStackOverString() == null)
                    s.setBuffDoesntStackOverString("");

                for (String effectid : s.getBuffStacksOverString().split(" ")) {
                    try {
                        SpellEffect e = this.getSpellEffect(Integer.valueOf(effectid));
                        if (e == null) {
                        } else {
                            // log bad spell effect id
                            s.getBuffStacksOver().add(e);
                        }
                    } catch (Exception __dummyCatchVar0) {
                    }

                }
                for (String effectid : s.getBuffDoesntStackOverString().split(" ")) {
                    try {
                        SpellEffect e = this.getSpellEffect(Integer.valueOf(effectid));
                        if (e == null) {
                        } else {
                            // log bad spell effect id
                            s.getBuffDoesntStackOver().add(e);
                        }
                    } catch (Exception __dummyCatchVar1) {
                    }

                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.println(e);
        }
    }

    /**
     * EffectCount, returns number of effects
     */
    public int getEffectCount() throws Exception {
        return this.effects.size();
    }

    /**
     * GetSpellEffect, returns spell effect
     */
    public SpellEffect getSpellEffect(int id) throws Exception {
        return this.effects.get(id);
    }

    /**
     * LoadSpells, loads all spells
     */
    public void loadSpells(GameWorld world) throws Exception {
        String query = "SELECT * FROM spells";
        ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
        while (resultSet.next()) {
            Spell spell = new Spell();
            spell.setID(resultSet.getInt("spell_id"));
            spell.setName(resultSet.getString("spell_name"));
            spell.setDescription(resultSet.getString("spell_description"));
            spell.setTarget(Goose.Spell.SpellTargets.values()[resultSet.getInt("spell_target")]);
            spell.setClassRestrictions(resultSet.getLong("class_restrictions"));
            spell.setAether(resultSet.getLong("spell_aether"));
            spell.setGraphic(resultSet.getInt("spellbook_graphic"));
            spell.setHPPercentCost(Double.parseDouble(resultSet.getString("hp_percent_cost")));
            spell.setHPStaticCost(resultSet.getInt("hp_static_cost"));
            spell.setMPPercentCost(Double.parseDouble(resultSet.getString("mp_percent_cost")));
            spell.setMPStaticCost(resultSet.getInt("mp_static_cost"));
            spell.setSPPercentCost(Double.parseDouble(resultSet.getString("sp_percent_cost")));
            spell.setSPStaticCost(resultSet.getInt("sp_static_cost"));
            spell.setSpellEffectID(resultSet.getInt("spell_effect_id"));
            spell.setSpellEffect(this.getSpellEffect(spell.getSpellEffectID()));
            if (spell.getSpellEffect() == null) {
                continue;
            }

            // log bad spell effect
            this.spells.put(spell.getID(), spell);
        }
        resultSet.close();
    }

    /**
     * Count, returns number of spells
     */
    public int getCount() throws Exception {
        return this.spells.size();
    }

    /**
     * GetSpell, returns spell
     */
    public Spell getSpell(int id) throws Exception {
        return this.spells.get(id);
    }

}
