package Goose;

import Goose.ChatFilter;
import Goose.ClassHandler;
import Goose.CombinationHandler;
import Goose.Event;
import Goose.EventHandler;
import Goose.Events.ClearCreatedHistoryEvent;
import Goose.Events.LoginEvent;
import Goose.Events.LogoutEvent;
import Goose.GameServer;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.GuildHandler;
import Goose.ItemHandler;
import Goose.ItemTile;
import Goose.Map;
import Goose.MapHandler;
import Goose.NPCHandler;
import Goose.Pet;
import Goose.PlayerHandler;
import Goose.RankHandler;
import Goose.SpellHandler;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Random;

/**
 * GameWorld, this is where all the game-related stuff will go
 * 
 * Currently holds the PlayerHandler but eventually will hold - EventHandler -
 * LogHandler - NPCHandler - MapHandler ... etc
 * 
 */
public class GameWorld {
	private PlayerHandler __PlayerHandler;

	public PlayerHandler getPlayerHandler() {
		return __PlayerHandler;
	}

	public void setPlayerHandler(PlayerHandler value) {
		__PlayerHandler = value;
	}

	private EventHandler __EventHandler;

	public EventHandler getEventHandler() {
		return __EventHandler;
	}

	public void setEventHandler(EventHandler value) {
		__EventHandler = value;
	}

	private MapHandler __MapHandler;

	public MapHandler getMapHandler() {
		return __MapHandler;
	}

	public void setMapHandler(MapHandler value) {
		__MapHandler = value;
	}

	private NPCHandler __NPCHandler;

	public NPCHandler getNPCHandler() {
		return __NPCHandler;
	}

	public void setNPCHandler(NPCHandler value) {
		__NPCHandler = value;
	}

	private ClassHandler __ClassHandler;

	public ClassHandler getClassHandler() {
		return __ClassHandler;
	}

	public void setClassHandler(ClassHandler value) {
		__ClassHandler = value;
	}

	private Connection __SqlConnection;

	public Connection getSqlConnection() {
		return __SqlConnection;
	}

	public void setSqlConnection(Connection value) {
		__SqlConnection = value;
	}

	private GameServer __GameServer;

	public GameServer getGameServer() {
		return __GameServer;
	}

	public void setGameServer(GameServer value) {
		__GameServer = value;
	}

	private ItemHandler __ItemHandler;

	public ItemHandler getItemHandler() {
		return __ItemHandler;
	}

	public void setItemHandler(ItemHandler value) {
		__ItemHandler = value;
	}

	private SpellHandler __SpellHandler;

	public SpellHandler getSpellHandler() {
		return __SpellHandler;
	}

	public void setSpellHandler(SpellHandler value) {
		__SpellHandler = value;
	}

	private GuildHandler __GuildHandler;

	public GuildHandler getGuildHandler() {
		return __GuildHandler;
	}

	public void setGuildHandler(GuildHandler value) {
		__GuildHandler = value;
	}

	private RankHandler __RankHandler;

	public RankHandler getRankHandler() {
		return __RankHandler;
	}

	public void setRankHandler(RankHandler value) {
		__RankHandler = value;
	}

	private CombinationHandler __CombinationHandler;

	public CombinationHandler getCombinationHandler() {
		return __CombinationHandler;
	}

	public void setCombinationHandler(CombinationHandler value) {
		__CombinationHandler = value;
	}

	private ChatFilter __ChatFilter;

	public ChatFilter getChatFilter() {
		return __ChatFilter;
	}

	public void setChatFilter(ChatFilter value) {
		__ChatFilter = value;
	}

	private HashMap<String, Integer> __CharactersCreatedPerIP;

	public HashMap<String, Integer> getCharactersCreatedPerIP() {
		return __CharactersCreatedPerIP;
	}

	public void setCharactersCreatedPerIP(HashMap<String, Integer> value) {
		__CharactersCreatedPerIP = value;
	}

	long timerfreq;

	public long getTimerFrequency() throws Exception {
		return this.timerfreq;
	}

	Random rng;

	public Random getRandom() throws Exception {
		return this.rng;
	}

	public long getTimeNow() throws Exception {
		return System.nanoTime();
	}

	private boolean __Running;

	public boolean getRunning() {
		return __Running;
	}

	public void setRunning(boolean value) {
		__Running = value;
	}

	/**
	 * Constructor
	 * 
	 * Constructs all of our Handler objects
	 * 
	 */
	public GameWorld(GameServer server) throws Exception {
		this.timerfreq = 1_000_000_000;
		this.rng = new Random();
		this.setGameServer(server);
		this.setPlayerHandler(new PlayerHandler());
		this.setEventHandler(new EventHandler());
		this.setMapHandler(new MapHandler());
		this.setNPCHandler(new NPCHandler());
		this.setClassHandler(new ClassHandler());
		this.setItemHandler(new ItemHandler());
		this.setSpellHandler(new SpellHandler());
		this.setGuildHandler(new GuildHandler());
		this.setRankHandler(new RankHandler());
		this.setCombinationHandler(new CombinationHandler());
		this.setChatFilter(new ChatFilter());
		// ";Trusted_Connection=yes;" + // hopefully remove the windows
		// connection
		java.lang.Class.forName("com.mysql.jdbc.Driver");

		Connection connect = null;
		connect = DriverManager.getConnection("jdbc:mysql://localhost/goose?"
				+ "user=GooseServer&password=password1");

		this.setSqlConnection(connect);
		// Statement statement = connect.createStatement();
		// ResultSet resultSet = statement
		// .executeQuery("SELECT * FROM feedback.comments");

		// this.setSqlConnection(new SqlConnection("user id=" +
		// GameSettings.getDefault().getDatabaseUsername() + ";password=" +
		// GameSettings.getDefault().getDatabasePassword() + ";server=" +
		// GameSettings.getDefault().getDatabaseAddress() + ";database=" +
		// GameSettings.getDefault().getDatabaseName() +
		// ";connection timeout=30" +
		// ";async=true;MultipleActiveResultSets=True"));
	}

	// this.SqlConnection = new
	// SqlConnection("Data Source=JAJA-PC;Initial Catalog=Goose;Integrated Security=True;async=true;MultipleActiveResultSets=True");
	// this.SqlConnection = new
	// SqlConnection("Data Source=JAJA-PC;database=Goose;Integrated Security=True;async=true;MultipleActiveResultSets=True");
	// this.SqlConnection = new
	// SqlConnection("Data Source=JAJA-PC;database=Goose;Integrated Security=True;Connect Timeout=15;Encrypt=False;TrustServerCertificate=False;async=true;MultipleActiveResultSets=True");
	/**
	 * Start, game startup
	 * 
	 * Loads all of the required information for the game
	 * 
	 */
	public void start() throws Exception {
		this.setRunning(false);
		System.out.println("Starting Goose Private Server v"
				+ GameSettings.getDefault().getServerVersion());
		System.out.print("Connecting to Database: ");
		// try
		// {
		// this.getSqlConnection().Open();
		// }
		// catch (SqlException e)
		// {
		// System.out.println("Fail, " + e.Message);
		// System.out.println("Aborting...");
		// return ;
		// }

		System.out.println("Connected.");
		/**
		 * 
		 * This is for fixing spell last cast time If the host pc has been
		 * restarted players will have a high chance of not being able to cast
		 * spells
		 * 
		 */
		// SqlCommand query = new
		// SqlCommand("UPDATE spellbook SET last_casted=0",
		// this.getSqlConnection());
		getSqlConnection().createStatement().executeUpdate(
				"UPDATE spellbook SET last_casted=0");
		// query.ExecuteNonQuery();
		System.out.print("Loading Guilds: ");
		try {
			this.getGuildHandler().loadGuilds(this);
			this.getGuildHandler().addSaveEvent(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getGuildHandler().getCount())
				+ " guilds loaded.");
		System.out.print("Loading Spell Effects: ");
		try {
			this.getSpellHandler().loadSpellEffects(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getSpellHandler()
				.getEffectCount()) + " spell effects loaded.");
		System.out.print("Loading Spells: ");
		try {
			this.getSpellHandler().loadSpells(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getSpellHandler().getCount())
				+ " spells loaded.");
		System.out.print("Loading Item Templates: ");
		try {
			this.getItemHandler().loadTemplates(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			e.printStackTrace();
			return;
		}

		System.out.println(String.valueOf(this.getItemHandler()
				.getTemplateCount()) + " item templates loaded.");
		System.out.print("Loading Items: ");
		try {
			this.getItemHandler().loadItems(this);
			this.getItemHandler().addSaveEvent(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getItemHandler().getItemCount())
				+ " items loaded.");
		System.out.print("Loading Maps: ");
		try {
			this.getMapHandler().loadMaps(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getMapHandler().getCount())
				+ " maps loaded.");
		System.out.print("Loading Classes: ");
		try {
			this.getClassHandler().loadClasses(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getClassHandler().getCount())
				+ " classes loaded.");
		System.out.print("Loading NPC Templates: ");
		try {
			this.getNPCHandler().loadNPCTemplates(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getNPCHandler()
				.getTemplateCount()) + " NPC templates loaded.");
		System.out.print("Loading NPC Spawns: ");
		try {
			this.getNPCHandler().loadNPCs(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getNPCHandler().getNPCCount())
				+ " NPCs spawned.");
		System.out.print("Loading Combinations: ");
		try {
			this.getCombinationHandler().loadCombinations(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			e.printStackTrace();
			return;
		}

		System.out.println(String.valueOf(this.getCombinationHandler()
				.getCount()) + " combinations loaded.");
		System.out.print("Loading Chat Filter: ");
		try {
			this.getChatFilter().loadFilter(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getChatFilter().getCount())
				+ " words loaded.");
		System.out.print("Loading Players: ");
		try {
			this.getPlayerHandler().loadPlayerData(this);
		} catch (Exception e) {
			System.out.println("Fail, " + e.getMessage());
			System.out.println("Aborting...");
			return;
		}

		System.out.println(String.valueOf(this.getPlayerHandler()
				.getPlayerDataCount()) + " players loaded.");
		this.getRankHandler().updateAll(this);
		this.setCharactersCreatedPerIP(new HashMap<String, Integer>());
		Event clearCreatedHistory = new ClearCreatedHistoryEvent();
		clearCreatedHistory.setTicks(clearCreatedHistory.getTicks()
				+ this.getTimerFrequency() * 1_000_000_000 * 24 * 60 * 60);
		this.getEventHandler().addEvent(clearCreatedHistory);
		System.out.println("Finished loading game. Ready to join.");
		this.setRunning(true);
	}

	/**
	 * Stop, game shutdown
	 * 
	 * Makes sure all information is saved properly, etc
	 * 
	 */
	public void stop() throws Exception {
		System.out.println("Shutting down server.");
		System.out.println("Clearing maps.");
		for (Map map : this.getMapHandler().getMaps()) {
			for (ItemTile tile : map.getItems()) {
				if (tile.getItemSlot().getItem() != this.getItemHandler()
						.getGold())
					tile.getItemSlot().getItem().setDelete(true);

			}
		}
		System.out.println("Saving items.");
		this.getItemHandler().save(this);
		System.out.println("Saving players.");
		for (Goose.Player player : this.getPlayerHandler().getPlayers()) {
			if (player.getState().compareTo(Goose.Player.States.LoadingGame) > 0) {
				player.saveToDatabase(this);
			}

		}
		System.out.println("Finished shutting down.");
	}

	/**
	 * NewConnection, player joined server
	 * 
	 * Creates a new Player object and gives it to the PlayerHandler
	 * 
	 * 
	 */
	public void newConnection(Socket sock) throws Exception {
		System.out.println("Connection attempt: "
				+ sock.getInetAddress().getHostAddress());
	}

	/**
	 * LostConnection, player left server
	 * 
	 * Removes the player that left
	 * 
	 * 
	 */
	public void lostConnection(Socket sock) throws Exception {
		try {
			System.out.println("Connection lost: "
					+ sock.getInetAddress().getHostAddress());
			this.getGameServer().disconnect(sock);
			Event ev = new LogoutEvent();
			ev.setData(sock);
			ev.setTicks(ev.getTicks()
					+ (GameSettings.getDefault().getLogoutLagTime() * this
							.getTimerFrequency()));
			this.getEventHandler().addEvent(ev);
		} catch (Exception __dummyCatchVar0) {
		}

	}

	// eaten
	/**
	 * Received, received data from socket
	 * 
	 * First we check if we already have the player, if we do we call the
	 * player's Received method Then parse the data
	 * 
	 * If the player is null then we haven't seen them before so create a new
	 * Player object then this bit is hackish but it really shouldn't be a
	 * problem.. We assume the data is the full login packet so add an event to
	 * the event handler
	 * 
	 */
	public void received(Socket sock, String data) throws Exception {
		Goose.Player player = this.getPlayerHandler().getPlayer(sock);
		if (player != null) {
			player.received(data);
			this.parseData(player);
		} else {
			// player = new Player(sock);
			// this.EventHandler.AddEvent(player,
			// data.TrimEnd("\x1".ToCharArray()));
			Event ev = new LoginEvent();
			ev.setData(new Object[] { sock, data });
			this.getEventHandler().addEvent(ev);
		}
	}

	/**
	 * ParseData, parses data received from player
	 * 
	 * Splits the data by \x1 which is chr(1) which the client uses to delimit
	 * packets Passes each packet along to the EventHandler.AddEvent to see if
	 * the EventHandler will add the event.
	 * 
	 */
	public void parseData(Goose.Player player) throws Exception {

		String data = player.getBuffer();
		System.out.println("parseData: " + "<" + player.getName() + ">" + data);
		char delim = '\u0001';
		String[] tokens = data.split(String.valueOf(delim));
		// int limit = tokens.length - 1;
		int limit = tokens.length;
		if (!data.endsWith(String.valueOf(delim))) {
			limit--;
		}

		String packet;
		for (int i = 0; i < limit; i++) {
			packet = tokens[i];
			// data = data.Remove(0, packet.length() + 1);
			data = data.substring(packet.length() + 1);
			this.getEventHandler().addEvent(player, packet);
			System.out.println("addEvent: " + "<" + player.getName() + ">"
					+ packet);
		}
		player.setBuffer(data);
	}

	/**
	 * Update, update the game world
	 * 
	 * Called every 5ms at least, will probably update the EventHandler, do NPC
	 * logic, etc
	 * 
	 */
	public void update() throws Exception {
		this.getEventHandler().update(this);
	}

	/**
	 * Send, sends data to player
	 * 
	 * Adds \x1 to the end which is the packet delimiter
	 * 
	 */
	public void send(Goose.Player player, String data) throws Exception {
		if (player instanceof Pet)
			return;

		// Console.Out.WriteLine("Send: " + data);
		System.out.println("send: " + data);
		data += "\u0001";
		try {
			player.getSock().getOutputStream().write(data.getBytes());
		} catch (Exception __dummyCatchVar1) {
		}

	}

	// No crash me pls pls pls
	/**
	 * SendToAll, sends data to all players
	 * 
	 * Sends data to all Players whose state is > Player.States.LoadingGame,
	 * because if they are loading the game or not logged in they'll probably
	 * crash
	 * 
	 */
	public void sendToAll(String data) throws Exception {
		for (Goose.Player player : this.getPlayerHandler().getPlayers()) {
			if (player.getState().compareTo(Goose.Player.States.LoadingGame) > 0) {
				this.send(player, data);
			}

		}
	}

	/**
	 * SendToMap, sends data to all players in map
	 * 
	 * Sends data to all Players whose state is Player.States.Ready, because
	 * everyone on the map should be ready
	 * 
	 */
	public void sendToMap(Map map, String data) throws Exception {
		for (Goose.Player player : map.getPlayers()) {
			if (player.getState() == Goose.Player.States.Ready) {
				this.send(player, data);
			}

		}
	}

	// public static void defaultEndExecuteNonQueryAsyncCallback(IAsyncResult
	// ar)
	// throws Exception {
	// SqlCommand command = (SqlCommand) ar.AsyncState;
	// try {
	// command.EndExecuteNonQuery(ar);
	// } catch (Exception e) {
	// }
	//
	// }

}
