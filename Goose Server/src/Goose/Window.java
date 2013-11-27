package Goose;

import Goose.AttributeSet;
import Goose.Class;
import Goose.GameSettings;
import Goose.IItem;
import Goose.Item;
import Goose.ItemTemplate;
import Goose.NPCVendorSlot;
import Goose.Pet;
import Goose.Ranks;

/**
 * Window
 * 
 * Holds information about a window in game
 * 
 */
public class Window {
	public enum ButtonTypes {
		Exit, Combine, Close, Three, Four, Five
	}

	public enum WindowFrames {
		__dummyEnum__0, __dummyEnum__1, __dummyEnum__2, __dummyEnum__3, __dummyEnum__4, __dummyEnum__5, __dummyEnum__6, __dummyEnum__7, __dummyEnum__8, __dummyEnum__9, __dummyEnum__10, __dummyEnum__11, __dummyEnum__12, Vendor, __dummyEnum__13, __dummyEnum__14, __dummyEnum__15, __dummyEnum__16, __dummyEnum__17, __dummyEnum__18, __dummyEnum__19, __dummyEnum__20, ItemInfo, EightSlot, TenSlot
	}

	private WindowFrames __Frame = WindowFrames.Vendor;

	public WindowFrames getFrame() {
		return __Frame;
	}

	public void setFrame(WindowFrames value) {
		__Frame = value;
	}

	public enum WindowTypes {
		__dummyEnum__0, Vendor, ItemInfo, CharInfo, Rank, CombineBag, PetInfo
	}

	private WindowTypes __Type = WindowTypes.Vendor;

	public WindowTypes getType() {
		return __Type;
	}

	public void setType(WindowTypes value) {
		__Type = value;
	}

	private String __Title;

	public String getTitle() {
		return __Title;
	}

	public void setTitle(String value) {
		__Title = value;
	}

	/**
	 * Buttons Seems to be 5 comma separated values one for each button type
	 * 
	 * eg vendor window is 0,1,0,0,0 The 1 making a close button on the window
	 * 
	 */
	private String __Buttons;

	public String getButtons() {
		return __Buttons;
	}

	public void setButtons(String value) {
		__Buttons = value;
	}

	private Goose.NPC __NPC;

	public Goose.NPC getNPC() {
		return __NPC;
	}

	public void setNPC(Goose.NPC value) {
		__NPC = value;
	}

	private int __ID;

	public int getID() {
		return __ID;
	}

	public void setID(int value) {
		__ID = value;
	}

	private Object __Data;

	public Object getData() {
		return __Data;
	}

	public void setData(Object value) {
		__Data = value;
	}

	/**
	 * Create, creates window on player's screen
	 * 
	 */
	public void create(Goose.Player player, GameWorld world) throws Exception {
		this.setID(player.getLastWindowID() + 1);
		switch (this.getType()) {
			case Vendor:
				this.setFrame(WindowFrames.Vendor);
				break;
			case Rank:
			case CharInfo:
			case ItemInfo:
			case PetInfo:
				this.setFrame(WindowFrames.ItemInfo);
				break;
			case CombineBag:
				this.setFrame(WindowFrames.TenSlot);
				break;

		}
		String create = "MKW" + this.getID() + ","
				+ ((Enum) this.getFrame()).ordinal() + "," + this.getTitle()
				+ "," + this.getButtons() + ",";
		switch (this.getType()) {
			case Vendor:
				create += this.getNPC().getLoginID() + "," + "0,0";
				break;
			case Rank:
			case CharInfo:
			case ItemInfo:
			case PetInfo:
				create += "0,0,0";
				break;
			case CombineBag:
				create += "0,0,0";
				break;

		}
		world.send(player, create);
		this.populate(player, world);
		world.send(player, "ENW" + this.getID());
	}

	/**
	 * Populate, initially populates the window
	 * 
	 */
	public void populate(Goose.Player player, GameWorld world) throws Exception {
		switch (this.getType()) {
			case Vendor:
				// start at 0 since first slot is always null
				int i = 0;
				for (NPCVendorSlot slot : this.getNPC().getVendorItems()) {
					if (slot == null) {
						i++;
						continue;
					}

					world.send(
							player,
							"WNF"
									+ this.getID()
									+ ","
									+ i
									+ ","
									+ slot.getItemTemplate().getName()
									+ (slot.getStack() > 1 ? " ("
											+ slot.getStack() + ")" : "") + "|"
									+ 0 + "|" + slot.getItemTemplate().getID()
									+ "|"
									+ slot.getItemTemplate().getGraphicTile()
									+ "|*");
					i++;
				}
				break;
			case ItemInfo:
				this.populateItemInfo(player, world);
				break;
			case CombineBag:
				player.getInventory().sendCombineBag(world);
				break;
			case CharInfo:
				this.populateCharInfo(player, world);
				break;
			case Rank:
				this.populateRanks(player, world);
				break;
			case PetInfo:
				this.populatePetInfo(player, world);
				break;

		}
	}

	/**
	 * Refresh, repopulates the window
	 * 
	 */
	public void refresh(Goose.Player player, GameWorld world) throws Exception {
		switch (this.getType()) {
			case Vendor:
				// start at 0 since first slot is always null
				int i = 0;
				for (NPCVendorSlot slot : this.getNPC().getVendorItems()) {
					if (slot == null) {
						world.send(player, "WNF" + this.getID() + "," + i
								+ ", |0|0|0|*");
					} else {
						world.send(
								player,
								"WNF"
										+ this.getID()
										+ ","
										+ i
										+ ","
										+ slot.getItemTemplate().getName()
										+ (slot.getStack() > 1 ? " ("
												+ slot.getStack() + ")" : "")
										+ "|"
										+ 0
										+ "|"
										+ slot.getItemTemplate().getID()
										+ "|"
										+ slot.getItemTemplate()
												.getGraphicTile() + "|*");
					}
					i++;
				}
				while (i <= GameSettings.getDefault().getVendorSlotSize()) {
					world.send(player, "WNF" + this.getID() + "," + i
							+ ", |0|0|0|*");
					i++;
				}
				break;
			case ItemInfo:
				this.populateItemInfo(player, world);
				break;
			case CombineBag:
				player.getInventory().sendCombineBag(world);
				break;
			case CharInfo:
				this.populateCharInfo(player, world);
				break;
			case Rank:
				this.populateRanks(player, world);
				break;
			case PetInfo:
				this.populatePetInfo(player, world);
				break;

		}
	}

	/**
	 * PopulateItemInfo, populates item info window
	 * 
	 */
	public void populateItemInfo(Goose.Player player, GameWorld world)
			throws Exception {
		IItem item = null;
		if (this.getData() instanceof Item) {
			item = (Item) this.getData();
		} else {
			item = (ItemTemplate) this.getData();
		}
		String line = "WNF" + this.getID() + "," + 1 + ",";
		if (item.getIsLore())
			line += "LORE ";

		if (item.getIsEvent())
			line += "EVENT ";

		if (item.getIsBindOnPickup())
			line += "BOP ";

		if (item.getIsBindOnEquip())
			line += "BOE ";

		if (item instanceof Item && ((Item) item).getIsBound())
			line += "BOUND ";

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 2 + "," + item.getDescription()
				+ "|0|0|0|*";
		world.send(player, line);
		if (item.getUseType() == Goose.ItemTemplate.UseTypes.Equipment) {
			if (item.getSlot() == Goose.ItemTemplate.ItemSlots.OneHanded
					|| item.getSlot() == Goose.ItemTemplate.ItemSlots.TwoHanded) {
				line = "WNF" + this.getID() + "," + 3 + ",";
				line += "DMG: " + item.getWeaponDamage();
				line += " DLY: " + item.getWeaponDelay();
				line += " " + item.getType().toString();
				line += "|0|0|0|*";
				world.send(player, line);
			} else {
				line = "WNF" + this.getID() + "," + 3 + ",";
				line += "Type: " + item.getType().toString();
				line += "|0|0|0|*";
				world.send(player, line);
			}
		} else {
			world.send(player, "WNF" + this.getID() + "," + 3 + ",|0|0|0|*");
		}
		AttributeSet stats = null;
		if (item instanceof Item)
			stats = ((Item) item).getTotalStats();
		else
			stats = item.getBaseStats();
		line = "WNF" + this.getID() + "," + 4 + ",";
		if (stats.getAC() != 0)
			line += "AC: " + stats.getAC() + " ";

		if (stats.getHP() != 0)
			line += "HP: " + stats.getHP() + " ";

		if (stats.getMP() != 0)
			line += "MP: " + stats.getMP() + " ";

		if (stats.getSP() != 0)
			line += "SP: " + stats.getSP() + " ";

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 5 + ",";
		if (stats.getStrength() != 0)
			line += "STR: " + stats.getStrength() + " ";

		if (stats.getStamina() != 0)
			line += "STA: " + stats.getStamina() + " ";

		if (stats.getIntelligence() != 0)
			line += "INT: " + stats.getIntelligence() + " ";

		if (stats.getDexterity() != 0)
			line += "DEX: " + stats.getDexterity() + " ";

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 6 + ",";
		if (stats.getFireResist() != 0)
			line += "FR: " + stats.getFireResist() + " ";

		if (stats.getAirResist() != 0)
			line += "AR: " + stats.getAirResist() + " ";

		if (stats.getEarthResist() != 0)
			line += "ER: " + stats.getEarthResist() + " ";

		if (stats.getWaterResist() != 0)
			line += "WR: " + stats.getWaterResist() + " ";

		if (stats.getSpiritResist() != 0)
			line += "SR: " + stats.getSpiritResist() + " ";

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 7 + ",";
		if (item.getMinLevel() >= 1)
			line += "Level " + item.getMinLevel() + " ";

		for (int i = 1; i <= world.getClassHandler().getCount(); i++) {
			/* This part will probably need changing later */
			Class c = world.getClassHandler().getClass(i);
			if ((item.getClassRestrictions() & ((int) (Math.pow(2,
					c.getClassID())))) == 0) {
				line += c.getClassName() + " ";
			}

		}
		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 8 + ",";
		if (item.getMinExperience() > 0) {
			line += "Sold: " + item.getMinExperience();
		}

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 9 + ",";
		if (item.getSpellEffect() != null) {
			line += "Effect: " + item.getSpellEffect().getName();
			line += " (" + (int) Math.round(item.getSpellEffectChance()) + "%)";
		}

		line += "|0|0|0|*";
		world.send(player, line);
		line = "WNF" + this.getID() + "," + 10 + ",";
		if (item.getUseType() == Goose.ItemTemplate.UseTypes.Equipment) {
			line += "Slot: " + item.getSlot().toString() + " ";
		}

		line += "Value: " + item.getValue();
		line += "|0|0|0|*";
		world.send(player, line);
	}

	/**
	 * InventoryToWindow, handles inventory to window event
	 * 
	 * 
	 */
	public void inventoryToWindow(Goose.Player player, int invslot, int toslot,
			GameWorld world) throws Exception {
		switch (this.getType()) {
			case CombineBag:
				player.getInventory().swapInventoryCombineSlots(invslot,
						toslot, world);
				break;

		}
	}

	/**
	 * WindowToInventory, handles window to inventory event
	 * 
	 * 
	 */
	public void windowToInventory(Goose.Player player, int fromslot,
			int invslot, GameWorld world) throws Exception {
		switch (this.getType()) {
			case CombineBag:
				player.getInventory().swapInventoryCombineSlots(invslot,
						fromslot, world);
				break;

		}
	}

	/**
	 * PopulateCharInfo, populates character info window
	 * 
	 */
	public void populateCharInfo(Goose.Player player, GameWorld world)
			throws Exception {
		String line;
		// Experience sold, hp, mp
		line = "WNF" + this.getID() + "," + 1 + "," + "Experience Sold: "
				+ player.getExperienceSold() + "|0|0|0|*";
		world.send(player, line);
		// Bound map, x, y
		line = "WNF" + this.getID() + "," + 2 + "," + "Bound: "
				+ player.getBoundMap().getName() + " (" + player.getBoundX()
				+ "," + player.getBoundY() + ")" + "|0|0|0|*";
		world.send(player, line);
		// HP regen
		line = "WNF" + this.getID() + "," + 3 + "," + "HP Regeneration: "
				+ Math.round(player.getMaxStats().getHPPercentRegen() * 100)
				+ "%" + "|0|0|0|*";
		world.send(player, line);
		// MP Regen
		line = "WNF" + this.getID() + "," + 4 + "," + "MP Regeneration: "
				+ Math.round(player.getMaxStats().getMPPercentRegen() * 100)
				+ "%" + "|0|0|0|*";
		world.send(player, line);
		// SD
		line = "WNF" + this.getID() + "," + 5 + "," + "Spell Damage Increase: "
				+ Math.round(player.getMaxStats().getSpellDamage() * 100) + "%"
				+ "|0|0|0|*";
		world.send(player, line);
		// SC
		line = "WNF" + this.getID() + "," + 6 + "," + "Spell Critical Chance: "
				+ Math.round(player.getMaxStats().getSpellCrit() * 100) + "%"
				+ "|0|0|0|*";
		world.send(player, line);
		// MD
		line = "WNF" + this.getID() + "," + 7 + "," + "Melee Damage Increase: "
				+ Math.round(player.getMaxStats().getMeleeDamage() * 100) + "%"
				+ "|0|0|0|*";
		world.send(player, line);
		// MC
		line = "WNF" + this.getID() + "," + 8 + "," + "Melee Critical Chance: "
				+ Math.round(player.getMaxStats().getMeleeCrit() * 100) + "%"
				+ "|0|0|0|*";
		world.send(player, line);
		// Haste
		line = "WNF" + this.getID() + "," + 9 + "," + "Haste: "
				+ Math.round(player.getMaxStats().getHaste() * 100) + "%"
				+ "|0|0|0|*";
		world.send(player, line);
		// Damage reduce
		line = "WNF" + this.getID() + "," + 10 + "," + "Damage Reduction: "
				+ Math.round(player.getMaxStats().getDamageReduction() * 100)
				+ "%" + "|0|0|0|*";
		world.send(player, line);
	}

	/**
	 * Ranks window
	 * 
	 */
	public void populateRanks(Goose.Player player, GameWorld world)
			throws Exception {
		Ranks rank = (Ranks) this.getData();
		int i = 1;
		for (String line : rank.getRanks(world)) {
			world.send(player, "WNF" + this.getID() + "," + i + "," + line
					+ "|0|0|0|*");
			i++;
		}
	}

	/**
	 * PopulatePetInfo, populates pet info window
	 * 
	 */
	public void populatePetInfo(Goose.Player player, GameWorld world)
			throws Exception {
		Pet pet = (Pet) this.getData();
		String line;
		// Name
		line = "WNF" + this.getID() + "," + 1 + "," + "Name: " + pet.getName()
				+ "|0|0|0|*";
		world.send(player, line);
		// Experience
		line = "WNF" + this.getID() + "," + 2 + "," + "Experience: "
				+ pet.getExperience() + "|0|0|0|*";
		world.send(player, line);
		// if max level show experience sold, else show tnl
		if (pet.getClas().getLevel(pet.getLevel()).getExperience() == 0) {
			// Experience Sold
			line = "WNF" + this.getID() + "," + 3 + "," + "Experience Sold: "
					+ pet.getExperienceSold() + "|0|0|0|*";
			world.send(player, line);
		} else {
			// TNL
			line = "WNF"
					+ this.getID()
					+ ","
					+ 3
					+ ","
					+ "Next Level: "
					+ (pet.getClas().getLevel(pet.getLevel()).getExperience() - pet
							.getExperience()) + "|0|0|0|*";
			world.send(player, line);
		}
		// Level
		line = "WNF" + this.getID() + "," + 4 + "," + "Level: "
				+ pet.getLevel() + "|0|0|0|*";
		world.send(player, line);
		// Weapon Damage
		line = "WNF" + this.getID() + "," + 5 + "," + "Weapon Damage: "
				+ pet.getWeaponDamage() + "|0|0|0|*";
		world.send(player, line);
		// Attack Speed
		line = "WNF" + this.getID() + "," + 6 + "," + "Attack Speed: "
				+ Math.round(pet.getAttackSpeed()) + "|0|0|0|*";
		world.send(player, line);
		// Move Speed
		line = "WNF" + this.getID() + "," + 7 + "," + "Move Speed: "
				+ Math.round(pet.getMoveSpeed()) + "|0|0|0|*";
		world.send(player, line);
		// Bought HP
		line = "WNF" + this.getID() + "," + 8 + "," + "Extra HP: "
				+ pet.getBaseStats().getHP() + "|0|0|0|*";
		world.send(player, line);
	}

}

/*
 * // MD line = "WNF" + this.ID + "," + 7 + "," + "Melee Damage Increase: " +
 * Math.Round(player.MaxStats.MeleeDamage * 100,0) + "%" + "|0|0|0|*";
 * world.Send(player, line); // MC line = "WNF" + this.ID + "," + 8 + "," +
 * "Melee Critical Chance: " + Math.Round(player.MaxStats.MeleeCrit * 100,0) +
 * "%" + "|0|0|0|*"; world.Send(player, line); // Haste line = "WNF" + this.ID +
 * "," + 9 + "," + "Haste: " + Math.Round(player.MaxStats.Haste * 100,0) + "%" +
 * "|0|0|0|*"; world.Send(player, line); // Damage reduce line = "WNF" + this.ID
 * + "," + 10 + "," + "Damage Reduction: " +
 * Math.Round(player.MaxStats.DamageReduction * 100, 0) + "%" + "|0|0|0|*";
 * world.Send(player, line);
 */