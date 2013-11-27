package Goose;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Guild, holds information regarding a guilds members, guild name and message
 * of the day
 * 
 */
public class Guild {
	public enum GuildRanks {
		/**
		 * GuildRanks, the different guild ranks
		 * 
		 */
		Deleted, Member, __dummyEnum__0, __dummyEnum__1, __dummyEnum__2, Officer, __dummyEnum__3, __dummyEnum__4, __dummyEnum__5, Leader
	}

	/**
	 * PlayerGuildStatus, holds information about a guild member
	 * 
	 */
	static class PlayerGuildStatus {
		private int __PlayerID;

		public int getPlayerID() {
			return __PlayerID;
		}

		public void setPlayerID(int value) {
			__PlayerID = value;
		}

		private GuildRanks __Rank = GuildRanks.Deleted;

		public GuildRanks getRank() {
			return __Rank;
		}

		public void setRank(GuildRanks value) {
			__Rank = value;
		}

		private boolean __Dirty;

		public boolean getDirty() {
			return __Dirty;
		}

		public void setDirty(boolean value) {
			__Dirty = value;
		}

		private boolean __JustAdded;

		public boolean getJustAdded() {
			return __JustAdded;
		}

		public void setJustAdded(boolean value) {
			__JustAdded = value;
		}

	}

	private Hashtable __Members;

	public Hashtable getMembers() {
		return __Members;
	}

	public void setMembers(Hashtable value) {
		__Members = value;
	}

	private int __ID;

	public int getID() {
		return __ID;
	}

	public void setID(int value) {
		__ID = value;
	}

	private String __MOTD;

	public String getMOTD() {
		return __MOTD;
	}

	public void setMOTD(String value) {
		__MOTD = value;
	}

	private String __Name;

	public String getName() {
		return __Name;
	}

	public void setName(String value) {
		__Name = value;
	}

	// Does data need updating
	private boolean __Dirty;

	public boolean getDirty() {
		return __Dirty;
	}

	public void setDirty(boolean value) {
		__Dirty = value;
	}

	private List<Goose.Player> __OnlineMembers;

	public List<Goose.Player> getOnlineMembers() {
		return __OnlineMembers;
	}

	public void setOnlineMembers(List<Goose.Player> value) {
		__OnlineMembers = value;
	}

	/**
	 * Constructor
	 */
	public Guild() throws Exception {
		this.setMembers(new Hashtable());
		this.setOnlineMembers(new ArrayList<Goose.Player>());
		this.setDirty(false);
	}

	/**
	 * AddMember, calls along to next addmember function supplying dirty as
	 * false.
	 * 
	 * Used when loading guilds
	 * 
	 */
	public void addMember(int playerid, GuildRanks rank) throws Exception {
		this.addMember(playerid, rank, false, false);
	}

	/**
	 * AddMember, adds a member to the guild
	 * 
	 */
	public void addMember(int playerid, GuildRanks rank, boolean dirty,
			boolean justadded) throws Exception {
		PlayerGuildStatus status = new PlayerGuildStatus();
		status.setDirty(dirty);
		status.setPlayerID(playerid);
		status.setRank(rank);
		status.setJustAdded(justadded);
		this.getMembers().put(playerid, status);
		this.setDirty(dirty);
	}

	/**
	 * SendToGuild, sends message to everyone online in the guild
	 * 
	 */
	public void sendToGuild(String message, GameWorld world) throws Exception {
		for (Goose.Player player : this.getOnlineMembers()) {
			world.send(player, message);
		}
	}

	/**
	 * GetRank, returns the rank of the specified player
	 * 
	 */
	public GuildRanks getRank(Goose.Player player) throws Exception {
		if (!this.getMembers().containsKey(player.getPlayerID()))
			return GuildRanks.Deleted;

		return ((PlayerGuildStatus) this.getMembers().get(player.getPlayerID()))
				.getRank();
	}

	/**
	 * JoinGuild, adds the player to the guild
	 * 
	 */
	public void joinGuild(Goose.Player player, GameWorld world)
			throws Exception {
		this.sendToGuild("$2[guild-notice] " + player.getName()
				+ " has joined the guild.", world);
		this.addMember(player.getPlayerID(), GuildRanks.Member, true, true);
		this.getOnlineMembers().add(player);
		player.setGuild(this);
		if (this.getID() != 0)
			player.setGuildID(this.getID());

		world.send(player, "$2[guild-notice] You have joined " + this.getName()
				+ ".");
		world.send(player, player.sNFString());
	}

	/**
	 * LeaveGuild, removes player from guild
	 * 
	 * Calls to next LeaveGuild method specifying kicked as false
	 * 
	 */
	public void leaveGuild(Goose.Player player, GameWorld world)
			throws Exception {
		this.leaveGuild(player, world, false);
	}

	/**
	 * LeaveGuild, removes player from guild
	 * 
	 */
	public void leaveGuild(Goose.Player player, GameWorld world, boolean kicked)
			throws Exception {
		this.removeMember(player.getPlayerID());
		this.getOnlineMembers().remove(player);
		player.setGuildID(0);
		player.setGuild(null);
		if (kicked) {
			this.sendToGuild("$2[guild-notice] " + player.getName()
					+ " was kicked from the guild.", world);
			world.send(player,
					"$2[guild-notice] You were kicked from the guild.");
		} else {
			this.sendToGuild("$2[guild-notice] " + player.getName()
					+ " left the guild.", world);
			world.send(player, "$2[guild-notice] You left the guild.");
		}
		world.send(player, player.sNFString());
	}

	/**
	 * RemoveMember, removes player from the guild
	 * 
	 */
	public void removeMember(int playerid) throws Exception {
		if (!this.getMembers().containsKey(playerid))
			return;

		((PlayerGuildStatus) this.getMembers().get(playerid)).setDirty(true);
		((PlayerGuildStatus) this.getMembers().get(playerid))
				.setRank(GuildRanks.Deleted);
		this.setDirty(true);
	}

	/**
	 * Save, saves to database
	 * 
	 * Adds itself to database if it isn't already in there
	 * 
	 */
	public void save(GameWorld world) throws Exception {
		boolean justsaved = false;
		String query;
		if (this.getID() == 0) {
			query = "INSERT INTO guilds (guild_name, guild_motd) VALUES (?, ?)";
			PreparedStatement preparedStatement = world.getSqlConnection()
					.prepareStatement(query);

			preparedStatement.setString(1, this.getName());
			preparedStatement.setString(2, this.getMOTD());
			preparedStatement.executeUpdate();

			ResultSet resultSet = world.getSqlConnection().createStatement()
					.executeQuery("SELECT @@IDENTITY");
			resultSet.next();
			// TODO: Have to check.
			this.setID(resultSet.getInt(1));
			justsaved = true;
			for (Goose.Player player : this.getOnlineMembers()) {
				player.setGuildID(this.getID());
			}
		}

		if (!justsaved) {
			query = "UPDATE guilds SET guild_name=?, guild_motd=? WHERE guild_id="
					+ this.getID();
			PreparedStatement preparedStatement = world.getSqlConnection()
					.prepareStatement(query);

			preparedStatement.setString(1, this.getName());
			preparedStatement.setString(2, this.getMOTD());
			preparedStatement.executeUpdate();
		}

		List<Integer> removed = new ArrayList<Integer>();
		PlayerGuildStatus status;
		for (Object __dummyForeachVar2 : this.getMembers().values()) {
			Object obj = (Object) __dummyForeachVar2;
			status = (PlayerGuildStatus) obj;
			if (status.getDirty()) {
				if (status.getRank() == GuildRanks.Deleted) {
					query = "DELETE FROM guild_members WHERE guild_id="
							+ this.getID() + " AND player_id="
							+ status.getPlayerID();
					world.getSqlConnection().createStatement()
							.executeQuery(query);
					removed.add(status.getPlayerID());
				} else {
					if (status.getJustAdded()) {
						query = "INSERT INTO guild_members (guild_id, player_id, guild_rank) VALUES ("
								+ this.getID()
								+ ", "
								+ status.getPlayerID()
								+ ", "
								+ ((Enum) status.getRank()).ordinal()
								+ ")";
						world.getSqlConnection().createStatement().executeQuery(query);
						status.setJustAdded(false);
					} else {
						query = "UPDATE guild_members SET guild_rank="
								+ ((Enum) status.getRank()).ordinal()
								+ " WHERE guild_id=" + this.getID()
								+ " AND player_id=" + status.getPlayerID();
						world.getSqlConnection().createStatement().executeQuery(query);
					}
				}
				status.setDirty(false);
			}

		}
		for (int i : removed) {
			this.getMembers().remove(i);
		}
		this.setDirty(false);
	}

	/**
	 * ChangeOwner, swaps ownership of guild
	 * 
	 */
	public void changeOwner(Goose.Player leader, Goose.Player newleader,
			GameWorld world) throws Exception {
		((PlayerGuildStatus) this.getMembers().get(leader.getPlayerID()))
				.setRank(GuildRanks.Member);
		((PlayerGuildStatus) this.getMembers().get(leader.getPlayerID()))
				.setDirty(true);
		((PlayerGuildStatus) this.getMembers().get(newleader.getPlayerID()))
				.setRank(GuildRanks.Leader);
		((PlayerGuildStatus) this.getMembers().get(newleader.getPlayerID()))
				.setDirty(true);
		this.setDirty(true);
		this.sendToGuild("$2[guild-notice] " + newleader.getName()
				+ " is now the new guild leader.", world);
	}

	/**
	 * ChangeRank, changes rank of player
	 * 
	 */
	public void changeRank(Goose.Player player, GuildRanks rank, GameWorld world)
			throws Exception {
		((PlayerGuildStatus) this.getMembers().get(player.getPlayerID()))
				.setRank(rank);
		((PlayerGuildStatus) this.getMembers().get(player.getPlayerID()))
				.setDirty(true);
		switch (rank) {
			case Officer:
				this.sendToGuild("$2[guild-notice] " + player.getName()
						+ " is now an officer.", world);
				break;
			case Member:
				this.sendToGuild("$2[guild-notice] " + player.getName()
						+ " is now a member.", world);
				break;

		}
		this.setDirty(true);
	}

}
