package Goose;

import org.apache.commons.codec.Charsets;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The GameServer class handles all of the basic Socket handling to do with a server It contains the
 * GameWorld class where all of the game specific stuff happens
 */
public class GameServer extends Thread{
    ServerSocket listen;
    ArrayList<Socket> sockets;
    GameWorld gameworld;

    /**
     * Constructor, constructs the GameWorld
     * <p>
     * Then calls Start to set up everything Then calls GameLoop, the main program loop
     */
    public GameServer() throws Exception {
        this.sockets = new ArrayList<>();
        this.gameworld = new GameWorld(this);
    }

    public void run(){
        try {
            this.init();
            this.gameLoop();
        } catch (Exception e) {
            Logger.INSTANCE.println(e);
        }
    }

    /**
     * Start, server setup
     * <p>
     * Calls along to the GameWorld.Start() Sets up a listen socket and adds it to the socket list
     */
    public void init() throws Exception {
        this.gameworld.start();
        String gameServerIP = GameSettings.getDefault().getGameServerIP();
        short gameServerPort = GameSettings.getDefault().getGameServerPort();

        this.listen = new ServerSocket(gameServerPort, 10, InetAddress.getByName(gameServerIP));
    }

    /**
     * GameLoop, the main game loop
     * <p>
     * Handles all of the low level socket details Eg, on a new connection it calls
     * GameWorld.NewConnection(Socket) on a closed connection calls GameWorld.LostConnection(Socket)
     * on receiving data calls GameWorld.Received(Socket, String)
     * <p>
     * At the end of the loop it calls GameWorld.Update(), Update returns a bool to specify to keep
     * the server going or not
     * <p>
     * Once the loop is stopped this.Stop() is called to tidy up
     */
    public void gameLoop() throws Exception {
        Thread serverThread = new Thread(new ServerSocketHandler(this.listen, this));
        serverThread.start();
        long last = System.nanoTime();
        while (this.gameworld.getRunning()) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < sockets.size(); i++) {
                Socket socket = sockets.get(i);

                byte[] buffer = new byte[512];
                int res = 0;
                try {
                    // socket.setSoTimeout(2);
                    if (socket.getInputStream().available() != 0) {
                        socket.setReceiveBufferSize(512);
                        res = socket.getInputStream().read(buffer);

                        if (res <= 0) {
                            this.gameworld.lostConnection(socket);
                        } else {
                            String strBuffer = new String(buffer, 0, res, Charsets.US_ASCII);
                            this.gameworld.received(socket, strBuffer);
                        }
                    } else {
//                        Not sure why it is doing this? This is sending bas requests to the client and I believe is messing things up in the long run
                        if (System.nanoTime() - last > GameSettings.getDefault().getLogoutLagTime() * 1_000_000_000) {
                            String ping = "PING" + "\u0001";
                            socket.getOutputStream().write(ping.getBytes());
                            last = System.nanoTime();
                        }

                    }
                } catch (java.net.SocketException e) { // this is called when client host closes aspereta.
                    this.gameworld.lostConnection(socket);
                }
            }
            this.gameworld.update();
            if(System.currentTimeMillis() - start > 50)
                Logger.INSTANCE.println("Loop took: " + (System.currentTimeMillis() - start));
        }
        this.stopServer();
    }

    private class ServerSocketHandler implements Runnable {
        ServerSocket listen;
        GameServer gameServer;

        public ServerSocketHandler(ServerSocket listen, GameServer gameServer) {
            this.listen = listen;
            this.gameServer = gameServer;
        }

        @Override
        public void run() {
            while (gameServer.gameworld.getRunning()) {
                try {
                    Socket clientSocket = listen.accept();
                    gameServer.gameworld.newConnection(clientSocket);
                    gameServer.sockets.add(clientSocket);
                } catch (Exception e) {
                    Logger.INSTANCE.println(e);
                }
            }
        }

    }

    /**
     * Stop, server shutdown tidyup
     * <p>
     * Calls along to GameWorld.Stop()
     * <p>
     * Closes all sockets
     */
    public void stopServer() throws Exception {
        this.gameworld.stop();
        for (Object __dummyForeachVar1 : this.sockets) {
            Socket sock = (Socket) __dummyForeachVar1;
            sock.close();
        }
    }

    /**
     * Disconnect, disconnect socket
     * <p>
     * Closes socket then removes from our sockets list
     */
    public void disconnect(Socket sock) throws Exception {
        sock.close();
        this.sockets.remove(sock);
    }

}
