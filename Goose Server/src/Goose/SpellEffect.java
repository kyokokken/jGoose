package Goose;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import Goose.Pet.Modes;

/**
 * SpellEffect, holds information for spell effects
 * 
 */
public class SpellEffect {
	public enum SpellDisplays {
		/**
		 * Character only shows the animation if there is a character there Tile
		 * shows animation all the time.
		 * 
		 */
		Character, Tile
	}

	public enum TargetTypes {
		/**
		 * Target types, what squares to hit
		 */
		Target, LineFront, Cross, Plus, Random, Area, TriangleFront
	}

	public enum SpellEffected {
		/**
		 * Who is effected by the spell, bitfield
		 */
		__dummyEnum__0, Self, NPC, __dummyEnum__1, Player, SelfNPC, __dummyEnum__2, SelfNPCPlayer, SelfPlayer, NPCPlayer
	}

	public enum EffectTypes {
		/**
		 * Spell Types
		 * 
		 * Formula = only use formulas Buff, temporarily increase stats/change
		 * body until effect wears off Permanent, permanently change stats/body
		 * Tick, do formula on every tick until effect wears off TickBuff, buff
		 * but with animation every tick Taunt, uses formula but does taunt
		 * damage also Viral, uses tick but also if it effects anyone it infects
		 * them too Tame, used for pet taming spells
		 * 
		 */
		Formula, Buff, Permanent, Tick, TickBuff, Teleport, Bind, Stun, Root, Snare, Viral, Invisible, SeeInvisible, OnAttack, OnMeleeHit, PetTame, PetAttack, PetDefend, PetDestroy, PetFollow, PetNeutral
	}

	public enum EnergyTypes {
		/**
		 * Spell Energy Type Possibly values for bitmask
		 * 
		 */
		__dummyEnum__0, None, Fire, __dummyEnum__1, Water, __dummyEnum__2, __dummyEnum__3, __dummyEnum__4, Spirit, __dummyEnum__5, __dummyEnum__6, __dummyEnum__7, __dummyEnum__8, __dummyEnum__9, __dummyEnum__10, __dummyEnum__11, Air, __dummyEnum__12, __dummyEnum__13, __dummyEnum__14, __dummyEnum__15, __dummyEnum__16, __dummyEnum__17, __dummyEnum__18, __dummyEnum__19, __dummyEnum__20, __dummyEnum__21, __dummyEnum__22, __dummyEnum__23, __dummyEnum__24, __dummyEnum__25, __dummyEnum__26, Earth
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

	private int __Animation;

	public int getAnimation() {
		return __Animation;
	}

	public void setAnimation(int value) {
		__Animation = value;
	}

	private SpellDisplays __Display = SpellDisplays.Character;

	public SpellDisplays getDisplay() {
		return __Display;
	}

	public void setDisplay(SpellDisplays value) {
		__Display = value;
	}

	private TargetTypes __TargetType = TargetTypes.Target;

	public TargetTypes getTargetType() {
		return __TargetType;
	}

	public void setTargetType(TargetTypes value) {
		__TargetType = value;
	}

	private int __TargetSize;

	public int getTargetSize() {
		return __TargetSize;
	}

	public void setTargetSize(int value) {
		__TargetSize = value;
	}

	private SpellEffected __Effected = SpellEffected.Self;

	public SpellEffected getEffected() {
		return __Effected;
	}

	public void setEffected(SpellEffected value) {
		__Effected = value;
	}

	private int __MinimumLevelEffected;

	public int getMinimumLevelEffected() {
		return __MinimumLevelEffected;
	}

	public void setMinimumLevelEffected(int value) {
		__MinimumLevelEffected = value;
	}

	private EffectTypes __EffectType = EffectTypes.Formula;

	public EffectTypes getEffectType() {
		return __EffectType;
	}

	public void setEffectType(EffectTypes value) {
		__EffectType = value;
	}

	private long __Duration;

	public long getDuration() {
		return __Duration;
	}

	public void setDuration(long value) {
		__Duration = value;
	}

	private boolean __DoAttackAnimation;

	public boolean getDoAttackAnimation() {
		return __DoAttackAnimation;
	}

	public void setDoAttackAnimation(boolean value) {
		__DoAttackAnimation = value;
	}

	private boolean __SpellDamageEffects;

	public boolean getSpellDamageEffects() {
		return __SpellDamageEffects;
	}

	public void setSpellDamageEffects(boolean value) {
		__SpellDamageEffects = value;
	}

	private long __EnergyType;

	public long getEnergyType() {
		return __EnergyType;
	}

	public void setEnergyType(long value) {
		__EnergyType = value;
	}

	private String __HPFormula;

	public String getHPFormula() {
		return __HPFormula;
	}

	public void setHPFormula(String value) {
		__HPFormula = value;
	}

	private String __MPFormula;

	public String getMPFormula() {
		return __MPFormula;
	}

	public void setMPFormula(String value) {
		__MPFormula = value;
	}

	private String __SPFormula;

	public String getSPFormula() {
		return __SPFormula;
	}

	public void setSPFormula(String value) {
		__SPFormula = value;
	}

	private String __OnEffectText;

	public String getOnEffectText() {
		return __OnEffectText;
	}

	public void setOnEffectText(String value) {
		__OnEffectText = value;
	}

	private String __OffEffectText;

	public String getOffEffectText() {
		return __OffEffectText;
	}

	public void setOffEffectText(String value) {
		__OffEffectText = value;
	}

	private long __TauntAggro;

	public long getTauntAggro() {
		return __TauntAggro;
	}

	public void setTauntAggro(long value) {
		__TauntAggro = value;
	}

	private int __TeleportMapID;

	public int getTeleportMapID() {
		return __TeleportMapID;
	}

	public void setTeleportMapID(int value) {
		__TeleportMapID = value;
	}

	private int __TeleportMapX;

	public int getTeleportMapX() {
		return __TeleportMapX;
	}

	public void setTeleportMapX(int value) {
		__TeleportMapX = value;
	}

	private int __TeleportMapY;

	public int getTeleportMapY() {
		return __TeleportMapY;
	}

	public void setTeleportMapY(int value) {
		__TeleportMapY = value;
	}

	private boolean __WorksInPVP;

	public boolean getWorksInPVP() {
		return __WorksInPVP;
	}

	public void setWorksInPVP(boolean value) {
		__WorksInPVP = value;
	}

	private boolean __WorksNotInPVP;

	public boolean getWorksNotInPVP() {
		return __WorksNotInPVP;
	}

	public void setWorksNotInPVP(boolean value) {
		__WorksNotInPVP = value;
	}

	private boolean __OnlyHitsOneNPC;

	public boolean getOnlyHitsOneNPC() {
		return __OnlyHitsOneNPC;
	}

	public void setOnlyHitsOneNPC(boolean value) {
		__OnlyHitsOneNPC = value;
	}

	/**
	 * If spell is a buff, can it be removed from buff bar by clicking it
	 * 
	 */
	private boolean __BuffCanBeRemoved;

	public boolean getBuffCanBeRemoved() {
		return __BuffCanBeRemoved;
	}

	public void setBuffCanBeRemoved(boolean value) {
		__BuffCanBeRemoved = value;
	}

	private int __BuffGraphic;

	public int getBuffGraphic() {
		return __BuffGraphic;
	}

	public void setBuffGraphic(int value) {
		__BuffGraphic = value;
	}

	private double __RandomJoinChance;

	public double getRandomJoinChance() {
		return __RandomJoinChance;
	}

	public void setRandomJoinChance(double value) {
		__RandomJoinChance = value;
	}

	private int __OnMeleeAttackSpellID;

	public int getOnMeleeAttackSpellID() {
		return __OnMeleeAttackSpellID;
	}

	public void setOnMeleeAttackSpellID(int value) {
		__OnMeleeAttackSpellID = value;
	}

	private int __OnMeleeHitSpellID;

	public int getOnMeleeHitSpellID() {
		return __OnMeleeHitSpellID;
	}

	public void setOnMeleeHitSpellID(int value) {
		__OnMeleeHitSpellID = value;
	}

	private SpellEffect __OnMeleeAttackSpell;

	public SpellEffect getOnMeleeAttackSpell() {
		return __OnMeleeAttackSpell;
	}

	public void setOnMeleeAttackSpell(SpellEffect value) {
		__OnMeleeAttackSpell = value;
	}

	private SpellEffect __OnMeleeHitSpell;

	public SpellEffect getOnMeleeHitSpell() {
		return __OnMeleeHitSpell;
	}

	public void setOnMeleeHitSpell(SpellEffect value) {
		__OnMeleeHitSpell = value;
	}

	private double __OnMeleeAttackSpellChance;

	public double getOnMeleeAttackSpellChance() {
		return __OnMeleeAttackSpellChance;
	}

	public void setOnMeleeAttackSpellChance(double value) {
		__OnMeleeAttackSpellChance = value;
	}

	private double __OnMeleeHitSpellChance;

	public double getOnMeleeHitSpellChance() {
		return __OnMeleeHitSpellChance;
	}

	public void setOnMeleeHitSpellChance(double value) {
		__OnMeleeHitSpellChance = value;
	}

	private double __SnarePercent;

	public double getSnarePercent() {
		return __SnarePercent;
	}

	public void setSnarePercent(double value) {
		__SnarePercent = value;
	}

	private String __BuffStacksOverString;

	public String getBuffStacksOverString() {
		return __BuffStacksOverString;
	}

	public void setBuffStacksOverString(String value) {
		__BuffStacksOverString = value;
	}

	private String __BuffDoesntStackOverString;

	public String getBuffDoesntStackOverString() {
		return __BuffDoesntStackOverString;
	}

	public void setBuffDoesntStackOverString(String value) {
		__BuffDoesntStackOverString = value;
	}

	private List<SpellEffect> __BuffStacksOver;

	public List<SpellEffect> getBuffStacksOver() {
		return __BuffStacksOver;
	}

	public void setBuffStacksOver(List<SpellEffect> value) {
		__BuffStacksOver = value;
	}

	private List<SpellEffect> __BuffDoesntStackOver;

	public List<SpellEffect> getBuffDoesntStackOver() {
		return __BuffDoesntStackOver;
	}

	public void setBuffDoesntStackOver(List<SpellEffect> value) {
		__BuffDoesntStackOver = value;
	}

	/**
	 * Stats to change
	 * 
	 */
	private AttributeSet __Stats;

	public AttributeSet getStats() {
		return __Stats;
	}

	public void setStats(AttributeSet value) {
		__Stats = value;
	}

	/**
	 * Hair style id
	 */
	private int __HairID;

	public int getHairID() {
		return __HairID;
	}

	public void setHairID(int value) {
		__HairID = value;
	}

	/**
	 * Hair colour r
	 */
	private int __HairR;

	public int getHairR() {
		return __HairR;
	}

	public void setHairR(int value) {
		__HairR = value;
	}

	/**
	 * Hair colour g
	 */
	private int __HairG;

	public int getHairG() {
		return __HairG;
	}

	public void setHairG(int value) {
		__HairG = value;
	}

	/**
	 * Hair colour b
	 */
	private int __HairB;

	public int getHairB() {
		return __HairB;
	}

	public void setHairB(int value) {
		__HairB = value;
	}

	/**
	 * Hair colour a
	 */
	private int __HairA;

	public int getHairA() {
		return __HairA;
	}

	public void setHairA(int value) {
		__HairA = value;
	}

	/**
	 * Face id
	 */
	private int __FaceID;

	public int getFaceID() {
		return __FaceID;
	}

	public void setFaceID(int value) {
		__FaceID = value;
	}

	/**
	 * Body ID
	 */
	private int __BodyID;

	public int getBodyID() {
		return __BodyID;
	}

	public void setBodyID(int value) {
		__BodyID = value;
	}

	// Used in random spell
	static class Point {
		public Point() {
		}

		public int x;
		public int y;

		public Point(int x, int y) throws Exception {
			this.x = x;
			this.y = y;
		}

	}

	/**
	 * CastFormulaSpell
	 * 
	 */
	public boolean castFormulaSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!this.canCastSpell(caster, target))
			return false;

		List<Goose.Player> range = target.getMap().getPlayersInRange(target);
		String packet = "";
		int hpresult = this.parseFormula(this.getHPFormula(), caster, target);
		int mpresult = this.parseFormula(this.getMPFormula(), caster, target);
		// int spresult = this.ParseFormula(this.SPFormula, caster, target);
		// if target is a player then no sd for pvp if not a heal
		if (!(hpresult < 0 && target instanceof Goose.Player)
				&& this.getSpellDamageEffects()) {
			if (world.getRandom().nextInt(10000) + 1 <= caster.getMaxStats()
					.getSpellCrit() * 10000)
				hpresult *= 2;

			if (hpresult * (1 + caster.getMaxStats().getSpellDamage()) < Integer.MIN_VALUE)
				hpresult = Integer.MIN_VALUE;
			else
				hpresult = ((int) ((hpresult * (1 + caster.getMaxStats()
						.getSpellDamage()))));
			if (world.getRandom().nextInt(10000) + 1 <= caster.getMaxStats()
					.getSpellCrit() * 10000)
				mpresult *= 2;

			mpresult = ((int) ((mpresult * (1 + caster.getMaxStats()
					.getSpellDamage()))));
		}

		hpresult = ((int) (((double) hpresult * GameSettings.getDefault()
				.getDamageModifier())));
		target.setCurrentMP(target.getCurrentMP() + mpresult);
		if (hpresult != 0) {
			target.attacked(caster, -hpresult, world);
		} else {
			packet = target.vCString();
			if (target instanceof Goose.Player) {
				world.send((Goose.Player) target, packet);
				world.send((Goose.Player) target,
						((Goose.Player) target).sNFString());
			}

			for (Goose.Player player : range) {
				world.send(player, packet);
			}
		}
		if (this.getAnimation() != 0)
			packet = "SPP" + target.getLoginID() + "," + this.getAnimation();

		if (target instanceof Goose.NPC && this.getTauntAggro() > 0) {
			((Goose.NPC) target).addAggro((Goose.Player) caster,
					this.getTauntAggro(), true, world);
			packet += "\u0001" + "BT" + target.getLoginID() + ",60,Taunted";
		}

		if (packet.contains("\u0001"))
		{
			packet = packet.substring(packet.indexOf("\u0001"));
		}
		
		if (target instanceof Goose.Player)
			world.send((Goose.Player) target, packet);

		for (Goose.Player player : range) {
			world.send(player, packet);
		}
		return true;
	}

	/**
	 * CastBindSpell
	 * 
	 */
	public boolean castBindSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!this.canCastSpell(caster, target))
			return false;

		if (!caster.getMap().getCanBind())
			return false;

		if (caster != target) {
			if (((Goose.Player) caster).getGroup() == null
					|| !((Goose.Player) caster).getGroup().getPlayers()
							.contains((Goose.Player) target)) {
				return false;
			}

		}

		((Goose.Player) target).setBoundMap(target.getMap());
		((Goose.Player) target).setBoundID(target.getMapID());
		((Goose.Player) target).setBoundX(target.getMapX());
		((Goose.Player) target).setBoundY(target.getMapY());
		List<Goose.Player> range = target.getMap().getPlayersInRange(target);
		String packet = "BT" + target.getLoginID() + ",60,Bound";
		if (this.getAnimation() != 0)
			packet += "\u0001SPP" + target.getLoginID() + ","
					+ this.getAnimation();

		world.send((Goose.Player) target,
				"$7Your soul has been bound to this spot.");
		world.send((Goose.Player) target, packet);
		for (Goose.Player player : range) {
			world.send(player, packet);
		}
		return true;
	}

	/**
	 * CastPermanentSpell
	 * 
	 */
	public boolean castPermanentSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!this.canCastSpell(caster, target))
			return false;

		List<Goose.Player> range = target.getMap().getPlayersInRange(target);
		target.setBaseStats(AttributeSet.add(target.getBaseStats(),
				this.getStats()));
		if (this.getHairID() != 0)
			target.setHairID(this.getHairID());

		// an alpha value of 0 means don't dye hair.
		// kinda hackish but it's better than having another field
		if (this.getHairA() != 0) {
			target.setHairR(this.getHairR());
			target.setHairG(this.getHairG());
			target.setHairB(this.getHairB());
			target.setHairA(this.getHairA());
		}

		if (this.getFaceID() != 0)
			target.setFaceID(this.getFaceID());

		if (this.getBodyID() != 0) {
			if (target.getCurrentBodyID() == target.getBodyID())
				target.setCurrentBodyID(this.getBodyID());

			target.setBodyID(this.getBodyID());
		}

		((Goose.Player) target).addRegenEvent(world);
		String packet = ((Goose.Player) target).vCString() + "\u0001"
				+ ((Goose.Player) target).cHPString();
		if (this.getAnimation() != 0)
			packet += "\u0001SPP" + target.getLoginID() + ","
					+ this.getAnimation();

		if (!("".equals(this.getOnEffectText())))
			world.send((Goose.Player) target, "$7" + this.getOnEffectText());

		world.send((Goose.Player) target, ((Goose.Player) target).sNFString());
		world.send((Goose.Player) target, packet);
		for (Goose.Player player : range) {
			world.send(player, packet);
		}
		return true;
	}

	/**
	 * CastBuffSpell
	 * 
	 */
	public boolean castBuffSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!this.canCastSpell(caster, target))
			return false;

		if (this.getEffectType() == EffectTypes.Stun) {
			List<Goose.Player> range = target.getMap()
					.getPlayersInRange(target);
			String packet = "BT" + target.getLoginID() + ",50";
			if (target instanceof Goose.Player)
				world.send((Goose.Player) target, packet);

			for (Goose.Player player : range) {
				world.send(player, packet);
			}
		} else if (this.getEffectType() == EffectTypes.Root) {
			List<Goose.Player> range = target.getMap()
					.getPlayersInRange(target);
			String packet = "BT" + target.getLoginID() + ",11";
			if (target instanceof Goose.Player)
				world.send((Goose.Player) target, packet);

			for (Goose.Player player : range) {
				world.send(player, packet);
			}
		}

		Buff buff = new Buff();
		buff.setTarget(target);
		buff.setCaster(caster);
		buff.setItemBuff(false);
		buff.setSpellEffect(this);
		target.addBuff(buff, world);
		return true;
	}

	/**
	 * CastTickSpell
	 * 
	 */
	public boolean castTickSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		return this.castBuffSpell(caster, target, world);
	}

	/**
	 * CastTeleportSpell
	 * 
	 */
	public boolean castTeleportSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!this.canCastSpell(caster, target))
			return false;

		if (this.getAnimation() != 0) {
			List<Goose.Player> range = target.getMap()
					.getPlayersInRange(target);
			String packet = "SPP" + target.getLoginID() + ","
					+ this.getAnimation();
			world.send((Goose.Player) target, packet);
			for (Goose.Player player : range) {
				world.send(player, packet);
			}
		}

		// if null tele to bound spot. used for gate spells
		Map map = world.getMapHandler().getMap(this.getTeleportMapID());
		if (map == null) {
			((Goose.Player) target).warpTo(world,
					((Goose.Player) target).getBoundMap(),
					((Goose.Player) target).getBoundX(),
					((Goose.Player) target).getBoundY());
		} else {
			if (map.playerCanJoin((Goose.Player) target, world)) {
				((Goose.Player) target).warpTo(world, map,
						this.getTeleportMapX(), this.getTeleportMapY());
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean canCastSpell(ICharacter caster, ICharacter target)
			throws Exception {
		if (target == null)
			return false;

		if (caster == target) {
			if ((((Enum) this.getEffected()).ordinal() & ((Enum) SpellEffected.Self)
					.ordinal()) == 0)
				return false;
			else
				return true;
		}

		if (target instanceof Goose.Player && !this.getWorksNotInPVP()
				&& !target.getMap().getCanPVP())
			return false;

		if (target.getLevel() >= this.getMinimumLevelEffected()) {
			if (target instanceof Goose.Player) {
				if ((((Enum) this.getEffected()).ordinal() & ((Enum) SpellEffected.Player)
						.ordinal()) != 0)
					return true;

			} else if (target instanceof Goose.NPC) {
				if ((((Enum) this.getEffected()).ordinal() & ((Enum) SpellEffected.NPC)
						.ordinal()) != 0) {
					if (this.getEffectType() == EffectTypes.Root) {
						if (((Goose.NPC) target).getCanBeRooted())
							return true;
						else
							return false;
					} else if (this.getEffectType() == EffectTypes.Stun) {
						if (((Goose.NPC) target).getCanBeStunned())
							return true;
						else
							return false;
					} else if (this.getEffectType() == EffectTypes.Snare) {
						if (((Goose.NPC) target).getCanBeSlowed())
							return true;
						else
							return false;
					} else
						return true;
				}

			}

			return false;
		}

		return false;
	}

	/**
	 * Pet Taming spell
	 * 
	 * @param caster
	 *            player who casted spell
	 * @param target
	 *            npc target
	 * @param world
	 * @return true if successful
	 */
	public boolean castTameSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (!((Goose.NPC) target).getCanBeKilled()
				|| ((Goose.NPC) target).getMoveSpeed() == 0)
			return false;

		Goose.Player player = (Goose.Player) caster;
		if (player.getPets().size() == GameSettings.getDefault()
				.getPetCountLimit()) {
			world.send(
					player,
					"$7You have reached the maximum amount of pets. Use /petdelete <id> to release one");
			return false;
		}

		double successrate = (double) (player.getBaseStats().getHP()
				+ player.getClas().getLevel(player.getLevel()).getBaseStats()
						.getHP() + player.getBaseStats().getMP() + player
				.getClas().getLevel(player.getLevel()).getBaseStats().getMP())
				/ (double) target.getMaxStats().getHP();
		if (world.getRandom().nextInt(100) + 1 <= successrate * 100) {
			Pet newpet = Pet.fromCharacter((Goose.NPC) target);
			player.addPet(newpet);
			world.send(player, "$7Successfully tamed " + target.getName() + ".");
			newpet.saveToDatabase(world);
			return true;
		} else {
			world.send(player, "$7Failed to tame " + target.getName() + ". ("
					+ target.getMaxStats().getHP() + " hp)");
			return false;
		}
	}

	/**
	 * Sets the targetted pet into defend mode
	 * 
	 * @param caster
	 *            should be the pet owner but gets checked
	 * @param target
	 *            a pet
	 * @param world
	 * @return
	 */
	public boolean castPetDefendSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		Pet pet = (Pet) target;
		if (pet.getOwner() != caster)
			return false;

		pet.setTarget(null);
		pet.setMode(Modes.Defend);
		return true;
	}

	/**
	 * Destroys the selected pet
	 * 
	 * @param caster
	 *            should be the pet owner but gets checked
	 * @param target
	 *            a pet
	 * @param world
	 * @return
	 */
	public boolean castPetDestroySpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		Pet pet = (Pet) target;
		if (pet.getOwner() != caster)
			return false;

		pet.destroy(world);
		return true;
	}

	/**
	 * Sets the targetted pet into follow mode
	 * 
	 * @param caster
	 *            should be the pet owner but gets checked
	 * @param target
	 *            a pet
	 * @param world
	 * @return
	 */
	public boolean castPetFollowSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		Pet pet = (Pet) target;
		if (pet.getOwner() != caster)
			return false;

		pet.setTarget(null);
		pet.setMode(Modes.Follow);
		return true;
	}

	/**
	 * Sets the targetted pet into neutral mode
	 * 
	 * @param caster
	 *            should be the pet owner but gets checked
	 * @param target
	 *            a pet
	 * @param world
	 * @return
	 */
	public boolean castPetNeutralSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		Pet pet = (Pet) target;
		if (pet.getOwner() != caster)
			return false;

		pet.setTarget(null);
		pet.setMode(Modes.Neutral);
		return true;
	}

	/**
	 * Sets a random neutral pet into attack mode
	 * 
	 * @param caster
	 *            pet owner
	 * @param target
	 *            a pet
	 * @param world
	 * @return
	 */
	public boolean castPetAttackSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (target instanceof Goose.Player && !target.getMap().getCanPVP())
			return false;
		List<Pet> pets = new ArrayList();
		for (Pet p : ((Player) caster).getPets()) {
			if (p.getIsAlive() && p.getMode() == Pet.Modes.Neutral) {
				pets.add(p);
			}
		}

		if (pets.size() == 0)
			return false;

		Pet pet = pets.get(world.getRandom().nextInt(pets.size()));
		pet.setMode(Modes.Attack);
		pet.setTarget(target);
		pet.addAttackEvent(world);
		return true;
	}

	/**
	 * CastSpell, uses the right spell function to cast the spell
	 * 
	 */
	public boolean castSpell(ICharacter caster, ICharacter target,
			GameWorld world) throws Exception {
		if (this.getEffectType() == EffectTypes.Formula) {
			return this.castFormulaSpell(caster, target, world);
		} else // bind can only effect players
		if (this.getEffectType() == EffectTypes.Bind
				&& target instanceof Goose.Player) {
			return this.castBindSpell(caster, target, world);
		} else // I think permanent can only effect players
		if (this.getEffectType() == EffectTypes.Permanent
				&& target instanceof Goose.Player) {
			return this.castPermanentSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Buff) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Tick) {
			return this.castTickSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.TickBuff) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Stun) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Root) {
			return this.castBuffSpell(caster, target, world);
		} else // snare can only effect npcs
		if (this.getEffectType() == EffectTypes.Snare
				&& target instanceof Goose.NPC) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Invisible) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.SeeInvisible) {
			return this.castBuffSpell(caster, target, world);
		} else // teleport can only effect players
		if (this.getEffectType() == EffectTypes.Teleport
				&& target instanceof Goose.Player) {
			return this.castTeleportSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.Viral) {
			return this.castFormulaSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.OnMeleeHit) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.OnAttack) {
			return this.castBuffSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetTame
				&& target instanceof Goose.NPC) {
			return this.castTameSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetDefend
				&& target instanceof Pet) {
			this.castPetDefendSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetDestroy
				&& target instanceof Pet) {
			this.castPetDestroySpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetFollow
				&& target instanceof Pet) {
			this.castPetFollowSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetNeutral
				&& target instanceof Pet) {
			this.castPetNeutralSpell(caster, target, world);
		} else if (this.getEffectType() == EffectTypes.PetAttack) {
			this.castPetAttackSpell(caster, target, world);
		}

		return false;
	}

	/**
	 * Cast, figures out how to cast the spell, centered on target
	 * 
	 */
	public boolean cast(ICharacter caster, ICharacter target, GameWorld world)
			throws Exception {
		if (!this.getWorksInPVP() && target.getMap().getCanPVP())
			return false;

		if (this.getTargetType() == TargetTypes.Target) {
			return this.castSpell(caster, target, world);
		} else {
			int ox = target.getMapX();
			int oy = target.getMapY();
			ICharacter hit;
			String packet = "";
			Map map = target.getMap();
			List<Goose.Player> range = map.getPlayersInRange(target);
			boolean hitone = false;
			if (this.getDoAttackAnimation())
				packet += "ATT" + caster.getLoginID();

			if (this.getTargetType() == TargetTypes.LineFront) {
				int x = ox;
				int y = oy;
				for (int i = 1; i <= this.getTargetSize(); i++) {
					switch (caster.getFacing()) {
						case 1:
							y--;
							break;
						case 2:
							x++;
							break;
						case 3:
							y++;
							break;
						case 4:
							x--;
							break;

					}
					if ((hit = map.getCharacterAt(x, y)) != null) {
						this.castSpell(caster, hit, world);
						if (this.getOnlyHitsOneNPC()
								&& hit instanceof Goose.NPC)
							hitone = true;

					}

					if (this.getDisplay() == SpellDisplays.Tile
							&& this.getAnimation() != 0) {
						packet += "\u0001SPA" + x + "," + y + ","
								+ this.getAnimation();
					}

					if (hitone)
						break;

				}
			} else if (this.getTargetType() == TargetTypes.Area) {
				for (int y = oy - this.getTargetSize(); y <= oy
						+ this.getTargetSize(); y++) {
					for (int x = ox - this.getTargetSize(); x <= ox
							+ this.getTargetSize(); x++) {
						if ((hit = map.getCharacterAt(x, y)) != null) {
							this.castSpell(caster, hit, world);
							if (this.getOnlyHitsOneNPC()
									&& hit instanceof Goose.NPC)
								hitone = true;

						}

						if (this.getDisplay() == SpellDisplays.Tile
								&& this.getAnimation() != 0) {
							packet += "\u0001SPA" + x + "," + y + ","
									+ this.getAnimation();
						}

						if (hitone)
							break;

					}
					if (hitone)
						break;

				}
			} else if (this.getTargetType() == TargetTypes.Cross) {
				for (int y = oy - this.getTargetSize(); y <= oy
						+ this.getTargetSize(); y++) {
					for (int x = ox - this.getTargetSize(); x <= ox
							+ this.getTargetSize(); x++) {
						if (x == ox - (y - oy) || x == ox + (y - oy)) {
							if ((hit = map.getCharacterAt(x, y)) != null) {
								this.castSpell(caster, hit, world);
								if (this.getOnlyHitsOneNPC()
										&& hit instanceof Goose.NPC)
									hitone = true;

							}

							if (this.getDisplay() == SpellDisplays.Tile
									&& this.getAnimation() != 0) {
								packet += "\u0001SPA" + x + "," + y + ","
										+ this.getAnimation();
							}

						}

						if (hitone)
							break;

					}
					if (hitone)
						break;

				}
			} else if (this.getTargetType() == TargetTypes.Plus) {
				for (int y = oy - this.getTargetSize(); y <= oy
						+ this.getTargetSize(); y++) {
					for (int x = ox - this.getTargetSize(); x <= ox
							+ this.getTargetSize(); x++) {
						if (x == ox || y == oy) {
							if ((hit = map.getCharacterAt(x, y)) != null) {
								this.castSpell(caster, hit, world);
								if (this.getOnlyHitsOneNPC()
										&& hit instanceof Goose.NPC)
									hitone = true;

							}

							if (this.getDisplay() == SpellDisplays.Tile
									&& this.getAnimation() != 0) {
								packet += "\u0001SPA" + x + "," + y + ","
										+ this.getAnimation();
							}

						}

						if (hitone)
							break;

					}
					if (hitone)
						break;

				}
			} else if (this.getTargetType() == TargetTypes.Random) {
				List<Point> points = new ArrayList<Point>();
				List<Point> done = new ArrayList<Point>();
				Point point = new Point();
				points.add(new Point(ox, oy));
				while (points.size() > 0) {
					point = points.get(0);
					// points.remove((int));
					if (point.x >= ox - this.getTargetSize()
							&& point.x <= ox + this.getTargetSize()
							&& point.y >= oy - this.getTargetSize()
							&& point.y <= oy + this.getTargetSize()) {
						if (!done.contains(point)) {
							done.add(point);
							if (world.getRandom().nextInt(10000) + 1 <= this
									.getRandomJoinChance() * 100) {
								points.add(new Point(point.x - 1, point.y));
							}

							if (world.getRandom().nextInt(10000) + 1 <= this
									.getRandomJoinChance() * 100) {
								points.add(new Point(point.x + 1, point.y));
							}

							if (world.getRandom().nextInt(10000) + 1 <= this
									.getRandomJoinChance() * 100) {
								points.add(new Point(point.x, point.y - 1));
							}

							if (world.getRandom().nextInt(10000) + 1 <= this
									.getRandomJoinChance() * 100) {
								points.add(new Point(point.x, point.y + 1));
							}

						}

					}

				}
				for (Point p : done) {
					if ((hit = map.getCharacterAt(p.x, p.y)) != null) {
						this.castSpell(caster, hit, world);
						if (this.getOnlyHitsOneNPC()
								&& hit instanceof Goose.NPC)
							hitone = true;

					}

					if (this.getDisplay() == SpellDisplays.Tile
							&& this.getAnimation() != 0) {
						packet += "\u0001SPA" + p.x + "," + p.y + ","
								+ this.getAnimation();
					}

					if (hitone)
						break;

				}
			} else if (this.getTargetType() == TargetTypes.TriangleFront) {
				int x = ox;
				int y = oy;
				for (int i = 1; i <= this.getTargetSize(); i++) {
					switch (caster.getFacing()) {
						case 1:
							y--;
							x = ox - i;
							break;
						case 2:
							x++;
							y = oy - i;
							break;
						case 3:
							y++;
							x = ox - i;
							break;
						case 4:
							x--;
							y = oy - i;
							break;

					}
					for (int j = 0; j < i * 2 - 1; j++) {
						switch (caster.getFacing()) {
							case 1:
								x++;
								break;
							case 2:
								y++;
								break;
							case 3:
								x++;
								break;
							case 4:
								y++;
								break;

						}
						if ((hit = map.getCharacterAt(x, y)) != null) {
							this.castSpell(caster, hit, world);
							if (this.getOnlyHitsOneNPC()
									&& hit instanceof Goose.NPC)
								hitone = true;

						}

						if (this.getDisplay() == SpellDisplays.Tile
								&& this.getAnimation() != 0) {
							packet += "\u0001SPA" + x + "," + y + ","
									+ this.getAnimation();
						}

						if (hitone)
							break;

					}
					if (hitone)
						break;

				}
			}
			if (packet.contains("\u0001"))
				packet = packet.substring(packet.indexOf("\u0001"));
			
			if (target instanceof Goose.Player)
				world.send((Goose.Player) target, packet);

			for (Goose.Player player : range) {
				world.send(player, packet);
			}
		}
		return true;
	}

	/**
	 * ParseFormula, parse spell formula
	 * 
	 * Uses http://en.wikipedia.org/wiki/Shunting-yard_algorithm to convert
	 * formula to reverse polish notation
	 * 
	 * Then uses http://en.wikipedia.org/wiki/Reverse_Polish_notation#
	 * The_postfix_algorithm to calculate the result
	 * 
	 */
	public int parseFormula(String formula, ICharacter caster, ICharacter target)
			throws Exception {
		List<Object> result = new ArrayList<Object>();
		Stack<Character> operators = new Stack<Character>();
		String buffer = "";
		char token;
		char op;
		double value;
		Hashtable symbolToValue = new Hashtable();
		symbolToValue.put("%cchp", caster.getCurrentHP());
		symbolToValue.put("%ccmp", caster.getCurrentMP());
		symbolToValue.put("%ccsp", caster.getCurrentSP());
		symbolToValue.put("%cstr", caster.getMaxStats().getStrength());
		symbolToValue.put("%cwdmg", caster.getWeaponDamage());
		symbolToValue.put("%clevel", caster.getLevel());
		symbolToValue.put("%chp", caster.getMaxStats().getHP());
		symbolToValue.put("%cmp", caster.getMaxStats().getMP());
		symbolToValue.put("%tchp", target.getCurrentHP());
		symbolToValue.put("%tcmp", target.getCurrentMP());
		symbolToValue.put("%tcsp", target.getCurrentSP());
		symbolToValue.put("%tstr", target.getMaxStats().getStrength());
		symbolToValue.put("%twdmg", target.getWeaponDamage());
		symbolToValue.put("%tlevel", target.getLevel());
		symbolToValue.put("%thp", target.getMaxStats().getHP());
		symbolToValue.put("%tmp", target.getMaxStats().getMP());
		for (int i = 0; i < formula.length(); i++) {
			token = formula.charAt(i);
			if (token == '-' || token == '+') {
				if (buffer.length() == 0 && result.size() == 0)
					buffer += '0';

				if (buffer.length() > 0) {
					if (buffer.charAt(0) == '%') {
						value = (Integer) symbolToValue.get(buffer);
					} else {
						// bad symbol
						// needa log here
						// if (value == null)
						// {
						// throw new Exception("Parse error: bad symbol " +
						// buffer);
						// }
						value = Double.valueOf(buffer);
					}
					result.add(value);
					buffer = "";
				}

				while (operators.size() > 0) {
					op = operators.peek();
					// +/- is lowest precedence so pop all operators
					if (op == '-' || op == '+' || op == '*' || op == '/')
						result.add(operators.pop());
					else
						break;
				}
				operators.push(token);
			} else if (token == '*' || token == '/') {
				if (buffer.length() != 0) {
					if (buffer.charAt(0) == '%') {
						value = (Integer) symbolToValue.get(buffer);
					} else {
						// bad symbol
						// needa log here
						// if (value == null)
						// {
						// throw new Exception("Parse error: bad symbol " +
						// buffer);
						// }
						value = Double.parseDouble(buffer);
					}
					result.add(value);
					buffer = "";
				}

				while (operators.size() > 0) {
					op = operators.peek();
					if (op == '*' || op == '/')
						result.add(operators.pop());
					else
						break;
				}
				operators.push(token);
			} else if (token >= 'a' && token <= 'z') {
				if (buffer.length() > 0 && buffer.charAt(0) == '%') {
					buffer += token;
				} else {
					throw new Exception(
							"Parse error: can't mix numbers and symbols");
				}
			} else if (token == '.' || token >= '0' && token <= '9') {
				if (buffer.length() > 0 && buffer.charAt(0) == '%') {
					throw new Exception(
							"Parse error: can't mix symbols and numbers");
				} else {
					buffer += token;
				}
			} else if (token == '%') {
				if (buffer.length() == 0) {
					buffer += token;
				} else {
					throw new Exception(
							"Parse error: can't mix numbers and symbols");
				}
			} else if (token == '(') {
				operators.push(token);
			} else if (token == ')') {
				if (buffer.length() != 0) {
					if (buffer.charAt(0) == '%') {
						value = (Integer) symbolToValue.get(buffer);
					} else {
						// bad symbol
						// needa log here
						// if (value == null)
						// {
						// throw new Exception("Parse error: bad symbol " +
						// buffer);
						// }
						value = Double.parseDouble(buffer);
					}
					result.add(value);
					buffer = "";
				}

				while (operators.size() > 0) {
					op = operators.peek();
					if (op != '(')
						result.add(operators.pop());
					else
						break;
				}
				if (operators.size() > 0 && operators.pop() != '(') {
					throw new Exception("Parse error: parenthesis mismatch");
				}

			}

		}
		if (buffer.length() != 0) {
			if (buffer.charAt(0) == '%') {
				value = (Integer) symbolToValue.get(buffer);
			} else {
				// bad symbol
				// needa log here
				// if (value == null)
				// {
				// throw new Exception("Parse error: bad symbol " + buffer);
				// }
				value = Double.valueOf(buffer);
			}
			result.add(value);
			buffer = "";
		}

		while (operators.size() > 0) {
			op = operators.peek();
			if (op == '(')
				throw new Exception("Parse error: parenthesis mismatch");
			else
				result.add(operators.pop());
		}
		double rs, ls;
		Object cur;
		while (result.size() > 1) {
			cur = result.get(0);
			result.remove(0);
			if (cur instanceof Character) {
				rs = (double) result.get(result.size() - 1);
				ls = (double) result.get(result.size() - 2);
				result.remove(result.size() - 1);
				result.remove(result.size() - 1);
				op = (Character) cur;
				switch (op) {
					case '*':
						result.add(ls * rs);
						break;
					case '/':
						result.add(ls / rs);
						break;
					case '+':
						result.add(ls + rs);
						break;
					case '-':
						result.add(ls - rs);
						break;

				}
			} else {
				result.add(cur);
			}
		}

		return ((Double)result.get(0)).intValue();

	}
}
