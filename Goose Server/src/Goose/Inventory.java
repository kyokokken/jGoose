package Goose;

import Goose.Buff;
import Goose.Combination;
import Goose.GameSettings;
import Goose.Item;
import Goose.ItemSlot;
import Goose.ItemTemplate;
import Goose.SpellEffect;
import Goose.SpellEffect.EffectTypes;
import Goose.Window;
import Goose.Window.WindowTypes;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Inventory, handles a players inventory
 * 
 */
public class Inventory {
	public enum EquipSlots {
		__dummyEnum__0, Weapon, Shield, Head, Chest, Legs, Feet, Pauldrons, Gloves, Cloak, Belt, Necklace, Ring1, Ring2
	}

	ItemSlot[] equipped;
	ItemSlot[] inventory;
	ItemSlot[] combine;
	/**
	 * The player this inventory belongs to
	 */
	Goose.Player player;

	public Inventory(Goose.Player player) throws Exception {
		// Inventory is numbered 1 to InventorySize.
		this.inventory = new ItemSlot[GameSettings.getDefault()
				.getInventorySize() + 1];
		// Equipped is numbered 1 to EquippedSize.
		this.equipped = new ItemSlot[GameSettings.getDefault()
				.getEquippedSize() + 1];
		// Combine is numbered 1 to CombineBagSize.
		this.combine = new ItemSlot[GameSettings.getDefault()
				.getCombineBagSize() + 1];
		this.player = player;
	}

	/**
	 * AddItem, adds an item to inventory
	 * 
	 * Adds to first free slot or stack that it can fit into. If it can't do any
	 * of these returns false.
	 * 
	 */
	public boolean addItem(Item item, long stack, GameWorld world)
			throws Exception {
		for (int i = 1; i <= GameSettings.getDefault().getInventorySize(); i++) {
			ItemSlot slot = new ItemSlot();
			slot.setItem(item);
			slot.setStack(stack);
			if (this.inventory[i] == null) {
				this.inventory[i] = slot;
				this.sendSlot(i, world);
				return true;
			} else if (this.inventory[i].canStack(slot)) {
				// Mark for deletion since we're adding to a stack
				slot.getItem().setDelete(true);
				this.inventory[i].setStack(this.inventory[i].getStack()
						+ slot.getStack());
				this.sendSlot(i, world);
				return true;
			}

		}
		return false;
	}

	/**
	 * SendSlot, sends information about slot i to the player who owns the
	 * inventory
	 * 
	 * if they're in game
	 * 
	 */
	public void sendSlot(int i, GameWorld world) throws Exception {
		if (i < 1 && i > GameSettings.getDefault().getInventorySize())
			return;

		if (this.player.getState().compareTo(Goose.Player.States.LoadingMap) >= 0) {
			ItemSlot slot = this.inventory[i];
			if (slot != null) {
				world.send(this.player, "SIS" + i + ","
						+ slot.getItem().getItemID() + ","
						+ slot.getItem().getName() + "," + slot.getStack()
						+ "," + slot.getItem().getGraphicTile() + ","
						+ slot.getItem().getGraphicR() + ","
						+ slot.getItem().getGraphicG() + ","
						+ slot.getItem().getGraphicB() + ","
						+ slot.getItem().getGraphicA());
			} else {
				world.send(this.player, "SIS" + i);
			}
		}

	}

	/**
	 * SendAll, sends all slots to player
	 * 
	 */
	public void sendAll(GameWorld world) throws Exception {
		for (int i = 1; i <= GameSettings.getDefault().getInventorySize(); i++) {
			this.sendSlot(i, world);
		}
		for (int i = 1; i <= GameSettings.getDefault().getEquippedSize(); i++) {
			this.sendEquippedSlot(EquipSlots.values()[i], world);
		}
	}

	/**
	 * GetSlot, returns slot i
	 * 
	 */
	public ItemSlot getSlot(int i) throws Exception {
		if (i > 0 && i <= GameSettings.getDefault().getInventorySize()) {
			return this.inventory[i];
		}

		return null;
	}

	// log bad slot id
	/**
	 * SwapSlots, swaps the 2 slots
	 * 
	 */
	public void swapSlots(int id1, int id2, GameWorld world) throws Exception {
		if (id1 <= 0 || id1 > GameSettings.getDefault().getInventorySize()
				|| id2 <= 0
				|| id2 > GameSettings.getDefault().getInventorySize()) {
			return;
		}

		// log id out of inventory range
		ItemSlot slot1 = this.getSlot(id1);
		ItemSlot slot2 = this.getSlot(id2);
		if (slot1 == null && slot2 == null)
			return;

		if (slot1 == null || slot2 == null) {
			this.inventory[id1] = slot2;
			this.inventory[id2] = slot1;
		} else // Same base item and they can stack
		if (slot1.getItem().getTemplateID() == slot2.getItem().getTemplateID()
				&& slot2.canStack(slot1)) {
			slot2.setStack(slot2.getStack() + slot1.getStack());
			slot1.getItem().setDelete(true);
			this.inventory[id1] = null;
		} else {
			this.inventory[id1] = slot2;
			this.inventory[id2] = slot1;
		}
		this.sendSlot(id1, world);
		this.sendSlot(id2, world);
	}

	/**
	 * SplitSlots, adds one item from slot 1 to slot 2
	 * 
	 */
	public void splitSlots(int id1, int id2, GameWorld world) throws Exception {
		if (id1 <= 0 || id1 > GameSettings.getDefault().getInventorySize()
				|| id2 <= 0
				|| id2 > GameSettings.getDefault().getInventorySize()) {
			return;
		}

		// log id out of inventory range
		ItemSlot slot1 = this.getSlot(id1);
		ItemSlot slot2 = this.getSlot(id2);
		if (slot1 == null)
			return;

		ItemSlot temp = new ItemSlot();
		temp.setItem(new Item());
		temp.getItem().loadFromTemplate(slot1.getItem().getTemplate());
		temp.setStack(0);
		if (temp.getItem().getIsBindOnPickup())
			temp.getItem().setIsBound(true);

		if (slot2 == null) {
			world.getItemHandler().addItem(temp.getItem());
			slot2 = temp;
		}

		if (slot2.canStack(slot1)) {
			slot2.setStack(slot2.getStack() + 1);
			this.inventory[id2] = slot2;
			if (slot1.getStack() > 1)
				slot1.setStack(slot1.getStack() - 1);
			else {
				slot1.getItem().setDelete(true);
				this.inventory[id1] = null;
			}
		}

		this.sendSlot(id1, world);
		this.sendSlot(id2, world);
	}

	/**
	 * Use, equip/use item at slot id if possible
	 * 
	 * Note: Assumes slot id is valid
	 * 
	 */
	public void use(int id, GameWorld world) throws Exception {
		ItemSlot slot = this.getSlot(id);
		if (slot == null)
			return;

		if (this.player.canUse(slot.getItem(), world)) {
			if (slot.getItem().getUseType() == Goose.ItemTemplate.UseTypes.Equipment) {
				this.equip(slot.getItem(), world);
			} else if (slot.getItem().getUseType() == Goose.ItemTemplate.UseTypes.Consumable) {
				this.useConsumable(slot.getItem(), world);
			} else if (slot.getItem().getUseType() == Goose.ItemTemplate.UseTypes.Scroll) {
				if (this.player.learnSpell(slot.getItem().getLearnSpellID(),
						world)) {
					this.removeItem(slot.getItem(), 1, world);
				}

			}

		} else if (!this.player.getMap().getCanUseItems()) {
			world.send(this.player, "#You can't use items in this map.");
		}

	}

	/**
	 * Equip, equips item
	 * 
	 * Returns true if item could be successfully equipped
	 * 
	 */
	public boolean equip(Item item, GameWorld world) throws Exception {
		EquipSlots equipslot = this.itemSlotToEquipSlot(item.getSlot());
		if (equipslot == Inventory.EquipSlots.values()[0]) {
			return false;
		}

		ItemSlot equipped = this.getEquippedSlot(equipslot);
		if (equipped == null) {
			// Try to unequip shield if 2handed weapon is tried to be equipped
			if (equipslot == EquipSlots.Weapon
					&& item.getSlot() == Goose.ItemTemplate.ItemSlots.TwoHanded
					&& this.getEquippedSlot(EquipSlots.Shield) != null) {
				// If we can't unequip the shield then we can't equip the
				// 2handed weapon, so return false
				if (!this.unequip(EquipSlots.Shield, world)) {
					return false;
				}

			} else // Try to unequip 2handed weapon if shield is tried to be
					// equipped
			if (equipslot == EquipSlots.Shield
					&& this.getEquippedSlot(EquipSlots.Weapon) != null
					&& this.getEquippedSlot(EquipSlots.Weapon).getItem()
							.getSlot() == Goose.ItemTemplate.ItemSlots.TwoHanded) {
				// If we can't unequip the weapon then we can't equip the
				// shield, so return false
				if (!this.unequip(EquipSlots.Weapon, world)) {
					return false;
				}

			}

		} else {
			// Try to unequip shield if 2handed weapon is tried to be equipped
			if (equipslot == EquipSlots.Weapon
					&& item.getSlot() == Goose.ItemTemplate.ItemSlots.TwoHanded
					&& this.getEquippedSlot(EquipSlots.Shield) != null) {
				// If we can't unequip the shield then we can't equip the
				// 2handed weapon, so return false
				if (!this.unequip(EquipSlots.Shield, world)) {
					return false;
				}

			}

			// Couldn't unequip equipped item, so therefore we can't equip new
			// item.
			// so return false
			if (!this.unequip(equipslot, world)) {
				return false;
			}

		}
		// At this point we have unequipped everything necessary to equip the
		// item, so go and do it
		// Remove 1 of the item from inventory
		ItemSlot slot = this.removeItem(item, 1, world);
		// if slot is null something went wrong
		if (slot == null) {
			return false;
		}

		// log something here
		this.equipped[((Enum) equipslot).ordinal()] = slot;
		this.player.addStats(slot.getItem().getTotalStats(), world);
		if (slot.getItem().getSpellEffect() != null) {
			Buff buff = new Buff();
			buff.setCaster(this.player);
			buff.setTarget(this.player);
			buff.setItemBuff(true);
			buff.setSpellEffect(slot.getItem().getSpellEffect());
			this.player.addBuff(buff, world, true);
		}

		this.sendEquippedSlot(equipslot, world);
		world.send(this.player, this.player.cHPString());
		world.send(this.player, this.player.sNFString());
		world.send(this.player, this.player.wPSString());
		List<Goose.Player> range = this.player.getMap().getPlayersInRange(
				this.player);
		for (Goose.Player p : range) {
			world.send(p, this.player.cHPString());
		}
		if (slot.getItem().getIsBindOnEquip()) {
			slot.getItem().setIsBound(true);
			slot.getItem().setDirty(true);
		}

		return true;
	}

	/**
	 * UseConsumable, use consumable item
	 * 
	 * Potions, teleports, kegs
	 * 
	 */
	public void useConsumable(Item item, GameWorld world) throws Exception {
		for (Buff b : this.player.getBuffs()) {
			if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
				// stunned battletext
				world.send(this.player, "BT" + this.player.getLoginID() + ",50");
				return;
			}

		}
		for (Window window : this.player.getWindows()) {
			if (window.getType() == WindowTypes.Vendor) {
				world.send(this.player,
						"$7You can't use items while with a vendor.");
				return;
			}

		}
		if (world.getRandom().nextInt(100000) + 1 <= item
				.getSpellEffectChance() * 1000) {
			item.getSpellEffect().cast(this.player, this.player, world);
			this.removeItem(item, 1, world);
		}

	}

	/**
	 * RemoveItem, removes number items from inventory
	 * 
	 * Returns ItemSlot with the item and stack
	 * 
	 */
	public ItemSlot removeItem(Item item, long number, GameWorld world)
			throws Exception {
		ItemSlot slot;
		for (int i = 1; i <= GameSettings.getDefault().getInventorySize(); i++) {
			slot = this.inventory[i];
			if (slot == null)
				continue;

			if (slot.getItem() != item)
				continue;

			// Return null since Item objects are unique, so the item has to be
			// this one
			// But the stack isn't big enough so something is wrong here
			if (slot.getStack() < number)
				return null;

			if (slot.getStack() == number) {
				this.inventory[i] = null;
				this.sendSlot(i, world);
				return slot;
			} else {
				slot.setStack(slot.getStack() - number);
				this.sendSlot(i, world);
				ItemSlot removed = new ItemSlot();
				removed.setItem(new Item());
				removed.getItem().loadFromTemplate(item.getTemplate());
				removed.setStack(number);
				return removed;
			}
		}
		return null;
	}

	/**
	 * Unequip, unequips equipped item at equip slot
	 * 
	 * Returns true if item could successfully be unequipped
	 * 
	 */
	public boolean unequip(EquipSlots equipslot, GameWorld world)
			throws Exception {
		ItemSlot slot = this.getEquippedSlot(equipslot);
		// maybe log something bad, i don't think this should happen
		if (slot == null)
			return true;

		if (!this.addItem(slot.getItem(), slot.getStack(), world))
			return false;

		this.equipped[((Enum) equipslot).ordinal()] = null;
		this.player.removeStats(slot.getItem().getTotalStats(), world);
		if (slot.getItem().getSpellEffect() != null) {
			Buff remove = null;
			for (Buff buff : this.player.getBuffs()) {
				if (buff.getItemBuff()
						&& buff.getSpellEffect() == slot.getItem()
								.getSpellEffect()) {
					remove = buff;
					break;
				}

			}
			if (remove != null) {
				this.player.removeBuff(remove, world);
			} else {
			}
		}

		// log bad buff
		this.sendEquippedSlot(equipslot, world);
		world.send(this.player, this.player.cHPString());
		world.send(this.player, this.player.sNFString());
		List<Goose.Player> range = this.player.getMap().getPlayersInRange(
				this.player);
		for (Goose.Player p : range) {
			world.send(p, this.player.cHPString());
		}
		return true;
	}

	/**
	 * Unequip, unequips equipped item at slot id
	 * 
	 * Note: Assumes slot id is valid
	 * 
	 * Returns true if item could successfully be unequipped
	 * 
	 */
	public boolean unequip(int id, GameWorld world) throws Exception {
		// id is inv size + id + 1, so get rid of inv + 1
		id -= GameSettings.getDefault().getInventorySize();
		id -= 1;
		return this.unequip(EquipSlots.values()[id], world);
	}

	/**
	 * ItemSlotToEquipSlot, returns the slot id for equipment
	 * 
	 * Note: ItemSlot refers to the ItemTemplate.ItemSlots enum, not the
	 * ItemSlot class, should probably name it better but it'll do
	 * 
	 */
	public EquipSlots itemSlotToEquipSlot(Goose.ItemTemplate.ItemSlots slot)
			throws Exception {
		switch (slot) {
			case Belt:
				return EquipSlots.Belt;
			case Chest:
				return EquipSlots.Chest;
			case Cloak:
				return EquipSlots.Cloak;
			case Gloves:
				return EquipSlots.Gloves;
			case Helmet:
				return EquipSlots.Head;
			case Necklace:
				return EquipSlots.Necklace;
			case OneHanded:
				return EquipSlots.Weapon;
			case Pants:
				return EquipSlots.Legs;
			case Pauldrons:
				return EquipSlots.Pauldrons;
			case Ring:
				if (this.getEquippedSlot(EquipSlots.Ring2) == null)
					return EquipSlots.Ring2;
				else
					return EquipSlots.Ring1;
			case Shield:
				return EquipSlots.Shield;
			case Shoes:
				return EquipSlots.Feet;
			case TwoHanded:
				return EquipSlots.Weapon;
			default:
				return EquipSlots.__dummyEnum__0;

		}
	}

	/**
	 * GetEquippedSlot, returns the equipped item at the specified slot
	 * 
	 */
	public ItemSlot getEquippedSlot(EquipSlots slot) throws Exception {
		return this.equipped[((Enum) slot).ordinal()];
	}

	/**
	 * GetEquippedSlot, returns equipped slot i
	 * 
	 * Note: inventory size is subtracted from i to get it into equipped array
	 * range since i as sent from the client is inventorysize + i
	 * 
	 */
	public ItemSlot getEquippedSlot(int i) throws Exception {
		if (i > GameSettings.getDefault().getInventorySize()
				&& i <= GameSettings.getDefault().getInventorySize()
						+ GameSettings.getDefault().getEquippedSize() + 1) {
			return this.equipped[i
					- GameSettings.getDefault().getInventorySize() - 1];
		}

		return null;
	}

	// log bad slot id
	/**
	 * EquippedDisplay, returns equipped items display for use in MKC and CHP
	 * 
	 * Note: keeps extra , on end
	 * 
	 */
	public String equippedDisplay() throws Exception {
		String e = "";
		EquipSlots[] slots = new EquipSlots[] { EquipSlots.Chest,
				EquipSlots.Head, EquipSlots.Legs, EquipSlots.Feet,
				EquipSlots.Shield, EquipSlots.Weapon };
		ItemSlot item;
		for (EquipSlots eq : slots) {
			item = this.getEquippedSlot(eq);
			if (item != null) {
				if (item.getItem().getGraphicA() == 0) {
					e += item.getItem().getGraphicEquipped() + ",*,";
				} else {
					e += item.getItem().getGraphicEquipped() + ","
							+ item.getItem().getGraphicR() + ","
							+ item.getItem().getGraphicG() + ","
							+ item.getItem().getGraphicB() + ","
							+ item.getItem().getGraphicA() + ",";
				}
			} else {
				e += "0,*,";
			}
		}
		return e;
	}

	/**
	 * SendEquippedSlot, sends info about equipped slot to player
	 * 
	 */
	public void sendEquippedSlot(EquipSlots equipslot, GameWorld world)
			throws Exception {
		if (this.player.getState().compareTo(Goose.Player.States.LoadingMap) >= 0) {
			ItemSlot slot = this.equipped[((Enum) equipslot).ordinal()];
			if (slot != null) {
				world.send(
						this.player,
						"WNF11," + ((Enum) equipslot).ordinal() + ","
								+ slot.getItem().getName() + "|"
								+ slot.getStack() + "|"
								+ slot.getItem().getItemID() + "|"
								+ slot.getItem().getGraphicTile() + "|"
								+ slot.getItem().getGraphicR() + "|"
								+ slot.getItem().getGraphicG() + "|"
								+ slot.getItem().getGraphicB() + "|"
								+ slot.getItem().getGraphicA());
			} else {
				world.send(this.player, "WNF11," + ((Enum) equipslot).ordinal()
						+ ", |0|0|0|*");
			}
		}

	}

	/**
	 * HasItem, returns true if inventory has templateid somewhere
	 * 
	 */
	public boolean hasItem(int templateid) throws Exception {
		for (ItemSlot slot : this.inventory) {
			if (slot != null
					&& slot.getItem().getTemplate().getID() == templateid)
				return true;

		}
		for (ItemSlot slot : this.equipped) {
			if (slot != null
					&& slot.getItem().getTemplate().getID() == templateid)
				return true;

		}
		for (ItemSlot slot : this.combine) {
			if (slot != null
					&& slot.getItem().getTemplate().getID() == templateid)
				return true;

		}
		return false;
	}

	/**
	 * GetWeaponDamage, returns currently equipped weapons damage
	 * 
	 * Or 1 if no weapon
	 * 
	 */
	public int getWeaponDamage() throws Exception {
		ItemSlot weapon = this.getEquippedSlot(EquipSlots.Weapon);
		if (weapon == null)
			return 1;

		return weapon.getItem().getWeaponDamage();
	}

	/**
	 * GetWeaponDelay, returns currently equipped weapons delay
	 * 
	 * Or 10 if no weapon
	 * 
	 */
	public int getWeaponDelay() throws Exception {
		ItemSlot weapon = this.getEquippedSlot(EquipSlots.Weapon);
		if (weapon == null)
			return 10;

		return weapon.getItem().getWeaponDelay();
	}

	/**
	 * Save, saves to database
	 * 
	 * Uses delete/insert so if the row doesn't exist it doesn't die.. maybe
	 * research into this more
	 * 
	 */
	public void save(GameWorld world) throws Exception {
		String query;
		ItemSlot slot;
		for (int i = 1; i <= GameSettings.getDefault().getInventorySize(); i++) {
			// Save inventory
			slot = this.getSlot(i);
			if (slot == null) {
				query = "DELETE FROM inventory WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i;
			} else {
				if (slot.getItem().getUnsaved())
					slot.getItem().addItem(world);

				// have to add item
				query = "DELETE FROM inventory WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i + ";";
				world.getSqlConnection().createStatement().executeUpdate(query);
				
				query = "INSERT INTO inventory (player_id, slot, item_id, stack) VALUES ("
						+ this.player.getPlayerID()
						+ ", "
						+ i
						+ ", "
						+ slot.getItem().getItemID()
						+ ", "
						+ slot.getStack()
						+ ");";
			}
			world.getSqlConnection().createStatement().executeUpdate(query);
		}
		for (int i = 1; i <= GameSettings.getDefault().getEquippedSize(); i++) {
			// command = new SqlCommand(query, world.SqlConnection);
			// command.BeginExecuteNonQuery(new
			// AsyncCallback(GameWorld.DefaultEndExecuteNonQueryAsyncCallback),
			// command);
			// Save equipped
			slot = this.equipped[i];
			if (slot == null) {
				query = "DELETE FROM equipped WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i;
			} else {
				if (slot.getItem().getUnsaved())
					slot.getItem().addItem(world);

				// have to add item
				query = "DELETE FROM equipped WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i + ";";
				world.getSqlConnection().createStatement().executeUpdate(query);
				query = "INSERT INTO equipped (player_id, slot, item_id) VALUES ("
						+ this.player.getPlayerID()
						+ ", "
						+ i
						+ ", "
						+ slot.getItem().getItemID() + ");";
			}
			world.getSqlConnection().createStatement().executeUpdate(query);
		}
		for (int i = 1; i <= GameSettings.getDefault().getCombineBagSize(); i++) {
			// Save combine bag
			slot = this.combine[i];
			if (slot == null) {
				query = "DELETE FROM combinebag WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i;
			} else {
				if (slot.getItem().getUnsaved())
					slot.getItem().addItem(world);

				// have to add item
				query = "DELETE FROM combinebag WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i + ";";
				world.getSqlConnection().createStatement().executeUpdate(query);
				query = "INSERT INTO combinebag (player_id, slot, item_id, stack) VALUES ("
						+ this.player.getPlayerID()
						+ ", "
						+ i
						+ ", "
						+ slot.getItem().getItemID()
						+ ", "
						+ slot.getStack()
						+ ");";
			}
			world.getSqlConnection().createStatement().executeUpdate(query);
		}
	}

	/**
	 * Load, loads from database
	 * 
	 */
	public void load(GameWorld world) throws Exception {
		String query;
		int slot;
		int stack;
		int itemid;
		Item item;
		// Load inventory
		query = "SELECT * FROM inventory WHERE player_id="
				+ this.player.getPlayerID();
		ResultSet resultSet = world.getSqlConnection().createStatement()
				.executeQuery(query);
		while (resultSet.next()) {
			slot = resultSet.getInt("slot");
			stack = resultSet.getInt("stack");
			itemid = resultSet.getInt("item_id");
			if (slot < 1 || slot > GameSettings.getDefault().getInventorySize())
				continue;

			// log bad slot
			if (stack < 1)
				continue;

			// log bad stack
			item = world.getItemHandler().getItem(itemid);
			if (item == null)
				continue;

			// log bad item id
			if (this.inventory[slot] != null)
				continue;

			// log 2 items trying to be in the same slot
			this.inventory[slot] = new ItemSlot();
			this.inventory[slot].setItem(item);
			this.inventory[slot].setStack(stack);
		}
		resultSet.close();
		// Load equipped
		query = "SELECT * FROM equipped WHERE player_id="
				+ this.player.getPlayerID();
		resultSet = world.getSqlConnection().createStatement()
				.executeQuery(query);
		while (resultSet.next()) {
			slot = resultSet.getInt("slot");
			itemid = resultSet.getInt("item_id");
			if (slot < 1 || slot > GameSettings.getDefault().getEquippedSize())
				continue;

			// log bad slot
			item = world.getItemHandler().getItem(itemid);
			if (item == null)
				continue;

			// log bad item id
			if (this.equipped[slot] != null)
				continue;

			// log 2 items trying to be in the same slot
			// if the database slot is a ring and the item's slot isn't,
			// or the item's slot isn't the same as the database slot
			// should probably check for conflicting equipment, eg a shield + 2
			// handed sword won't work
			if (EquipSlots.values()[slot] == EquipSlots.Ring1
					|| EquipSlots.values()[slot] == EquipSlots.Ring2) {
				if (item.getSlot() != Goose.ItemTemplate.ItemSlots.Ring)
					continue;

			} else // log bad equipment in slot
			if (this.itemSlotToEquipSlot(item.getSlot()) != EquipSlots.values()[slot]) {
				continue;
			}

			// log bad equipment in slot
			this.equipped[slot] = new ItemSlot();
			this.equipped[slot].setItem(item);
			this.equipped[slot].setStack(1);
			this.player.addStats(this.equipped[slot].getItem().getTotalStats(),
					world);
			if (this.equipped[slot].getItem().getSpellEffect() != null) {
				Buff buff = new Buff();
				buff.setCaster(this.player);
				buff.setTarget(this.player);
				buff.setItemBuff(true);
				buff.setSpellEffect(this.equipped[slot].getItem()
						.getSpellEffect());
				this.player.addBuff(buff, world, false);
			}

		}
		resultSet.close();
		// Load combine bag
		query = "SELECT * FROM combinebag WHERE player_id="
				+ this.player.getPlayerID();
		resultSet = world.getSqlConnection().createStatement()
				.executeQuery(query);
		while (resultSet.next()) {
			slot = resultSet.getInt("slot");
			stack = resultSet.getInt("stack");
			itemid = resultSet.getInt("item_id");
			if (slot < 1
					|| slot > GameSettings.getDefault().getCombineBagSize())
				continue;

			// log bad slot
			if (stack < 1)
				continue;

			// log bad stack
			item = world.getItemHandler().getItem(itemid);
			if (item == null)
				continue;

			// log bad item id
			if (this.combine[slot] != null)
				continue;

			// log 2 items trying to be in the same slot
			this.combine[slot] = new ItemSlot();
			this.combine[slot].setItem(item);
			this.combine[slot].setStack(stack);
		}
		resultSet.close();
	}

	/**
	 * ShowStatsWindow, shows stats window if player has the itemid
	 * 
	 */
	public void showStatsWindow(int itemid, GameWorld world) throws Exception {
		for (ItemSlot slot : this.equipped) {
			if (slot != null && slot.getItem().getItemID() == itemid) {
				this.player.showStatsWindow(itemid, world);
				return;
			}

		}
		for (ItemSlot slot : this.inventory) {
			if (slot != null && slot.getItem().getItemID() == itemid) {
				this.player.showStatsWindow(itemid, world);
				return;
			}

		}
	}

	/**
	 * Combine, combines whatever is in combine bag if possible
	 * 
	 */
	public void combine(GameWorld world) throws Exception {
        HashMap<Integer,Integer> combineHash = new HashMap<Integer,Integer>();
        for (ItemSlot slot : this.combine)
        {
            if (slot == null)
                continue;
             
            if (!combineHash.containsKey(slot.getItem().getTemplateID()))
            {
                combineHash.put(slot.getItem().getTemplateID(), 1);
            }
            else
            {
                combineHash.put(slot.getItem().getTemplateID(), combineHash.get(slot.getItem().getTemplateID()) + 1);
            } 
        }
        Combination match = world.getCombinationHandler().getMatch(combineHash);
        if (match == null)
        {
            world.send(this.player,"$7Couldn't combine items.");
            return ;
        }
         
        if (match.getMinLevel() > this.player.getLevel())
        {
            world.send(this.player,"$7You need to be level " + match.getMinLevel() + " to create " + match.getName() + ".");
            return ;
        }
        else if (match.getMaxLevel() > 0 && match.getMaxLevel() < this.player.getLevel())
        {
            world.send(this.player,"$7You need to be less than level " + match.getMaxLevel() + " to create " + match.getName() + ".");
            return ;
        }
        else if (match.getMinExperience() > this.player.getExperience() + this.player.getExperienceSold())
        {
            world.send(this.player,"$7You need " + match.getMinExperience() + " experience to create " + match.getName() + ".");
            return ;
        }
        else if (match.getMaxExperience() > 0 && match.getMaxExperience() < this.player.getExperience() + this.player.getExperienceSold())
        {
            world.send(this.player,"$7You need less than " + match.getMaxExperience() + " experience to create " + match.getName() + ".");
            return ;
        }
        else if ((match.getClassRestrictions() & (long)Math.round(Math.pow(2.0,(double)this.player.getClas().getClassID()))) != 0)
        {
            world.send(this.player,"$7You are the wrong class to create " + match.getName() + ".");
            return ;
        }
             
        List<Integer> freeslots = new ArrayList<Integer>();
        ItemSlot[] newcombine = new ItemSlot[GameSettings.getDefault().getCombineBagSize() + 1];
        HashMap<Integer,Integer> reqhash = new HashMap<Integer,Integer>();
        for (Object __dummyForeachVar12 : match.getRequiredHash().entrySet())
        {
            Entry<Integer,Integer> req = (Entry<Integer,Integer>)__dummyForeachVar12;
            reqhash.put(req.getKey(), req.getValue());
        }
        Item item;
        long count;
        long slotcount;
        for (int i = 1;i < this.combine.length;i++)
        {
            if (this.combine[i] == null)
            {
                freeslots.add(i);
                continue;
            }
             
            item = this.combine[i].getItem();
            slotcount = this.combine[i].getStack();
            // if this item is in the combination and it still requires more
            if (reqhash.containsKey(item.getTemplateID()))
                count = reqhash.get(item.getTemplateID());
            else
                count = 0; 
            if (count > 0)
            {
                // lower required by how many we have, don't care if it's negative
                // since the check above catches it
                reqhash.put(item.getTemplateID(), (int)(count - slotcount));
                // lower the amount in the stack/slot by how many we actually needed
                slotcount -= count;
            }
             
            // if we still have some left over, add it back to combine bag
            if (slotcount > 0)
            {
                newcombine[i] = new ItemSlot();
                newcombine[i].setItem(item);
                newcombine[i].setStack(slotcount);
            }
            else
            {
                freeslots.add(i);
                // else mark the item for deletion
                item.setDelete(true);
            } 
        }
        if (freeslots.size() < match.getResultItems().size())
        {
            world.send(this.player,"$7Not enough free slots to create " + match.getName() + ".");
            return ;
        }
         
        int index;
        for (ItemTemplate template : match.getResultItems())
        {
            if (template.getIsLore() && this.hasItem(template.getID()))
            {
                world.send(this.player,"$7Already have LORE item " + template.getName() + ".");
                return ;
            }
             
            item = new Item();
            item.loadFromTemplate(template);
            world.getItemHandler().addItem(item);
            if (item.getIsBindOnPickup())
                item.setIsBound(true);
             
            index = freeslots.get(0);
            freeslots.remove(0);
            newcombine[index] = new ItemSlot();
            newcombine[index].setItem(item);
            newcombine[index].setStack(1);
            world.send(this.player,"$7Successfully created " + template.getName() + ".");
        }
        this.combine = newcombine;
        this.sendCombineBag(world);
    }

	/**
	 * SendCombineSlot, sends combine bag slot i to player
	 * 
	 * kinda inefficient since it searches for combine bag window id every
	 * time..
	 * 
	 */
	public void sendCombineSlot(int i, GameWorld world) throws Exception {
		if (this.player.getState().compareTo(Goose.Player.States.LoadingMap) >= 0) {
			for (Window window : this.player.getWindows()) {
				if (window.getType() != WindowTypes.CombineBag)
					continue;

				ItemSlot slot = this.combine[i];
				if (slot != null) {
					world.send(
							this.player,
							"WNF" + window.getID() + "," + i + ","
									+ slot.getItem().getName() + "|"
									+ slot.getStack() + "|"
									+ slot.getItem().getItemID() + "|"
									+ slot.getItem().getGraphicTile() + "|"
									+ slot.getItem().getGraphicR() + "|"
									+ slot.getItem().getGraphicG() + "|"
									+ slot.getItem().getGraphicB() + "|"
									+ slot.getItem().getGraphicA());
				} else {
					world.send(this.player, "WNF" + window.getID() + "," + i
							+ ", |0|0|0|*");
				}
			}
		}

	}

	/**
	 * SendCombineBag, sends combine bag items to player
	 * 
	 */
	public void sendCombineBag(GameWorld world) throws Exception {
		for (int i = 1; i <= GameSettings.getDefault().getCombineBagSize(); i++) {
			this.sendCombineSlot(i, world);
		}
	}

	/***
	 * SwapInventoryCombineSlots, swaps inventory slot with combine slot
	 * 
	 * Assumes values are valid
	 * 
	 */
	public void swapInventoryCombineSlots(int invslot, int combslot,
			GameWorld world) throws Exception {
		ItemSlot inventoryslot = this.inventory[invslot];
		ItemSlot combineslot = this.combine[combslot];
		this.combine[combslot] = inventoryslot;
		this.inventory[invslot] = combineslot;
		this.sendSlot(invslot, world);
		this.sendCombineSlot(combslot, world);
	}

}
