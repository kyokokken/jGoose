package Goose;

import Goose.Event;
import Goose.Events.AetherCommandEvent;
import Goose.Events.ApproachEvent;
import Goose.Events.AuctionCommandEvent;
import Goose.Events.BuyManaCommandEvent;
import Goose.Events.BuyVitaCommandEvent;
import Goose.Events.ChangeClassEvent;
import Goose.Events.ChangeNameCommandEvent;
import Goose.Events.ChangePasswordCommandEvent;
import Goose.Events.CharacterInfoCommandEvent;
import Goose.Events.ChatEvent;
import Goose.Events.CheckNameCommandEvent;
import Goose.Events.DestroyItemEvent;
import Goose.Events.DestroySpellEvent;
import Goose.Events.DoneLoadingMapEvent;
import Goose.Events.EmoteEvent;
import Goose.Events.FacingEvent;
import Goose.Events.GMBanCommandEvent;
import Goose.Events.GMBroadcastCommandEvent;
import Goose.Events.GMClassChangeCommandEvent;
import Goose.Events.GMGetItemCommandEvent;
import Goose.Events.GMGiveExperienceCommandEvent;
import Goose.Events.GMKickCommandEvent;
import Goose.Events.GMShutdownServer;
import Goose.Events.GMSpawnNPC;
import Goose.Events.GroupAddEvent;
import Goose.Events.GroupChatEvent;
import Goose.Events.GroupRemoveEvent;
import Goose.Events.GuildAddCommandEvent;
import Goose.Events.GuildChatCommandEvent;
import Goose.Events.GuildCreateCommandEvent;
import Goose.Events.GuildMotdCommandEvent;
import Goose.Events.GuildOfficerCommandEvent;
import Goose.Events.GuildOwnerCommandEvent;
import Goose.Events.GuildRemoveCommandEvent;
import Goose.Events.HaxCommandEvent;
import Goose.Events.InstaLevelCommandEvent;
import Goose.Events.InventoryChangeSlotEvent;
import Goose.Events.InventorySplitEvent;
import Goose.Events.InventoryToWindowEvent;
import Goose.Events.InventoryUseEvent;
import Goose.Events.ItemInfoEvent;
import Goose.Events.KillBuffEvent;
import Goose.Events.LocationEvent;
import Goose.Events.LoginContinuedEvent;
import Goose.Events.LoginEvent;
import Goose.Events.MoveEvent;
import Goose.Events.OpenCombineBagEvent;
import Goose.Events.PetDamageCommandEvent;
import Goose.Events.PetDeleteCommandEvent;
import Goose.Events.PetInfoCommandEvent;
import Goose.Events.PetListCommandEvent;
import Goose.Events.PetSpawnCommandEvent;
import Goose.Events.PetVitaCommandEvent;
import Goose.Events.PickupItemEvent;
import Goose.Events.PlayerAttackEvent;
import Goose.Events.PlayerCastSpellEvent;
import Goose.Events.PlayerDropGoldEvent;
import Goose.Events.PlayerDropItemEvent;
import Goose.Events.PlayerPongEvent;
import Goose.Events.PlayerRightClickEvent;
import Goose.Events.RandomCommandEvent;
import Goose.Events.RankCommandEvent;
import Goose.Events.RefreshPositionEvent;
import Goose.Events.RespawnMapCommandEvent;
import Goose.Events.SaveConfigCommandEvent;
import Goose.Events.SetConfigCommandEvent;
import Goose.Events.ShoutCommandEvent;
import Goose.Events.ShutdownCommandEvent;
import Goose.Events.SpellbookSwapEvent;
import Goose.Events.SummonEvent;
import Goose.Events.TellEvent;
import Goose.Events.ToggleCommandEvent;
import Goose.Events.ToggleGroupCommandEvent;
import Goose.Events.UnbanCommandEvent;
import Goose.Events.VendorPurchaseInventoryEvent;
import Goose.Events.VendorSellInventoryEvent;
import Goose.Events.WarpEvent;
import Goose.Events.WhoEvent;
import Goose.Events.WindowButtonClickEvent;
import Goose.Events.WindowToInventoryEvent;
import Goose.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * EventHandler, does events at the specified time
 * 
 */
public class EventHandler {
	/**
	 * SortedList acts like a priority queue
	 * 
	 */
//	SortedList<Long, Event> events = new SortedList<Long, Event>();
	LinkedHashMap<Long, Event> events;
	/**
	 * StringToEvent, converts a string to an event creator delegate
	 * 
	 */
//	HashMap<String, CreateEvent> stringToEvent;
	HashMap<String, Event> stringToEvent;
	
	Player player;
	Object data;

	static class __MultiCreateEvent implements CreateEvent {
		public Event invoke(Player player, Object data) throws Exception {
			List<CreateEvent> copy, members = this.getInvocationList();
			synchronized (members) {
				copy = new LinkedList<CreateEvent>(members);
			}
			CreateEvent prev = null;
			for (CreateEvent d : copy) {
				if (prev != null)
					prev.invoke(player, data);

				prev = d;
			}
			return prev.invoke(player, data);
		}

		private List<CreateEvent> _invocationList = new ArrayList<CreateEvent>();

		public static CreateEvent combine(CreateEvent a, CreateEvent b)
				throws Exception {
			if (a == null)
				return b;

			if (b == null)
				return a;

			__MultiCreateEvent ret = new __MultiCreateEvent();
			ret._invocationList = a.getInvocationList();
			ret._invocationList.addAll(b.getInvocationList());
			return ret;
		}

		/**
		 * Constructor, constructs sortedlist
		 * 
		 */
		public static CreateEvent remove(CreateEvent a, CreateEvent b)
				throws Exception {
			if (a == null || b == null)
				return a;

			List<CreateEvent> aInvList = a.getInvocationList();
//			List<CreateEvent> newInvList = ListSupport.removeFinalStretch(
//					aInvList, b.getInvocationList());
			List<CreateEvent> newInvList = null;
			if (aInvList == newInvList) {
				return a;
			} else {
				__MultiCreateEvent ret = new __MultiCreateEvent();
				ret._invocationList = newInvList;
				return ret;
			}
		}

		public List<CreateEvent> getInvocationList() throws Exception {
			return _invocationList;
		}

	}

	static interface CreateEvent {
		Event invoke(Player player, Object data) throws Exception;
		List<CreateEvent> getInvocationList() throws Exception;

	}

	public EventHandler() throws Exception {
		this.events = new LinkedHashMap<Long, Event>();
		this.stringToEvent = new HashMap<String, Event>();
		this.stringToEvent.put("LOGIN", LoginEvent.create(player, data));
		this.stringToEvent.put("LCNT", LoginContinuedEvent.create(player, data));
		this.stringToEvent.put("DLM", DoneLoadingMapEvent.create(player, data));
		this.stringToEvent.put(";", ChatEvent.create(player, data));
		this.stringToEvent.put("M1", MoveEvent.create(player, data));
		this.stringToEvent.put("M2", MoveEvent.create(player, data));
		this.stringToEvent.put("M3", MoveEvent.create(player, data));
		this.stringToEvent.put("M4", MoveEvent.create(player, data));
		this.stringToEvent.put("F1", FacingEvent.create(player, data));
		this.stringToEvent.put("F2", FacingEvent.create(player, data));
		this.stringToEvent.put("F3", FacingEvent.create(player, data));
		this.stringToEvent.put("F4", FacingEvent.create(player, data));
		this.stringToEvent.put("/tell ", TellEvent.create(player, data));
		this.stringToEvent.put("/who", WhoEvent.create(player, data));
		this.stringToEvent.put("/changeclass ", ChangeClassEvent.create(player, data));
		this.stringToEvent.put("/summon ", SummonEvent.create(player, data));
		this.stringToEvent.put("/warp ", WarpEvent.create(player, data));
		this.stringToEvent.put("/approach ", ApproachEvent.create(player, data));
		this.stringToEvent.put("CHANGE", InventoryChangeSlotEvent.create(player, data));
		this.stringToEvent.put("SPLIT", InventorySplitEvent.create(player, data));
		this.stringToEvent.put("USE", InventoryUseEvent.create(player, data));
		this.stringToEvent.put("GET", PickupItemEvent.create(player, data));
		this.stringToEvent.put("DRP", PlayerDropItemEvent.create(player, data));
		this.stringToEvent.put("/dropgold ", PlayerDropGoldEvent.create(player, data));
		this.stringToEvent.put("ATT", PlayerAttackEvent.create(player, data));
		this.stringToEvent.put("PONG", PlayerPongEvent.create(player, data));
		this.stringToEvent.put("/shutdown", ShutdownCommandEvent.create(player, data));
		this.stringToEvent.put("/location", LocationEvent.create(player, data));
		this.stringToEvent.put("/refresh", RefreshPositionEvent.create(player, data));
		this.stringToEvent.put("CAST", PlayerCastSpellEvent.create(player, data));
		this.stringToEvent.put("/getitem ", GMGetItemCommandEvent.create(player, data));
		this.stringToEvent.put("/hax ", HaxCommandEvent.create(player, data));
		this.stringToEvent.put("/togglegroup", ToggleGroupCommandEvent.create(player, data));
		this.stringToEvent.put("/group ", GroupChatEvent.create(player, data));
		this.stringToEvent.put("/groupadd ", GroupAddEvent.create(player, data));
		this.stringToEvent.put("/groupremove", GroupRemoveEvent.create(player, data));
		this.stringToEvent.put("RC", PlayerRightClickEvent.create(player, data));
		this.stringToEvent.put("WBC", WindowButtonClickEvent.create(player, data));
		this.stringToEvent.put("VPI", VendorPurchaseInventoryEvent.create(player, data));
		this.stringToEvent.put("VSI", VendorSellInventoryEvent.create(player, data));
		this.stringToEvent.put("/ban ", GMBanCommandEvent.create(player, data));
		this.stringToEvent.put("/kick ", GMKickCommandEvent.create(player, data));
		this.stringToEvent.put("GID", ItemInfoEvent.create(player, data));
		this.stringToEvent.put("/shout ", ShoutCommandEvent.create(player, data));
		this.stringToEvent.put("/auction ", AuctionCommandEvent.create(player, data));
		this.stringToEvent.put("/random", RandomCommandEvent.create(player, data));
		this.stringToEvent.put("/broadcast ", GMBroadcastCommandEvent.create(player, data));
		this.stringToEvent.put("EMOT", EmoteEvent.create(player, data));
		this.stringToEvent.put("/buyvita", BuyVitaCommandEvent.create(player, data));
		this.stringToEvent.put("/buymana", BuyManaCommandEvent.create(player, data));
		this.stringToEvent.put("DITM", DestroyItemEvent.create(player, data));
		this.stringToEvent.put("DSPL", DestroySpellEvent.create(player, data));
		this.stringToEvent.put("SWAP", SpellbookSwapEvent.create(player, data));
		this.stringToEvent.put("OCB", OpenCombineBagEvent.create(player, data));
		this.stringToEvent.put("ITW", InventoryToWindowEvent.create(player, data));
		this.stringToEvent.put("WTI", WindowToInventoryEvent.create(player, data));
		this.stringToEvent.put("/charinfo", CharacterInfoCommandEvent.create(player, data));
		this.stringToEvent.put("/guildcreate ", GuildCreateCommandEvent.create(player, data));
		this.stringToEvent.put("/guildadd ", GuildAddCommandEvent.create(player, data));
		this.stringToEvent.put("/guildremove", GuildRemoveCommandEvent.create(player, data));
		this.stringToEvent.put("/guildmotd", GuildMotdCommandEvent.create(player, data));
		this.stringToEvent.put("/guildowner ", GuildOwnerCommandEvent.create(player, data));
		this.stringToEvent.put("/guildofficer ",
				GuildOfficerCommandEvent.create(player, data));
		this.stringToEvent.put("/guild ", GuildChatCommandEvent.create(player, data));
		this.stringToEvent.put("/rank", RankCommandEvent.create(player, data));
		this.stringToEvent.put("/setconfig ", SetConfigCommandEvent.create(player, data));
		this.stringToEvent.put("/saveconfig", SaveConfigCommandEvent.create(player, data));
		this.stringToEvent.put("/respawnmap", RespawnMapCommandEvent.create(player, data));
		this.stringToEvent.put("/changepassword ",
				ChangePasswordCommandEvent.create(player, data));
		this.stringToEvent.put("KBUF", KillBuffEvent.create(player, data));
		this.stringToEvent.put("/toggle ", ToggleCommandEvent.create(player, data));
		this.stringToEvent.put("/aether ", AetherCommandEvent.create(player, data));
		this.stringToEvent.put("/instalevel", InstaLevelCommandEvent.create(player, data));
		this.stringToEvent.put("/petlist", PetListCommandEvent.create(player, data));
		this.stringToEvent.put("/petspawn ", PetSpawnCommandEvent.create(player, data));
		this.stringToEvent.put("/petinfo ", PetInfoCommandEvent.create(player, data));
		this.stringToEvent.put("/petdamage ", PetDamageCommandEvent.create(player, data));
		this.stringToEvent.put("/petvita ", PetVitaCommandEvent.create(player, data));
		this.stringToEvent.put("/petdelete ", PetDeleteCommandEvent.create(player, data));
		this.stringToEvent.put("/unban ", UnbanCommandEvent.create(player, data));
		this.stringToEvent.put("/checkname ", CheckNameCommandEvent.create(player, data));
		this.stringToEvent.put("/classchange ",
				GMClassChangeCommandEvent.create(player, data));
		this.stringToEvent.put("/changename ", ChangeNameCommandEvent.create(player, data));
		this.stringToEvent.put("/giveexperience ",
				GMGiveExperienceCommandEvent.create(player, data));
		this.stringToEvent.put("/shutdownserver ", GMShutdownServer.create(player, data));
		this.stringToEvent.put("/spawn ", GMSpawnNPC.create(player, data));
	}

	/**
	 * AddEvent, creates Event object from packet and adds it to events
	 * 
	 * This function is pretty sexy, not sure if it's a very good way of doing
	 * it though What it does is searches our stringtToEvent dictionary and sees
	 * if any of the keys match with the start of the packet.
	 * 
	 * The stringToEvent dictionary holds a delegate which calls the static
	 * member of the Event class which creates a new object of that event type
	 * and returns it.
	 * 
	 * If we find a matching packet we return true. If we don't find a match
	 * returns false.
	 * 
	 */
	public boolean addEvent(Player player, String packet) throws Exception {
        for (String key : this.stringToEvent.keySet())
        {
            if (packet.startsWith(key))
            {
                Event e = this.stringToEvent.get(key);
                e.setPlayer(player);
                e.setData(packet);
                this.addEvent(e);
                return true;
            }
             
        }
        return false;
    }

	/**
	 * AddEvent, adds the Event object to events
	 * 
	 */
	public void addEvent(Event e) throws Exception {
		if (this.events.containsKey(e.getTicks())) {
			e.setTicks(e.getTicks() + 1);
			this.addEvent(e);
		} else {
			this.events.put(e.getTicks(), e);
		}
	}

	// this.events[e.Ticks] = e;
	/**
	 * RemoveEvent, removes event from event handler
	 * 
	 */
	public void removeEvent(Event e) throws Exception {
		this.events.remove(e.getTicks());
	}

	/**
	 * Update, loops through list doing events if they need to be done
	 * 
	 */
	public void update(GameWorld world) throws Exception {
        long now;
        // High resolution timing
        now = System.nanoTime();
        LinkedHashMap<Long, Event> readyEvents = new LinkedHashMap<>();
        for (long key : this.events.keySet())
        {
        	if (key <= now)
        	{
        		readyEvents.put(key, this.events.get(key));
        	}
        }
        
        for (Entry<Long, Event> entry : readyEvents.entrySet())
        {
        	Event e = entry.getValue();
        	e.ready(world);
//        	int index = 0;
//        	for (Iterator<Event> i = this.events.values().iterator();i.hasNext();)
//        	{
//        		Event next = i.next();
//        		if (next.equals(e))
//        		{
//        			break;
//        		}
//        		index++;
//        	}
        
//        	if (index < readyEvents.size())
        		this.events.remove(entry.getKey());
        }
        
        
        /*
        for (int i = 0;i < readyEvents.size() ;i++)
        {
            Event ev = (Event) readyEvents.values().toArray()[i];
            ev.ready(world);
            ArrayList<Event> eventsAL = new ArrayList<>(this.events.values());
//            if (eventsAL.indexOf(ev) < readyEvents.size())
//            	this.events.remove(ev);
            this.events.remove(i);
             
        }
        */
        
    }
}
