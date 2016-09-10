package Goose;

import java.util.List;

/**
 * ClassLevel, holds information about a level for a class
 */
public class ClassLevel {
    /**
     * Experience of next level
     */
    private long __Experience;

    public long getExperience() {
        return __Experience;
    }

    public void setExperience(long value) {
        __Experience = value;
    }

    /**
     * Level
     */
    private int __Level;

    public int getLevel() {
        return __Level;
    }

    public void setLevel(int value) {
        __Level = value;
    }

    /**
     * Class ID
     */
    private int __ClassID;

    public int getClassID() {
        return __ClassID;
    }

    public void setClassID(int value) {
        __ClassID = value;
    }

    /**
     * BaseStats, stats loaded from database
     */
    private AttributeSet __BaseStats;

    public AttributeSet getBaseStats() {
        return __BaseStats;
    }

    public void setBaseStats(AttributeSet value) {
        __BaseStats = value;
    }

    private List<Spell> __Spells;

    public List<Spell> getSpells() {
        return __Spells;
    }

    public void setSpells(List<Spell> value) {
        __Spells = value;
    }

}
