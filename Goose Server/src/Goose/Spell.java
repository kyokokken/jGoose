//
// Translated by CS2J (http://www.cs2j.com): 15/11/2013 18:39:04
//

package Goose;

import Goose.SpellEffect;

/**
     * Spell, holds information for a spell
     * 
     */
public class Spell   
{
    public enum SpellTargets
    {
        Target,
        Self,
        Group
    }
    private int __ID;
    public int getID() {
        return __ID;
    }

    public void setID(int value) {
        __ID = value;
    }

    private String __Name;
    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    private String __Description;
    public String getDescription() {
        return __Description;
    }

    public void setDescription(String value) {
        __Description = value;
    }

    private SpellTargets __Target = SpellTargets.Target;
    public SpellTargets getTarget() {
        return __Target;
    }

    public void setTarget(SpellTargets value) {
        __Target = value;
    }

    private long __ClassRestrictions;
    public long getClassRestrictions() {
        return __ClassRestrictions;
    }

    public void setClassRestrictions(long value) {
        __ClassRestrictions = value;
    }

    /**
             * Aether in milliseconds
             */
    private long __Aether;
    public long getAether() {
        return __Aether;
    }

    public void setAether(long value) {
        __Aether = value;
    }

    private long __Graphic;
    public long getGraphic() {
        return __Graphic;
    }

    public void setGraphic(long value) {
        __Graphic = value;
    }

    private int __HPStaticCost;
    public int getHPStaticCost() {
        return __HPStaticCost;
    }

    public void setHPStaticCost(int value) {
        __HPStaticCost = value;
    }

    private double __HPPercentCost;
    public double getHPPercentCost() {
        return __HPPercentCost;
    }

    public void setHPPercentCost(double value) {
        __HPPercentCost = value;
    }

    private int __MPStaticCost;
    public int getMPStaticCost() {
        return __MPStaticCost;
    }

    public void setMPStaticCost(int value) {
        __MPStaticCost = value;
    }

    private double __MPPercentCost;
    public double getMPPercentCost() {
        return __MPPercentCost;
    }

    public void setMPPercentCost(double value) {
        __MPPercentCost = value;
    }

    private int __SPStaticCost;
    public int getSPStaticCost() {
        return __SPStaticCost;
    }

    public void setSPStaticCost(int value) {
        __SPStaticCost = value;
    }

    private double __SPPercentCost;
    public double getSPPercentCost() {
        return __SPPercentCost;
    }

    public void setSPPercentCost(double value) {
        __SPPercentCost = value;
    }

    private int __SpellEffectID;
    public int getSpellEffectID() {
        return __SpellEffectID;
    }

    public void setSpellEffectID(int value) {
        __SpellEffectID = value;
    }

    private SpellEffect __SpellEffect;
    public SpellEffect getSpellEffect() {
        return __SpellEffect;
    }

    public void setSpellEffect(SpellEffect value) {
        __SpellEffect = value;
    }

}


