package games;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class App  {
    public static void main(String args[]) throws Exception {
        Server s = new Server("Sample game ");
        s.initialize();
    }

}