package Goose.Events;

import Goose.Buff;
import Goose.Event;
import Goose.Events.MoveEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.ITile;
import Goose.SpellEffect;
import Goose.SpellEffect.EffectTypes;
import Goose.WarpTile;
import Goose.Window;
import Goose.Window.WindowTypes;

/**
 * MoveEvent, event for "M" + 1-4 packet
 * 
 * Called when someone moves Packet format: MDirection Direction being a number
 * 1-4 corresponding to 1,2,3,4 = up,right,down,left
 * 
 * Server responds: MOCLoginID,X,Y Server sends the response to everyone in the
 * area excluding the player who generated it
 * 
 */
public class MoveEvent extends Event {
	public MoveEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new MoveEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			for (Object __dummyForeachVar0 : this.getPlayer().getBuffs()) {
				Buff b = (Buff) __dummyForeachVar0;
				// can't move when stunned or rooted
				if (b.getSpellEffect().getEffectType() == EffectTypes.Stun) {
					// stunned battletext
					world.send(this.getPlayer(), "BT"
							+ this.getPlayer().getLoginID() + ",50");
					// Fix the clients position
					world.send(this.getPlayer(), "SUP"
							+ this.getPlayer().getMapX() + ","
							+ this.getPlayer().getMapY());
					return;
				} else if (b.getSpellEffect().getEffectType() == EffectTypes.Root) {
					// rooted battletext
					world.send(this.getPlayer(), "BT"
							+ this.getPlayer().getLoginID() + ",11");
					// Fix the clients position
					world.send(this.getPlayer(), "SUP"
							+ this.getPlayer().getMapX() + ","
							+ this.getPlayer().getMapY());
					return;
				}

			}
			for (Object __dummyForeachVar1 : this.getPlayer().getWindows()) {
				Window window = (Window) __dummyForeachVar1;
				if (window.getType() == WindowTypes.Vendor) {
					world.send(this.getPlayer(),
							"$7You can't move while with a vendor.");
					return;
				}

			}
			if (((String) this.getData()).length() == 1)
				return;

			// log bad move event
			int direction = Integer.valueOf(String.valueOf(((String) this
					.getData()).charAt(1)));
			if (direction <= 0 || direction >= 5)
				return;

			// log bad move event
			/* Speedhack detection */
			if (GameSettings.getDefault().getSpeedhackDetectionEnabled()) {
				if (this.getPlayer().getMovementRecordingSteps() == 0) {
					this.getPlayer().setMovementRecordingStarted(
							world.getTimeNow());
				}

				this.getPlayer().setMovementRecordingSteps(
						this.getPlayer().getMovementRecordingSteps() + 1);
				if (this.getPlayer().getMovementRecordingSteps() >= 15) {
					long diff = world.getTimeNow()
							- this.getPlayer().getMovementRecordingStarted();
					double secs = (double) diff
							/ (double) world.getTimerFrequency();
					double rate = (double) this.getPlayer()
							.getMovementRecordingSteps() / secs;
					if (rate > 4.0) {
						System.out.println("SUSPECTED SPEEDHACK: "
								+ this.getPlayer().getName() + " 15sq/" + secs
								+ "sec = " + rate);
					}

					this.getPlayer().setMovementRecordingSteps(0);
				}

			}

			// original x/y
			int ox = this.getPlayer().getMapX();
			int oy = this.getPlayer().getMapY();
			int x = ox;
			int y = oy;
			switch (direction) {
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
			if (this.getPlayer().canMoveTo(x, y)) {
				this.getPlayer().moveTo(world, x, y);
				// Check if new tile is a warp tile
				ITile tile = this.getPlayer().getMap().getTile(x, y);
				if (tile != null && tile instanceof WarpTile) {
					WarpTile warp = (WarpTile) tile;
					if (warp.getWarpMap()
							.playerCanJoin(this.getPlayer(), world)) {
						this.getPlayer().warpTo(world, warp.getWarpMap(),
								warp.getWarpX(), warp.getWarpY());
					} else {
						// Have to move player back
						this.getPlayer().moveTo(world, ox, oy);
						// Fix the clients position
						world.send(this.getPlayer(), "SUP"
								+ this.getPlayer().getMapX() + ","
								+ this.getPlayer().getMapY());
					}
				}

			} else {
				// Fix the clients position
				world.send(this.getPlayer(), "SUP" + this.getPlayer().getMapX()
						+ "," + this.getPlayer().getMapY());
			}
		}

	}

}
