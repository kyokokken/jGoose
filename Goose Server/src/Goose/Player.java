package Goose;

import Goose.Events.BuffExpireEvent;
import Goose.Events.BuffTickEvent;
import Goose.Events.PlayerSaveEvent;
import Goose.Events.RegenEvent;
import Goose.Inventory.EquipSlots;
import Goose.Spell.SpellTargets;
import Goose.SpellEffect.EffectTypes;
import Goose.Window.WindowTypes;
import org.apache.commons.codec.binary.Base64;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Player,
 * <p>
 * Implements the ICharacter interface
 */
public class Player implements ICharacter {
    /**
     * Networking stuff
     * <p>
     * Socket and receive buffer
     */
    Socket sock;

    public Socket getSock() throws Exception {
        return this.sock;
    }

    public void setSock(Socket value) throws Exception {
        this.sock = value;
    }

    private String __Buffer;

    public String getBuffer() {
        return __Buffer;
    }

    public void setBuffer(String value) {
        __Buffer = value;
    }

    public enum ExperienceMessage {
        /**
         * ExperienceMessage, used for sending to AddExperience to diaplay the right message when
         * gaining experience
         */
        TooLow, TooHigh, TooFarAway, Normal
    }

    public enum States {
        /**
         * Player's state
         */
        NotLoggedIn, LoadingGame, LoadingMap, Ready
    }

    private Goose.Player.States __State = Goose.Player.States.NotLoggedIn;

    public Goose.Player.States getState() {
        return __State;
    }

    public void setState(Goose.Player.States value) {
        __State = value;
    }

    public enum AccessStatus {
        /**
         * Player account access status
         */
        Deleted, Banned, Normal, __dummyEnum__0, __dummyEnum__1, __dummyEnum__2, __dummyEnum__3, __dummyEnum__4, __dummyEnum__5, GameMaster
    }

    private Goose.Player.AccessStatus __Access = Goose.Player.AccessStatus.Deleted;

    public Goose.Player.AccessStatus getAccess() {
        return __Access;
    }

    public void setAccess(Goose.Player.AccessStatus value) {
        __Access = value;
    }

    /**
     * Temporary information used when a player is autocreated
     * <p>
     * AutoCreatedNotSaved - When a player is autocreated they're not saved until the player save
     * event is run
     */
    private boolean __AutoCreatedNotSaved;

    public boolean getAutoCreatedNotSaved() {
        return __AutoCreatedNotSaved;
    }

    public void setAutoCreatedNotSaved(boolean value) {
        __AutoCreatedNotSaved = value;
    }

    /**
     * Player info
     *
     */
    /**
     * PlayerID is the ID of the character in the database
     */
    private int __PlayerID;

    public int getPlayerID() {
        return __PlayerID;
    }

    public void setPlayerID(int value) {
        __PlayerID = value;
    }

    /**
     * Character name
     */
    private String __Name;

    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    /**
     * Name prefix
     */
    private String __Title;

    public String getTitle() {
        return __Title;
    }

    public void setTitle(String value) {
        __Title = value;
    }

    /**
     * Name postfix
     */
    private String __Surname;

    public String getSurname() {
        return __Surname;
    }

    public void setSurname(String value) {
        __Surname = value;
    }

    /**
     * md5 password hash
     */
    private String __PasswordHash;

    public String getPasswordHash() {
        return __PasswordHash;
    }

    public void setPasswordHash(String value) {
        __PasswordHash = value;
    }

    /**
     * NOTE: Salt is stored base64 encoded
     */
    private String __PasswordSalt;

    public String getPasswordSalt() {
        return __PasswordSalt;
    }

    public void setPasswordSalt(String value) {
        __PasswordSalt = value;
    }

    /**
     * LoginID is the ID assigned by the server on login
     */
    private int __LoginID;

    public int getLoginID() throws Exception {
        return __LoginID;
    }

    public void setLoginID(int value) {
        __LoginID = value;
    }

    /**
     * Current map id
     */
    private int __MapID;

    public int getMapID() {
        return __MapID;
    }

    public void setMapID(int value) {
        __MapID = value;
    }

    /**
     * Current map x
     */
    private int __MapX;

    public int getMapX() {
        return __MapX;
    }

    public void setMapX(int value) {
        __MapX = value;
    }

    /**
     * Current map y
     */
    private int __MapY;

    public int getMapY() {
        return __MapY;
    }

    public void setMapY(int value) {
        __MapY = value;
    }

    /**
     * Current Map object
     */
    private Map __Map;

    public Map getMap() {
        return __Map;
    }

    public void setMap(Map value) {
        __Map = value;
    }

    /**
     * Facing direction
     */
    private int __Facing;

    public int getFacing() {
        return __Facing;
    }

    public void setFacing(int value) {
        __Facing = value;
    }

    /**
     * BaseStats, stats loaded from database
     */
    private AttributeSet __BaseStats;

    public AttributeSet getBaseStats() {
        return __BaseStats;
    }

    public void setBaseStats(AttributeSet value) {
        __BaseStats = value;
    }

    /**
     * Stats from base, items, buffs
     */
    private AttributeSet __MaxStats;

    public AttributeSet getMaxStats() {
        return __MaxStats;
    }

    public void setMaxStats(AttributeSet value) {
        __MaxStats = value;
    }

    /**
     * Current HP
     */
    int currentHP;

    public int getCurrentHP() throws Exception {
        return this.currentHP;
    }

    public void setCurrentHP(int value) throws Exception {
        this.currentHP = value;
        if (this.currentHP > this.getMaxStats().getHP()) this.currentHP = this.getMaxStats().getHP();

    }

    /**
     * Current MP
     */
    int currentMP;

    public int getCurrentMP() throws Exception {
        return this.currentMP;
    }

    public void setCurrentMP(int value) throws Exception {
        currentMP = value;
        if (this.currentMP > this.getMaxStats().getMP()) this.currentMP = this.getMaxStats().getMP();

    }

    /**
     * Current SP
     */
    int currentSP;

    public int getCurrentSP() throws Exception {
        return this.currentSP;
    }

    public void setCurrentSP(int value) throws Exception {
        this.currentSP = value;
        if (this.currentSP > this.getMaxStats().getSP()) this.currentSP = this.getMaxStats().getSP();

    }

    /**
     * Bound/respawn map id
     */
    private int __BoundID;

    public int getBoundID() {
        return __BoundID;
    }

    public void setBoundID(int value) {
        __BoundID = value;
    }

    /**
     * Bound/respawn map x
     */
    private int __BoundX;

    public int getBoundX() {
        return __BoundX;
    }

    public void setBoundX(int value) {
        __BoundX = value;
    }

    /**
     * Bound/respawn map y
     */
    private int __BoundY;

    public int getBoundY() {
        return __BoundY;
    }

    public void setBoundY(int value) {
        __BoundY = value;
    }

    /**
     * Bound Map
     */
    private Map __BoundMap;

    public Map getBoundMap() {
        return __BoundMap;
    }

    public void setBoundMap(Map value) {
        __BoundMap = value;
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

    /**
     * Current Body ID
     */
    private int __CurrentBodyID;

    public int getCurrentBodyID() {
        return __CurrentBodyID;
    }

    public void setCurrentBodyID(int value) {
        __CurrentBodyID = value;
    }

    /**
     * Body state/pose
     */
    private int __BodyState;

    public int getBodyState() {
        return __BodyState;
    }

    public void setBodyState(int value) {
        __BodyState = value;
    }

    /**
     * Gold
     */
    private long __Gold;

    public long getGold() {
        return __Gold;
    }

    public void setGold(long value) {
        __Gold = value;
    }

    /**
     * Experience
     */
    private long __Experience;

    public long getExperience() {
        return __Experience;
    }

    public void setExperience(long value) {
        __Experience = value;
    }

    /**
     * Experience sold
     */
    private long __ExperienceSold;

    public long getExperienceSold() {
        return __ExperienceSold;
    }

    public void setExperienceSold(long value) {
        __ExperienceSold = value;
    }

    /**
     * Level
     */
    private int __Level;

    public int getLevel() {
        return __Level;
    }

    public void setLevel(int value) {
        __Level = value;
    }

    /**
     * Class ID
     */
    private int __ClassID;

    public int getClassID() {
        return __ClassID;
    }

    public void setClassID(int value) {
        __ClassID = value;
    }

    /**
     * Guild ID
     */
    private int __GuildID;

    public int getGuildID() {
        return __GuildID;
    }

    public void setGuildID(int value) {
        __GuildID = value;
    }

    /**
     * Guild object
     */
    private Guild __Guild;

    public Guild getGuild() {
        return __Guild;
    }

    public void setGuild(Guild value) {
        __Guild = value;
    }

    /**
     * Class object
     */
    private Class __Class;

    public Class getClas() {
        return __Class;
    }

    public void setClass(Class value) {
        __Class = value;
    }

    /**
     * So regen event doesn't double up
     */
    private boolean __RegenEventExists;

    public boolean getRegenEventExists() {
        return __RegenEventExists;
    }

    public void setRegenEventExists(boolean value) {
        __RegenEventExists = value;
    }

    /**
     * Player's inventory
     */
    private Inventory __Inventory;

    public Inventory getInventory() {
        return __Inventory;
    }

    public void setInventory(Inventory value) {
        __Inventory = value;
    }

    /**
     * Time of last melee attack
     */
    private long __LastAttack;

    public long getLastAttack() {
        return __LastAttack;
    }

    public void setLastAttack(long value) {
        __LastAttack = value;
    }

    /**
     * For ping timeout
     */
    private long __LastPing;

    public long getLastPing() {
        return __LastPing;
    }

    public void setLastPing(long value) {
        __LastPing = value;
    }

    /**
     * Holds players spells
     */
    private Spellbook __Spellbook;

    public Spellbook getSpellbook() {
        return __Spellbook;
    }

    public void setSpellbook(Spellbook value) {
        __Spellbook = value;
    }

    /**
     * Buffs, holds players buffs
     */
    private List<Buff> __Buffs;

    public List<Buff> getBuffs() {
        return __Buffs;
    }

    public void setBuffs(List<Buff> value) {
        __Buffs = value;
    }

    /**
     * The group the player is in
     * <p>
     * If none is null.
     */
    private Group __Group;

    public Group getGroup() {
        return __Group;
    }

    public void setGroup(Group value) {
        __Group = value;
    }

    private boolean __GroupInvitesEnabled;

    public boolean getGroupInvitesEnabled() {
        return __GroupInvitesEnabled;
    }

    public void setGroupInvitesEnabled(boolean value) {
        __GroupInvitesEnabled = value;
    }

    private int __LastWindowID;

    public int getLastWindowID() {
        return __LastWindowID;
    }

    public void setLastWindowID(int value) {
        __LastWindowID = value;
    }

    private List<Window> __Windows;

    public List<Window> getWindows() {
        return __Windows;
    }

    public void setWindows(List<Window> value) {
        __Windows = value;
    }

    private long __MovementRecordingStarted;

    public long getMovementRecordingStarted() {
        return __MovementRecordingStarted;
    }

    public void setMovementRecordingStarted(long value) {
        __MovementRecordingStarted = value;
    }

    private long __MovementRecordingSteps;

    public long getMovementRecordingSteps() {
        return __MovementRecordingSteps;
    }

    public void setMovementRecordingSteps(long value) {
        __MovementRecordingSteps = value;
    }

    public enum ToggleSetting {
        /**
         * Bitfield for toggled settings
         */
        __dummyEnum__0(0), Experience(1), Tell(2), __dummyEnum__1(0), WordFilter(4);
        private final int value;

        private ToggleSetting(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private ToggleSetting __ToggleSettings = ToggleSetting.Experience;

    public ToggleSetting getToggleSettings() {
        return __ToggleSettings;
    }

    public void setToggleSettings(ToggleSetting value) {
        __ToggleSettings = value;
    }

    public boolean getChatFilterEnabled() throws Exception {
        return ((this.getToggleSettings().getValue() & ToggleSetting.WordFilter.getValue()) == 0);
    }

    private double __AetherThreshold;

    public double getAetherThreshold() {
        return __AetherThreshold;
    }

    public void setAetherThreshold(double value) {
        __AetherThreshold = value;
    }

    /**
     * Holds all of the Player's pets
     */
    private List<Pet> __Pets;

    public List<Pet> getPets() {
        return __Pets;
    }

    public void setPets(List<Pet> value) {
        __Pets = value;
    }

    /**
     * Constructor
     */
    public Player(int unused) throws Exception {
        this.setBuffer("");
        this.setLastAttack(0);
        this.setLastPing(0);
        this.setLastWindowID(1000);
        this.setWindows(new ArrayList<Window>());
        this.setState(Goose.Player.States.NotLoggedIn);
        this.setBuffs(new ArrayList<Buff>());
        this.setPets(new ArrayList<Pet>());
        this.setGroupInvitesEnabled(false);
        this.setMovementRecordingSteps(0);
    }

    public Player() {
    }

    public Player(Socket sock) throws Exception {
        this.sock = sock;
    }

    /**
     * Received, received data from player
     * <p>
     * Adds the data to the receive buffer
     */
    public void received(String data) throws Exception {
        this.setBuffer(this.getBuffer() + data);
    }

    /**
     * MKCString, returns the MKC packet string for this character
     * <p>
     * MKCid,character type,name,title,surname,guild,x,y,facing,hp percent,body, body pose,hair
     * id,chest id,chest r,g,b,a,helm id,helm r,g,b,a, pants id,pants r,g,b,a,shoes id,shoes r,g,b,a,
     * shield id,shield r,g,b,a,weapon id,weapon r,g,b,a,hair_r,hair_g,hair_b,hair_a,invis,head
     * <p>
     * character type = 1 for player, 2 for regular npc, some others for banker and vendors will find
     * later hair id = 20ish for the normal hairs head = 70-73 for faces body pose/state = 1 for
     * normal, 3 for staff, 4 for sword body = values 100-166 are illusions, 1 is male, 11 is female.
     * 2/12 are naga. 3 is skeleton invis = not sure at moment
     * <p>
     * For item r,g,b,a of 0,0,0,0 you can use * instead
     */
    public String mKCString() throws Exception {
        int pose = this.getBodyState();
        ItemSlot weapon = this.getInventory().getEquippedSlot(EquipSlots.Weapon);
        if (weapon != null) {
            pose = weapon.getItem().getBodyState();
        }

        return "MKC"
                + this.getLoginID()
                // + ",5,"
                + "," + (this.getAccess() == AccessStatus.GameMaster ? "12" : "5") + ","
                + (this.getAccess() == AccessStatus.GameMaster ? "GM " : "") + this.getName() + ","
                + this.getTitle() + "," + this.getSurname() + "," + "" + "," + this.getMapX() + ","
                + this.getMapY() + "," + this.getFacing() + ","
                + (int) (((float) this.getCurrentHP() / this.getMaxStats().getHP()) * 100) + ","
                + this.getCurrentBodyID() + "," + (this.getCurrentBodyID() >= 100 ? 1 : pose) + ","
                + (this.getCurrentBodyID() >= 100 ? 0 : this.getHairID()) + ","
                + this.getInventory().equippedDisplay() + this.getHairR() + "," + this.getHairG() + ","
                + this.getHairB() + "," + this.getHairA() + "," + "0" + ","
                + (this.getCurrentBodyID() >= 100 ? 0 : this.getFaceID());
    }

    // Guild name
    // HP %
    // Note: equippedDisplay() adds it's own , on end
    // Invis thing

    /**
     * LoadFromAutoCreate, fills in player info from server defaults
     */
    public void loadFromAutoCreate(String name, String password, GameWorld world) throws Exception {
        byte[] saltBytes = new byte[16];
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(saltBytes);
        String base64Salt = Base64.encodeBase64String(saltBytes);
        String salt = new String(base64Salt.getBytes(), StandardCharsets.US_ASCII);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] data = (salt + password + GameSettings.getDefault().getServerName()).getBytes();
        data = md5.digest(data);
        String passwordHash = new String(data);
        passwordHash = passwordHash.replace("-", "").toLowerCase();
        passwordHash = passwordHash.replace("'", "").toLowerCase();
        this.setAutoCreatedNotSaved(true);
        this.setPlayerID(world.getPlayerHandler().getCurrentID());
        world.getPlayerHandler().setCurrentID(world.getPlayerHandler().getCurrentID() + 1);
        this.setName(name);
        this.setTitle(GameSettings.getDefault().getStartingTitle());
        this.setSurname(GameSettings.getDefault().getStartingSurname());
        this.setPasswordHash(passwordHash);
        this.setPasswordSalt(base64Salt);
        this.setAccess(Goose.Player.AccessStatus.Normal);
        this.setMapID(GameSettings.getDefault().getStartingMapID());
        this.setMapX(GameSettings.getDefault().getStartingMapX());
        this.setMapY(GameSettings.getDefault().getStartingMapY());
        this.setFacing(2);
        this.setBoundID(GameSettings.getDefault().getStartingMapID());
        this.setBoundX(GameSettings.getDefault().getStartingMapX());
        this.setBoundY(GameSettings.getDefault().getStartingMapY());
        this.setBoundMap(world.getMapHandler().getMap(this.getBoundID()));
        this.setGold(GameSettings.getDefault().getStartingGold());
        this.setLevel(GameSettings.getDefault().getStartingLevel());
        this.setClassID(GameSettings.getDefault().getStartingClassID());
        this.setGuildID(GameSettings.getDefault().getStartingGuildID());
        this.setGuild(world.getGuildHandler().getGuild(this.getGuildID()));
        this.setExperience(GameSettings.getDefault().getStartingExperience());
        this.setExperienceSold(GameSettings.getDefault().getStartingExperienceSold());
        this.setBodyID(GameSettings.getDefault().getStartingBodyID());
        this.setCurrentBodyID(this.getBodyID());
        this.setFaceID(GameSettings.getDefault().getStartingFaceID());
        this.setHairID(GameSettings.getDefault().getStartingHairID());
        this.setHairR(GameSettings.getDefault().getStartingHairR());
        this.setHairG(GameSettings.getDefault().getStartingHairG());
        this.setHairB(GameSettings.getDefault().getStartingHairB());
        this.setHairA(GameSettings.getDefault().getStartingHairA());
        this.setBaseStats(new AttributeSet());
        this.getBaseStats().setHP(GameSettings.getDefault().getStartingHP());
        this.getBaseStats().setMP(GameSettings.getDefault().getStartingMP());
        this.getBaseStats().setSP(GameSettings.getDefault().getStartingSP());
        this.getBaseStats().setAC(GameSettings.getDefault().getStartingAC());
        this.getBaseStats().setStrength(GameSettings.getDefault().getStartingStrength());
        this.getBaseStats().setStamina(GameSettings.getDefault().getStartingStamina());
        this.getBaseStats().setIntelligence(GameSettings.getDefault().getStartingIntelligence());
        this.getBaseStats().setDexterity(GameSettings.getDefault().getStartingDexterity());
        this.getBaseStats().setFireResist(GameSettings.getDefault().getStartingFireResist());
        this.getBaseStats().setAirResist(GameSettings.getDefault().getStartingAirResist());
        this.getBaseStats().setEarthResist(GameSettings.getDefault().getStartingEarthResist());
        this.getBaseStats().setSpiritResist(GameSettings.getDefault().getStartingSpiritResist());
        this.getBaseStats().setWaterResist(GameSettings.getDefault().getStartingWaterResist());
        this.setMaxStats(new AttributeSet());
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getBaseStats()));
        this.getMaxStats().setHaste(GameSettings.getDefault().getBaseHaste());
        this.getMaxStats().setSpellDamage(GameSettings.getDefault().getBaseSpellDamage());
        this.getMaxStats().setSpellCrit(GameSettings.getDefault().getBaseSpellCrit());
        this.getMaxStats().setMeleeDamage(GameSettings.getDefault().getBaseMeleeDamage());
        this.getMaxStats().setMeleeCrit(GameSettings.getDefault().getBaseMeleeCrit());
        this.getMaxStats().setDamageReduction(GameSettings.getDefault().getBaseDamageReduction());
        this.getMaxStats().setHPPercentRegen(GameSettings.getDefault().getBaseHPPercentRegen());
        this.getMaxStats().setHPStaticRegen(GameSettings.getDefault().getBaseHPStaticRegen());
        this.getMaxStats().setMPPercentRegen(GameSettings.getDefault().getBaseMPPercentRegen());
        this.getMaxStats().setMPStaticRegen(GameSettings.getDefault().getBaseMPStaticRegen());
        this.setClass(world.getClassHandler().getClass(this.getClassID()));
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getClas().getLevel(this.getLevel())
                .getBaseStats()));
        this.setBodyState(1);
        this.setAetherThreshold(GameSettings.getDefault().getDefaultAetherThreshold());
        this.setInventory(new Inventory(this));
        String[] items = GameSettings.getDefault().getStartingItems().split(" ");
        if (items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                try {
                    int templateid = Integer.valueOf(items[i]);
                    ItemTemplate template = world.getItemHandler().getTemplate(templateid);
                    if (template == null) {
                        continue;
                    }

                    // log bad id in starting items
                    Item item = new Item();
                    item.loadFromTemplate(template);
                    world.getItemHandler().addItem(item);
                    if (!this.getInventory().addItem(item, 1, world)) {
                    }

                } catch (Exception __dummyCatchVar0) {
                }

            }
        }

        // log not enough inventory space for starting items
        // eaten
        // log bad id in starting items
        this.setSpellbook(new Spellbook(this));
    }

    /**
     * LoadFromReader, loads player info from a Sq1DataReader
     * <p>
     * Takes the password to hash with the salt from dataabase first to check if the password is
     * correct.
     * <p>
     * Closes the reader
     * <p>
     * Returns a LoginEvent.LoginMessages value indicating Banned/WrongPassword or Success
     */
    public Goose.Events.LoginEvent.LoginMessages loadFromReaderOld(String password, GameWorld world,
                                                                   ResultSet resultSet) throws Exception {
        this.setAccess((Goose.Player.AccessStatus.values()[resultSet.getInt("access_status")]));
        if (this.getAccess() == Goose.Player.AccessStatus.Banned) {
            resultSet.close();
            return Goose.Events.LoginEvent.LoginMessages.Banned;
        }

        String databaseHash = resultSet.getString("password_hash");
        String base64Salt = resultSet.getString("password_salt");
        String salt = new String(base64Salt.getBytes(), StandardCharsets.US_ASCII);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] data = (salt + password + GameSettings.getDefault().getServerName()).getBytes();
        data = md5.digest(data);
        String passwordHash = new String(data);
        passwordHash = passwordHash.replace("-", "").toLowerCase();
        // Incorrect password
        if (!databaseHash.equals(passwordHash)) {
            resultSet.close();
            return Goose.Events.LoginEvent.LoginMessages.WrongPassword;
        }

        this.setAutoCreatedNotSaved(false);
        this.setPlayerID(resultSet.getInt("player_id"));
        this.setName(resultSet.getString("player_name"));
        this.setTitle(resultSet.getString("player_title"));
        this.setSurname(resultSet.getString("player_surname"));
        this.setPasswordHash(databaseHash);
        this.setPasswordSalt(base64Salt);
        this.setMapID(resultSet.getInt("map_id"));
        this.setMapX(resultSet.getInt("map_x"));
        this.setMapY(resultSet.getInt("map_y"));
        this.setFacing(resultSet.getInt("player_facing"));
        this.setBoundID(resultSet.getInt("bound_id"));
        this.setBoundX(resultSet.getInt("bound_x"));
        this.setBoundY(resultSet.getInt("bound_y"));
        this.setBoundMap(world.getMapHandler().getMap(this.getBoundID()));
        this.setGold(resultSet.getLong("player_gold"));
        this.setLevel(resultSet.getInt("player_level"));
        this.setClassID(resultSet.getInt("class_id"));
        this.setGuildID(resultSet.getInt("guild_id"));
        this.setGuild(world.getGuildHandler().getGuild(this.getGuildID()));
        this.setExperience(resultSet.getLong("experience"));
        this.setExperienceSold(resultSet.getLong("experience_sold"));
        this.setBodyID(resultSet.getInt("body_id"));
        this.setCurrentBodyID(this.getBodyID());
        this.setFaceID(resultSet.getInt("face_id"));
        this.setHairID(resultSet.getInt("hair_id"));
        this.setHairR(resultSet.getInt("hair_r"));
        this.setHairG(resultSet.getInt("hair_g"));
        this.setHairB(resultSet.getInt("hair_b"));
        this.setHairA(resultSet.getInt("hair_a"));
        this.setBaseStats(new AttributeSet());
        this.getBaseStats().setHP(resultSet.getInt("player_hp"));
        this.getBaseStats().setMP(resultSet.getInt("player_mp"));
        this.getBaseStats().setSP(resultSet.getInt("player_sp"));
        this.getBaseStats().setAC(resultSet.getInt("stat_ac"));
        this.getBaseStats().setStrength(resultSet.getInt("stat_str"));
        this.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
        this.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
        this.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
        this.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
        this.getBaseStats().setAirResist(resultSet.getInt("res_air"));
        this.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
        this.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
        this.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
        this.setMaxStats(new AttributeSet());
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getBaseStats()));
        this.getMaxStats().setHaste(GameSettings.getDefault().getBaseHaste());
        this.getMaxStats().setSpellDamage(GameSettings.getDefault().getBaseSpellDamage());
        this.getMaxStats().setSpellCrit(GameSettings.getDefault().getBaseSpellCrit());
        this.getMaxStats().setMeleeDamage(GameSettings.getDefault().getBaseMeleeDamage());
        this.getMaxStats().setMeleeCrit(GameSettings.getDefault().getBaseMeleeCrit());
        this.getMaxStats().setDamageReduction(GameSettings.getDefault().getBaseDamageReduction());
        this.getMaxStats().setHPPercentRegen(GameSettings.getDefault().getBaseHPPercentRegen());
        this.getMaxStats().setHPStaticRegen(GameSettings.getDefault().getBaseHPStaticRegen());
        this.getMaxStats().setMPPercentRegen(GameSettings.getDefault().getBaseMPPercentRegen());
        this.getMaxStats().setMPStaticRegen(GameSettings.getDefault().getBaseMPStaticRegen());
        this.setClass(world.getClassHandler().getClass(this.getClassID()));
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getClas().getLevel(this.getLevel())
                .getBaseStats()));
        this.setCurrentHP(((int) ((this.getMaxStats().getHP() * 0.8))));
        this.setCurrentMP(((int) ((this.getMaxStats().getMP() * 0.8))));
        this.setCurrentSP(this.getMaxStats().getSP());
        this.setAetherThreshold(resultSet.getDouble("aether_threshold"));
        // Close rs here so inventory can create it's own rs
        resultSet.close();
        this.setInventory(new Inventory(this));
        this.getInventory().load(world);
        this.setSpellbook(new Spellbook(this));
        this.getSpellbook().load(world);
        this.setBodyState(1);
        this.loadPets(world);
        this.addSaveEvent(world);
        return Goose.Events.LoginEvent.LoginMessages.Success;
    }

    /**
     * LoadFromReader, loads player info from a Sq1DataReader
     */
    public void loadFromReader(GameWorld world, ResultSet resultSet) throws Exception {
        this.setAccess(Goose.Player.AccessStatus.values()[resultSet.getInt("access_status")]);
        String databaseHash = resultSet.getString("password_hash");
        String base64Salt = resultSet.getString("password_salt");
        this.setAutoCreatedNotSaved(false);
        this.setPlayerID(resultSet.getInt("player_id"));
        this.setName(resultSet.getString("player_name"));
        this.setTitle(resultSet.getString("player_title"));
        this.setSurname(resultSet.getString("player_surname"));
        this.setPasswordHash(databaseHash);
        this.setPasswordSalt(base64Salt);
        this.setMapID(resultSet.getInt("map_id"));
        this.setMapX(resultSet.getInt("map_x"));
        this.setMapY(resultSet.getInt("map_y"));
        this.setFacing(resultSet.getInt("player_facing"));
        this.setBoundID(resultSet.getInt("bound_id"));
        this.setBoundX(resultSet.getInt("bound_x"));
        this.setBoundY(resultSet.getInt("bound_y"));
        this.setBoundMap(world.getMapHandler().getMap(this.getBoundID()));
        this.setGold(resultSet.getLong("player_gold"));
        this.setLevel(resultSet.getInt("player_level"));
        this.setClassID(resultSet.getInt("class_id"));
        this.setGuildID(resultSet.getInt("guild_id"));
        this.setGuild(world.getGuildHandler().getGuild(this.getGuildID()));
        this.setExperience(resultSet.getLong("experience"));
        this.setExperienceSold(resultSet.getLong("experience_sold"));
        this.setBodyID(resultSet.getInt("body_id"));
        this.setCurrentBodyID(this.getBodyID());
        this.setFaceID(resultSet.getInt("face_id"));
        this.setHairID(resultSet.getInt("hair_id"));
        this.setHairR(resultSet.getInt("hair_r"));
        this.setHairG(resultSet.getInt("hair_g"));
        this.setHairB(resultSet.getInt("hair_b"));
        this.setHairA(resultSet.getInt("hair_a"));
        this.setBaseStats(new AttributeSet());
        this.getBaseStats().setHP(resultSet.getInt("player_hp"));
        this.getBaseStats().setMP(resultSet.getInt("player_mp"));
        this.getBaseStats().setSP(resultSet.getInt("player_sp"));
        this.getBaseStats().setAC(resultSet.getInt("stat_ac"));
        this.getBaseStats().setStrength(resultSet.getInt("stat_str"));
        this.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
        this.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
        this.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
        this.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
        this.getBaseStats().setAirResist(resultSet.getInt("res_air"));
        this.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
        this.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
        this.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
        this.setMaxStats(new AttributeSet());
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getBaseStats()));
        this.getMaxStats().setHaste(GameSettings.getDefault().getBaseHaste());
        this.getMaxStats().setSpellDamage(GameSettings.getDefault().getBaseSpellDamage());
        this.getMaxStats().setSpellCrit(GameSettings.getDefault().getBaseSpellCrit());
        this.getMaxStats().setMeleeDamage(GameSettings.getDefault().getBaseMeleeDamage());
        this.getMaxStats().setMeleeCrit(GameSettings.getDefault().getBaseMeleeCrit());
        this.getMaxStats().setDamageReduction(GameSettings.getDefault().getBaseDamageReduction());
        this.getMaxStats().setHPPercentRegen(GameSettings.getDefault().getBaseHPPercentRegen());
        this.getMaxStats().setHPStaticRegen(GameSettings.getDefault().getBaseHPStaticRegen());
        this.getMaxStats().setMPPercentRegen(GameSettings.getDefault().getBaseMPPercentRegen());
        this.getMaxStats().setMPStaticRegen(GameSettings.getDefault().getBaseMPStaticRegen());
        this.setClass(world.getClassHandler().getClass(this.getClassID()));
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getClas().getLevel(this.getLevel())
                .getBaseStats()));
        this.setAetherThreshold(resultSet.getDouble("aether_threshold"));
    }

    public void loadAdditional(GameWorld world) throws Exception {
        this.setInventory(new Inventory(this));
        this.getInventory().load(world);
        this.setSpellbook(new Spellbook(this));
        this.getSpellbook().load(world);
        this.setBodyState(1);
        this.loadPets(world);
    }

    /**
     * Loads all pets from database
     *
     * @param world
     */
    public void loadPets(GameWorld world) throws Exception {
        String query = "SELECT * FROM pets WHERE owner_id=" + this.getPlayerID();
        ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
        while (resultSet.next()) {
            this.addPet(Pet.fromReader(resultSet, world));
        }
        resultSet.close();
    }

    /**
     * SaveToDatabase, saves player info to database
     */
    public Thread saveToDatabase(GameWorld world) throws Exception {
        Thread t = new Thread(() -> {
            try {
                if (this.getGuildID() == 0 && this.getGuild() != null) this.getGuild().save(world);

                if (this.getAutoCreatedNotSaved()) {
                    String query =
                            "INSERT INTO players (player_id, player_name, player_title, player_surname, "
                                    + "password_hash, password_salt, access_status, map_id, map_x, map_y, player_facing, "
                                    + "bound_id, bound_x, bound_y, player_gold, player_level, experience, experience_sold, "
                                    + "player_hp, player_mp, player_sp, class_id, guild_id, stat_ac, stat_str, stat_sta, "
                                    + "stat_dex, stat_int, res_fire, res_water, res_spirit, res_air, res_earth, body_id, "
                                    + "face_id, hair_id, hair_r, hair_g, hair_b, hair_a, aether_threshold, toggle_settings) VALUES"
                                    + "('"
                                    + this.getPlayerID()
                                    + "', '"
                                    + this.getName()
                                    + "', '"
                                    + this.getTitle()
                                    + "', '"
                                    + this.getSurname()
                                    + "', "
                                    + "'"
                                    + this.getPasswordHash()
                                    + "', "
                                    + "'"
                                    + this.getPasswordSalt()
                                    + "', "
                                    + this.getAccess().ordinal()
                                    + ", "
                                    + this.getMapID()
                                    + ", "
                                    + this.getMapX()
                                    + ", "
                                    + this.getMapY()
                                    + ", "
                                    + this.getFacing()
                                    + ", "
                                    + this.getBoundID()
                                    + ", "
                                    + this.getBoundX()
                                    + ", "
                                    + this.getBoundY()
                                    + ", "
                                    + this.getGold()
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
                                    + this.getClassID()
                                    + ", "
                                    + this.getGuildID()
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
                                    + this.getAetherThreshold()
                                    + ", "
                                    + (long) this.getToggleSettings().getValue() + ")";
                    this.setAutoCreatedNotSaved(false);
                    world.getSqlConnection().createStatement().executeUpdate(query);
                } else {
                    String query =
                            "UPDATE players SET " + "player_name='" + this.getName() + "', " + "player_title='"
                                    + this.getTitle() + "', " + "player_surname='" + this.getSurname() + "', "
                                    + "password_hash='" + this.getPasswordHash() + "', " + "password_salt='"
                                    + this.getPasswordSalt() + "', " + "access_status=" + this.getAccess().ordinal()
                                    + ", " + "map_id=" + this.getMapID() + ", " + "map_x=" + this.getMapX() + ", "
                                    + "map_y=" + this.getMapY() + ", " + "player_facing=" + this.getFacing() + ", "
                                    + "bound_id=" + this.getBoundID() + ", " + "bound_x=" + this.getBoundX() + ", "
                                    + "bound_y=" + this.getBoundY() + ", " + "player_gold=" + this.getGold() + ", "
                                    + "player_level=" + this.getLevel() + ", " + "experience=" + this.getExperience()
                                    + ", " + "experience_sold=" + this.getExperienceSold() + ", " + "player_hp="
                                    + this.getBaseStats().getHP() + ", " + "player_mp=" + this.getBaseStats().getMP()
                                    + ", " + "player_sp=" + this.getBaseStats().getSP() + ", " + "class_id="
                                    + this.getClassID() + ", " + "guild_id=" + this.getGuildID() + ", " + "stat_ac="
                                    + this.getBaseStats().getAC() + ", " + "stat_str="
                                    + this.getBaseStats().getStrength() + ", " + "stat_sta="
                                    + this.getBaseStats().getStamina() + ", " + "stat_dex="
                                    + this.getBaseStats().getDexterity() + ", " + "stat_int="
                                    + this.getBaseStats().getIntelligence() + ", " + "res_fire="
                                    + this.getBaseStats().getFireResist() + ", " + "res_water="
                                    + this.getBaseStats().getWaterResist() + ", " + "res_spirit="
                                    + this.getBaseStats().getSpiritResist() + ", " + "res_air="
                                    + this.getBaseStats().getAirResist() + ", " + "res_earth="
                                    + this.getBaseStats().getEarthResist() + ", " + "body_id=" + this.getBodyID() + ", "
                                    + "face_id=" + this.getFaceID() + ", " + "hair_id=" + this.getHairID() + ", "
                                    + "hair_r=" + this.getHairR() + ", " + "hair_g=" + this.getHairG() + ", " + "hair_b="
                                    + this.getHairB() + ", " + "hair_a=" + this.getHairA() + ", " + "aether_threshold="
                                    + this.getAetherThreshold() + ", " + "toggle_settings="
                                    + (long) this.getToggleSettings().getValue() + " " + "WHERE player_id="
                                    + this.getPlayerID();
                    world.getSqlConnection().createStatement().executeUpdate(query);
                }
                this.getInventory().save(world);
                this.getSpellbook().save(world);
                for (Pet pet : this.getPets()) {
                    pet.saveToDatabase(world).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return t;
    }

    /**
     * CanMoveTo, checks if player can move to the specified x,y
     */
    public synchronized boolean canMoveTo(int x, int y) throws Exception {
        if (this.getMap().canMoveTo(this, x, y)) {
            return true;
        }

        return false;
    }

    /**
     * MoveTo, moves player to x, y
     */
    public void moveTo(GameWorld world, int x, int y) throws Exception {
        List<Player> beforeRange = this.getMap().getPlayersInRange(this);
        List<Goose.NPC> beforeNPCRange = this.getMap().getNPCsInRange(this);
        // move off this square so null
        this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
        this.setMapX(x);
        this.setMapY(y);
        // move onto this square so this
        this.getMap().setCharacter(this, this.getMapX(), this.getMapY());
        List<Player> afterRange = this.getMap().getPlayersInRange(this);
        List<Goose.NPC> afterNPCRange = this.getMap().getNPCsInRange(this);
        String mkc = this.mKCString();
        List<Player> afterRangeExceptBeforeRange = new ArrayList<>();
        List<Player> afterRangeUnionBeforeRangeDistinct = new ArrayList<>();
        List<Player> beforeRangeExceptAfterRange = new ArrayList<>();
        List<Goose.NPC> afterNPCRangeExceptBeforeNPCRange = new ArrayList<>();
        List<Goose.NPC> afterNPCRangeUnionBeforeNPCRangeDistinct = new ArrayList<>();
        List<Goose.NPC> beforeNPCRangeExceptAfterNPCRange = new ArrayList<>();
        for (Player p : afterRange) {
            if (!beforeRange.contains(p)) {
                afterRangeExceptBeforeRange.add(p);
            }

            afterRangeUnionBeforeRangeDistinct.add(p);
        }
        for (Player p : beforeRange) {
            if (!afterRangeUnionBeforeRangeDistinct.contains(p)) {
                afterRangeUnionBeforeRangeDistinct.add(p);
            }

            if (!afterRange.contains(p)) {
                beforeRangeExceptAfterRange.add(p);
            }
        }
        for (NPC n : afterNPCRange) {
            if (!beforeNPCRange.contains(n)) {
                afterNPCRangeExceptBeforeNPCRange.add(n);
            }
            afterNPCRangeUnionBeforeNPCRangeDistinct.add(n);
        }
        for (NPC n : beforeNPCRange) {
            if (!afterNPCRangeUnionBeforeNPCRangeDistinct.contains(n)) {
                afterNPCRangeUnionBeforeNPCRangeDistinct.add(n);
            }

            if (!afterNPCRange.contains(n)) {
                beforeNPCRangeExceptAfterNPCRange.add(n);
            }
        }

        for (Object __dummyForeachVar1 : afterRangeExceptBeforeRange) {
            // Send to all people that are in after but aren't in before MKC
            // MKC on client too
            Goose.Player player = (Goose.Player) __dummyForeachVar1;
            world.send(player, mkc);
            world.send(this, player.mKCString());
        }
        for (Object __dummyForeachVar2 : afterNPCRangeExceptBeforeNPCRange) {
            // MKC all new npcs
            Goose.NPC npc = (Goose.NPC) __dummyForeachVar2;
            world.send(this, npc.mKCString());
        }
        // Send to everyone MOC
        String packet = "MOC" + this.getLoginID() + "," + this.getMapX() + "," + this.getMapY();
        for (Object __dummyForeachVar3 : afterRangeUnionBeforeRangeDistinct) {
            Goose.Player player = (Goose.Player) __dummyForeachVar3;
            world.send(player, packet);
        }
        for (Object __dummyForeachVar4 : afterNPCRangeUnionBeforeNPCRangeDistinct) {
            // check if aggro any npcs
            Goose.NPC npc = (Goose.NPC) __dummyForeachVar4;
            npc.aggroIfInRange(this, world);
        }
        String erc = "ERC" + this.getLoginID();
        for (Object __dummyForeachVar5 : beforeRangeExceptAfterRange) {
            // Send to all people that aren't in after but are in before ERC
            // Erase from client too
            Goose.Player player = (Goose.Player) __dummyForeachVar5;
            world.send(player, erc);
            world.send(this, "ERC" + player.getLoginID());
        }
        for (Object __dummyForeachVar6 : beforeNPCRangeExceptAfterNPCRange) {
            // Erase old npcs
            // Remove npc aggro towards player
            Goose.NPC npc = (Goose.NPC) __dummyForeachVar6;
            world.send(this, "ERC" + npc.getLoginID());
            npc.removeAggro(this);
        }
    }

    /**
     * WarpTo, warps player to map, x, y Defaults to losing aggro
     */
    public void warpTo(GameWorld world, Map map, int x, int y) throws Exception {
        this.warpTo(world, map, x, y, true);
    }

    /**
     * WarpTo, warps player to map, x, y
     */
    public void warpTo(GameWorld world, Map map, int x, int y, boolean loseaggro) throws Exception {
        String erc = "ERC" + this.getLoginID();
        for (Goose.Player player : this.getMap().getPlayersInRange(this)) {
            world.send(player, erc);
            world.send(this, "ERC" + player.getLoginID());
        }
        for (Goose.NPC npc : this.getMap().getNPCsInRange(this)) {
            world.send(this, "ERC" + npc.getLoginID());
            if (loseaggro) npc.removeAggro(this);

        }
        if (map == this.getMap()) {
            // Same map, no need to reload map
            // move off this square so null
            this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
            this.setMapX(x);
            this.setMapY(y);
            this.getMap().placeCharacter(this);
            // move onto this square so this
            this.getMap().setCharacter(this, this.getMapX(), this.getMapY());
            world.send(this, "SUP" + this.getMapX() + "," + this.getMapY());
            String mkc = this.mKCString();
            for (Goose.Player player : this.getMap().getPlayersInRange(this)) {
                world.send(player, mkc);
                world.send(this, player.mKCString());
            }
            for (Goose.NPC npc : this.getMap().getNPCsInRange(this)) {
                world.send(this, npc.mKCString());
                npc.aggroIfInRange(this, world);
            }
        } else {
            this.setState(Goose.Player.States.LoadingMap);
            // move off this square so null
            this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
            this.getMap().removePlayer(this);
            this.setMap(null);
            this.setMapID(map.getID());
            this.setMapX(x);
            this.setMapY(y);
            world.send(this, "SCM" + map.getID() + ",1," + map.getName());
        }
    }

    /**
     * SNFString, send info string
     * <p>
     * SNFguildname,,classname,level,max_hp,max_mp,max_sp,cur_,cur_mp,cur_sp,
     * stat_str,stat_sta,stat_int,stat_dex,ac,res_f,res_w,res_e,res_a,res_s,gold
     */
    public String sNFString() throws Exception {
        return "SNF" + (this.getGuild() != null ? this.getGuild().getName() : "") + "," + "" + ","
                + this.getClas().getClassName() + "," + this.getLevel() + "," + this.getMaxStats().getHP()
                + "," + this.getMaxStats().getMP() + "," + this.getMaxStats().getSP() + ","
                + this.getCurrentHP() + "," + this.getCurrentMP() + "," + this.getCurrentSP() + ","
                + this.getMaxStats().getStrength() + "," + this.getMaxStats().getStamina() + ","
                + this.getMaxStats().getIntelligence() + "," + this.getMaxStats().getDexterity() + ","
                + this.getMaxStats().getAC() + "," + this.getMaxStats().getFireResist() + ","
                + this.getMaxStats().getWaterResist() + "," + this.getMaxStats().getEarthResist() + ","
                + this.getMaxStats().getAirResist() + "," + this.getMaxStats().getSpiritResist() + ","
                + this.getGold();
    }

    // guild name
    // Not sure

    /**
     * AddRegenEvent, adds regen event to eventhandler if needed
     */
    public void addRegenEvent(GameWorld world) throws Exception {
        if (this.getRegenEventExists()) return;

        if ((this.getCurrentHP() == this.getMaxStats().getHP())
                && (this.getCurrentMP() == this.getMaxStats().getMP())) {
            return;
        }

        // Already max stats
        RegenEvent ev = new RegenEvent();
        ev.setTicks(ev.getTicks()
                + (long) (GameSettings.getDefault().getRegenSpeed() * world.getTimerFrequency()));
        ev.setData(this);
        this.setRegenEventExists(true);
        world.getEventHandler().addEvent(ev);
    }

    /**
     * TNLString, returns data for exp bar
     */
    public String tNLString() throws Exception {
        long percent, tnl, exp;
        if (this.getClas().getLevel(this.getLevel()).getExperience() == 0) {
            percent = 100;
            tnl = exp = this.getExperience();
        } else {
            long prev;
            if (this.getLevel() == 1)
                prev = 0;
            else
                prev = this.getClas().getLevel(this.getLevel() - 1).getExperience();
            long next = this.getClas().getLevel(this.getLevel()).getExperience();
            tnl = next - this.getExperience();
            exp = this.getExperience();
            percent = (long) (((float) (exp - prev) / (next - prev)) * 100);
        }
        return "TNL" + percent + "," + exp + "," + tnl;
    }

    /**
     * ChangeClass, changes players class
     * <p>
     * Resets level/exp to starting values
     */
    public void changeClass(int classid, GameWorld world) throws Exception {
        // todo unequip equipment i guess
        this.removeStats(this.getBaseStats(), world);
        this.setMaxStats(AttributeSet.subtract(this.getMaxStats(),
                this.getClas().getLevel(this.getLevel()).getBaseStats()));
        this.setLevel(GameSettings.getDefault().getStartingLevel());
        this.setExperience(GameSettings.getDefault().getStartingExperience());
        this.setExperienceSold(GameSettings.getDefault().getStartingExperienceSold());
        this.setClassID(classid);
        this.setClass(world.getClassHandler().getClass(this.getClassID()));
        this.getBaseStats().setHP(0);
        this.getBaseStats().setMP(0);
        this.addStats(this.getClas().getLevel(this.getLevel()).getBaseStats(), world);
        this.addStats(this.getBaseStats(), world);
        world.send(this, this.sNFString());
        world.send(this, this.tNLString());
        world.send(this, "$7Changed class to " + this.getClas().getClassName() + ".");
        for (Spell spell : this.getClas().getLevel(this.getLevel()).getSpells()) {
            this.learnSpell(spell.getID(), world);
        }
    }

    /**
     * SendInventory, sends inventory to player
     */
    public void sendInventory(GameWorld world) throws Exception {
        this.getInventory().sendAll(world);
    }

    /**
     * CanUse, returns true if player can use item
     */
    public boolean canUse(Item item, GameWorld world) throws Exception {
        if (this.getAccess() == Goose.Player.AccessStatus.GameMaster) return true;

        if (item.getMinLevel() != 0 && this.getLevel() < item.getMinLevel()) {
            world.send(this, "$7You are too low level to use " + item.getName() + ".");
            return false;
        }

        if (item.getMaxLevel() != 0 && this.getLevel() > item.getMaxLevel()) {
            world.send(this, "$7You are too high level to use " + item.getName() + ".");
            return false;
        }

        if ((item.getMinExperience() != 0)
                && (this.getExperience() + this.getExperienceSold() < item.getMinExperience())) {
            world.send(this, "$7You are too low experienced to use " + item.getName() + ".");
            return false;
        }

        if ((item.getMaxExperience() != 0)
                && (this.getExperience() + this.getExperienceSold() > item.getMaxExperience())) {
            world.send(this, "$7You are too high experienced to use " + item.getName() + ".");
            return false;
        }

        if ((item.getClassRestrictions() & (long) Math.round(Math.pow(2.0, (double) this.getClas()
                .getClassID()))) != 0) {
            world.send(this, "$7You are the wrong class to use " + item.getName() + ".");
            return false;
        }

        return true;
    }

    /**
     * CHPString, update character string
     */
    public String cHPString() throws Exception {
        int pose = this.getBodyState();
        ItemSlot weapon = this.getInventory().getEquippedSlot(EquipSlots.Weapon);
        if (weapon != null) {
            pose = weapon.getItem().getBodyState();
        }

        return "CHP" + this.getLoginID() + "," + this.getCurrentBodyID() + "," + pose + ","
                + this.getHairID() + "," + this.getInventory().equippedDisplay() + this.getHairR() + ","
                + this.getHairG() + "," + this.getHairB() + "," + this.getHairA() + "," + "0" + ","
                + this.getFaceID();
    }

    // Note: equippedDisplay() adds it's own , on end
    // Invis thing

    /**
     * AddGold, adds amount of gold to player
     */
    public void addGold(long amount, GameWorld world) throws Exception {
        this.setGold(this.getGold() + amount);
        world.send(this, this.sNFString());
    }

    /**
     * RemoveGold, removes amount of gold from player
     */
    public void removeGold(long amount, GameWorld world) throws Exception {
        if (amount > this.getGold()) return;

        this.setGold(this.getGold() - amount);
        world.send(this, this.sNFString());
    }

    /**
     * AddStats, add stats to player
     */
    public void addStats(AttributeSet stats, GameWorld world) throws Exception {
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), stats));
        this.getMaxStats().setHP(
                this.getMaxStats().getHP()
                        + (stats.getStamina() * GameSettings.getDefault().getStaminaToHP()));
        this.getMaxStats().setMP(
                this.getMaxStats().getMP()
                        + (stats.getIntelligence() * GameSettings.getDefault().getIntelligenceToMP()));
        if (this.getCurrentHP() > this.getMaxStats().getHP()) {
            this.setCurrentHP(this.getMaxStats().getHP());
        }

        if (this.getCurrentMP() > this.getMaxStats().getMP()) {
            this.setCurrentMP(this.getMaxStats().getMP());
        }

        if (this.getCurrentSP() > this.getMaxStats().getSP()) {
            this.setCurrentSP(this.getMaxStats().getSP());
        }

        // world.send(this, this.sNFString());
        this.addRegenEvent(world);
    }

    /**
     * RemoveStats, remove stats from player
     */
    public void removeStats(AttributeSet stats, GameWorld world) throws Exception {
        this.setMaxStats(AttributeSet.subtract(this.getMaxStats(), stats));
        this.getMaxStats().setHP(
                this.getMaxStats().getHP()
                        - (stats.getStamina() * GameSettings.getDefault().getStaminaToHP()));
        this.getMaxStats().setMP(
                this.getMaxStats().getMP()
                        - (stats.getIntelligence() * GameSettings.getDefault().getIntelligenceToMP()));
        if (this.getCurrentHP() > this.getMaxStats().getHP()) {
            this.setCurrentHP(this.getMaxStats().getHP());
        }

        if (this.getCurrentMP() > this.getMaxStats().getMP()) {
            this.setCurrentMP(this.getMaxStats().getMP());
        }

        if (this.getCurrentSP() > this.getMaxStats().getSP()) {
            this.setCurrentSP(this.getMaxStats().getSP());
        }

        world.send(this, this.sNFString());
        this.addRegenEvent(world);
    }

    /**
     * HasItem, returns true if player has templateid somewhere
     */
    public boolean hasItem(int templateid) throws Exception {
        return this.getInventory().hasItem(templateid);
    }

    /**
     * Attack, attack character if possible
     */
    public void attack(ICharacter character, GameWorld world) throws Exception {
        if (character instanceof Goose.Player
                && (!this.getMap().getCanPVP() && this.getAccess() != Goose.Player.AccessStatus.GameMaster)) {
            return;
        }

        double damage = 0;
        if (this.getWeaponDamage() == 1) {
            damage = this.getMaxStats().getStrength() + 1 + (this.getLevel() - character.getLevel());
        } else {
            damage =
                    this.getMaxStats().getStrength() + this.getWeaponDamage() + this.getLevel()
                            + world.getRandom().nextInt(this.getLevel()) + 1
                            + (this.getLevel() - character.getLevel());
        }
        double maxac = GameSettings.getDefault().getMaxAC();
        double absorb =
                (1 - ((double) (character.getMaxStats().getAC() * character.getClas().getACMultiplier()) / maxac));
        if (world.getRandom().nextInt(10000) + 1 <= this.getMaxStats().getMeleeCrit() * 10000)
            damage *= 2;

        damage *= (double) GameSettings.getDefault().getDamageModifier();
        damage *= (1 + (double) this.getMaxStats().getMeleeDamage());
        damage *= (1 - (double) character.getMaxStats().getDamageReduction());
        // damage -= absorb;
        damage *= absorb;
        damage -=
                (double) (character.getMaxStats().getAC() * character.getClas().getACMultiplier() / 25);
        character.attacked(this, damage, world);
        if (damage > 0) {
            character.onMeleeHit(this, world);
            this.onMeleeAttack(character, world);
        }

    }

    /**
     * WeaponDamage
     */
    public int getWeaponDamage() throws Exception {
        return this.getInventory().getWeaponDamage();
    }

    public void setWeaponDamage(int value) throws Exception {
    }

    /**
     * WeaponDelay
     */
    public int getWeaponDelay() throws Exception {
        return this.getInventory().getWeaponDelay();
    }

    public void setWeaponDelay(int value) throws Exception {
    }

    /**
     * AddExperience, player gained experience
     */
    public void addExperience(long exp, GameWorld world, ExperienceMessage message) throws Exception {
        if (GameSettings.getDefault().getExperienceCap() > 0
                && this.getExperience() + this.getExperienceSold() > GameSettings.getDefault()
                .getExperienceCap()) {
            if ((this.getToggleSettings().getValue() & ToggleSetting.Experience.getValue()) != 0) return;

            world.send(this, "$7You have reached the experience cap. Gained 0 experience points.");
            return;
        }

        // Experience modifier for everyone under the limit
        if (!(GameSettings.getDefault().getExperienceModifierLimit() > 0 && this.getExperience() + this.getExperienceSold() > GameSettings.getDefault().getExperienceModifierLimit())) {
            exp = (long) (exp * GameSettings.getDefault().getExperienceModifier());
        }

        // To stop the client from crashing
        if (this.getExperience() + exp > Integer.MAX_VALUE) {
            if ((this.getToggleSettings().getValue() & ToggleSetting.Experience.getValue()) != 0) return;

            world.send(this, "$7You have reached the max banked. Gained 0 experience points.");
            return;
        }

        this.setExperience(this.getExperience() + exp);
        if ((this.getToggleSettings().getValue() & ToggleSetting.Experience.getValue()) == 0) {
            switch (message) {
                case Normal:
                    world.send(this, "$7You have gained " + exp + " experience points.");
                    break;
                case TooFarAway:
                    world.send(this, "$7You were too far away to gain experience.");
                    break;
                case TooHigh:
                    world.send(this, "$7You were too experienced, you only gained " + exp
                            + " experience points.");
                    break;
                case TooLow:
                    world.send(this, "$7Group members too high to gain any experience.");
                    break;

            }
        }

        long levelup;
        int levels = 0;
        int i = this.getLevel();
        ClassLevel level = this.getClas().getLevel(i);
        while (this.getClas().getLevel(i) != null) {
            levelup = level.getExperience();
            if (levelup == 0) break;

            if (this.getExperience() >= levelup) {
                levels++;
                if (this.getClas().getLevel(i + 1) != null && this.getClas().getLevel(i + 1).getSpells().size() > 0) {
                    for (Spell spell : this.getClas().getLevel(i + 1).getSpells()) {
                        this.learnSpell(spell.getID(), world);
                    }
                }

            } else {
                break;
            }
            i++;
            level = this.getClas().getLevel(i);
        }
        if (levels == 0) {
            world.send(this, this.tNLString());
            return;
        }

        this.removeStats(this.getClas().getLevel(this.getLevel()).getBaseStats(), world);
        this.setLevel(this.getLevel() + levels);
        this.addStats(this.getClas().getLevel(this.getLevel()).getBaseStats(), world);
        this.setCurrentHP(this.getMaxStats().getHP());
        this.setCurrentMP(this.getMaxStats().getMP());
        world.send(this, this.sNFString());
        world.send(this, this.vCString());
        if (levels == 1)
            world.send(this, "$7You have gained a level.");
        else
            world.send(this, "$7You have gained " + levels + " levels.");
        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet = "BT" + this.getLoginID() + ",60,Level Up!," + this.getName();
        world.send(this, packet);
        for (Goose.Player player : range) {
            world.send(player, packet);
        }
        world.send(this, this.tNLString());
    }

    /**
     * Player was attacked by character
     */
    public void attacked(ICharacter character, double damage, GameWorld world) throws Exception {
        if (this.getState() != Goose.Player.States.Ready) return;

        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet;
        if (damage == 0 || this.getAccess() == Goose.Player.AccessStatus.GameMaster) {
            packet = "BT" + this.getLoginID() + ",21,," + character.getName();
            world.send(this, packet);
            for (Goose.Player p : range) {
                world.send(p, packet);
            }
            return;
        }

        double dodge = this.getMaxStats().getDexterity() / 100.0;
        if (dodge > 50) dodge = 50;

        if (damage > 0 && world.getRandom().nextInt(10001) <= dodge * 100) {
            packet = "BT" + this.getLoginID() + ",20,," + character.getName();
            world.send(this, packet);
            for (Goose.Player p : range) {
                world.send(p, packet);
            }
            return;
        }

        int dmg = ((int) (damage));
        if (damage > 0) {
            // pvp 1/3 damage
            if (character instanceof Goose.Player) dmg /= 3;

            packet = "BT" + this.getLoginID() + ",1," + (-dmg) + "," + character.getName() + "\u0001";
        } else {
            packet = "BT" + this.getLoginID() + ",7,+" + (-dmg) + "," + character.getName() + "\u0001";
        }
        this.setCurrentHP(this.getCurrentHP() - dmg);
        if (this.getCurrentHP() <= 0) {
            this.setCurrentHP(((int) ((this.getMaxStats().getHP() * 0.5))));
            this.setCurrentMP(((int) ((this.getMaxStats().getMP() * 0.1))));
            world.sendToMap(this.getMap(), "$7" + this.getName() + " was slain by " + character.getName() + ".");
            this.warpTo(world, this.getBoundMap(), this.getBoundX(), this.getBoundY());
            this.addRegenEvent(world);
            world.send(this, this.vCString());
            world.send(this, this.sNFString());
            // Remove all buffs on death
            List<Buff> removebuff = new ArrayList<Buff>();
            for (Buff b : this.getBuffs()) {
                if (!b.getItemBuff()) removebuff.add(b);

            }
            for (Buff b : removebuff) {
                this.removeBuff(b, world, false);
            }
            this.sendBuffBar(world);
        } else {
            packet += this.vCString();
            this.addRegenEvent(world);
        }
        world.send(this, this.sNFString());
        world.send(this, packet);
        for (Goose.Player p : range) {
            world.send(p, packet);
        }
        if (dmg > 0) {
            for (Pet pet : this.getPets()) {
                if (pet.getMode() == Pet.Modes.Defend && pet.getTarget() == null) {
                    pet.setTarget(character);
                    pet.addAttackEvent(world);
                }
            }
        }

        return;
    }

    /**
     * VCString, returns regen event string
     */
    public String vCString() throws Exception {
        return "VC" + this.getLoginID() + ","
                + (int) (((float) this.getCurrentHP() / this.getMaxStats().getHP()) * 100) + ","
                + (int) (((float) this.getCurrentMP() / this.getMaxStats().getMP()) * 100);
    }

    /**
     * AddSaveEvent, Adds save event. Also does ping timeout stuff
     */
    public void addSaveEvent(GameWorld world) throws Exception {
        if (this.getLastPing() == 0) this.setLastPing(world.getTimeNow());

        if ((world.getTimeNow() - this.getLastPing()) > ((GameSettings.getDefault().getPlayerSavePeriod() * 2.00) * world.getTimerFrequency())) {
            world.lostConnection(this.getSock());
        } else {
            world.send(this, "PING");
            PlayerSaveEvent ev = new PlayerSaveEvent();
            ev.setPlayer(this);
            ev.setTicks(ev.getTicks() + (GameSettings.getDefault().getPlayerSavePeriod() * world.getTimerFrequency()));
            world.getEventHandler().addEvent(ev);
        }
    }

    /**
     * LearnSpell, learns spell id
     */
    public boolean learnSpell(int spellid, GameWorld world) throws Exception {
        return this.getSpellbook().learnSpell(spellid, world);
    }

    /**
     * SendSpellbook, sends spellbook to player
     */
    public void sendSpellbook(GameWorld world) throws Exception {
        this.getSpellbook().sendAll(world);
    }

    /**
     * CastSpell, casts spellslot spell on target
     */
    public void castSpell(int spellslot, ICharacter target, GameWorld world) throws Exception {
        Spell spell = this.getSpellbook().getSlot(spellslot);
        if (spell == null) return;

        for (Buff b : this.getBuffs()) {
            // can't cast when stunned
            if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
                world.send(this, "BT" + this.getLoginID() + ",50");
                return;
            }

        }
        for (Window window : this.getWindows()) {
            if (window.getType() == WindowTypes.Vendor) {
                world.send(this, "$7You can't cast spells while with a vendor.");
                return;
            }

        }
        if ((spell.getTarget() == SpellTargets.Group || spell.getTarget() == SpellTargets.Self)
                && target != this) {
            target = this;
        }

        // log bad target
        long lastcast = this.getSpellbook().getSlotLastCast(spellslot);
        long now = world.getTimeNow();
        if (now - lastcast >= (long) ((spell.getAether() / 1000.0) * world.getTimerFrequency())) {
            if (this.getCurrentHP() >= spell.getHPStaticCost()
                    && this.getCurrentMP() >= spell.getMPStaticCost()
                    && this.getCurrentSP() >= spell.getSPStaticCost()) {
                if (spell.getTarget() == SpellTargets.Group) {
                    if (this.getGroup() != null) {
                        for (Goose.Player p : this.getGroup().getPlayers()) {
                            if (p != this && p.getMap() == this.getMap()
                                    && Math.abs(p.getMapX() - this.getMapX()) <= Map.RANGE_X
                                    && Math.abs(p.getMapY() - this.getMapY()) <= Map.RANGE_Y) {
                                spell.getSpellEffect().cast(this, p, world);
                            }

                        }
                    }

                    spell.getSpellEffect().cast(this, this, world);
                } else {
                    spell.getSpellEffect().cast(this, target, world);
                }
                this.setCurrentHP(this.getCurrentHP() - spell.getHPStaticCost());
                this.setCurrentMP(this.getCurrentMP() - spell.getMPStaticCost());
                this.setCurrentSP(this.getCurrentSP() - spell.getSPStaticCost());
                this.setCurrentHP(this.getCurrentHP()
                        - ((int) ((this.getCurrentHP() * (spell.getHPPercentCost() / (double) 100.0)))));
                this.setCurrentMP(this.getCurrentMP()
                        - ((int) ((this.getCurrentMP() * (spell.getMPPercentCost() / (double) 100.0)))));
                this.setCurrentSP(this.getCurrentSP()
                        - ((int) ((this.getCurrentSP() * (spell.getSPPercentCost() / (double) 100.0)))));
                if (this.getCurrentHP() <= 0) this.setCurrentHP(1);

                if (this.getCurrentMP() < 0) this.setCurrentMP(0);

                if (this.getCurrentSP() < 0) this.setCurrentSP(0);

                this.getSpellbook().setSlotLastCast(spellslot, now);
                this.addRegenEvent(world);
                if (this.getState() == Goose.Player.States.Ready) {
                    String packet = this.vCString();
                    world.send(this, packet);
                    world.send(this, this.sNFString());
                    for (Goose.Player player : this.getMap().getPlayersInRange(this)) {
                        world.send(player, packet);
                    }
                }

            } else {
                String packet = "BT" + this.getLoginID() + ",60,Fizzle";
                world.send(this, packet);
                for (Goose.Player player : this.getMap().getPlayersInRange(this)) {
                    world.send(player, packet);
                }
            }
        } else {
            double wait =
                    (((double) ((spell.getAether() / 1000.0) * world.getTimerFrequency()) - (now - lastcast)) / world
                            .getTimerFrequency());
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#.00", otherSymbols);
            if (wait >= this.getAetherThreshold()) {
                // world.send(this, "$7You must wait " + df.format(wait)
                // + " seconds to cast this spell.");
                String packet = "BT" + this.getLoginID() + ",60," + df.format(wait);
                world.send(this, packet);
            }

        }
    }

    public void addBuff(Buff buff, GameWorld world) throws Exception {
        this.addBuff(buff, world, true);
    }

    /**
     * AddBuff, add buff to players buff list
     */
    public void addBuff(Buff buff, GameWorld world, boolean refreshbar) throws Exception {
        if (this.getState().compareTo(Goose.Player.States.LoadingGame) <= 0) {
            this.getBuffs().add(buff);
            // Add/remove stats
            this.addStats(buff.getSpellEffect().getStats(), world);
            return;
        }

        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet;
        for (Buff b : this.getBuffs()) {
            if (buff.getSpellEffect().getBuffDoesntStackOver().contains(b.getSpellEffect())) {
                world.send(this, "$7The buff had no effect.");
                return;
            }

            // already have that buff so renew the time cast
            if (!b.getItemBuff()
                    && !buff.getItemBuff()
                    && (buff.getSpellEffect() == b.getSpellEffect() || buff.getSpellEffect()
                    .getBuffStacksOver().contains(b.getSpellEffect()))) {
                this.removeStats(b.getSpellEffect().getStats(), world);
                this.addStats(buff.getSpellEffect().getStats(), world);
                world.send(this, this.wPSString());
                b.setTimeCast(world.getTimeNow());
                b.setSpellEffect(buff.getSpellEffect());
                b.setCaster(buff.getCaster());
                if (buff.getSpellEffect().getAnimation() != 0) {
                    packet = "SPP" + this.getLoginID() + "," + buff.getSpellEffect().getAnimation();
                    if (buff.getSpellEffect().getDoAttackAnimation())
                        packet += "\u0001ATT" + this.getLoginID();

                    // kinda weird but k
                    world.send(this, packet);
                    for (Goose.Player player : range) {
                        world.send(player, packet);
                    }
                }

                if (!("".equals(b.getSpellEffect().getOffEffectText())))
                    world.send(this, "$7" + buff.getSpellEffect().getOffEffectText());

                if (!("".equals((buff.getSpellEffect().getOnEffectText()))))
                    world.send(this, "$7" + buff.getSpellEffect().getOnEffectText());

                this.sendBuffBar(world);
                return;
            }

        }
        if (buff.getSpellEffect().getDuration() > 0) {
            // else we don't have the buff. add it
            Event ev = new BuffExpireEvent();
            ev.setTicks(ev.getTicks() + buff.getSpellEffect().getDuration() * world.getTimerFrequency());
            ev.setPlayer(this);
            ev.setData(buff);
            world.getEventHandler().addEvent(ev);
            buff.setBuffExpireEvent(ev);
        }

        if (buff.getSpellEffect().getEffectType() == EffectTypes.Tick
                || buff.getSpellEffect().getEffectType() == EffectTypes.TickBuff
                || buff.getSpellEffect().getEffectType() == EffectTypes.Viral
                || buff.getSpellEffect().getEffectType() == EffectTypes.Root
                || buff.getSpellEffect().getEffectType() == EffectTypes.Stun) {
            // buff will expire before next tick
            if (buff.getBuffExpireEvent().getTicks() - world.getTimeNow() > GameSettings.getDefault()
                    .getSpellEffectPeriod() * world.getTimerFrequency()) {
                BuffTickEvent ev = new BuffTickEvent();
                ev.setData(buff);
                ev.setPlayer(this);
                ev.setTicks(ev.getTicks()
                        + (long) (GameSettings.getDefault().getSpellEffectPeriod() * world.getTimerFrequency()));
                world.getEventHandler().addEvent(ev);
            }

        }

        this.getBuffs().add(buff);
        // Add/remove stats
        this.addStats(buff.getSpellEffect().getStats(), world);
        packet = this.vCString();
        if (buff.getSpellEffect().getStats().getHaste() != 0.0) {
            world.send(this, this.wPSString());
        }

        // for illusions
        if (buff.getSpellEffect().getBodyID() != 0) {
            this.setCurrentBodyID(buff.getSpellEffect().getBodyID());
            packet += "\u0001" + this.cHPString();
        }

        this.addRegenEvent(world);
        if (buff.getSpellEffect().getAnimation() != 0)
            packet += "\u0001SPP" + this.getLoginID() + "," + buff.getSpellEffect().getAnimation();

        if (buff.getSpellEffect().getDoAttackAnimation()) packet += "\u0001ATT" + this.getLoginID();

        // kinda weird but k
        if (!("".equals(buff.getSpellEffect().getOnEffectText())))
            world.send(this, "$7" + buff.getSpellEffect().getOnEffectText());

        world.send(this, this.sNFString());
        world.send(this, packet);
        for (Goose.Player player : range) {
            world.send(player, packet);
        }
        if (refreshbar) this.sendBuffBar(world);

    }

    public void removeBuff(Buff buff, GameWorld world) throws Exception {
        this.removeBuff(buff, world, true);
    }

    /**
     * RemoveBuff, removes buff from buffs list
     */
    public void removeBuff(Buff buff, GameWorld world, boolean refreshbar) throws Exception {
        this.getBuffs().remove(buff);
        if (buff.getBuffExpireEvent() != null) {
            world.getEventHandler().removeEvent(buff.getBuffExpireEvent());
            buff.setBuffExpireEvent(null);
        }

        // Add/remove stats
        this.removeStats(buff.getSpellEffect().getStats(), world);
        String packet = this.vCString();
        if (buff.getSpellEffect().getStats().getHaste() != 0.0) {
            world.send(this, this.wPSString());
        }

        // for illusions
        if (buff.getSpellEffect().getBodyID() != 0) {
            this.setCurrentBodyID(this.getBodyID());
            packet += "\u0001" + this.cHPString();
        }

        this.addRegenEvent(world);
        if (this.getState() == Goose.Player.States.Ready) {
            List<Player> range = this.getMap().getPlayersInRange(this);
            if (!("".equals(buff.getSpellEffect().getOffEffectText())))
                world.send(this, "$7" + buff.getSpellEffect().getOffEffectText());

            world.send(this, this.sNFString());
            world.send(this, packet);
            for (Goose.Player player : range) {
                world.send(player, packet);
            }
        }

        if (refreshbar) this.sendBuffBar(world);

    }

    /**
     * SendBuffBar, sends buff bar to player
     */
    public void sendBuffBar(GameWorld world) throws Exception {
        if (this.getState().compareTo(Goose.Player.States.LoadingGame) <= 0) return;

        int i = 1;
        for (Buff buff : this.getBuffs()) {
            world.send(this, "BUF" + i + "," + buff.getSpellEffect().getBuffGraphic() + ","
                    + buff.getSpellEffect().getName());
            i++;
        }
        while (i <= GameSettings.getDefault().getBuffBarVisibleSize()) {
            world.send(this, "BUF" + i);
            i++;
        }
    }

    /**
     * OnMeleeHit, when hit by melee cast any reaction spells
     */
    public void onMeleeHit(ICharacter hitter, GameWorld world) throws Exception {
        for (Buff b : this.getBuffs()) {
            if (b.getSpellEffect().getEffectType() == EffectTypes.OnMeleeHit) {
                if (world.getRandom().nextInt(10000) + 1 <= b.getSpellEffect().getOnMeleeHitSpellChance() * 100)
                    b.getSpellEffect().getOnMeleeHitSpell().cast(this, hitter, world);

            }

        }
    }

    /**
     * OnMeleeAttack, casts melee attack spells when we hit something
     */
    public void onMeleeAttack(ICharacter hit, GameWorld world) throws Exception {
        for (Buff b : this.getBuffs()) {
            if (b.getSpellEffect().getEffectType() == EffectTypes.OnAttack) {
                if (world.getRandom().nextInt(10000) + 1 <= b.getSpellEffect()
                        .getOnMeleeAttackSpellChance() * 100)
                    b.getSpellEffect().getOnMeleeAttackSpell().cast(this, this, world);

            }

        }
    }

    /**
     * WPSSTring, weapon speed string
     */
    public String wPSString() throws Exception {
        int wps =
                ((int) (((double) (this.getWeaponDelay() / 10.0) * (1 - this.getMaxStats().getHaste()) * 1000)));
        return "WPS" + wps;
    }

    /**
     * ShowStatsWindow, opens up item stats window for id
     * <p>
     * ID is a template id if less than GameSettings.Default.ItemIDStartpoint
     */
    public void showStatsWindow(int id, GameWorld world) throws Exception {
        Object item = null;
        if (id < GameSettings.getDefault().getItemIDStartpoint()) {
            item = world.getItemHandler().getTemplate(id);
        } else {
            item = world.getItemHandler().getItem(id);
        }
        // log bad id
        if (item == null) return;

        Window window = new Window();
        window.setTitle((item instanceof ItemTemplate ? ((ItemTemplate) item).getName() : ((Item) item)
                .getName()));
        window.setButtons("0,0,0,0,0");
        window.setType(WindowTypes.ItemInfo);
        window.setData(item);
        this.getWindows().add(window);
        window.create(this, world);
    }

    /**
     * Adds a pet to this player's control
     *
     * @param pet pet to add
     */
    public void addPet(Pet pet) throws Exception {
        pet.setOwner(this);
        this.getPets().add(pet);
    }

}
