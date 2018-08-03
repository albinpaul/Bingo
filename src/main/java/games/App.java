package games;
import javax.swing.*;
import java.net.*;
import java.io.*;
public class App 
{
    public static void main( String[] args )
    {

        Server serverthread = null;
        try {
            serverthread = new Server(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverthread.run();

    }
}
