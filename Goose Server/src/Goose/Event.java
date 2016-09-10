package Goose;

public abstract class Event {
    private long __Ticks;

    public long getTicks() {
        return __Ticks;
    }

    public void setTicks(long value) {
        __Ticks = value;
    }

    private Player __Player;

    public Player getPlayer() {
        return __Player;
    }

    public void setPlayer(Player value) {
        __Player = value;
    }

    private Object __Data;

    public Object getData() {
        return __Data;
    }

    public void setData(Object value) {
        __Data = value;
    }

    private NPC __NPC;

    public NPC getNPC() {
        return __NPC;
    }

    public void setNPC(NPC value) {
        __NPC = value;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructor
     * <p>
     * Ticks defaults to the current time
     */
    public Event(String name) throws Exception {
        this.name = name;
        this.setTicks(System.nanoTime());
    }

    public abstract void ready(GameWorld world) throws Exception;

}
