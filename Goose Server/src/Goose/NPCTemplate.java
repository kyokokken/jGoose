package Goose;

import java.util.List;

/**
 * NPCTemplate, holds all of the information to create an npc
 */
public class NPCTemplate {
    public enum Types {
        __dummyEnum__0, __dummyEnum__1, Monster, __dummyEnum__2, __dummyEnum__3, __dummyEnum__4, __dummyEnum__5, __dummyEnum__6, __dummyEnum__7, __dummyEnum__8, Vendor, __dummyEnum__9, Quest, __dummyEnum__11, __dummyEnum__12, __dummyEnum__13
    }

    private Goose.NPCTemplate.Types __NPCType = Goose.NPCTemplate.Types.Monster;

    public Goose.NPCTemplate.Types getNPCType() {
        return __NPCType;
    }

    public void setNPCType(Goose.NPCTemplate.Types value) {
        __NPCType = value;
    }

    public enum BehaviourTypes {
        /**
         * BehaviourTypes specifies the behaviour of the npc when it hasn't attacked for the specified
         * time
         */
        DoNothing, TeleportToAggro, TeleportAggro
    }

    private Goose.NPCTemplate.BehaviourTypes __Behaviour = Goose.NPCTemplate.BehaviourTypes.DoNothing;

    public Goose.NPCTemplate.BehaviourTypes getBehaviour() {
        return __Behaviour;
    }

    public void setBehaviour(Goose.NPCTemplate.BehaviourTypes value) {
        __Behaviour = value;
    }

    private long __BehaviourTimeout;

    public long getBehaviourTimeout() {
        return __BehaviourTimeout;
    }

    public void setBehaviourTimeout(long value) {
        __BehaviourTimeout = value;
    }

    /**
     * Template ID
     */
    private int __NPCTemplateID;

    public int getNPCTemplateID() {
        return __NPCTemplateID;
    }

    public void setNPCTemplateID(int value) {
        __NPCTemplateID = value;
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
     * Respawn time in seconds
     */
    private int __RespawnTime;

    public int getRespawnTime() {
        return __RespawnTime;
    }

    public void setRespawnTime(int value) {
        __RespawnTime = value;
    }

    /**
     * Aggro range in tiles
     */
    private int __AggroRange;

    public int getAggroRange() {
        return __AggroRange;
    }

    public void setAggroRange(int value) {
        __AggroRange = value;
    }

    /**
     * Attack range in tiles
     */
    private int __AttackRange;

    public int getAttackRange() {
        return __AttackRange;
    }

    public void setAttackRange(int value) {
        __AttackRange = value;
    }

    /**
     * Does Root effect this npc
     */
    private boolean __CanBeRooted;

    public boolean getCanBeRooted() {
        return __CanBeRooted;
    }

    public void setCanBeRooted(boolean value) {
        __CanBeRooted = value;
    }

    /**
     * Does Stun effect this npc
     */
    private boolean __CanBeStunned;

    public boolean getCanBeStunned() {
        return __CanBeStunned;
    }

    public void setCanBeStunned(boolean value) {
        __CanBeStunned = value;
    }

    /**
     * Does snare effect this npc
     */
    private boolean __CanBeSlowed;

    public boolean getCanBeSlowed() {
        return __CanBeSlowed;
    }

    public void setCanBeSlowed(boolean value) {
        __CanBeSlowed = value;
    }

    /**
     * Is npc invincible
     */
    private boolean __CanBeKilled;

    public boolean getCanBeKilled() {
        return __CanBeKilled;
    }

    public void setCanBeKilled(boolean value) {
        __CanBeKilled = value;
    }

    /**
     * Attack speed in seconds
     */
    private double __AttackSpeed;

    public double getAttackSpeed() {
        return __AttackSpeed;
    }

    public void setAttackSpeed(double value) {
        __AttackSpeed = value;
    }

    /**
     * Move speed in seconds
     */
    private double __MoveSpeed;

    public double getMoveSpeed() {
        return __MoveSpeed;
    }

    public void setMoveSpeed(double value) {
        __MoveSpeed = value;
    }

    /**
     * Stationary
     */
    private boolean __CanMove;

    public boolean getCanMove() {
        return __CanMove;
    }

    public void setCanMove(boolean value) {
        __CanMove = value;
    }

    /**
     * Items part of MKC String
     */
    private String __EquippedItems;

    public String getEquippedItems() {
        return __EquippedItems;
    }

    public void setEquippedItems(String value) {
        __EquippedItems = value;
    }

    /**
     * Weapon damage
     */
    private int __WeaponDamage;

    public int getWeaponDamage() {
        return __WeaponDamage;
    }

    public void setWeaponDamage(int value) {
        __WeaponDamage = value;
    }

    /**
     * Allies, space delimited list of template ids
     */
    private String __AlliesString;

    public String getAlliesString() {
        return __AlliesString;
    }

    public void setAlliesString(String value) {
        __AlliesString = value;
    }

    /**
     * Allies, list of npctemplates
     */
    private List<NPCTemplate> __Allies;

    public List<NPCTemplate> getAllies() {
        return __Allies;
    }

    public void setAllies(List<NPCTemplate> value) {
        __Allies = value;
    }

    /**
     * Drops, holds a list of the drops
     */
    private List<NPCDropInfo> __Drops;

    public List<NPCDropInfo> getDrops() {
        return __Drops;
    }

    public void setDrops(List<NPCDropInfo> value) {
        __Drops = value;
    }

    /**
     * Holds the items this npc is selling
     */
    private NPCVendorSlot[] __VendorItems;

    public NPCVendorSlot[] getVendorItems() {
        return __VendorItems;
    }

    public void setVendorItems(NPCVendorSlot[] value) {
        __VendorItems = value;
    }

}
