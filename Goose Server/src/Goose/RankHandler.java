package Goose;

import Goose.Ranks;
import Goose.Ranks.RankTypes;

/**
 * RankHandler, handles caching of ranks
 * 
 * 
 */
public class RankHandler {
	private Ranks __Magus;

	public Ranks getMagus() {
		return __Magus;
	}

	public void setMagus(Ranks value) {
		__Magus = value;
	}

	private Ranks __Warrior;

	public Ranks getWarrior() {
		return __Warrior;
	}

	public void setWarrior(Ranks value) {
		__Warrior = value;
	}

	private Ranks __Rogue;

	public Ranks getRogue() {
		return __Rogue;
	}

	public void setRogue(Ranks value) {
		__Rogue = value;
	}

	private Ranks __Priest;

	public Ranks getPriest() {
		return __Priest;
	}

	public void setPriest(Ranks value) {
		__Priest = value;
	}

	private Ranks __All;

	public Ranks getAll() {
		return __All;
	}

	public void setAll(Ranks value) {
		__All = value;
	}

	private Ranks __Gold;

	public Ranks getGold() {
		return __Gold;
	}

	public void setGold(Ranks value) {
		__Gold = value;
	}

	public RankHandler() throws Exception {
		this.setMagus(new Ranks(RankTypes.Magus));
		this.setWarrior(new Ranks(RankTypes.Warrior));
		this.setRogue(new Ranks(RankTypes.Rogue));
		this.setPriest(new Ranks(RankTypes.Priest));
		this.setAll(new Ranks(RankTypes.All));
		this.setGold(new Ranks(RankTypes.Gold));
	}

	public void updateAll(GameWorld world) throws Exception {
		this.getMagus().update(world);
		this.getWarrior().update(world);
		this.getRogue().update(world);
		this.getPriest().update(world);
		this.getAll().update(world);
		this.getGold().update(world);
	}

}
