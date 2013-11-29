package Goose;

import Goose.AttributeSet;
import Goose.ItemTemplate;
import Goose.SpellEffect;

/**
 * Item interface
 * 
 * so can interchange ItemTemplate/Items mainly used for Item info window
 * 
 */
public interface IItem {
	String getName() throws Exception;
	void setName(String value) throws Exception;
	String getDescription() throws Exception;
	void setDescription(String value) throws Exception;
	int getGraphicEquipped() throws Exception;
	void setGraphicEquipped(int value) throws Exception;
	int getGraphicTile() throws Exception;
	void setGraphicTile(int value) throws Exception;
	int getGraphicR() throws Exception;
	void setGraphicR(int value) throws Exception;
	int getGraphicG() throws Exception;
	void setGraphicG(int value) throws Exception;
	int getGraphicB() throws Exception;
	void setGraphicB(int value) throws Exception;
	int getGraphicA() throws Exception;
	void setGraphicA(int value) throws Exception;
	int getBodyState() throws Exception;
	void setBodyState(int value) throws Exception;
	AttributeSet getBaseStats() throws Exception;
	void setBaseStats(AttributeSet value) throws Exception;
	long getValue() throws Exception;
	void setValue(long value) throws Exception;
	int getWeaponDamage() throws Exception;
	void setWeaponDamage(int value) throws Exception;
	int getWeaponDelay() throws Exception;
	int getStackSize() throws Exception;
	boolean getIsLore() throws Exception;
	boolean getIsBindOnPickup() throws Exception;
	boolean getIsBindOnEquip() throws Exception;
	boolean getIsEvent() throws Exception;
	ItemTemplate.ItemSlots getSlot() throws Exception;
	ItemTemplate.ItemTypes getType() throws Exception;
	ItemTemplate.UseTypes getUseType() throws Exception;
	int getMinLevel() throws Exception;
	int getMaxLevel() throws Exception;
	long getMinExperience() throws Exception;
	long getMaxExperience() throws Exception;
	long getClassRestrictions() throws Exception;
	SpellEffect getSpellEffect() throws Exception;
	double getSpellEffectChance() throws Exception;
	int getLearnSpellID() throws Exception;
}
