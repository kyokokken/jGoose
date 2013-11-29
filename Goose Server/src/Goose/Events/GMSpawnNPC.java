package Goose.Events;

import Goose.Event;
import Goose.GameWorld;
import Goose.NPC;
import Goose.NPCTemplate;
import Goose.Player.AccessStatus;

public class GMSpawnNPC extends Event {

	public GMSpawnNPC() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new GMSpawnNPC();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	@Override
	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			if (this.getPlayer().getAccess() != AccessStatus.GameMaster)
				return;

			int id = 0;
			int stack = 1;
			String[] t = ((String) this.getData()).split(" ");
			if (t.length <= 2) {
				try {
					id = Integer.valueOf(t[1]);
				} catch (Exception e) {
					return;
				}

			}

			if (t.length >= 3) {
				try {
					id = Integer.valueOf(t[1]);
					stack = Integer.valueOf(t[2]);
				} catch (Exception e) {
					stack = 1;
				}

			}

			if (id <= 0 || stack <= 0 || stack > 99)
				return;

			NPCTemplate template = world.getNPCHandler().getNPCTemplate(id);
			if (template == null)
				return;

			for (int i = 0; i < stack; i++) {
				NPC npc = new NPC();
				npc.setLoginID(this.getPlayer().getMap().getNPCs().size() + 1);
				npc.loadFromTemplate(world, this.getPlayer().getMapID(), this
						.getPlayer().getMapX(), this.getPlayer().getMapY(),
						template);
			}
		}
	}

}
