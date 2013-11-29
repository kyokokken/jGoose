package Goose;

import Goose.GameServer;
import Goose.Program;

public class Program   
{
    /**
     * Just starts our GameServer
     *
     */
    public static void main(String[] args) throws Exception {
        Program.Main(args);
    }

    static void Main(String[] args) throws Exception {
        new GameServer();
        System.in.read();
    }

}