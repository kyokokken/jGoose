package Goose;

import java.util.ArrayList;
import java.util.List;

import Goose.GameSettings;

/**
 * Ranks, holds a list of strings which contain rank info
 * 
 */
public class Ranks {
	public enum RankTypes {
		All, Rogue, Warrior, Priest, Magus, Gold
	}

	private RankTypes __Type = RankTypes.All;

	public RankTypes getType() {
		return __Type;
	}

	public void setType(RankTypes value) {
		__Type = value;
	}

	List<String> ranks;
	long lastUpdated;

	/**
	 * Constructor
	 */
	public Ranks(RankTypes type) throws Exception {
		this.ranks = new ArrayList<String>();
		this.lastUpdated = 0;
		this.setType(type);
	}

	/**
	 * GetRanks, returns the list of ranks
	 * 
	 * If the information is out of date then it updates first
	 * 
	 */
	public List<String> getRanks(GameWorld world) throws Exception {
		if ((world.getTimeNow() - this.lastUpdated) * world.getTimerFrequency() > GameSettings
				.getDefault().getRankUpdatePeriod()) {
			this.update(world);
		}

		return this.ranks;
	}

	/**
	 * Update, updates ranking information
	 * 
	 */
	public void update(GameWorld world) throws Exception {
		this.ranks = new ArrayList<String>();
		List<Goose.Player> result = new ArrayList<>();
		switch (this.getType()) {
			case All:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10) {
						result.add(p);
					}
				}
				break;
			case Gold:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10) {
						result.add(p);
					}
				}
				break;
			case Magus:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10 && p.getClassID() == 4) {
						result.add(p);
					}
				}
				break;
			case Priest:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10 && p.getClassID() == 5) {
						result.add(p);
					}
				}
				break;
			case Rogue:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10 && p.getClassID() == 2) {
						result.add(p);
					}
				}
				break;
			case Warrior:
				for (Player p : world.getPlayerHandler().getAllPlayerData()) {
					if (result.size() < 10 && p.getClassID() == 3) {
						result.add(p);
					}
				}
				break;

		}
		String line = "";
		int i = 1;
		for (Goose.Player player : result) {
			switch (this.getType()) {
				case Magus:
				case Priest:
				case Rogue:
				case Warrior:
					line = i
							+ ". "
							+ player.getName()
							+ ", "
							+ (player.getExperience() + player
									.getExperienceSold()) + " xp";
					break;
				case All:
					line = i
							+ ". "
							+ player.getName()
							+ ", "
							+ player.getClas().getClassName()
							+ ", "
							+ (player.getExperience() + player
									.getExperienceSold()) + " xp";
					break;
				case Gold:
					line = i + ". " + player.getName() + ", "
							+ player.getGold() + " gp";
					break;

			}
			i++;
			this.ranks.add(line);
		}
		while (i <= GameSettings.getDefault().getNumberOfRanks()) {
			this.ranks.add(i + ". ");
			i++;
		}
		this.lastUpdated = world.getTimeNow();
	}

}
