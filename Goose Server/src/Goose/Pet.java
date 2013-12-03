package Goose;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Goose.Events.PetAttackEvent;
import Goose.Events.PetMoveEvent;

/**
 * Pets yay. Internally a player object since it's easier than adding another character type
 */
public class Pet extends Goose.Player {
  /**
   * Maps Login IDs to pet objects
   */
  public static Hashtable<Integer, Pet> LoginIDToPet = new Hashtable<Integer, Pet>();

  /**
   * Returns the first empty login id for a pet
   * 
   * @return
   * @throws Exception
   */
  public int getLoginID() {
    int startpoint =
        GameSettings.getDefault().getMaxPlayers() + GameSettings.getDefault().getMaxNPCs() + 1;
    for (int i = startpoint;; i++) {
      if (Pet.LoginIDToPet.get(i) == null) return i;

    }
  }

  static int currentdbid = 1;

  /**
   * Gets/sets the next available pet database id
   */
  public static int getCurrentID() throws Exception {
    return Pet.currentdbid;
  }

  public static void setCurrentID(int value) throws Exception {
    Pet.currentdbid = value;
  }

  public enum Modes {
    Neutral,
    // Laze around owner
    Follow,
    // Follow owner
    Defend,
    // Defend owner
    Attack
  }

  // Attack something as directed by owner
  /**
   * ID of Pet in the database
   */
  private int __PetID;

  public int getPetID() {
    return __PetID;
  }

  public void setPetID(int value) {
    __PetID = value;
  }

  /**
   * Owner of the pet
   */
  private Goose.Player __Owner;

  public Goose.Player getOwner() {
    return __Owner;
  }

  public void setOwner(Goose.Player value) {
    __Owner = value;
  }

  /**
   * System time of allowed next respawn
   */
  private long __NextRespawnTime;

  public long getNextRespawnTime() {
    return __NextRespawnTime;
  }

  public void setNextRespawnTime(long value) {
    __NextRespawnTime = value;
  }

  /**
   * Total time between respawns in seconds
   */
  private int __RespawnTime;

  public int getRespawnTime() {
    return __RespawnTime;
  }

  public void setRespawnTime(int value) {
    __RespawnTime = value;
  }

  /**
   * Pet's Weapon Damage
   */
  private int __WeaponDamage;

  public int getWeaponDamage() {
    return __WeaponDamage;
  }

  public void setWeaponDamage(int value) {
    __WeaponDamage = value;
  }

  /**
   * Items to display on humanoid pets
   */
  private String __EquippedItems;

  public String getEquippedItems() {
    return __EquippedItems;
  }

  public void setEquippedItems(String value) {
    __EquippedItems = value;
  }

  /**
   * Gets a boolean indicating whether the pet is spawned or not
   */
  public boolean getIsAlive() throws Exception {
    return (this.getMap() != null);
  }

  /**
   * The current move event
   */
  private PetMoveEvent __MoveEvent;

  public PetMoveEvent getMoveEvent() {
    return __MoveEvent;
  }

  public void setMoveEvent(PetMoveEvent value) {
    __MoveEvent = value;
  }

  /**
   * Time between movement events
   */
  private double __MoveSpeed;

  public double getMoveSpeed() {
    return __MoveSpeed;
  }

  public void setMoveSpeed(double value) {
    __MoveSpeed = value;
  }

  /**
   * The current attack event
   */
  private PetAttackEvent __AttackEvent;

  public PetAttackEvent getAttackEvent() {
    return __AttackEvent;
  }

  public void setAttackEvent(PetAttackEvent value) {
    __AttackEvent = value;
  }

  /**
   * Time between attack events
   */
  private double __AttackSpeed;

  public double getAttackSpeed() {
    return __AttackSpeed;
  }

  public void setAttackSpeed(double value) {
    __AttackSpeed = value;
  }

  /**
   * Maximum distance in tiles from pet that can be hit
   */
  private int __AttackRange;

  public int getAttackRange() {
    return __AttackRange;
  }

  public void setAttackRange(int value) {
    __AttackRange = value;
  }

  /**
   * Current mode of Pet, defined in enum above
   */
  private Modes __Mode = Modes.Neutral;

  public Modes getMode() {
    return __Mode;
  }

  public void setMode(Modes value) {
    __Mode = value;
  }

  /**
   * Current target, used in both attack and defend modes
   */
  private ICharacter __Target;

  public ICharacter getTarget() {
    return __Target;
  }

  public void setTarget(ICharacter value) {
    __Target = value;
  }

  /**
   * Unused, aggro range
   */
  private int __AggroRange;

  public int getAggroRange() {
    return __AggroRange;
  }

  public void setAggroRange(int value) {
    __AggroRange = value;
  }

  /**
   * Does this pet need to be deleted?
   */
  private boolean __Delete;

  public boolean getDelete() {
    return __Delete;
  }

  public void setDelete(boolean value) {
    __Delete = value;
  }

  /**
   * Constructs a Pet object with info from another character
   * 
   * @param character character to take info from
   */
  public static Pet fromCharacter(Goose.NPC character) throws Exception {
    Pet pet = new Pet();
    pet.setPetID(Pet.getCurrentID());
    Pet.setCurrentID(Pet.getCurrentID() + 1);
    pet.setTitle("");
    pet.setSurname("");
    pet.setName(character.getName());
    pet.setLevel(character.getLevel());
    pet.setClass(character.getClas());
    pet.setClassID(character.getClassID());
    pet.setBaseStats(AttributeSet.add(character.getBaseStats(), new AttributeSet()));
    pet.getBaseStats().setHPPercentRegen(0);
    pet.getBaseStats().setHPStaticRegen(0);
    pet.getBaseStats().setMPPercentRegen(0);
    pet.getBaseStats().setMPStaticRegen(0);
    pet.setMaxStats(AttributeSet.add(pet.getBaseStats(), pet.getClas().getLevel(pet.getLevel())
        .getBaseStats()));
    pet.getMaxStats().setHaste(
        pet.getMaxStats().getHaste() + GameSettings.getDefault().getBaseHaste());
    pet.getMaxStats().setSpellDamage(
        pet.getMaxStats().getSpellDamage() + GameSettings.getDefault().getBaseSpellDamage());
    pet.getMaxStats().setSpellCrit(
        pet.getMaxStats().getSpellCrit() + GameSettings.getDefault().getBaseSpellCrit());
    pet.getMaxStats().setMeleeDamage(
        pet.getMaxStats().getMeleeDamage() + GameSettings.getDefault().getBaseMeleeDamage());
    pet.getMaxStats().setMeleeCrit(
        pet.getMaxStats().getMeleeCrit() + GameSettings.getDefault().getBaseMeleeCrit());
    pet.getMaxStats()
        .setDamageReduction(
            pet.getMaxStats().getDamageReduction()
                + GameSettings.getDefault().getBaseDamageReduction());
    pet.getMaxStats().setHPPercentRegen(
        pet.getMaxStats().getHPPercentRegen() + GameSettings.getDefault().getBaseHPPercentRegen());
    pet.getMaxStats().setHPStaticRegen(
        pet.getMaxStats().getHPStaticRegen() + GameSettings.getDefault().getBaseHPStaticRegen());
    pet.getMaxStats().setMPPercentRegen(
        pet.getMaxStats().getMPPercentRegen() + GameSettings.getDefault().getBaseMPPercentRegen());
    pet.getMaxStats().setMPStaticRegen(
        pet.getMaxStats().getMPStaticRegen() + GameSettings.getDefault().getBaseMPStaticRegen());
    pet.setCurrentHP(pet.getMaxStats().getHP());
    pet.setCurrentMP(pet.getMaxStats().getMP());
    pet.setExperience(pet.getClas().getLevel(pet.getLevel()).getExperience() / 2);
    pet.setExperienceSold(0);
    pet.setWeaponDamage(character.getWeaponDamage());
    pet.setRespawnTime(character.getRespawnTime());
    pet.setHairID(character.getHairID());
    pet.setHairR(character.getHairR());
    pet.setHairG(character.getHairG());
    pet.setHairB(character.getHairB());
    pet.setHairA(character.getHairA());
    pet.setFacing(character.getFacing());
    pet.setFaceID(character.getFaceID());
    pet.setBodyID(character.getBodyID());
    pet.setCurrentBodyID(pet.getBodyID());
    pet.setEquippedItems(character.getEquippedItems());
    pet.setBodyState(character.getBodyState());
    pet.setMoveSpeed(character.getMoveSpeed());
    pet.setAttackRange(character.getAttackRange());
    pet.setAttackSpeed(character.getAttackSpeed());
    pet.setAggroRange(character.getAggroRange());
    pet.setAutoCreatedNotSaved(true);
    pet.setDelete(false);
    return pet;
  }

  public static Pet fromReader(ResultSet resultSet, GameWorld world) throws Exception {
    Pet pet = new Pet();
    pet.setPetID(resultSet.getInt("pet_id"));
    pet.setTitle(resultSet.getString("pet_title"));
    pet.setName(resultSet.getString("pet_name"));
    pet.setSurname(resultSet.getString("pet_surname"));
    pet.setLevel(resultSet.getInt("pet_level"));
    pet.setClassID(resultSet.getInt("class_id"));
    pet.setClass(world.getClassHandler().getClass(pet.getClassID()));
    pet.setExperience(resultSet.getLong("experience"));
    pet.setExperienceSold(resultSet.getLong("experience_sold"));
    pet.setBodyID(resultSet.getInt("body_id"));
    pet.setCurrentBodyID(pet.getBodyID());
    pet.setFaceID(resultSet.getInt("face_id"));
    pet.setHairID(resultSet.getInt("hair_id"));
    pet.setHairR(resultSet.getInt("hair_r"));
    pet.setHairG(resultSet.getInt("hair_g"));
    pet.setHairB(resultSet.getInt("hair_b"));
    pet.setHairA(resultSet.getInt("hair_a"));
    pet.setBaseStats(new AttributeSet());
    pet.getBaseStats().setHP(resultSet.getInt("pet_hp"));
    pet.getBaseStats().setMP(resultSet.getInt("pet_mp"));
    pet.getBaseStats().setSP(resultSet.getInt("pet_sp"));
    pet.getBaseStats().setAC(resultSet.getInt("stat_ac"));
    pet.getBaseStats().setStrength(resultSet.getInt("stat_str"));
    pet.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
    pet.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
    pet.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
    pet.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
    pet.getBaseStats().setAirResist(resultSet.getInt("res_air"));
    pet.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
    pet.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
    pet.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
    pet.setMaxStats(new AttributeSet());
    pet.setMaxStats(AttributeSet.add(pet.getMaxStats(), pet.getBaseStats()));
    pet.getMaxStats().setHaste(
        pet.getMaxStats().getHaste() + GameSettings.getDefault().getBaseHaste());
    pet.getMaxStats().setSpellDamage(
        pet.getMaxStats().getSpellDamage() + GameSettings.getDefault().getBaseSpellDamage());
    pet.getMaxStats().setSpellCrit(
        pet.getMaxStats().getSpellCrit() + GameSettings.getDefault().getBaseSpellCrit());
    pet.getMaxStats().setMeleeDamage(
        pet.getMaxStats().getMeleeDamage() + GameSettings.getDefault().getBaseMeleeDamage());
    pet.getMaxStats().setMeleeCrit(
        pet.getMaxStats().getMeleeCrit() + GameSettings.getDefault().getBaseMeleeCrit());
    pet.getMaxStats()
        .setDamageReduction(
            pet.getMaxStats().getDamageReduction()
                + GameSettings.getDefault().getBaseDamageReduction());
    pet.getMaxStats().setHPPercentRegen(
        pet.getMaxStats().getHPPercentRegen() + GameSettings.getDefault().getBaseHPPercentRegen());
    pet.getMaxStats().setHPStaticRegen(
        pet.getMaxStats().getHPStaticRegen() + GameSettings.getDefault().getBaseHPStaticRegen());
    pet.getMaxStats().setMPPercentRegen(
        pet.getMaxStats().getMPPercentRegen() + GameSettings.getDefault().getBaseMPPercentRegen());
    pet.getMaxStats().setMPStaticRegen(
        pet.getMaxStats().getMPStaticRegen() + GameSettings.getDefault().getBaseMPStaticRegen());
    pet.setClass(world.getClassHandler().getClass(pet.getClassID()));
    pet.setMaxStats(AttributeSet.add(pet.getMaxStats(), pet.getClas().getLevel(pet.getLevel())
        .getBaseStats()));
    pet.setCurrentHP(pet.getMaxStats().getHP());
    pet.setCurrentMP(pet.getMaxStats().getMP());
    pet.setWeaponDamage(resultSet.getInt("weapon_damage"));
    pet.setRespawnTime(resultSet.getInt("respawn_time"));
    pet.setNextRespawnTime(resultSet.getLong("next_respawn_time"));
    pet.setEquippedItems(resultSet.getString("equipped_items"));
    pet.setBodyState(resultSet.getInt("body_state"));
    pet.setAggroRange(resultSet.getInt("aggro_range"));
    pet.setMoveSpeed(resultSet.getDouble("move_speed"));
    pet.setAttackRange(resultSet.getInt("attack_range"));
    pet.setAttackSpeed(resultSet.getDouble("attack_speed"));
    pet.setAutoCreatedNotSaved(false);
    pet.setDelete(false);
    if (pet.getPetID() >= Pet.getCurrentID()) {
      Pet.setCurrentID(pet.getPetID() + 1);
    }

    return pet;
  }

  public Pet() throws Exception {
    this.setBuffs(new ArrayList<Buff>());
  }

  public void saveToDatabase(GameWorld world) throws Exception {
    if (this.getAutoCreatedNotSaved()) {
      String query =
          "INSERT INTO pets (pet_id, pet_name, pet_title, pet_surname, "
              + "pet_facing, pet_level, experience, experience_sold, "
              + "pet_hp, pet_mp, pet_sp, class_id, stat_ac, stat_str, stat_sta, "
              + "stat_dex, stat_int, res_fire, res_water, res_spirit, res_air, res_earth, body_id, "
              + "face_id, hair_id, hair_r, hair_g, hair_b, hair_a, respawn_time, aggro_range, attack_speed, attack_range, "
              + "move_speed, body_state, equipped_items, weapon_damage, hp_percent_regen, hp_static_regen, "
              + "mp_percent_regen, mp_static_regen, owner_id) VALUES" + "("
              + this.getPetID()
              + ", "
              + "?"
              + ", "
              + "?"
              + ", "
              + "?"
              + ", "
              + this.getFacing()
              + ", "
              + this.getLevel()
              + ", "
              + this.getExperience()
              + ", "
              + this.getExperienceSold()
              + ", "
              + this.getBaseStats().getHP()
              + ", "
              + this.getBaseStats().getMP()
              + ", "
              + this.getBaseStats().getSP()
              + ", "
              + this.getClas().getClassID()
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
              + this.getBodyID()
              + ", "
              + this.getFaceID()
              + ", "
              + this.getHairID()
              + ", "
              + this.getHairR()
              + ", "
              + this.getHairG()
              + ", "
              + this.getHairB()
              + ", "
              + this.getHairA()
              + ", "
              + this.getRespawnTime()
              + ", "
              + this.getAggroRange()
              + ", "
              + this.getAttackSpeed()
              + ", "
              + this.getAttackRange()
              + ", "
              + this.getMoveSpeed()
              + ", "
              + this.getBodyState()
              + ", "
              + "'"
              + this.getEquippedItems()
              + "', "
              + this.getWeaponDamage()
              + ", "
              + this.getBaseStats().getHPPercentRegen()
              + ", "
              + this.getBaseStats().getHPStaticRegen()
              + ", "
              + this.getBaseStats().getMPPercentRegen()
              + ", "
              + this.getBaseStats().getMPStaticRegen() + ", " + this.getOwner().getPlayerID() + ")";
      PreparedStatement preparedStatement = world.getSqlConnection().prepareStatement(query);
      preparedStatement.setString(1, this.getName());
      preparedStatement.setString(2, this.getTitle());
      preparedStatement.setString(3, this.getSurname());
      preparedStatement.executeUpdate();
      this.setAutoCreatedNotSaved(false);
    } else if (this.getDelete()) {
      String query = "DELETE FROM pets WHERE pet_id=" + this.getPetID();
      world.getSqlConnection().createStatement().executeUpdate(query);

    } else {
      String query =
          "UPDATE pets SET " + "pet_name=" + "?" + ", " + "pet_title=" + "?" + ", "
              + "pet_surname=" + "?" + ", " + "pet_facing="
              + this.getFacing()
              + ", "
              + "pet_level="
              + this.getLevel()
              + ", "
              + "experience="
              + this.getExperience()
              + ", "
              + "experience_sold="
              + this.getExperienceSold()
              + ", "
              + "pet_hp="
              + this.getBaseStats().getHP()
              + ", "
              + "pet_mp="
              + this.getBaseStats().getMP()
              + ", "
              + "pet_sp="
              + this.getBaseStats().getSP()
              + ", "
              + "class_id="
              + this.getClassID()
              + ", "
              + "stat_ac="
              + this.getBaseStats().getAC()
              + ", "
              + "stat_str="
              + this.getBaseStats().getStrength()
              + ", "
              + "stat_sta="
              + this.getBaseStats().getStamina()
              + ", "
              + "stat_dex="
              + this.getBaseStats().getDexterity()
              + ", "
              + "stat_int="
              + this.getBaseStats().getIntelligence()
              + ", "
              + "res_fire="
              + this.getBaseStats().getFireResist()
              + ", "
              + "res_water="
              + this.getBaseStats().getWaterResist()
              + ", "
              + "res_spirit="
              + this.getBaseStats().getSpiritResist()
              + ", "
              + "res_air="
              + this.getBaseStats().getAirResist()
              + ", "
              + "res_earth="
              + this.getBaseStats().getEarthResist()
              + ", "
              + "body_id="
              + this.getBodyID()
              + ", "
              + "face_id="
              + this.getFaceID()
              + ", "
              + "hair_id="
              + this.getHairID()
              + ", "
              + "hair_r="
              + this.getHairR()
              + ", "
              + "hair_g="
              + this.getHairG()
              + ", "
              + "hair_b="
              + this.getHairB()
              + ", "
              + "hair_a="
              + this.getHairA()
              + ", "
              + "respawn_time="
              + this.getRespawnTime()
              + ", "
              + "aggro_range="
              + this.getAggroRange()
              + ", "
              + "attack_speed="
              + this.getAttackSpeed()
              + ", "
              + "attack_range="
              + this.getAttackRange()
              + ", "
              + "move_speed="
              + this.getMoveSpeed()
              + ", "
              + "body_state="
              + this.getBodyState()
              + ", "
              + "equipped_items='"
              + this.getEquippedItems()
              + "', "
              + "weapon_damage="
              + this.getWeaponDamage()
              + ", "
              + "hp_percent_regen="
              + this.getBaseStats().getHPPercentRegen()
              + ", "
              + "hp_static_regen="
              + this.getBaseStats().getHPStaticRegen()
              + ", "
              + "mp_percent_regen="
              + this.getBaseStats().getMPPercentRegen()
              + ", "
              + "mp_static_regen="
              + this.getBaseStats().getMPStaticRegen()
              + ", "
              + "owner_id="
              + this.getOwner().getPlayerID() + " " + "WHERE pet_id=" + this.getPetID();
      PreparedStatement preparedStatement = world.getSqlConnection().prepareStatement(query);
      preparedStatement.setString(1, this.getName());
      preparedStatement.setString(2, this.getTitle());
      preparedStatement.setString(3, this.getSurname());
      preparedStatement.executeUpdate();
    }
  }

  /**
   * Spawns this Pet
   * 
   * @param world
   */
  public void spawn(GameWorld world) throws Exception {
    this.destroy(world);
    this.setLoginID(getLoginID());
    Pet.LoginIDToPet.put(this.getLoginID(), this);
    this.setFacing(this.getOwner().getFacing());
    this.setCurrentHP(this.getMaxStats().getHP());
    this.setCurrentMP(this.getMaxStats().getMP());
    this.setCurrentSP(this.getMaxStats().getSP());
    this.setCurrentBodyID(this.getBodyID());
    this.setLastAttack(world.getTimeNow());
    // Fixes regen event
    this.setState(States.Ready);
    this.setMap(this.getOwner().getMap());
    this.setMapX(this.getOwner().getMapX());
    this.setMapY(this.getOwner().getMapY());
    this.getMap().placeCharacter(this);
    this.getMap().setCharacter(this, this.getMapX(), this.getMapY());
    this.getMap().addPlayer(this);
    this.setMode(Modes.Neutral);
    String packet = this.mKCString();
    List<Goose.Player> range = this.getMap().getPlayersInRange(this);
    for (Goose.Player player : range) {
      world.send(player, packet);
    }
    List<Goose.NPC> npcrange = this.getMap().getNPCsInRange(this);
    for (Goose.NPC npc : npcrange) {
      npc.aggroIfInRange(this, world);
    }
    this.addMoveEvent(world);
  }

  /**
   * Kills this Pet
   * 
   * @param world
   */
  // TODO: Broken Method.
  public void destroy(GameWorld world) throws Exception {
    if (!this.getIsAlive()) return;

    String erase = "ERC" + this.getLoginID();
    List<Goose.Player> oldrange = this.getMap().getPlayersInRange(this);
    for (Goose.Player player : oldrange) {
      world.send(player, erase);
    }
    List<Goose.NPC> oldnpcrange = this.getMap().getNPCsInRange(this);
    for (Goose.NPC npc : oldnpcrange) {
      npc.removeAggro(this);
    }
    List<Buff> removebuff = new ArrayList<Buff>();
    for (Buff b : this.getBuffs()) {
      if (!b.getItemBuff()) removebuff.add(b);

    }
    for (Buff b : removebuff) {
      this.removeBuff(b, world, false);
    }
    this.getMap().removePlayer(this);
    this.setState(States.NotLoggedIn);
    Pet.LoginIDToPet.remove(this.getLoginID());
    this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
    this.setMap(null);
  }

  public String mKCString() throws Exception {
    return "MKC" + this.getLoginID() + ",12," + this.getName() + "," + this.getTitle() + ","
        + this.getSurname() + "," + "" + "," + this.getMapX() + "," + this.getMapY() + ","
        + this.getFacing() + ","
        + (int) (((float) this.getCurrentHP() / this.getMaxStats().getHP()) * 100) + ","
        + this.getCurrentBodyID() + ","
        + (this.getCurrentBodyID() >= 100 ? 1 : this.getBodyState()) + ","
        + (this.getCurrentBodyID() >= 100 ? 0 : this.getHairID()) + "," + this.getEquippedItems()
        + "," + this.getHairR() + "," + this.getHairG() + "," + this.getHairB() + ","
        + this.getHairA() + "," + "0" + ","
        + (this.getCurrentBodyID() >= 100 ? 0 : this.getFaceID());
  }

  // Guild name
  // HP %
  // Invis thing
  public void addMoveEvent(GameWorld world) throws Exception {
    if (this.getMoveEvent() == null) {
      PetMoveEvent ev = new PetMoveEvent();
      ev.setTicks(ev.getTicks() + (long) (this.getMoveSpeed() * world.getTimerFrequency()));
      ev.setPlayer(this);
      world.getEventHandler().addEvent(ev);
      this.setMoveEvent(ev);
    }

  }

  /**
   * NextStepTo, returns direction to go to get to x,y
   * 
   * 1,2,3,4 = up,right,down,left
   * 
   */
  public int nextStepTo(int x, int y, GameWorld world) throws Exception {
    int nx, ny;
    int dx, dy;
    dx = x - this.getMapX();
    dy = y - this.getMapY();
    if (dx == 0 && dy == -1) return 1;

    if (dx == 0 && dy == 1) return 3;

    if (dx == 1 && dy == 0) return 2;

    if (dx == -1 && dy == 0) return 4;

    int shortestpath = Math.abs(x - (this.getMapX())) + Math.abs(y - (this.getMapY()));
    int shortest = 0;
    int temp;
    int f1 = 0, f2 = 0, f3 = 0, f4 = 0;
    int d1 = 0, d2 = 0, d3 = 0, d4 = 0;
    nx = this.getMapX();
    ny = this.getMapY() - 1;
    if ((temp = (Math.abs(x - nx) + Math.abs(y - ny))) <= shortestpath) {
      d1 = 1;
      if (this.canMoveTo(nx, ny)) {
        shortestpath = temp;
        shortest = 1;
        f1++;
      }

    }

    nx = this.getMapX() + 1;
    ny = this.getMapY();
    if ((temp = (Math.abs(x - nx) + Math.abs(y - ny))) <= shortestpath) {
      d2 = 1;
      if (this.canMoveTo(nx, ny)) {
        shortestpath = temp;
        shortest = 2;
        f2++;
      }

    }

    nx = this.getMapX();
    ny = this.getMapY() + 1;
    if ((temp = (Math.abs(x - nx) + Math.abs(y - ny))) <= shortestpath) {
      d3 = 1;
      if (this.canMoveTo(nx, ny)) {
        shortestpath = temp;
        shortest = 3;
        f3++;
      }

    }

    nx = this.getMapX() - 1;
    ny = this.getMapY();
    if ((temp = (Math.abs(x - nx) + Math.abs(y - ny))) <= shortestpath) {
      d4 = 1;
      if (this.canMoveTo(nx, ny)) {
        shortestpath = temp;
        shortest = 4;
        f4++;
      }

    }

    nx = this.getMapX();
    ny = this.getMapY();
    if ((f1 == f2) && (f1 == 1)) {
      shortest = (Math.abs(x - nx) > Math.abs(y - ny)) ? 2 : 1;
    }

    if ((f1 == f4) && (f1 == 1)) {
      shortest = (Math.abs(x - nx) > Math.abs(y - ny)) ? 4 : 1;
    }

    if ((f2 == f3) && (f2 == 1)) {
      shortest = (Math.abs(x - nx) > Math.abs(y - ny)) ? 2 : 3;
    }

    if ((f3 == f4) && (f3 == 1)) {
      shortest = (Math.abs(x - nx) > Math.abs(y - ny)) ? 4 : 3;
    }

    int rand = 0;
    if (shortestpath == Math.abs(x - (this.getMapX())) + Math.abs(y - (this.getMapY()))) {
      rand = world.getRandom().nextInt(2) + 1;
      if (d1 == 1) {
        shortest = (rand == 1) ? 2 : 4;
      }

      if (d2 == 1) {
        shortest = (rand == 1) ? 1 : 3;
      }

      if (d3 == 1) {
        shortest = (rand == 1) ? 2 : 4;
      }

      if (d4 == 1) {
        shortest = (rand == 1) ? 1 : 3;
      }

    }

    return shortest;
  }

  /**
   * FaceTo, faces to direction
   * 
   */
  public void faceTo(int direction, GameWorld world) throws Exception {
    if (this.getFacing() == direction) return;

    this.setFacing(direction);
    String packet = "CHH" + this.getLoginID() + "," + this.getFacing();
    List<Goose.Player> range = this.getMap().getPlayersInRange(this);
    for (Goose.Player player : range) {
      world.send(player, packet);
    }
  }

  public void addAttackEvent(GameWorld world) throws Exception {
    if (this.getAttackEvent() == null) {
      PetAttackEvent ev = new PetAttackEvent();
      ev.setTicks(ev.getTicks() + (long) (this.getAttackSpeed() * world.getTimerFrequency()));
      ev.setPlayer(this);
      world.getEventHandler().addEvent(ev);
      this.setAttackEvent(ev);
    }

  }

  /**
   * Player was attacked by character
   * 
   */
  public void attacked(ICharacter character, double damage, GameWorld world) throws Exception {
    if (!this.getIsAlive()) return;

    List<Goose.Player> range = this.getMap().getPlayersInRange(this);
    String packet;
    if (damage == 0) {
      packet = "BT" + this.getLoginID() + ",21,," + character.getName();
      for (Goose.Player p : range) {
        world.send(p, packet);
      }
      return;
    }

    double dodge = this.getMaxStats().getDexterity() / 100.0;
    if (dodge > 50) dodge = 50;

    if (world.getRandom().nextInt(10001) <= dodge * 100) {
      packet = "BT" + this.getLoginID() + ",20,," + character.getName();
      for (Goose.Player p : range) {
        world.send(p, packet);
      }
      return;
    }

    int dmg = ((int) (damage));
    if (damage > 0) {
      // pvp 1/2 damage
      if (character instanceof Goose.Player) dmg /= 2;

      packet = "BT" + this.getLoginID() + ",1," + (-dmg) + "," + character.getName() + "\u0001";
    } else {
      packet = "BT" + this.getLoginID() + ",7,+" + (-dmg) + "," + character.getName() + "\u0001";
    }
    this.setCurrentHP(this.getCurrentHP() - dmg);
    if (this.getCurrentHP() <= 0) {
      this.destroy(world);
    } else {
      packet += this.vCString();
      this.addRegenEvent(world);
    }
    for (Goose.Player p : range) {
      world.send(p, packet);
    }
  }

  public void addExperience(long exp, GameWorld world, ExperienceMessage message) throws Exception {
    if (GameSettings.getDefault().getExperienceCap() > 0
        && this.getExperience() + this.getExperienceSold() > GameSettings.getDefault()
            .getExperienceCap()) {
      return;
    }

    // Experience modifier for everyone under the limit
    if (!(GameSettings.getDefault().getExperienceModifierLimit() > 0 && this.getExperience()
        + this.getExperienceSold() > GameSettings.getDefault().getExperienceModifierLimit())) {
      exp = (long) (exp * GameSettings.getDefault().getExperienceModifier());
    }

    this.setExperience(this.getExperience() + exp);
    long levelup;
    int levels = 0;
    int i = this.getLevel();
    ClassLevel level = this.getClas().getLevel(i);
    while (this.getClas().getLevel(i) != null) {
      levelup = level.getExperience();
      if (levelup == 0) break;

      if (this.getExperience() >= levelup) {
        levels++;
      } else {
        break;
      }
      i++;
      level = this.getClas().getLevel(i);
    }
    if (levels == 0) {
      return;
    }

    this.removeStats(this.getClas().getLevel(this.getLevel()).getBaseStats(), world);
    this.setLevel(this.getLevel() + levels);
    this.addStats(this.getClas().getLevel(this.getLevel()).getBaseStats(), world);
    this.setCurrentHP(this.getMaxStats().getHP());
    this.setCurrentMP(this.getMaxStats().getMP());
    this.setWeaponDamage(this.getWeaponDamage()
        + ((levels * 3) + (world.getRandom().nextInt(3) * levels)));
    List<Goose.Player> range = this.getMap().getPlayersInRange(this);
    String packet = "BT" + this.getLoginID() + ",60,Level Up!," + this.getName();
    world.send(this, packet);
    for (Goose.Player player : range) {
      world.send(player, packet);
    }
  }

  /**
   * WPSSTring, weapon speed string Overrides to fix a bug since pets don't have a weapondelay
   * 
   */
  public String wPSString() throws Exception {
    int wps = (int) (this.getAttackSpeed() * 1000);
    return "WPS" + wps;
  }

  public int getWeaponDelay() throws Exception {
    return (int) this.getAttackSpeed();
  }

  public void setWeaponDelay(int value) throws Exception {}

}
