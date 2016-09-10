package Goose;

/**
 * ICharacter, interface for characters, NPCs and Players
 * <p>
 * Provides a common interface for NPCs and Players to implement
 */
public interface ICharacter {
    int getLoginID() throws Exception;

    void setLoginID(int value) throws Exception;

    String getName() throws Exception;

    void setName(String value) throws Exception;

    /**
     * MKCString, returns the MKC packet string for this character
     */
    String mKCString() throws Exception;

    /**
     * Current map id
     */
    int getMapID() throws Exception;

    void setMapID(int value) throws Exception;

    /**
     * Current map x
     */
    int getMapX() throws Exception;

    void setMapX(int value) throws Exception;

    /**
     * Current map y
     */
    int getMapY() throws Exception;

    void setMapY(int value) throws Exception;

    /**
     * Facing direction
     */
    int getFacing() throws Exception;

    void setFacing(int value) throws Exception;

    /**
     * BaseStats, stats loaded from database
     */
    AttributeSet getBaseStats() throws Exception;

    void setBaseStats(AttributeSet value) throws Exception;

    /**
     * Stats from base, items, buffs
     */
    AttributeSet getMaxStats() throws Exception;

    void setMaxStats(AttributeSet value) throws Exception;

    /**
     * Current HP
     */
    int getCurrentHP() throws Exception;

    void setCurrentHP(int value) throws Exception;

    /**
     * Current MP
     */
    int getCurrentMP() throws Exception;

    void setCurrentMP(int value) throws Exception;

    /**
     * Current SP
     */
    int getCurrentSP() throws Exception;

    void setCurrentSP(int value) throws Exception;

    /**
     * Level
     */
    int getLevel() throws Exception;

    void setLevel(int value) throws Exception;

    /**
     * Class ID
     */
    int getClassID() throws Exception;

    void setClassID(int value) throws Exception;

    /**
     * Experience
     */
    long getExperience() throws Exception;

    void setExperience(long value) throws Exception;

    boolean canMoveTo(int x, int y) throws Exception;

    void moveTo(GameWorld world, int x, int y) throws Exception;

    void attacked(ICharacter character, double damage, GameWorld world) throws Exception;

    Map getMap() throws Exception;

    void setMap(Map value) throws Exception;

    String vCString() throws Exception;

    int getWeaponDamage() throws Exception;

    /**
     * Hair style id
     */
    int getHairID() throws Exception;

    void setHairID(int value) throws Exception;

    /**
     * Hair colour r
     */
    int getHairR() throws Exception;

    void setHairR(int value) throws Exception;

    /**
     * Hair colour g
     */
    int getHairG() throws Exception;

    void setHairG(int value) throws Exception;

    /**
     * Hair colour b
     */
    int getHairB() throws Exception;

    void setHairB(int value) throws Exception;

    /**
     * Hair colour a
     */
    int getHairA() throws Exception;

    void setHairA(int value) throws Exception;

    /**
     * Face id
     */
    int getFaceID() throws Exception;

    void setFaceID(int value) throws Exception;

    /**
     * Body ID
     */
    int getBodyID() throws Exception;

    void setBodyID(int value) throws Exception;

    /**
     * Current Body ID
     */
    int getCurrentBodyID() throws Exception;

    void setCurrentBodyID(int value) throws Exception;

    void addBuff(Buff buff, GameWorld world) throws Exception;

    void removeBuff(Buff buff, GameWorld world) throws Exception;

    void onMeleeHit(ICharacter attacker, GameWorld world) throws Exception;

    Class getClas() throws Exception;

    void setClass(Class value) throws Exception;

}
