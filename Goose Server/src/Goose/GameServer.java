package Goose;

import Goose.GameSettings;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.Selector;
import java.util.ArrayList;

import org.apache.commons.codec.Charsets;

/**
 * The GameServer class handles all of the basic Socket handling to do with a
 * server It contains the GameWorld class where all of the game specific stuff
 * happens
 * 
 */
public class GameServer {
	Socket listen;
	ArrayList sockets;
	GameWorld gameworld;

	/**
	 * Constructor, constructs the GameWorld
	 * 
	 * Then calls Start to set up everything Then calls GameLoop, the main
	 * program loop
	 * 
	 */
	public GameServer() throws Exception {
		this.sockets = new ArrayList();
		this.gameworld = new GameWorld(this);
		this.start();
		this.gameLoop();
	}

	/**
	 * Start, server setup
	 * 
	 * Calls along to the GameWorld.Start() Sets up a listen socket and adds it
	 * to the socket list
	 * 
	 */
	public void start() throws Exception {
		this.gameworld.start();
		// this.listen = new NetSocket(AddressFamily.InterNetwork,
		// SocketType.Stream, ProtocolType.Tcp);
		String gameServerIP = GameSettings.getDefault().getGameServerIP();
		short gameServerPort = GameSettings.getDefault().getGameServerPort();

		// this.listen = new Socket(InetAddress.getByName(gameServerIP),
		// gameServerPort);
		this.listen = new ServerSocket(gameServerPort, 10,
				InetAddress.getByName(gameServerIP)).accept();

		this.sockets.add(this.listen);
	}

	/**
	 * GameLoop, the main game loop
	 * 
	 * Handles all of the low level socket details Eg, on a new connection it
	 * calls GameWorld.NewConnection(Socket) on a closed connection calls
	 * GameWorld.LostConnection(Socket) on receiving data calls
	 * GameWorld.Received(Socket, String)
	 * 
	 * At the end of the loop it calls GameWorld.Update(), Update returns a bool
	 * to specify to keep the server going or not
	 * 
	 * Once the loop is stopped this.Stop() is called to tidy up
	 * 
	 */
	public void gameLoop() throws Exception {
		ArrayList readList;
		ArrayList writeList;
		while (this.gameworld.getRunning()) {
			Thread.sleep(1);
			readList = (ArrayList) this.sockets.clone();
			writeList = (ArrayList) this.sockets.clone();
			// TODO: Have to use Selector or other libs.
			// NetSocket.Select(readList, writeList, null, 2000);

			for (Object readableSocket : readList) {
				Socket socket = (Socket) readableSocket;
				if (socket == this.listen && !socket.isConnected() && socket.isClosed()) {
//					Socket newsock = new Socket(this.listen.getInetAddress(),
//							this.listen.getPort());
					this.sockets.add(socket);
					this.gameworld.newConnection(socket);
					String gameServerIP = GameSettings.getDefault().getGameServerIP();
					short gameServerPort = GameSettings.getDefault().getGameServerPort();
					socket.connect(listen.getLocalSocketAddress());
				} else {
					byte[] buffer = new byte[512];
					int res = 0;
					try {
						socket.setReceiveBufferSize(512);
						res = socket.getInputStream().read(buffer);
					} catch (SocketException e) {
					}

					// GTFO, I want C-style
					if (res <= 0) {
						this.gameworld.lostConnection(socket);
					} else {
						String strBuffer = new String(buffer, 0, res,
								Charsets.US_ASCII);
						this.gameworld.received(socket, strBuffer);
					}
				}
			}
			this.gameworld.update();
		}
		this.stop();
	}

	/**
	 * Stop, server shutdown tidyup
	 * 
	 * Calls along to GameWorld.Stop()
	 * 
	 * Closes all sockets
	 * 
	 */
	public void stop() throws Exception {
		this.gameworld.stop();
		for (Object __dummyForeachVar1 : this.sockets) {
			Socket sock = (Socket) __dummyForeachVar1;
			sock.close();
		}
	}

	/**
	 * Disconnect, disconnect socket
	 * 
	 * Closes socket then removes from our sockets list
	 * 
	 */
	public void disconnect(Socket sock) throws Exception {
		sock.close();
		this.sockets.remove(sock);
	}

}
