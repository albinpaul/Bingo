package games;
import java.net.*;
import java.io.*;
public class Client {
    Client(){

    }
    public void run(String[] args){
        String serveripaddress =args[0];
        int port = Integer.parseInt(args[1]);
        try{
            System.out.println("Connecting to serverName"+serveripaddress+"on port "+port);
            Socket client =  new Socket(serveripaddress,port);
            System.out.println("Just connect to "+client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
        }
         catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
