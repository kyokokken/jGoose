package Goose;

/**
 * AttributeSet, holds a set of attributes
 */
public class AttributeSet {
    private int __HP;

    public int getHP() {
        return __HP;
    }

    public void setHP(int value) {
        __HP = value;
    }

    private int __MP;

    public int getMP() {
        return __MP;
    }

    public void setMP(int value) {
        __MP = value;
    }

    private int __SP;

    public int getSP() {
        return __SP;
    }

    public void setSP(int value) {
        __SP = value;
    }

    private double __HPPercentRegen;

    public double getHPPercentRegen() {
        return __HPPercentRegen;
    }

    public void setHPPercentRegen(double value) {
        __HPPercentRegen = value;
    }

    private int __HPStaticRegen;

    public int getHPStaticRegen() {
        return __HPStaticRegen;
    }

    public void setHPStaticRegen(int value) {
        __HPStaticRegen = value;
    }

    private double __MPPercentRegen;

    public double getMPPercentRegen() {
        return __MPPercentRegen;
    }

    public void setMPPercentRegen(double value) {
        __MPPercentRegen = value;
    }

    private int __MPStaticRegen;

    public int getMPStaticRegen() {
        return __MPStaticRegen;
    }

    public void setMPStaticRegen(int value) {
        __MPStaticRegen = value;
    }

    private int __Strength;

    public int getStrength() {
        return __Strength;
    }

    public void setStrength(int value) {
        __Strength = value;
    }

    private int __Stamina;

    public int getStamina() {
        return __Stamina;
    }

    public void setStamina(int value) {
        __Stamina = value;
    }

    private int __Intelligence;

    public int getIntelligence() {
        return __Intelligence;
    }

    public void setIntelligence(int value) {
        __Intelligence = value;
    }

    private int __Dexterity;

    public int getDexterity() {
        return __Dexterity;
    }

    public void setDexterity(int value) {
        __Dexterity = value;
    }

    private int __FireResist;

    public int getFireResist() {
        return __FireResist;
    }

    public void setFireResist(int value) {
        __FireResist = value;
    }

    private int __SpiritResist;

    public int getSpiritResist() {
        return __SpiritResist;
    }

    public void setSpiritResist(int value) {
        __SpiritResist = value;
    }

    private int __WaterResist;

    public int getWaterResist() {
        return __WaterResist;
    }

    public void setWaterResist(int value) {
        __WaterResist = value;
    }

    private int __AirResist;

    public int getAirResist() {
        return __AirResist;
    }

    public void setAirResist(int value) {
        __AirResist = value;
    }

    private int __EarthResist;

    public int getEarthResist() {
        return __EarthResist;
    }

    public void setEarthResist(int value) {
        __EarthResist = value;
    }

    private int __AC;

    public int getAC() {
        return __AC;
    }

    public void setAC(int value) {
        __AC = value;
    }

    private double __Haste;

    public double getHaste() {
        return __Haste;
    }

    public void setHaste(double value) {
        __Haste = value;
    }

    private double __SpellDamage;

    public double getSpellDamage() {
        return __SpellDamage;
    }

    public void setSpellDamage(double value) {
        __SpellDamage = value;
    }

    private double __SpellCrit;

    public double getSpellCrit() {
        return __SpellCrit;
    }

    public void setSpellCrit(double value) {
        __SpellCrit = value;
    }

    private double __MeleeDamage;

    public double getMeleeDamage() {
        return __MeleeDamage;
    }

    public void setMeleeDamage(double value) {
        __MeleeDamage = value;
    }

    private double __MeleeCrit;

    public double getMeleeCrit() {
        return __MeleeCrit;
    }

    public void setMeleeCrit(double value) {
        __MeleeCrit = value;
    }

    private double __DamageReduction;

    public double getDamageReduction() {
        return __DamageReduction;
    }

    public void setDamageReduction(double value) {
        __DamageReduction = value;
    }

    public AttributeSet() throws Exception {
        this.setHP(0);
        this.setMP(0);
        this.setSP(0);
        this.setHPPercentRegen(0);
        this.setHPStaticRegen(0);
        this.setMPPercentRegen(0);
        this.setMPStaticRegen(0);
        this.setStrength(0);
        this.setStamina(0);
        this.setIntelligence(0);
        this.setDexterity(0);
        this.setFireResist(0);
        this.setSpiritResist(0);
        this.setWaterResist(0);
        this.setAirResist(0);
        this.setEarthResist(0);
        this.setAC(0);
        this.setHaste(0);
        this.setSpellDamage(0);
        this.setSpellCrit(0);
        this.setMeleeDamage(0);
        this.setMeleeCrit(0);
        this.setDamageReduction(0);
    }

    public static AttributeSet add(AttributeSet a1, AttributeSet a2) throws Exception {
        AttributeSet ret = new AttributeSet();
        ret.setAC(a1.getAC() + a2.getAC());
        ret.setAirResist(a1.getAirResist() + a2.getAirResist());
        ret.setDamageReduction(a1.getDamageReduction() + a2.getDamageReduction());
        ret.setDexterity(a1.getDexterity() + a2.getDexterity());
        ret.setEarthResist(a1.getEarthResist() + a2.getEarthResist());
        ret.setFireResist(a1.getFireResist() + a2.getFireResist());
        ret.setHaste(a1.getHaste() + a2.getHaste());
        ret.setHP(a1.getHP() + a2.getHP());
        ret.setHPPercentRegen(a1.getHPPercentRegen() + a2.getHPPercentRegen());
        ret.setHPStaticRegen(a1.getHPStaticRegen() + a2.getHPStaticRegen());
        ret.setIntelligence(a1.getIntelligence() + a2.getIntelligence());
        ret.setMeleeCrit(a1.getMeleeCrit() + a2.getMeleeCrit());
        ret.setMeleeDamage(a1.getMeleeDamage() + a2.getMeleeDamage());
        ret.setMP(a1.getMP() + a2.getMP());
        ret.setMPPercentRegen(a1.getMPPercentRegen() + a2.getMPPercentRegen());
        ret.setMPStaticRegen(a1.getMPStaticRegen() + a2.getMPStaticRegen());
        ret.setSP(a1.getSP() + a2.getSP());
        ret.setSpellCrit(a1.getSpellCrit() + a2.getSpellCrit());
        ret.setSpellDamage(a1.getSpellDamage() + a2.getSpellDamage());
        ret.setSpiritResist(a1.getSpiritResist() + a2.getSpiritResist());
        ret.setStamina(a1.getStamina() + a2.getStamina());
        ret.setStrength(a1.getStrength() + a2.getStrength());
        ret.setWaterResist(a1.getWaterResist() + a2.getWaterResist());
        return ret;
    }

    public static AttributeSet multiply(AttributeSet a1, double mult) throws Exception {
        AttributeSet ret = new AttributeSet();
        ret.setAC((int) Math.ceil(a1.getAC() * mult));
        ret.setAirResist((int) Math.ceil(a1.getAirResist() * mult));
        ret.setDamageReduction(a1.getDamageReduction() * mult);
        ret.setDexterity((int) Math.ceil(a1.getDexterity() * mult));
        ret.setEarthResist((int) Math.ceil(a1.getEarthResist() * mult));
        ret.setFireResist((int) Math.ceil(a1.getFireResist() * mult));
        ret.setHaste(a1.getHaste() * mult);
        ret.setHP((int) Math.ceil(a1.getHP() * mult));
        ret.setHPPercentRegen(a1.getHPPercentRegen() * mult);
        ret.setHPStaticRegen((int) Math.ceil(a1.getHPStaticRegen() * mult));
        ret.setIntelligence((int) Math.ceil(a1.getIntelligence() * mult));
        ret.setMeleeCrit(a1.getMeleeCrit() * mult);
        ret.setMeleeDamage(a1.getMeleeDamage() * mult);
        ret.setMP((int) Math.ceil(a1.getMP() * mult));
        ret.setMPPercentRegen(a1.getMPPercentRegen() * mult);
        ret.setMPStaticRegen((int) Math.ceil(a1.getMPStaticRegen() * mult));
        ret.setSP((int) Math.ceil(a1.getSP() * mult));
        ret.setSpellCrit(a1.getSpellCrit() * mult);
        ret.setSpellDamage(a1.getSpellDamage() * mult);
        ret.setSpiritResist((int) Math.ceil(a1.getSpiritResist() * mult));
        ret.setStamina((int) Math.ceil(a1.getStamina() * mult));
        ret.setStrength((int) Math.ceil(a1.getStrength() * mult));
        ret.setWaterResist((int) Math.ceil(a1.getWaterResist() * mult));
        return ret;
    }

    public static AttributeSet subtract(AttributeSet a1, AttributeSet a2) throws Exception {
        AttributeSet ret = new AttributeSet();
        ret.setAC(a1.getAC() - a2.getAC());
        ret.setAirResist(a1.getAirResist() - a2.getAirResist());
        ret.setDamageReduction(a1.getDamageReduction() - a2.getDamageReduction());
        ret.setDexterity(a1.getDexterity() - a2.getDexterity());
        ret.setEarthResist(a1.getEarthResist() - a2.getEarthResist());
        ret.setFireResist(a1.getFireResist() - a2.getFireResist());
        ret.setHaste(a1.getHaste() - a2.getHaste());
        ret.setHP(a1.getHP() - a2.getHP());
        ret.setHPPercentRegen(a1.getHPPercentRegen() - a2.getHPPercentRegen());
        ret.setHPStaticRegen(a1.getHPStaticRegen() - a2.getHPStaticRegen());
        ret.setIntelligence(a1.getIntelligence() - a2.getIntelligence());
        ret.setMeleeCrit(a1.getMeleeCrit() - a2.getMeleeCrit());
        ret.setMeleeDamage(a1.getMeleeDamage() - a2.getMeleeDamage());
        ret.setMP(a1.getMP() - a2.getMP());
        ret.setMPPercentRegen(a1.getMPPercentRegen() - a2.getMPPercentRegen());
        ret.setMPStaticRegen(a1.getMPStaticRegen() - a2.getMPStaticRegen());
        ret.setSP(a1.getSP() - a2.getSP());
        ret.setSpellCrit(a1.getSpellCrit() - a2.getSpellCrit());
        ret.setSpellDamage(a1.getSpellDamage() - a2.getSpellDamage());
        ret.setSpiritResist(a1.getSpiritResist() - a2.getSpiritResist());
        ret.setStamina(a1.getStamina() - a2.getStamina());
        ret.setStrength(a1.getStrength() - a2.getStrength());
        ret.setWaterResist(a1.getWaterResist() - a2.getWaterResist());
        return ret;
    }

}
