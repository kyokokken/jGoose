package Goose.Events;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import Goose.Event;
import Goose.Events.LoginEvent;
import Goose.GameSettings;
import Goose.GameWorld;
import Goose.Window;

/**
 * LoginEvent, event for LOGIN packet
 * 
 * Called as soon as a client connects Packet format:
 * LOGINUsername,password,ALPHA33,3.5.2
 * 
 * Server responds: LOKServername or LNOReason for failure Login OK or Login Not
 * OK
 * 
 */
public class LoginEvent extends Event{
	public LoginEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public enum LoginMessages {
		Success, WrongPassword, Banned
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new LoginEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer() != null)
			return;

		String packet = (String) (((Object[]) this.getData())[1]);
		Socket sock = (Socket) (((Object[]) this.getData())[0]);
		if (packet.indexOf(',') == -1)
			return;

		String name = packet.substring(5, (5) + (packet.indexOf(',') - 5));
		String[] t = packet.split(",");
		if (t.length < 2)
			return;

		String password = t[1];
		if (name.length() <= 1 || password.length() <= 1) {
			world.send(new Goose.Player(),
					"LNOPlease use a longer username or password.");
			world.getGameServer().disconnect(sock);
			return;
		}

		if (world.getPlayerHandler().isLoggedIn(name.toLowerCase())) {
			world.send(new Goose.Player(), "LNOCharacter is already logged in.");
			world.getGameServer().disconnect(sock);
			return;
		}

		Goose.Player player = world.getPlayerHandler().getPlayerFromData(name);
		if (player == null) {
			// limit characters created per day
			if (GameSettings.getDefault().getNewCharactersPerDayPerIP() > 0) {
				String IP = sock.getInetAddress().getHostAddress();
//				IP = IP.substring(0, (0) + (IP.indexOf(":")));
				boolean exists = world.getCharactersCreatedPerIP().containsKey(
						IP);
				int created = 0;
				if (exists) {
					created = world.getCharactersCreatedPerIP().get(IP);
					if (created >= GameSettings.getDefault()
							.getNewCharactersPerDayPerIP()) {
						world.send(new Goose.Player(),
								"LNOYou can't create any more characters today.");
						world.getGameServer().disconnect(sock);
						return;
					}
				}
				created++;
				world.getCharactersCreatedPerIP().put(IP, created);
			}

			if (GameSettings.getDefault().getAutoCharacterCreation()) {
				player = new Goose.Player(0);
				player.setSock(sock);
				player.loadFromAutoCreate(name, password, world);
				world.getPlayerHandler().addPlayerToData(player);
				this.setPlayer(player);
			} else {
				world.send(new Goose.Player(), "LNOCharacter does not exist.");
				world.getGameServer().disconnect(sock);
				return;
			}
		} else {
			this.setPlayer(player);
			this.getPlayer().setSock(sock);
			String salt = new String(this.getPlayer().getPasswordSalt()
					.getBytes(), StandardCharsets.US_ASCII);
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			byte[] data = (salt + password + GameSettings.getDefault()
					.getServerName()).getBytes();
			data = md5.digest(data);
			String hash = new String(data);
			hash = hash.replace("-", "").toLowerCase();
			// Incorrect password
			if (!this.getPlayer().getPasswordHash().equals(hash)) {
				world.send(this.getPlayer(), "LNOWrong password for character.");
				world.getGameServer().disconnect(this.getPlayer().getSock());
				return;
			} else if (player.getAccess() == Goose.Player.AccessStatus.Banned) {
				world.send(this.getPlayer(),
						"LNOYour character has been banned.");
				world.getGameServer().disconnect(this.getPlayer().getSock());
				return;
			}

		}
		if (GameSettings.getDefault().getLockdownModeEnabled()
				&& this.getPlayer().getAccess() != Goose.Player.AccessStatus.GameMaster) {
			world.send(this.getPlayer(),
					"LNOServer is currently under maintenance. Try again soon.");
			world.getGameServer().disconnect(this.getPlayer().getSock());
			return;
		}

		world.getPlayerHandler().addPlayer(this.getPlayer());
		if (this.getPlayer().getLoginID() == 0) {
			world.send(this.getPlayer(),
					"LNOThe server is full. Try again later.");
			world.getGameServer().disconnect(this.getPlayer().getSock());
			world.getPlayerHandler().removePlayer(this.getPlayer());
			return;
		}

		world.sendToAll("$7" + this.getPlayer().getName()
				+ " has joined the world.");
		this.getPlayer().setState(Goose.Player.States.LoadingGame);
		world.send(this.getPlayer(), "LOK"
				+ GameSettings.getDefault().getServerName());
		this.getPlayer().setWindows(new ArrayList<Window>());
		this.getPlayer().setLastPing(world.getTimeNow());
		this.getPlayer().addSaveEvent(world);
		this.getPlayer().setCurrentHP(
				(int) (this.getPlayer().getMaxStats().getHP() * 0.8));
		this.getPlayer().setCurrentMP(
				(int) (this.getPlayer().getMaxStats().getMP() * 0.8));
		this.getPlayer().setCurrentSP(this.getPlayer().getMaxStats().getSP());
		System.out.println(this.getPlayer().getName() + " logged in.");
	}

}
