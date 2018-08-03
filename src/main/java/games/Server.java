package games;
import java.net.*;
import java.io.*;
public class Server extends Thread
{
    private ServerSocket serversocket;
    public Server(int port) throws IOException{
        this.serversocket =  new ServerSocket(port);
    }
    public void run(String[] args){
        while(true){
            try{
                System.out.println("Waiting for client for port"+serversocket.getLocalPort()+"...");
                Socket server = serversocket.accept();
                System.out.println("Just connected to "+server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());



            }catch (SocketException s){
                System.out.println("Socket Timed out");
            }catch(IOException e){
                e.printStackTrace();
                break;
            }


        }
    }
}
