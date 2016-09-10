package Goose;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Map, holds map information/methods
 */
public class Map {
    // Viewing ranges
    public static int RANGE_X = 16;
    public static int RANGE_Y = 14;
    /**
     * map_id
     */
    private int __ID;

    public int getID() {
        return __ID;
    }

    public void setID(int value) {
        __ID = value;
    }

    /**
     * map_name
     */
    private String __Name;

    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    /**
     * map_x
     */
    private int __Width;

    public int getWidth() {
        return __Width;
    }

    public void setWidth(int value) {
        __Width = value;
    }

    /**
     * map_y
     */
    private int __Height;

    public int getHeight() {
        return __Height;
    }

    public void setHeight(int value) {
        __Height = value;
    }

    private int __MinLevel;

    public int getMinLevel() {
        return __MinLevel;
    }

    public void setMinLevel(int value) {
        __MinLevel = value;
    }

    private int __MaxLevel;

    public int getMaxLevel() {
        return __MaxLevel;
    }

    public void setMaxLevel(int value) {
        __MaxLevel = value;
    }

    private long __MinExperience;

    public long getMinExperience() {
        return __MinExperience;
    }

    public void setMinExperience(long value) {
        __MinExperience = value;
    }

    private long __MaxExperience;

    public long getMaxExperience() {
        return __MaxExperience;
    }

    public void setMaxExperience(long value) {
        __MaxExperience = value;
    }

    private boolean __CanPVP;

    public boolean getCanPVP() {
        return __CanPVP;
    }

    public void setCanPVP(boolean value) {
        __CanPVP = value;
    }

    private boolean __CanChat;

    public boolean getCanChat() {
        return __CanChat;
    }

    public void setCanChat(boolean value) {
        __CanChat = value;
    }

    private boolean __CanAuction;

    public boolean getCanAuction() {
        return __CanAuction;
    }

    public void setCanAuction(boolean value) {
        __CanAuction = value;
    }

    private boolean __CanShout;

    public boolean getCanShout() {
        return __CanShout;
    }

    public void setCanShout(boolean value) {
        __CanShout = value;
    }

    private boolean __CanCast;

    public boolean getCanCast() {
        return __CanCast;
    }

    public void setCanCast(boolean value) {
        __CanCast = value;
    }

    private boolean __CanBind;

    public boolean getCanBind() {
        return __CanBind;
    }

    public void setCanBind(boolean value) {
        __CanBind = value;
    }

    private boolean __CanUseItems;

    public boolean getCanUseItems() {
        return __CanUseItems;
    }

    public void setCanUseItems(boolean value) {
        __CanUseItems = value;
    }

    private boolean __CanSpawnPets;

    public boolean getCanSpawnPets() {
        return __CanSpawnPets;
    }

    public void setCanSpawnPets(boolean value) {
        __CanSpawnPets = value;
    }

    ICharacter[] characters;
    ITile[] tiles;
    List<Player> players;
    List<Integer> requiredItems;
    List<Goose.NPC> npcs;
    List<ItemTile> items;

    /**
     * Players, returns player list
     */
    public List<Player> getPlayers() throws Exception {
        return this.players;
    }

    /**
     * Constructor
     */
    public Map(int width, int height) throws Exception {
        this.setWidth(width);
        this.setHeight(height);
        this.characters = new ICharacter[(this.getWidth() + 1) * (this.getHeight() + 1)];
        this.tiles = new ITile[(this.getWidth() + 1) * (this.getHeight() + 1)];
        this.players = new ArrayList<Player>();
        this.requiredItems = new ArrayList<Integer>();
        this.npcs = new ArrayList<Goose.NPC>();
        this.items = new ArrayList<ItemTile>();
    }

    /**
     * GetPlayersInRange, returns all players that the character can see
     */
    public List<Player> getPlayersInRange(ICharacter character) throws Exception {
        List<Player> result = new ArrayList<>();

        for (Goose.Player p : this.players) {
            if ((Math.abs(p.getMapX() - character.getMapX()) < RANGE_X) && (Math.abs(p.getMapY() - character.getMapY()) < RANGE_Y) && p != character) {
                result.add(p);
            }

        }
        return result;
    }

    /**
     * GetNPCsInRange, returns all npcs that the character can see
     */
    public List<Goose.NPC> getNPCsInRange(ICharacter character) throws Exception {
        List<Goose.NPC> result = new ArrayList<>();

        for (Goose.NPC npc : this.npcs) {
            if ((Math.abs(npc.getMapX() - character.getMapX()) < RANGE_X)
                    && (Math.abs(npc.getMapY() - character.getMapY()) < RANGE_Y) && npc != character
                    && (npc.getState() == NPC.States.Alive)) {
                result.add(npc);
            }
        }

        return result;
    }

    /**
     * AddPlayer, adds player to players list
     */
    public void addPlayer(Goose.Player player) throws Exception {
        this.players.add(player);
    }

    /**
     * RemovePlayer, removes player from players list
     */
    public void removePlayer(Goose.Player player) throws Exception {
        this.players.remove(player);
    }

    /**
     * AddNPC, adds npc to npcs list
     */
    public void addNPC(Goose.NPC npc) throws Exception {
        this.npcs.add(npc);
    }

    /**
     * RemoveNPC, removes npc from npcs list
     */
    public void removeNPC(Goose.NPC npc) throws Exception {
        this.npcs.remove(npc);
    }

    /**
     * AddItem, adds item to items list adds item to tiles array
     * <p>
     * Updates everyone in the map about the item
     */
    public void addItem(ItemTile item, GameWorld world) throws Exception {
        this.items.add(item);
        this.tiles[item.getX() * this.getWidth() + item.getY()] = item;
        item.setDroppedTime(world.getTimeNow());
        world.sendToMap(this, item.mOBString());
    }

    /**
     * RemoveItem, removes item from items list removes item from tiles array
     * <p>
     * Updates everyone in the map about the item
     */
    public void removeItem(ItemTile item, GameWorld world) throws Exception {
        this.items.remove(item);
        this.tiles[item.getX() * this.getWidth() + item.getY()] = null;
        world.sendToMap(this, item.eOBString());
    }

    /**
     * CanMoveTo, checks if character can move to x, y
     */
    public boolean canMoveTo(ICharacter character, int x, int y) throws Exception {
        // invalid coordinates
        if (x < 1 || x >= this.getWidth() + 1 || y < 1 || y >= this.getHeight() + 1) return false;

        if ((Math.abs(character.getMapX() - x) == 1 && Math.abs(character.getMapY() - y) == 0)
                || (Math.abs(character.getMapX() - x) == 0 && Math.abs(character.getMapY() - y) == 1)) {
            ITile tile = this.tiles[x * this.getWidth() + y];
            if (tile != null) {
                if (tile instanceof WarpTile) {
                    if (character instanceof Pet)
                        return false;
                    else if (character instanceof Goose.Player)
                        return true;
                    else
                        return false;
                }

            }

            return !this.isTileBlocked(character, x, y);
        }

        return false;
    }

    /**
     * PlacePlayer, places a character on the map
     * <p>
     * This method checks if the players current coordinates are valid and not blocked if they're
     * blocked it moves the player until they can be placed
     */
    public void placeCharacter(ICharacter character) throws Exception {
        // radius at which we're searching
        int r = 0;
        // set origin
        int ox = character.getMapX();
        int oy = character.getMapY();
        while (true) {
            for (int y = oy - r; y < oy + r + 1; y++) {
                // this loop is for increasing radius until we find a good tile
                // searches the radius around origin
                // within map bounds
                if (y > 0 && y <= this.getHeight()) {
                    for (int x = ox - r; x < ox + r + 1; x++) {
                        // searches the radius around origin
                        // within map bounds
                        if (x > 0 && x <= this.getWidth()) {
                            // if x or y is at the radius we're searching
                            // so we don't search already searched tiles
                            if ((y == oy - r || y == oy + r) || (x == ox - r || x == ox + r)) {
                                if (!this.isTileBlocked(character, x, y)) {
                                    character.setMapX(x);
                                    character.setMapY(y);
                                    return;
                                }

                            }

                        }

                    }
                }

            }
            r++;
        }
    }

    /**
     * PlaceItem, places an item on the map
     * <p>
     * This method checks if the items current coordinates are valid and not blocked if they're
     * blocked it moves the item until it can be placed
     * <p>
     * returns true if could place item
     */
    public boolean placeItem(ItemTile item) throws Exception {
        // radius at which we're searching
        int r = 0;
        // set origin
        int ox = item.getX();
        int oy = item.getY();
        while (true) {
            for (int y = oy - r; y < oy + r + 1; y++) {
                // this loop is for increasing radius until we find a good tile
                // searches the radius around origin
                // within map bounds
                if (y > 0 && y <= this.getHeight()) {
                    for (int x = ox - r; x < ox + r + 1; x++) {
                        // searches the radius around origin
                        // within map bounds
                        if (x > 0 && x <= this.getWidth()) {
                            // if x or y is at the radius we're searching
                            // so we don't search already searched tiles
                            if ((y == oy - r || y == oy + r) || (x == ox - r || x == ox + r)) {
                                ITile tile = this.getTile(x, y);
                                if (tile == null) {
                                    item.setX(x);
                                    item.setY(y);
                                    return true;
                                } else if (tile instanceof ItemTile) {
                                    if (((ItemTile) tile).getItemSlot().canStack(item.getItemSlot())) {
                                        item.setX(x);
                                        item.setY(y);
                                        return true;
                                    }

                                }

                            }

                        }

                    }
                }

            }
            r++;
        }
    }

    /**
     * IsTileBlocked, checks if the tile at x,y is blocked
     * <p>
     * Blocked either by another character or warp/unwalkable tiles
     */
    public boolean isTileBlocked(ICharacter ignore, int x, int y) throws Exception {
        // invalid coordinates
        if (x < 1 || x >= this.getWidth() + 1 || y < 1 || y >= this.getHeight() + 1) return true;

        ITile tile = this.tiles[x * this.getWidth() + y];
        if (tile != null) {
            if (tile instanceof WarpTile) {
                return true;
            }
            if (tile instanceof BlockedTile) {
                if (ignore instanceof Goose.Player
                        && ((Goose.Player) ignore).getAccess() == Goose.Player.AccessStatus.GameMaster) {
                    return false;
                } else {
                    return true;
                }
            }

        }

        ICharacter character = this.getCharacterAt(x, y);
        if (character == null || character == ignore) return false;

        return true;
    }

    /**
     * LoadData, loads warp/blocked tiles, required items
     */
    public void loadData(GameWorld world) throws Exception {
        ResultSet resultSet =
                world.getSqlConnection().createStatement()
                        .executeQuery("SELECT * FROM warptiles " + "WHERE map_id=" + this.getID());
        while (resultSet.next()) {
            WarpTile warp = new WarpTile();
            warp.setWarpMap(world.getMapHandler().getMap(resultSet.getInt("warp_id")));
            warp.setWarpX(resultSet.getInt("warp_x"));
            warp.setWarpY(resultSet.getInt("warp_y"));
            int x = resultSet.getInt("map_x");
            int y = resultSet.getInt("map_y");
            this.tiles[x * this.getWidth() + y] = warp;
        }
        resultSet.close();
        resultSet =
                world.getSqlConnection().createStatement()
                        .executeQuery("SELECT * FROM blockedtiles " + "WHERE map_id=" + this.getID());

        while (resultSet.next()) {
            BlockedTile blocked = new BlockedTile();
            int x = resultSet.getInt("map_x");
            int y = resultSet.getInt("map_y");
            this.tiles[x * this.getWidth() + y] = blocked;
        }
        resultSet.close();
        resultSet =
                world.getSqlConnection().createStatement()
                        .executeQuery("SELECT * FROM map_required_items " + "WHERE map_id=" + this.getID());

        while (resultSet.next()) {
            this.requiredItems.add(resultSet.getInt("item_template_id"));
        }
        resultSet.close();
    }

    /**
     * GetTile, returns the tile at x, y
     */
    public ITile getTile(int x, int y) throws Exception {
        // invalid coordinates
        if (x < 1 || x >= this.getWidth() + 1 || y < 1 || y >= this.getHeight() + 1) return null;

        return this.tiles[x * this.getWidth() + y];
    }

    /**
     * PlayerCanJoin, checks if player meets requirements to join map
     */
    public boolean playerCanJoin(Goose.Player player, GameWorld world) throws Exception {
        if (player.getAccess() == Goose.Player.AccessStatus.GameMaster) return true;

        if (this.getMinLevel() != 0 && player.getLevel() < this.getMinLevel()) {
            return false;
        }

        if (this.getMaxLevel() != 0 && player.getLevel() > this.getMaxLevel()) {
            return false;
        }

        if ((this.getMinExperience() != 0) && (player.getExperience() + player.getExperienceSold() < this.getMinExperience())) {
            return false;
        }

        if ((this.getMaxExperience() != 0) && (player.getExperience() + player.getExperienceSold() > this.getMaxExperience())) {
            return false;
        }

        for (int id : this.requiredItems) {
            if (!player.hasItem(id)) return false;

        }
        return true;
    }

    /**
     * Items, returns items list
     */
    public List<ItemTile> getItems() throws Exception {
        return this.items;
    }

    /**
     * GetCharacterAt, gets character at x,y
     */
    public ICharacter getCharacterAt(int x, int y) throws Exception {
        if (x < 1 || x >= this.getWidth() + 1 || y < 1 || y >= this.getHeight() + 1) return null;

        return this.characters[x * this.getWidth() + y];
    }

    /**
     * Set Character at x,y to character
     */
    public void setCharacter(ICharacter character, int x, int y) throws Exception {
        if (x < 1 || x >= this.getWidth() + 1 || y < 1 || y >= this.getHeight() + 1) return;

        this.characters[x * this.getWidth() + y] = character;
    }

    /**
     * NPCs, returns npcs list
     */
    public List<Goose.NPC> getNPCs() throws Exception {
        return this.npcs;
    }

}
