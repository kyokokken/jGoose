package Goose;

import java.util.Hashtable;

/**
 * Class, holds class information
 */
public class Class {
    Hashtable<Integer, ClassLevel> levels;
    private int __ClassID;

    public int getClassID() {
        return __ClassID;
    }

    public void setClassID(int value) {
        __ClassID = value;
    }

    private String __ClassName;

    public String getClassName() {
        return __ClassName;
    }

    public void setClassName(String value) {
        __ClassName = value;
    }

    private double __ACMultiplier;

    public double getACMultiplier() {
        return __ACMultiplier;
    }

    public void setACMultiplier(double value) {
        __ACMultiplier = value;
    }

    private long __VitaCost;

    public long getVitaCost() {
        return __VitaCost;
    }

    public void setVitaCost(long value) {
        __VitaCost = value;
    }

    private long __ManaCost;

    public long getManaCost() {
        return __ManaCost;
    }

    public void setManaCost(long value) {
        __ManaCost = value;
    }

    public Class() throws Exception {
        this.levels = new Hashtable<Integer, ClassLevel>();
    }

    public ClassLevel getLevel(int level) throws Exception {
        return (ClassLevel) this.levels.get(level);
    }

    public void addLevel(ClassLevel c) throws Exception {
        this.levels.put(c.getLevel(), c);
    }

    public int getMaxLevel() throws Exception {
        return this.levels.size() + 1;
    }

}
