package Goose;

import Goose.Events.BuffExpireEvent;
import Goose.Events.BuffTickEvent;
import Goose.Events.NPCAttackEvent;
import Goose.Events.NPCMoveEvent;
import Goose.Events.NPCSpawnEvent;
import Goose.Events.RegenEvent;
import Goose.Player.ExperienceMessage;
import Goose.SpellEffect.EffectTypes;
import Goose.Window.WindowTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * NPC
 */
public class NPC implements ICharacter {
    public enum States {
        Alive, Dead
    }

    private Goose.NPC.States __State = Goose.NPC.States.Alive;

    public Goose.NPC.States getState() {
        return __State;
    }

    public void setState(Goose.NPC.States value) {
        __State = value;
    }

    public static class Aggro implements Comparable<Aggro> {
        public long Taunt;
        public long Damage;

        public Aggro(long damage, long taunt) throws Exception {
            this.Taunt = taunt;
            this.Damage = damage;
        }

        @Override
        public int compareTo(Aggro arg0) {
            if (this.Damage + this.Taunt > arg0.Damage + arg0.Taunt)
                return 1;
            else if (this.Damage + this.Taunt == arg0.Damage + arg0.Taunt)
                return 0;
            else
                return -1;
        }

    }

    /**
     * LoginID is the ID assigned by the server on login
     */
    private int __LoginID;

    public int getLoginID() {
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
     * spawn map x
     */
    private int __SpawnX;

    public int getSpawnX() {
        return __SpawnX;
    }

    public void setSpawnX(int value) {
        __SpawnX = value;
    }

    /**
     * spawn map y
     */
    private int __SpawnY;

    public int getSpawnY() {
        return __SpawnY;
    }

    public void setSpawnY(int value) {
        __SpawnY = value;
    }

    private Goose.NPCTemplate.Types __NPCType = Goose.NPCTemplate.Types.Monster;

    public Goose.NPCTemplate.Types getNPCType() {
        return __NPCType;
    }

    public void setNPCType(Goose.NPCTemplate.Types value) {
        __NPCType = value;
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

    private NPCTemplate __NPCTemplate;

    public NPCTemplate getNPCTemplate() {
        return __NPCTemplate;
    }

    public void setNPCTemplate(NPCTemplate value) {
        __NPCTemplate = value;
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
     * Body ID
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
     * AggroTarget, the player the npc is aggro at if any
     */
    private Goose.Player __AggroTarget;

    public Goose.Player getAggroTarget() {
        return __AggroTarget;
    }

    public void setAggroTarget(Goose.Player value) {
        __AggroTarget = value;
    }

    /**
     * AggroValue, the amount of aggro the npc has against target
     */
    private Goose.NPC.Aggro __AggroValue;

    public Goose.NPC.Aggro getAggroValue() {
        return __AggroValue;
    }

    public void setAggroValue(Goose.NPC.Aggro value) {
        __AggroValue = value;
    }

    /**
     * Mapping of all aggros
     */
    private HashMap<Player, Aggro> __AggroTargetToValue;

    public HashMap<Player, Aggro> getAggroTargetToValue() {
        return __AggroTargetToValue;
    }

    public void setAggroTargetToValue(HashMap<Player, Aggro> value) {
        __AggroTargetToValue = value;
    }

    public List<NPCTemplate> getAllies() throws Exception {
        return this.getNPCTemplate().getAllies();
    }

    /**
     * So move event doesn't double up
     */
    private Event __MoveEvent;

    public Event getMoveEvent() {
        return __MoveEvent;
    }

    public void setMoveEvent(Event value) {
        __MoveEvent = value;
    }

    /**
     * So attack event doesn't double up
     */
    private Event __AttackEvent;

    public Event getAttackEvent() {
        return __AttackEvent;
    }

    public void setAttackEvent(Event value) {
        __AttackEvent = value;
    }

    private List<Buff> __Buffs;

    public List<Buff> getBuffs() {
        return __Buffs;
    }

    public void setBuffs(List<Buff> value) {
        __Buffs = value;
    }

    public NPCVendorSlot[] getVendorItems() throws Exception {
        return this.getNPCTemplate().getVendorItems();
    }

    public Goose.NPCTemplate.BehaviourTypes getBehaviour() throws Exception {
        return this.getNPCTemplate().getBehaviour();
    }

    public long getBehaviourTimeout() throws Exception {
        return this.getNPCTemplate().getBehaviourTimeout();

    }

    private long __LastAttackTime;

    public long getLastAttackTime() {
        return __LastAttackTime;
    }

    public void setLastAttackTime(long value) {
        __LastAttackTime = value;
    }

    /**
     * MKCString, see Player.MKCString for details
     */
    public String mKCString() throws Exception {
        return "MKC" + this.getLoginID() + ",2," + this.getName() + "," + this.getTitle() + ","
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

    /**
     * CanMoveTo, checks if character can move to the specified x,y
     */
    public boolean canMoveTo(int x, int y) throws Exception {
        if (this.getMap().canMoveTo(this, x, y)) {
            return true;
        }

        return false;
    }

    /**
     * MoveTo, moves character
     */
    public void moveTo(GameWorld world, int x, int y) throws Exception {
        List<Player> beforeRange = this.getMap().getPlayersInRange(this);
        // move off this square so null
        this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
        this.setMapX(x);
        this.setMapY(y);
        // moveto this square so this
        this.getMap().setCharacter(this, this.getMapX(), this.getMapY());
        List<Player> afterRange = this.getMap().getPlayersInRange(this);
        String mkc = this.mKCString();
        List<Player> afterRangeExceptBeforeRange = new ArrayList<>();
        List<Player> afterRangeUnionBeforeRangeDistinct = new ArrayList<>();
        List<Player> beforeRangeExceptAfterRange = new ArrayList<>();
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
        for (Object __dummyForeachVar0 : afterRangeExceptBeforeRange) {
            // Send to all people that are in after but aren't in before MKC
            Goose.Player player = (Goose.Player) __dummyForeachVar0;
            world.send(player, mkc);
        }
        // Send to everyone MOC
        String packet = "MOC" + this.getLoginID() + "," + this.getMapX() + "," + this.getMapY();
        for (Object __dummyForeachVar1 : afterRangeUnionBeforeRangeDistinct) {
            Goose.Player player = (Goose.Player) __dummyForeachVar1;
            world.send(player, packet);
            this.aggroIfInRange(player, world);
        }
        String erc = "ERC" + this.getLoginID();
        for (Object __dummyForeachVar2 : beforeRangeExceptAfterRange) {
            // Send to all people that aren't in after but are in before ERC
            Goose.Player player = (Goose.Player) __dummyForeachVar2;
            world.send(player, erc);
            this.removeAggro(player);
        }
        if (this.getAggroTarget() != null) {
            packet = "";
            List<NPC> npcs = this.getMap().getNPCsInRange(this);
            for (Goose.NPC npc : npcs) {
                if (!this.getAllies().contains(npc.getNPCTemplate())) continue;

                if (npc.getAggroTarget() != null) continue;

                if (Math.abs(npc.getMapX() - this.getMapX()) <= npc.getAggroRange()
                        && Math.abs(npc.getMapY() - this.getMapY()) <= npc.getAggroRange()) {
                    npc.addAggro(this.getAggroTarget(), 1, world);
                    packet += "EMOT" + npc.getLoginID() + ",8\u0001";
                    npc.addMoveEvent(world);
                    npc.addAttackEvent(world);
                }

            }
            if (packet.equals("")) return;

            packet = packet.substring(0, packet.lastIndexOf("\u0001"));
//            if (packet.contains("\u0001")) packet = packet.substring(packet.lastIndexOf("\u0001"));
            for (Object __dummyForeachVar4 : afterRangeUnionBeforeRangeDistinct) {
                Goose.Player p = (Goose.Player) __dummyForeachVar4;
                world.send(p, packet);
            }
        }

    }

    /**
     * LoadFromTemplate
     */
    public boolean loadFromTemplate(GameWorld world, int map_id, int map_x, int map_y,
                                    NPCTemplate template) throws Exception {
        this.setMap(world.getMapHandler().getMap(map_id));
        if (this.getMap() == null) return false;

        this.setMapID(map_id);
        this.setMapX(map_x);
        this.setMapY(map_y);
        this.setState(Goose.NPC.States.Dead);
        this.setAggroRange(template.getAggroRange());
        this.setAttackRange(template.getAttackRange());
        this.setAttackSpeed(template.getAttackSpeed());
        this.setBaseStats(new AttributeSet());
        this.setBaseStats(AttributeSet.add(this.getBaseStats(), template.getBaseStats()));
        this.setBodyID(template.getBodyID());
        this.setBodyState(template.getBodyState());
        this.setCanBeKilled(template.getCanBeKilled());
        this.setCanBeRooted(template.getCanBeRooted());
        this.setCanBeSlowed(template.getCanBeSlowed());
        this.setCanBeStunned(template.getCanBeStunned());
        this.setCanMove(template.getCanMove());
        this.setClassID(template.getClassID());
        this.setEquippedItems(template.getEquippedItems());
        this.setExperience(template.getExperience());
        this.setFaceID(template.getFaceID());
        this.setFacing(template.getFacing());
        this.setHairA(template.getHairA());
        this.setHairB(template.getHairB());
        this.setHairG(template.getHairG());
        this.setHairID(template.getHairID());
        this.setHairR(template.getHairR());
        this.setLevel(template.getLevel());
        this.setMaxStats(new AttributeSet());
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getBaseStats()));
        this.setMoveSpeed(template.getMoveSpeed());
        this.setName(template.getName());
        this.setNPCTemplateID(template.getNPCTemplateID());
        this.setNPCTemplate(template);
        this.setNPCType(template.getNPCType());
        this.setRespawnTime(template.getRespawnTime());
        this.setSurname(template.getSurname());
        this.setTitle(template.getTitle());
        this.setWeaponDamage(template.getWeaponDamage());
        this.setClass(world.getClassHandler().getClass(this.getClassID()));
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), this.getClas().getLevel(this.getLevel())
                .getBaseStats()));
        this.setSpawnX(this.getMapX());
        this.setSpawnY(this.getMapY());
        this.setBuffs(new ArrayList<Buff>());
        this.getMap().addNPC(this);
        this.spawn(world);
        return true;
    }

    /**
     * Spawn, spawns npc
     * <p>
     * adds it to map list, adds a movement event if npc can move
     */
    public void spawn(GameWorld world) throws Exception {
        this.setMapX(this.getSpawnX());
        this.setMapY(this.getSpawnY());
        this.getMap().placeCharacter(this);
        this.setState(Goose.NPC.States.Alive);
        // move to this square so this
        this.getMap().setCharacter(this, this.getMapX(), this.getMapY());
        this.setCurrentHP(this.getMaxStats().getHP());
        this.setCurrentMP(this.getMaxStats().getMP());
        this.setCurrentSP(this.getMaxStats().getSP());
        this.setCurrentBodyID(this.getBodyID());
        this.setFacing(this.getNPCTemplate().getFacing());
        this.setMoveEvent(null);
        this.setAggroTarget(null);
        this.setAggroValue(new Goose.NPC.Aggro(0, 0));
        this.setAggroTargetToValue(new HashMap<Player, Aggro>());
        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet = this.mKCString();
        for (Goose.Player player : range) {
            world.send(player, packet);
            this.aggroIfInRange(player, world);
        }
        this.addMoveEvent(world);
    }

    /**
     * AddMoveEvent, adds move event to event handler
     */
    public void addMoveEvent(GameWorld world) throws Exception {
        if (this.getMoveEvent() == null && this.getMoveSpeed() > 0.0) {
            double snared = 0;
            for (Buff buff : this.getBuffs()) {
                if (buff.getSpellEffect().getEffectType() == EffectTypes.Snare) {
                    if (snared == 0)
                        snared = (buff.getSpellEffect().getSnarePercent() / (double) 100.0);
                    else
                        snared *= (buff.getSpellEffect().getSnarePercent() / (double) 100.0);
                }

            }
            NPCMoveEvent ev = new NPCMoveEvent();
            ev.setTicks(ev.getTicks() + (long) ((this.getMoveSpeed() * (1 + snared)) * world.getTimerFrequency()));
            ev.setNPC(this);
            world.getEventHandler().addEvent(ev);
            this.setMoveEvent(ev);
        }

    }

    /**
     * NextStepTo, returns direction to go to get to x,y
     * <p>
     * 1,2,3,4 = up,right,down,left
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
     */
    public void faceTo(int direction, GameWorld world) throws Exception {
        if (this.getFacing() == direction) return;

        this.setFacing(direction);
        String packet = "CHH" + this.getLoginID() + "," + this.getFacing();
        List<Player> range = this.getMap().getPlayersInRange(this);
        for (Goose.Player player : range) {
            world.send(player, packet);
        }
    }

    /**
     * AddRegenEvent, adds regen event to eventhandler if needed
     */
    public void addRegenEvent(GameWorld world) throws Exception {
        if (this.getRegenEventExists()) return;

        if (this.getState() == Goose.NPC.States.Dead) return;

        if ((this.getCurrentHP() == this.getMaxStats().getHP())
                && (this.getCurrentMP() == this.getMaxStats().getMP())) {
            return;
        }

        // Already max stats
        RegenEvent ev = new RegenEvent();
        ev.setTicks(ev.getTicks()
                + (long) (GameSettings.getDefault().getRegenSpeed() * world.getTimerFrequency()));
        ev.setNPC(this);
        this.setRegenEventExists(true);
        world.getEventHandler().addEvent(ev);
    }

    /**
     *
     *
     */
    public void addAggro(Goose.Player player, long value, GameWorld world) throws Exception {
        this.addAggro(player, value, false, world);
    }

    /**
     * AddAggro, adds aggro to player
     */
    public void addAggro(Goose.Player player, long value, boolean wastaunt, GameWorld world)
            throws Exception {
        this.addMoveEvent(world);
        this.addAttackEvent(world);
        // no target so just add to max/mapping
        if (this.getAggroTarget() == null) {
            this.setAggroTarget(player);
            if (wastaunt)
                this.setAggroValue(new Goose.NPC.Aggro(0, value));
            else
                this.setAggroValue(new Goose.NPC.Aggro(value, 0));
            this.getAggroTargetToValue().put(this.getAggroTarget(), this.getAggroValue());
            // Set last attack time so it doesn't teleport/whatever right away
            this.setLastAttackTime(world.getTimeNow());
        } else // already got max aggro so increase it
            if (this.getAggroTarget() == player) {
                if (wastaunt)
                    this.getAggroValue().Taunt += value;
                else
                    this.getAggroValue().Damage += value;
                this.getAggroTargetToValue().put(player, this.getAggroValue());
            } else {
                // if player exists increment their aggro and check for max
                Aggro current = this.getAggroTargetToValue().get(player);
                if (current != null) {
                    if (wastaunt)
                        current.Taunt += value;
                    else
                        current.Damage += value;
                    if (current.compareTo(this.getAggroValue()) > 0) {
                        this.setAggroTarget(player);
                        this.setAggroValue(current);
                    } else {
                        this.getAggroTargetToValue().put(player, current);
                    }
                } else {
                    // else player didn't exist so check if they're the new max and
                    // add them
                    current = new Goose.NPC.Aggro(0, 0);
                    if (wastaunt)
                        current.Taunt += value;
                    else
                        current.Damage += value;
                    if (current.compareTo(this.getAggroValue()) > 0) {
                        this.setAggroTarget(player);
                        this.setAggroValue(current);
                        this.getAggroTargetToValue().put(player, this.getAggroValue());
                    } else {
                        this.getAggroTargetToValue().put(player, current);
                    }
                }
            }
    }

    /**
     * RemoveAggro, removes aggro towards player
     */
    public void removeAggro(Goose.Player player) throws Exception {
        this.getAggroTargetToValue().remove(player);
        if (this.getAggroTarget() == player) {
            // Set aggro to next highest
            Goose.Player newaggro = null;
            Goose.NPC.Aggro highest = new Goose.NPC.Aggro(0, 0);
            for (Entry<Player, Aggro> p : this.getAggroTargetToValue().entrySet()) {
                if (p.getValue().compareTo(highest) > 0) {
                    highest = p.getValue();
                    newaggro = p.getKey();
                }

            }
            this.setAggroTarget(newaggro);
            this.setAggroValue(highest);
        }

    }

    /**
     * AggroIfInRange, sets aggro to player if needed
     */
    public void aggroIfInRange(Goose.Player player, GameWorld world) throws Exception {
        if (this.getAggroRange() == 0) return;

        if (this.getAggroTarget() != null) return;

        if (Math.abs(this.getMapX() - player.getMapX()) <= this.getAggroRange() && Math.abs(this.getMapY() - player.getMapY()) <= this.getAggroRange()) {
            String packet = "EMOT" + this.getLoginID() + ",8\u0001";
            this.addAggro(player, 1, world);
            List<NPC> npcs = this.getMap().getNPCsInRange(this);
            for (Goose.NPC npc : npcs) {
                if (!this.getAllies().contains(npc.getNPCTemplate())) continue;

                if (npc.getAggroTarget() != null) continue;

                if (Math.abs(npc.getMapX() - this.getMapX()) <= npc.getAggroRange() && Math.abs(npc.getMapY() - this.getMapY()) <= npc.getAggroRange()) {
                    npc.addAggro(player, 1, world);
                    packet += "EMOT" + npc.getLoginID() + ",8\u0001";
                    npc.addMoveEvent(world);
                    npc.addAttackEvent(world);
                }

            }
            List<Player> range = this.getMap().getPlayersInRange(this);
//            packet = packet.replace("\u0001", "");
            packet = packet.substring(0, packet.lastIndexOf("\u0001"));
            for (Goose.Player p : range) {
                world.send(p, packet);
            }
            this.addMoveEvent(world);
            this.addAttackEvent(world);
        }

    }

    /**
     * NPC was attacked by character
     * <p>
     * BattleText ids: 1 - Red text 7 - Green text 10 - STUNNED 11 - ROOTED 20 - DODGE 21 - MISS 60 -
     * Yellow text
     */
    public void attacked(ICharacter character, double damage, GameWorld world) throws Exception {
        if (character instanceof Goose.Player) {
            List<Player> range = this.getMap().getPlayersInRange(this);
            Goose.Player player = (Goose.Player) character;
            String packet;
            if (!this.getCanBeKilled()) {
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

            packet = "";
            int dmg = ((int) (damage));
            if (dmg <= 0) {
                packet = "BT" + this.getLoginID() + ",21,," + character.getName();
                for (Goose.Player p : range) {
                    world.send(p, packet);
                }
                this.addAggro(player, 1, world);
                this.addAttackEvent(world);
                return;
            }

            this.setCurrentHP(this.getCurrentHP() - dmg);
            packet += "BT" + this.getLoginID() + ",1," + (-dmg) + "," + character.getName() + "\u0001";
            this.addAggro(player, dmg, world);
            this.addAttackEvent(world);
            if (this.getCurrentHP() <= 0) {
                // move off this square so null
                this.getMap().setCharacter(null, this.getMapX(), this.getMapY());
                this.setState(Goose.NPC.States.Dead);
                this.setMoveEvent(null);
                this.setCurrentHP(0);
                packet = "ERC" + this.getLoginID();
                this.addRespawnEvent(world);
                HashMap<Object, Long> damages = new HashMap<Object, Long>();
                for (Entry<Player, Aggro> p : this.getAggroTargetToValue().entrySet()) {
                    if (p.getKey().getGroup() != null) {
                        if (damages.containsKey(p.getKey().getGroup())) {
                            damages.put(p.getKey().getGroup(),
                                    (long) damages.get(p.getKey().getGroup()) + p.getValue().Damage);
                        } else {
                            damages.put(p.getKey().getGroup(), p.getValue().Damage);
                        }
                    } else {
                        damages.put(p.getKey(), p.getValue().Damage);
                    }
                }
                Object highest = null;
                long highestdamage = 0;
                for (Entry<Object, Long> p : damages.entrySet()) {
                    if (p.getValue() > highestdamage) {
                        highestdamage = p.getValue();
                        highest = p.getKey();
                    }

                }
                if (highest instanceof Group) {
                    ((Group) highest).gainExperience(this, world);
                } else {
                    if (this.getLevel() + 9 < ((Goose.Player) highest).getLevel()) {
                        if (highest instanceof Pet) {
                            ((Pet) highest).getOwner().addExperience((long) (this.getExperience() * 0.10), world,
                                    ExperienceMessage.TooHigh);
                        }

                        ((Goose.Player) highest).addExperience((long) (this.getExperience() * 0.10), world,
                                ExperienceMessage.TooHigh);
                    } else {
                        if (highest instanceof Pet) {
                            ((Pet) highest).getOwner().addExperience(this.getExperience(), world,
                                    ExperienceMessage.Normal);
                        }

                        ((Goose.Player) highest).addExperience(this.getExperience(), world,
                                ExperienceMessage.Normal);
                    }
                }
                List<Buff> removebuff = new ArrayList<Buff>();
                for (Buff b : this.getBuffs()) {
                    removebuff.add(b);
                }
                for (Buff b : removebuff) {
                    this.removeBuff(b, world);
                }
                if (highest instanceof Group)
                    this.dropItems(((Group) highest).getPlayers().get(0), world);
                else
                    this.dropItems(((Goose.Player) highest), world);
            } else {
                packet += this.vCString();
                this.addRegenEvent(world);
            }
            if (packet.contains("\u0001")) packet = packet.substring(packet.lastIndexOf("\u0001"));

            for (Goose.Player p : range) {
                world.send(p, packet);
            }
        }

    }

    /**
     * AddRespawnEvent, adds respawn event
     */
    public void addRespawnEvent(GameWorld world) throws Exception {
        NPCSpawnEvent ev = new NPCSpawnEvent();
        ev.setTicks(ev.getTicks() + (long) (this.getRespawnTime() * world.getTimerFrequency()));
        ev.setNPC(this);
        world.getEventHandler().addEvent(ev);
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
     * AddAttackEvent, adds attack event if none exist
     */
    public void addAttackEvent(GameWorld world) throws Exception {
        if (this.getAttackEvent() != null) return;

        if (this.getAggroTarget() == null) return;

        if (this.getAttackSpeed() <= 0.0 || this.getAttackRange() <= 0) return;

        double snared = 0;
        for (Buff buff : this.getBuffs()) {
            if (buff.getSpellEffect().getEffectType() == EffectTypes.Snare) {
                if (snared == 0)
                    snared = (buff.getSpellEffect().getSnarePercent() / (double) 100.0);
                else
                    snared *= (buff.getSpellEffect().getSnarePercent() / (double) 100.0);
            }

        }
        NPCAttackEvent ev = new NPCAttackEvent();
        ev.setTicks(ev.getTicks()
                + (long) ((this.getAttackSpeed() * (1 + snared)) * world.getTimerFrequency()));
        ev.setNPC(this);
        this.setAttackEvent(ev);
        world.getEventHandler().addEvent(ev);
    }

    /**
     * Attack, attack character
     */
    public void attack(ICharacter character, GameWorld world) throws Exception {
        double damage =
                this.getMaxStats().getStrength() + this.getWeaponDamage() + this.getLevel()
                        + (this.getLevel() - character.getLevel());
        double maxac = GameSettings.getDefault().getMaxAC();
        double absorb =
                (1 - ((double) (character.getMaxStats().getAC() * character.getClas().getACMultiplier()) / maxac));
        if (world.getRandom().nextInt(10000) + 1 <= this.getMaxStats().getMeleeCrit() * 10000)
            damage *= 2;

        damage *= (1 + (double) this.getMaxStats().getMeleeDamage());
        damage *= (1 - (double) character.getMaxStats().getDamageReduction());
        damage *= absorb;
        damage -=
                (double) (character.getMaxStats().getAC() * character.getClas().getACMultiplier() / 25);
        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet = "ATT" + this.getLoginID();
        for (Goose.Player player : range) {
            world.send(player, packet);
        }
        if (damage > 0) {
            character.attacked(this, damage, world);
            character.onMeleeHit(this, world);
            this.onMeleeAttack(character, world);
        } else {
            packet = "BT" + character.getLoginID() + ",21,," + this.getName();
            for (Goose.Player player : range) {
                world.send(player, packet);
            }
        }
        if (character == this.getAggroTarget()) this.setLastAttackTime(world.getTimeNow());

    }

    /**
     * DropItems, drop items if any
     */
    public void dropItems(Goose.Player player, GameWorld world) throws Exception {
        for (NPCDropInfo dropinfo : this.getNPCTemplate().getDrops()) {
            if (world.getRandom().nextInt(1000000) + 1 <= GameSettings.getDefault().getDropRateModifier()
                    * dropinfo.getDropRate() * 10000) {
                ItemSlot drop = new ItemSlot();
                if (dropinfo.getItemTemplate().getID() == GameSettings.getDefault().getGoldItemID()) {
                    drop.setItem(world.getItemHandler().getGold());
                } else {
                    drop.setItem(new Item());
                    if (dropinfo.getItemTemplate().getUseType() == Goose.ItemTemplate.UseTypes.Equipment
                            && world.getRandom().nextInt(100000) + 1 <= 1000) {
                        drop.getItem().setName("Powerful " + dropinfo.getItemTemplate().getName());
                        drop.getItem().setStatMultiplier(2);
                        drop.getItem().loadFromTemplate(dropinfo.getItemTemplate());
                        drop.getItem().setTotalStats(dropinfo.getItemTemplate().getBaseStats());
                        drop.getItem().setTotalStats(
                                AttributeSet.multiply(drop.getItem().getTotalStats(), drop.getItem()
                                        .getStatMultiplier()));
                        drop.getItem().setTotalStats(
                                AttributeSet.add(drop.getItem().getTotalStats(), drop.getItem().getBaseStats()));
                    } else {
                        drop.getItem().loadFromTemplate(dropinfo.getItemTemplate());
                    }
                    world.getItemHandler().addItem(drop.getItem());
                }
                drop.setStack(dropinfo.getStack());
                ItemTile tile = new ItemTile();
                tile.setItemSlot(drop);
                tile.setX(this.getMapX());
                tile.setY(this.getMapY());
                if (player instanceof Pet)
                    tile.setOwner(((Pet) player).getOwner());
                else
                    tile.setOwner(player);
                tile.setPickupTime(world.getTimeNow()
                        + (GameSettings.getDefault().getItemProtectedTime() * world.getTimerFrequency()));
                this.getMap().placeItem(tile);
                // tile can stack
                ItemTile maptile = (ItemTile) this.getMap().getTile(tile.getX(), tile.getY());
                if (maptile != null && maptile instanceof ItemTile) {
                    if (drop.getItem().getItemID() != GameSettings.getDefault().getItemIDStartpoint()
                            + GameSettings.getDefault().getGoldItemID()) {
                        drop.getItem().setDelete(true);
                    }

                    maptile.getItemSlot().setStack(maptile.getItemSlot().getStack() + drop.getStack());
                    maptile.setOwner(tile.getOwner());
                    maptile.setPickupTime(tile.getPickupTime());
                    world.sendToMap(this.getMap(), maptile.mOBString());
                } else {
                    this.getMap().addItem(tile, world);
                }
            }

        }
    }

    public void addBuff(Buff buff, GameWorld world) throws Exception {
        List<Player> range = this.getMap().getPlayersInRange(this);
        String packet;
        for (Buff b : this.getBuffs()) {
            if (buff.getSpellEffect().getBuffDoesntStackOver().contains(b.getSpellEffect())) return;

            // already have that buff so renew the time cast
            if (buff.getSpellEffect() == b.getSpellEffect()
                    || buff.getSpellEffect().getBuffStacksOver().contains(b.getSpellEffect())) {
                b.setTimeCast(world.getTimeNow());
                b.setSpellEffect(buff.getSpellEffect());
                b.setCaster(buff.getCaster());
                if (buff.getSpellEffect().getAnimation() != 0) {
                    packet = "SPP" + this.getLoginID() + "," + buff.getSpellEffect().getAnimation();
                    if (buff.getSpellEffect().getDoAttackAnimation())
                        packet += "\u0001ATT" + this.getLoginID();

                    for (Goose.Player player : range) {
                        // kinda weird but k
                        world.send(player, packet);
                    }
                }

                return;
            }

        }
        if (buff.getSpellEffect().getDuration() > 0) {
            // else we don't have the buff. add it
            Event ev = new BuffExpireEvent();
            ev.setTicks(ev.getTicks() + buff.getSpellEffect().getDuration() * world.getTimerFrequency());
            ev.setNPC(this);
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
                ev.setNPC(this);
                ev.setTicks(ev.getTicks()
                        + (long) (GameSettings.getDefault().getSpellEffectPeriod() * world.getTimerFrequency()));
                world.getEventHandler().addEvent(ev);
            }

        }

        this.getBuffs().add(buff);
        // Add/remove stats
        this.setMaxStats(AttributeSet.add(this.getMaxStats(), buff.getSpellEffect().getStats()));
        this.addRegenEvent(world);
        packet = this.vCString();
        if (buff.getSpellEffect().getAnimation() != 0)
            packet += "\u0001SPP" + this.getLoginID() + "," + buff.getSpellEffect().getAnimation();

        if (buff.getSpellEffect().getDoAttackAnimation()) packet += "\u0001ATT" + this.getLoginID();

        for (Goose.Player player : range) {
            // kinda weird but k
            world.send(player, packet);
        }
    }

    public void removeBuff(Buff buff, GameWorld world) throws Exception {
        this.getBuffs().remove(buff);
        if (buff.getBuffExpireEvent() != null) {
            world.getEventHandler().removeEvent(buff.getBuffExpireEvent());
            buff.setBuffExpireEvent(null);
        }

        // Add/remove stats
        this.setMaxStats(AttributeSet.subtract(this.getMaxStats(), buff.getSpellEffect().getStats()));
        this.addRegenEvent(world);
        if (this.getState() == Goose.NPC.States.Alive) {
            List<Player> range = this.getMap().getPlayersInRange(this);
            String packet = this.vCString();
            for (Goose.Player player : range) {
                world.send(player, packet);
            }
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
                    b.getSpellEffect().getOnMeleeAttackSpell().cast(this, hit, world);

            }

        }
    }

    /**
     * OpenVendorWindow, opens vendor window with player if needed
     */
    public void openVendorWindow(Goose.Player player, GameWorld world) throws Exception {
        for (Window window : player.getWindows()) {
            if (window.getType() == WindowTypes.Vendor && window.getNPC() == this) {
                window.refresh(player, world);
                return;
            }

        }
        Window w = new Window();
        w.setType(WindowTypes.Vendor);
        w.setTitle("Welcome to my shop!");
        w.setButtons("0,1,0,0,0");
        w.setNPC(this);
        player.getWindows().add(w);
        w.create(player, world);
    }

    /**
     * CloseVendorWindow, removes vendor window from players info
     */
    public void closeVendorWindow(Window window, Goose.Player player, GameWorld world)
            throws Exception {
        player.getWindows().remove(window);
    }

}
