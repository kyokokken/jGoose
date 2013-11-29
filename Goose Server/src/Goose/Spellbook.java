package Goose;

import java.sql.ResultSet;

import Goose.GameSettings;
import Goose.Spell;
import Goose.Spell.SpellTargets;

/**
 * Holds the spells a player knows
 * 
 */
public class Spellbook {
	Spell[] spells;
	/**
	 * Lastcast holds when each spell was last cast
	 */
	long[] lastcast;
	Goose.Player player;

	public Spellbook(Goose.Player player) throws Exception {
		this.spells = new Spell[GameSettings.getDefault().getSpellbookSize() + 1];
		this.lastcast = new long[GameSettings.getDefault().getSpellbookSize() + 1];
		this.player = player;
	}

	/**
	 * Load, loads spells for player from database
	 * 
	 */
	public void load(GameWorld world) throws Exception {
		ResultSet resultSet = world.getSqlConnection()
				.createStatement()
				.executeQuery(
						"SELECT * FROM spellbook WHERE player_id="
								+ this.player.getPlayerID());
		int slot, spellid;
		while (resultSet.next()) {
			spellid = resultSet.getInt("spell_id");
			slot = resultSet.getInt("slot");
			if (slot < 1 || slot > GameSettings.getDefault().getSpellbookSize()) {
				continue;
			}

			// log bad spellbook loading
			this.spells[slot] = world.getSpellHandler().getSpell(spellid);
			if (this.spells[slot] == null) {
				continue;
			}

			// log bad spell loading
			this.lastcast[slot] = resultSet.getLong("last_casted");
		}
		resultSet.close();
	}

	/**
	 * Save, saves spells for player into database
	 * 
	 */
	public void save(GameWorld world) throws Exception {
		String query;
		Spell slot;
		for (int i = 1; i <= GameSettings.getDefault().getSpellbookSize(); i++) {
			// Save spells
			slot = this.getSlot(i);
			if (slot == null) {
				query = "DELETE FROM spellbook WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i;
			} else {
				query = "DELETE FROM spellbook WHERE player_id="
						+ this.player.getPlayerID() + " AND slot=" + i + ";";
				world.getSqlConnection().createStatement().executeUpdate(query);
				query = "INSERT INTO spellbook (player_id, slot, spell_id, last_casted) VALUES ("
						+ this.player.getPlayerID()
						+ ", "
						+ i
						+ ", "
						+ slot.getID() + ", " + this.getSlotLastCast(i) + ");";
			}
			world.getSqlConnection().createStatement().executeUpdate(query);
			if (slot != null) {
//				System.out.println("SpellID: " + slot.getID() + " - "
//						+ executeUpdate);
			}

		}
	}

	/**
	 * SendSlot, sends spellbook slot to player
	 * 
	 */
	public void sendSlot(int slot, GameWorld world) throws Exception {
		if (slot < 1 || slot > GameSettings.getDefault().getSpellbookSize()) {
			return;
		}

		// log bad spell slot
		Spell spell = this.spells[slot];
		if (spell == null) {
			world.send(this.player, "SSS" + slot);
		} else {
			world.send(this.player, "SSS" + slot + "," + spell.getName()
					+ ",0,0,"
					+ (spell.getTarget() == SpellTargets.Target ? "T" : "X")
					+ "," + spell.getGraphic());
		}
	}

	/**
	 * SendAll, sends all spell slots to player
	 * 
	 */
	public void sendAll(GameWorld world) throws Exception {
		for (int i = 1; i <= GameSettings.getDefault().getSpellbookSize(); i++) {
			this.sendSlot(i, world);
		}
	}

	/**
	 * GetSlot, returns spell at slot
	 * 
	 */
	public Spell getSlot(int slot) throws Exception {
		return this.spells[slot];
	}

	/**
	 * GetSlotLastCast, returns spell last cast at slot
	 * 
	 */
	public long getSlotLastCast(int slot) throws Exception {
		return this.lastcast[slot];
	}

	/**
	 * SetSlotLastCast, sets spell last cast at slot
	 * 
	 */
	public void setSlotLastCast(int slot, long last) throws Exception {
		this.lastcast[slot] = last;
	}

	/**
	 * LearnSpell, learns spell if possible
	 * 
	 */
	public boolean learnSpell(int spellid, GameWorld world) throws Exception {
		Spell spell = world.getSpellHandler().getSpell(spellid);
		if (spell == null) {
			return false;
		}

		return this.addSpell(spell, world);
	}

	// log bad spell
	/**
	 * AddSpell, Adds spell if possible
	 * 
	 */
	public boolean addSpell(Spell spell, GameWorld world) throws Exception {
		for (Spell s : this.spells) {
			// first pass to check if player knows spell
			if (s == spell) {
				return false;
			}

		}
		for (int i = 1; i <= GameSettings.getDefault().getSpellbookSize(); i++) {
			// second pass to check if empty slot to add
			if (this.spells[i] == null) {
				this.spells[i] = spell;
				this.lastcast[i] = 0;
				this.sendSlot(i, world);
				world.send(this.player, "$7You have learned " + spell.getName()
						+ ".");
				return true;
			}

		}
		return false;
	}

	/**
	 * RemoveSpell, removes spell at slot
	 * 
	 */
	public boolean removeSpell(int slot, GameWorld world) throws Exception {
		if (slot <= 0 || slot > GameSettings.getDefault().getSpellbookSize())
			return false;

		if (this.spells[slot] != null) {
			world.send(this.player,
					"$7You have unlearned " + this.spells[slot].getName() + ".");
			this.spells[slot] = null;
			this.lastcast[slot] = 0;
			this.sendSlot(slot, world);
			return true;
		}

		return false;
	}

	/**
	 * SwapSlots, swaps two slots in spellbook
	 * 
	 */
	public void swapSlots(int slot1, int slot2, GameWorld world)
			throws Exception {
		if (slot1 <= 0 || slot1 > GameSettings.getDefault().getSpellbookSize()
				|| slot2 <= 0
				|| slot2 > GameSettings.getDefault().getSpellbookSize()) {
			return;
		}

		Spell spell1 = this.spells[slot1];
		long aether1 = this.lastcast[slot1];
		Spell spell2 = this.spells[slot2];
		long aether2 = this.lastcast[slot2];
		this.spells[slot1] = spell2;
		this.spells[slot2] = spell1;
		this.lastcast[slot1] = aether2;
		this.lastcast[slot2] = aether1;
		this.sendSlot(slot1, world);
		this.sendSlot(slot2, world);
	}

	public void removeNonClassSpells(GameWorld world) throws Exception {
		Spell slot;
		for (int i = 1; i <= GameSettings.getDefault().getSpellbookSize(); i++) {
			slot = this.getSlot(i);
			if (slot == null)
				continue;

			if ((slot.getClassRestrictions() & (long) Math.round(Math.pow(2.0,
					(double) this.player.getClas().getClassID()))) != 0) {
				this.spells[i] = null;
				this.lastcast[i] = 0;
				this.sendSlot(i, world);
			}

		}
	}

}
