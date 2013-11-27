package Goose;

import Goose.GameSettings;

import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
     * PlayerHandler, handles Player objects
     * 
     * Has a list of Players and a mapping of Sockets to Players
     * 
     */
public class PlayerHandler   
{
    /**
    * A mapping of all player names to their corresponding Player object. Loaded on startup
    */
    Hashtable allNameToPlayer;
    List<Goose.Player> players;
    Hashtable sockToPlayer;
    Goose.Player[] idToPlayer;
    int currentdbid = 1;
    /**
    * Gets/sets the next available player database id
    */
    public int getCurrentID() throws Exception {
        return this.currentdbid;
    }

    public void setCurrentID(int value) throws Exception {
        this.currentdbid = value;
    }

    /**
             * Constructor
             * 
             * Constructs list/mapping
             * 
             */
    public PlayerHandler() throws Exception {
        this.players = new ArrayList<Goose.Player>();
        this.sockToPlayer = new Hashtable();
        this.idToPlayer = new Goose.Player[GameSettings.getDefault().getMaxPlayers()];
        this.allNameToPlayer = new Hashtable();
    }

    /**
             * AddPlayer, adds a player to the handler
             * 
             * Takes a Player, adds the player to Players list
             * Also adds a key to the hashtable to map Socket to Player
             * 
             * Automatically gives a player an ID
             * 
             */
    public void addPlayer(Goose.Player player) throws Exception {
        player.setLoginID(this.getNewID());
        this.players.add(player);
        this.sockToPlayer.put(player.getSock(), player);
        this.idToPlayer[player.getLoginID()] = player;
    }

    /**
             * RemovePlayer, removes a player from the PlayerHandler by Socket
             * 
             * First finds the associated Player from the Socket
             * Then calls RemovePlayer(Player)
             * 
             */
    public void removePlayer(Socket sock) throws Exception {
        Goose.Player player = (Goose.Player)this.sockToPlayer.get(sock);
        if (player != null)
        {
            this.removePlayer(player);
        }
         
    }

    /**
             * RemovePlayer, removes a player from the PlayerHandler by Player
             * 
             * Removes the Player from our hashtable and list.
             * 
             */
    public void removePlayer(Goose.Player player) throws Exception {
        this.sockToPlayer.remove(player.getSock());
        this.players.remove(player);
        this.idToPlayer[player.getLoginID()] = null;
    }

    /**
             * GetNewID, returns a new player id
             * 
             * If we ran out of player ids returns 0
             * 
             */
    public int getNewID() throws Exception {
        for (int i = 1;i < GameSettings.getDefault().getMaxPlayers();i++)
        {
            if (this.idToPlayer[i] == null)
            {
                return i;
            }
             
        }
        return 0;
    }

    /**
             * GetPlayer, takes a socket and returns the associated Player
             * 
             * Just uses our dictionary mapping to get the Player object
             * 
             */
    public Goose.Player getPlayer(Socket sock) throws Exception {
        return (Goose.Player)this.sockToPlayer.get(sock);
    }

    /**
             * GetPlayer, takes a name and returns the associated Player
             * 
             */
    public Goose.Player getPlayer(String name) throws Exception {
        name = name.toLowerCase();
        for (Goose.Player player : this.players)
        {
            if (name.equals(player.getName().toLowerCase()))
                return player;
             
        }
        return null;
    }

    /**
             * IsLoggedIn, checks if a player is logged in
             * 
             * 
             */
    public boolean isLoggedIn(String name) throws Exception {
        for (Goose.Player player : this.players)
        {
            if (name.equals(player.getName().toLowerCase()))
                return true;
             
        }
        return false;
    }

    /**
             * PlayerCount, readonly, returns the player count
             * 
             */
    public int getPlayerCount() throws Exception {
        return this.players.size();
    }

    /**
             * Players, readonly, returns the current players
             * 
             */
    public List<Goose.Player> getPlayers() throws Exception {
        return this.players;
    }

    /**
             * PlayerDataCount, readonly, returns the player database count
             * 
             */
    public int getPlayerDataCount() throws Exception {
        return this.allNameToPlayer.size();
    }

    /**
             * GetPlayerFromData, takes a name and returns the associated Player from the in-memory database
             * 
             */
    public Goose.Player getPlayerFromData(String name) throws Exception {
        name = name.toLowerCase();
        return (Goose.Player)this.allNameToPlayer.get(name);
    }

    public void loadPlayerData(GameWorld world) throws Exception {
    	String query = "SELECT * FROM players";
    	ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
        while (resultSet.next())
        {
            Goose.Player player = new Goose.Player(0);
            player.loadFromReader(world, resultSet);
            if (player.getPlayerID() >= this.getCurrentID())
            {
                this.setCurrentID(player.getPlayerID() + 1);
            }
             
            if (player.getAccess() == Goose.Player.AccessStatus.Deleted)
                continue;
             
            this.allNameToPlayer.put(player.getName().toLowerCase(), player);
        }
        for (Object __dummyForeachVar2 : this.allNameToPlayer.values())
        {
            Goose.Player player = (Goose.Player)__dummyForeachVar2;
            player.loadAdditional(world);
        }
    }

    public void addPlayerToData(Goose.Player player) throws Exception {
        this.allNameToPlayer.put(player.getName().toLowerCase(), player);
    }

    public void removePlayerFromData(Goose.Player player) throws Exception {
        this.allNameToPlayer.remove(player.getName().toLowerCase());
    }

    public List<Goose.Player> getAllPlayerData() throws Exception {
    	List<Player> ret = new ArrayList<>();
    	Collection values = this.allNameToPlayer.values();
    	for (Object player : values)
    	{
    		ret.add((Player)player);
    	}
//        return (List<Player>) this.allNameToPlayer.values();
    	return ret;
    }

}

