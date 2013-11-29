package Goose;

import Goose.Combination;
import Goose.GameSettings;
import Goose.ItemTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

public class CombinationHandler {
	Hashtable<Integer, Combination> combinations;

	/**
	 * Constructor
	 * 
	 */
	public CombinationHandler() throws Exception {
		this.combinations = new Hashtable<Integer, Combination>();
	}

	/**
	 * LoadCombinations, loads all combinations from the database
	 * 
	 */
	public void loadCombinations(GameWorld world) throws Exception {
		ResultSet resultSet = world.getSqlConnection().createStatement()
				.executeQuery("SELECT * FROM combinations");
		while (resultSet.next()) {
			Combination comb = new Combination();
			comb.setID(resultSet.getInt("combination_id"));
			comb.setName(resultSet.getString("combination_name"));
			comb.setMinLevel(resultSet.getInt("min_level"));
			comb.setMaxLevel(resultSet.getInt("max_level"));
			comb.setMinExperience(resultSet.getLong("min_experience"));
			comb.setMaxExperience(resultSet.getLong("max_experience"));
			comb.setClassRestrictions(resultSet.getInt("class_restrictions"));
			this.combinations.put(comb.getID(), comb);
		}
		resultSet.close();
		int itemid;
		ItemTemplate template;
		int count;
		for (Combination comb : this.combinations.values()) {
			// Load required items
			comb.setRequiredHash(new HashMap<Integer, Integer>());
			resultSet = world.getSqlConnection()
					.createStatement()
					.executeQuery(
							"SELECT item_template_id FROM combination_item_required "
									+ "WHERE combination_id=" + comb.getID());
			count = 0;
			while (resultSet.next()) {
				count++;
				if (count > GameSettings.getDefault().getCombineBagSize()) {
					throw new Exception(
							"number of required items exceeds combine bag size. In combination "
									+ comb.getName());
				}

				itemid = resultSet.getInt("item_template_id");
				template = world.getItemHandler().getTemplate(itemid);
				if (template == null) {
					throw new Exception("required Item ID " + itemid
							+ " doesn't exist. In combination "
							+ comb.getName());
				}

				if (!comb.getRequiredHash().containsKey(itemid)) {
					comb.getRequiredHash().put(itemid, 1);
				} else {
					comb.getRequiredHash().put(itemid,
							((int) (comb.getRequiredHash().get(itemid))) + 1);
				}
			}
			resultSet.close();
			// Load resulting items
			comb.setResultItems(new ArrayList<ItemTemplate>());
			resultSet = world
					.getSqlConnection()
					.createStatement()
					.executeQuery(
							"SELECT item_template_id FROM combination_item_results "
									+ "WHERE combination_id=" + comb.getID());

			count = 0;
			while (resultSet.next()) {
				count++;
				if (count > GameSettings.getDefault().getCombineBagSize()) {
					throw new Exception(
							"number of result items exceeds combine bag size. In combination "
									+ comb.getName());
				}

				itemid = resultSet.getInt("item_template_id");
				template = world.getItemHandler().getTemplate(itemid);
				if (template == null) {
					throw new Exception("result Item ID " + itemid
							+ " doesn't exist. In combination "
							+ comb.getName());
				}

				comb.getResultItems().add(template);
			}
			resultSet.close();
		}
	}

	/**
	 * Count, returns the number of combinations
	 * 
	 */
	public int getCount() throws Exception {
		return this.combinations.keySet().size();
	}

	/**
	 * GetMatch, takes a hashtable and tries to match the ingredients with an
	 * existing combination
	 * 
	 * Returns the combination found, or null if none
	 * 
	 */
	public Combination getMatch(HashMap<Integer, Integer> combine)
			throws Exception {
		int c;
		boolean matched;
		for (Object __dummyForeachVar2 : this.combinations.values()) {
			Combination comb = (Combination) __dummyForeachVar2;
			matched = true;
			for (Entry<Integer, Integer> req : comb.getRequiredHash().entrySet()) {
				if (combine.containsKey(req.getKey()))
					c = combine.get(req.getKey());
				else
					c = 0;
				if (c <= 0 || req.getValue() < c) {
					matched = false;
					break;
				}

			}
			if (matched)
				return comb;

		}
		return null;
	}

}
