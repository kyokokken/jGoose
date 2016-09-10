package Goose;

/**
 * ItemTemplate, base stats for an item
 */
public class ItemTemplate implements IItem {
    public enum UseTypes {
        Equipment, Consumable, Useless, Scroll
    }

    public enum ItemSlots {
        Helmet, Shield, OneHanded, TwoHanded, Ring, Necklace, Pauldrons, Cloak, Belt, Gloves, Chest, Pants, Shoes, _13, _14, _15;
    }

    public enum ItemTypes {
        Accessory, Cloth, Silk, Plate, OneHandedBlunt, OneHandedSlash, OneHandedPierce, TwoHandedBlunt, TwoHandedSlash, TwoHandedPierce
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

    private Goose.ItemTemplate.UseTypes __UseType = Goose.ItemTemplate.UseTypes.Equipment;

    public Goose.ItemTemplate.UseTypes getUseType() {
        return __UseType;
    }

    public void setUseType(Goose.ItemTemplate.UseTypes value) {
        __UseType = value;
    }

    private int __MinLevel;

    public int getMinLevel() {
        return __MinLevel;
    }

    public void setMinLevel(int value) {
        __MinLevel = value;
    }

    private int __MaxLevel;

    public int getMaxLevel() {
        return __MaxLevel;
    }

    public void setMaxLevel(int value) {
        __MaxLevel = value;
    }

    private long __MinExperience;

    public long getMinExperience() {
        return __MinExperience;
    }

    public void setMinExperience(long value) {
        __MinExperience = value;
    }

    private long __MaxExperience;

    public long getMaxExperience() {
        return __MaxExperience;
    }

    public void setMaxExperience(long value) {
        __MaxExperience = value;
    }

    private AttributeSet __BaseStats;

    public AttributeSet getBaseStats() {
        return __BaseStats;
    }

    public void setBaseStats(AttributeSet value) {
        __BaseStats = value;
    }

    private int __WeaponDelay;

    public int getWeaponDelay() {
        return __WeaponDelay;
    }

    public void setWeaponDelay(int value) {
        __WeaponDelay = value;
    }

    private int __WeaponDamage;

    public int getWeaponDamage() {
        return __WeaponDamage;
    }

    public void setWeaponDamage(int value) {
        __WeaponDamage = value;
    }

    private ItemTemplate.ItemSlots __Slot = Goose.ItemTemplate.ItemSlots.Helmet;

    public ItemTemplate.ItemSlots getSlot() {
        return __Slot;
    }

    public void setSlot(Goose.ItemTemplate.ItemSlots value) {
        __Slot = value;
    }

    private ItemTemplate.ItemTypes __Type = Goose.ItemTemplate.ItemTypes.Accessory;

    public ItemTemplate.ItemTypes getType() {
        return __Type;
    }

    public void setType(Goose.ItemTemplate.ItemTypes value) {
        __Type = value;
    }

    private int __GraphicEquipped;

    public int getGraphicEquipped() {
        return __GraphicEquipped;
    }

    public void setGraphicEquipped(int value) {
        __GraphicEquipped = value;
    }

    private int __GraphicTile;

    public int getGraphicTile() {
        return __GraphicTile;
    }

    public void setGraphicTile(int value) {
        __GraphicTile = value;
    }

    private int __GraphicR;

    public int getGraphicR() {
        return __GraphicR;
    }

    public void setGraphicR(int value) {
        __GraphicR = value;
    }

    private int __GraphicG;

    public int getGraphicG() {
        return __GraphicG;
    }

    public void setGraphicG(int value) {
        __GraphicG = value;
    }

    private int __GraphicB;

    public int getGraphicB() {
        return __GraphicB;
    }

    public void setGraphicB(int value) {
        __GraphicB = value;
    }

    private int __GraphicA;

    public int getGraphicA() {
        return __GraphicA;
    }

    public void setGraphicA(int value) {
        __GraphicA = value;
    }

    private long __Value;

    public long getValue() {
        return __Value;
    }

    public void setValue(long value) {
        __Value = value;
    }

    private boolean __IsLore;

    public boolean getIsLore() {
        return __IsLore;
    }

    public void setIsLore(boolean value) {
        __IsLore = value;
    }

    private boolean __IsBindOnPickup;

    public boolean getIsBindOnPickup() {
        return __IsBindOnPickup;
    }

    public void setIsBindOnPickup(boolean value) {
        __IsBindOnPickup = value;
    }

    private boolean __IsBindOnEquip;

    public boolean getIsBindOnEquip() {
        return __IsBindOnEquip;
    }

    public void setIsBindOnEquip(boolean value) {
        __IsBindOnEquip = value;
    }

    private boolean __IsEvent;

    public boolean getIsEvent() {
        return __IsEvent;
    }

    public void setIsEvent(boolean value) {
        __IsEvent = value;
    }

    /**
     * This is a bitmask Therefore only limited to about 64 classes, which should be enough. If the
     * bit is set then that class id CAN'T use the item.
     */
    private long __ClassRestrictions;

    public long getClassRestrictions() {
        return __ClassRestrictions;
    }

    public void setClassRestrictions(long value) {
        __ClassRestrictions = value;
    }

    private int __StackSize;

    public int getStackSize() {
        return __StackSize;
    }

    public void setStackSize(int value) {
        __StackSize = value;
    }

    /**
     * Body pose/state 1 for normal, 3 for staff, 4 for sword
     */
    private int __BodyState;

    public int getBodyState() {
        return __BodyState;
    }

    public void setBodyState(int value) {
        __BodyState = value;
    }

    /**
     * Spell effect id
     */
    private int __SpellEffectID;

    public int getSpellEffectID() {
        return __SpellEffectID;
    }

    public void setSpellEffectID(int value) {
        __SpellEffectID = value;
    }

    /**
     * Spell effect
     */
    private SpellEffect __SpellEffect;

    public SpellEffect getSpellEffect() {
        return __SpellEffect;
    }

    public void setSpellEffect(SpellEffect value) {
        __SpellEffect = value;
    }

    private double __SpellEffectChance;

    public double getSpellEffectChance() {
        return __SpellEffectChance;
    }

    public void setSpellEffectChance(double value) {
        __SpellEffectChance = value;
    }

    private int __LearnSpellID;

    public int getLearnSpellID() {
        return __LearnSpellID;
    }

    public void setLearnSpellID(int value) {
        __LearnSpellID = value;
    }

}
