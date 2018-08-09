package games;
import java.net.*;
import java.io.*;
public class Client {
    //private String clientname;
    private Socket clientsocket;
    private DataOutputStream sendstream;
    private DataInputStream instream;
    public Client(String[] args){
        String serveripaddress =args[0];
        int port= Integer.parseInt(args[1]);;
        System.out.println("Connecting to serverName"+serveripaddress+"on port "+port);
        try{
            this.clientsocket = new Socket(serveripaddress,port);
            this.sendstream = new DataOutputStream(this.clientsocket.getOutputStream());
            this.instream = new DataInputStream(this.clientsocket.getInputStream());
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public void run(String[] args){
        String serveripaddress =args[0];
        int port = Integer.parseInt(args[1]);
        int a =1;
    }
}
