package Goose;

import Goose.NPC;
import Goose.Player;

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

	/**
	 * Constructor
	 * 
	 * Ticks defaults to the current time
	 */
	public Event() throws Exception {
		this.setTicks(System.nanoTime());
	}

	// this.Ticks = DateTime.Now.Ticks;
	public abstract void ready(GameWorld world) throws Exception;

}
