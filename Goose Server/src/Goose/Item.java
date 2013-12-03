package Goose;

import java.sql.PreparedStatement;

import Goose.AttributeSet;
import Goose.IItem;
import Goose.ItemTemplate;
import Goose.SpellEffect;

/**
 * Item, holds the actual item data
 * 
 * Each item in game is seperate Holds the original template and modified/added stats
 * 
 */
public class Item implements IItem {
  private int __ItemID;

  public int getItemID() {
    return __ItemID;
  }

  public void setItemID(int value) {
    __ItemID = value;
  }

  private int __TemplateID;

  public int getTemplateID() {
    return __TemplateID;
  }

  public void setTemplateID(int value) {
    __TemplateID = value;
  }

  private ItemTemplate __Template;

  public ItemTemplate getTemplate() {
    return __Template;
  }

  public void setTemplate(ItemTemplate value) {
    __Template = value;
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

  int weapondamage = 0;

  public int getWeaponDamage() throws Exception {
    return this.weapondamage
        + (int) Math.ceil(this.getTemplate().getWeaponDamage() * this.getStatMultiplier());
  }

  public void setWeaponDamage(int value) throws Exception {
    this.weapondamage = value;
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

  private AttributeSet __BaseStats;

  public AttributeSet getBaseStats() {
    return __BaseStats;
  }

  public void setBaseStats(AttributeSet value) {
    __BaseStats = value;
  }

  private AttributeSet __TotalStats;

  public AttributeSet getTotalStats() {
    return __TotalStats;
  }

  public void setTotalStats(AttributeSet value) {
    __TotalStats = value;
  }

  private double __StatMultiplier;

  public double getStatMultiplier() {
    return __StatMultiplier;
  }

  public void setStatMultiplier(double value) {
    __StatMultiplier = value;
  }

  /**
   * Dirty, has data changed since loading
   * 
   */
  private boolean __Dirty;

  public boolean getDirty() {
    return __Dirty;
  }

  public void setDirty(boolean value) {
    __Dirty = value;
  }

  /**
   * Delete, item is no longer in game so delete it
   */
  private boolean __Delete;

  public boolean getDelete() {
    return __Delete;
  }

  public void setDelete(boolean value) {
    __Delete = value;
  }

  private long __Value;

  public long getValue() {
    return __Value;
  }

  public void setValue(long value) {
    __Value = value;
  }

  boolean bound = false;

  public boolean getIsBound() throws Exception {
    return this.bound;
  }

  public void setIsBound(boolean value) throws Exception {
    this.bound = value;
  }

  /**
   * These properties are read only and just pass along from the templates properties
   * 
   */
  public int getWeaponDelay() throws Exception {
    return this.getTemplate().getWeaponDelay();
  }

  public int getStackSize() throws Exception {
    return this.getTemplate().getStackSize();
  }

  public boolean getIsLore() throws Exception {
    return this.getTemplate().getIsLore();
  }

  public boolean getIsBindOnPickup() throws Exception {
    return this.getTemplate().getIsBindOnPickup();
  }

  public boolean getIsBindOnEquip() throws Exception {
    return this.getTemplate().getIsBindOnEquip();
  }

  public boolean getIsEvent() throws Exception {
    return this.getTemplate().getIsEvent();
  }

  public ItemTemplate.ItemSlots getSlot() throws Exception {
    return this.getTemplate().getSlot();
  }

  public ItemTemplate.ItemTypes getType() throws Exception {
    return this.getTemplate().getType();
  }

  public ItemTemplate.UseTypes getUseType() throws Exception {
    return this.getTemplate().getUseType();
  }

  public int getMinLevel() throws Exception {
    return this.getTemplate().getMinLevel();
  }

  public int getMaxLevel() throws Exception {
    return this.getTemplate().getMaxLevel();
  }

  public long getMinExperience() throws Exception {
    return this.getTemplate().getMinExperience();
  }

  public long getMaxExperience() throws Exception {
    return this.getTemplate().getMaxExperience();
  }

  /**
   * This is a bitmask Therefore only limited to about 64 classes, which should be enough. If the
   * bit is set then that class id CAN'T use the item.
   * 
   */
  public long getClassRestrictions() throws Exception {
    return this.getTemplate().getClassRestrictions();
  }

  public SpellEffect getSpellEffect() throws Exception {
    return this.getTemplate().getSpellEffect();
  }

  public double getSpellEffectChance() throws Exception {
    return this.getTemplate().getSpellEffectChance();
  }

  public int getLearnSpellID() throws Exception {
    return this.getTemplate().getLearnSpellID();
  }

  private boolean __Unsaved;

  public boolean getUnsaved() {
    return __Unsaved;
  }

  public void setUnsaved(boolean value) {
    __Unsaved = value;
  }

  public Item() throws Exception {
    this.setUnsaved(true);
    this.setItemID(0);
    this.setTotalStats(new AttributeSet());
    this.setBaseStats(new AttributeSet());
    this.setStatMultiplier(1);
    this.setDirty(true);
    this.setDelete(false);
  }

  /**
   * LoadFromTemplate, loads item from a template
   * 
   * This is when we want an item the same as the template.
   * 
   */
  public void loadFromTemplate(ItemTemplate template) throws Exception {
    this.setTemplate(template);
    this.setTemplateID(this.getTemplate().getID());
    this.setTotalStats(AttributeSet.add(this.getTotalStats(), this.getTemplate().getBaseStats()));
    this.setName(this.getTemplate().getName());
    this.setDescription(this.getTemplate().getDescription());
    this.setGraphicEquipped(this.getTemplate().getGraphicEquipped());
    this.setGraphicTile(this.getTemplate().getGraphicTile());
    this.setGraphicR(this.getTemplate().getGraphicR());
    this.setGraphicG(this.getTemplate().getGraphicG());
    this.setGraphicB(this.getTemplate().getGraphicB());
    this.setGraphicA(this.getTemplate().getGraphicA());
    this.setValue(this.getTemplate().getValue());
    this.setBodyState(this.getTemplate().getBodyState());
  }

  /**
   * LoadTemplate, adds template to item
   * 
   * This is when we want to just add the templates stats to our item ie when loading the items
   * database, eg for surname/titled items
   * 
   * Note: Doesn't load the value from the template as we want to keep the value as 0 if the item is
   * custom for example
   * 
   */
  public void loadTemplate(ItemTemplate template) throws Exception {
    this.setTotalStats(AttributeSet.add(this.getTotalStats(), template.getBaseStats()));
    this.setTotalStats(AttributeSet.multiply(this.getTotalStats(), this.getStatMultiplier()));
    this.setTotalStats(AttributeSet.add(this.getTotalStats(), this.getBaseStats()));
  }

  /**
   * AddItem, adds item to database
   * 
   */
  public void addItem(GameWorld world) throws Exception {

    String query =
        "INSERT INTO items (item_id, item_template_id, item_name, item_description, "
            + "player_hp, player_mp, player_sp, stat_ac, stat_str, stat_sta, stat_dex, stat_int, "
            + "res_fire, res_water, res_spirit, res_air, res_earth, weapon_damage, item_value, "
            + "graphic_tile, graphic_equip, graphic_r, graphic_g, graphic_b, graphic_a, stat_multiplier, "
            + "bound, body_state) VALUES ("
            + this.getItemID()
            + ","
            + this.getTemplateID()
            + ", "
            + "?"
            + ", "
            + "?"
            + ", "
            + this.getBaseStats().getHP()
            + ", "
            + this.getBaseStats().getMP()
            + ", "
            + this.getBaseStats().getSP()
            + ", "
            + this.getBaseStats().getAC()
            + ", "
            + this.getBaseStats().getStrength()
            + ", "
            + this.getBaseStats().getStamina()
            + ", "
            + this.getBaseStats().getDexterity()
            + ", "
            + this.getBaseStats().getIntelligence()
            + ", "
            + this.getBaseStats().getFireResist()
            + ", "
            + this.getBaseStats().getWaterResist()
            + ", "
            + this.getBaseStats().getSpiritResist()
            + ", "
            + this.getBaseStats().getAirResist()
            + ", "
            + this.getBaseStats().getEarthResist()
            + ", "
            + this.weapondamage
            + ", "
            + this.getValue()
            + ", "
            + this.getGraphicTile()
            + ", "
            + this.getGraphicEquipped()
            + ", "
            + this.getGraphicR()
            + ", "
            + this.getGraphicG()
            + ", "
            + this.getGraphicB()
            + ", "
            + this.getGraphicA()
            + ", "
            + this.getStatMultiplier()
            + ", "
            + (this.bound ? "'1'" : "'0'") + ", " + this.getBodyState() + ")";
    PreparedStatement preparedStatement = world.getSqlConnection().prepareStatement(query);
    preparedStatement.setString(1, this.getName());
    preparedStatement.setString(2, this.getDescription());
    this.setDirty(false);
    this.setUnsaved(false);
    int executeUpdate = preparedStatement.executeUpdate();
//    System.out.println("Item ID: " + this.getItemID() + " - " + executeUpdate);
  }

  /**
   * SaveItem, updates item info in database
   * 
   */
  public void saveItem(GameWorld world) throws Exception {
    String query =
        "UPDATE items SET " + "item_template_id=" + this.getTemplateID() + ", " + "item_name="
            + "?" + ", " + "item_description=" + "?" + ", " + "player_hp="
            + this.getBaseStats().getHP() + ", " + "player_mp=" + this.getBaseStats().getMP()
            + ", " + "player_sp=" + this.getBaseStats().getSP() + ", " + "stat_ac="
            + this.getBaseStats().getAC() + ", " + "stat_str=" + this.getBaseStats().getStrength()
            + ", " + "stat_sta=" + this.getBaseStats().getStamina() + ", " + "stat_dex="
            + this.getBaseStats().getDexterity() + ", " + "stat_int="
            + this.getBaseStats().getIntelligence() + ", " + "res_fire="
            + this.getBaseStats().getFireResist() + ", " + "res_water="
            + this.getBaseStats().getWaterResist() + ", " + "res_spirit="
            + this.getBaseStats().getSpiritResist() + ", " + "res_air="
            + this.getBaseStats().getAirResist() + ", " + "res_earth="
            + this.getBaseStats().getEarthResist() + ", " + "weapon_damage=" + this.weapondamage
            + ", " + "item_value=" + this.getValue() + ", " + "graphic_tile="
            + this.getGraphicTile() + ", " + "graphic_equip=" + this.getGraphicEquipped() + ", "
            + "graphic_r=" + this.getGraphicR() + ", " + "graphic_g=" + this.getGraphicG() + ", "
            + "graphic_b=" + this.getGraphicB() + ", " + "graphic_a=" + this.getGraphicA() + ", "
            + "stat_multiplier=" + this.getStatMultiplier() + ", " + "bound="
            + (this.bound ? "'1'" : "'0'") + ", " + "body_state=" + this.getBodyState()
            + " WHERE item_id=" + this.getItemID();
    PreparedStatement preparedStatement = world.getSqlConnection().prepareStatement(query);
    preparedStatement.setString(1, this.getName());
    preparedStatement.setString(2, this.getDescription());
    preparedStatement.executeUpdate();
    this.setDirty(false);
  }

  /**
   * DeleteItem, deletes item from database
   * 
   */
  public void deleteItem(GameWorld world) throws Exception {
    world.getSqlConnection().createStatement()
        .executeUpdate("DELETE FROM items WHERE item_id=" + this.getItemID());
  }

}
