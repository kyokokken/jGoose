package Goose.Events;

import Goose.Event;
import Goose.Events.ChangePasswordCommandEvent;
import Goose.GameSettings;
import Goose.GameWorld;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class ChangePasswordCommandEvent extends Event {
	public ChangePasswordCommandEvent() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Event create(Goose.Player player, Object data)
			throws Exception {
		Event e = new ChangePasswordCommandEvent();
		e.setPlayer(player);
		e.setData(data);
		return e;
	}

	public void ready(GameWorld world) throws Exception {
		if (this.getPlayer().getState() == Goose.Player.States.Ready) {
			String password = ((String) this.getData()).substring(16);
			if (password.length() < 3) {
				world.send(this.getPlayer(),
						"$7Your password needs to be more than 3 characters long.");
				return;
			}

			if (password.length() > 10) {
				world.send(this.getPlayer(),
						"$7Your password needs to be less than 10 characters long.");
				return;
			}

			byte[] saltBytes = new byte[16];
			SecureRandom rng = new java.security.SecureRandom();
			rng.nextBytes(saltBytes);
			String salt = new String(saltBytes, StandardCharsets.US_ASCII);
			String base64Salt = Base64.encodeBase64String(saltBytes);
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			byte[] data = (salt + password + GameSettings.getDefault()
					.getServerName()).getBytes();
			data = md5.digest(data);
			String passwordHash = new String(data);
			passwordHash = passwordHash.replace("-", "").toLowerCase();
			this.getPlayer().setPasswordHash(passwordHash);
			this.getPlayer().setPasswordSalt(base64Salt);
			world.send(this.getPlayer(), "$7Your password has been changed.");
		}

	}

}
