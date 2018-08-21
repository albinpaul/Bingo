package games;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;


class ServiceQueue extends Thread{
    public Queue <Socket> socketqueue;
    private Server server;
    ServiceQueue(Server se){
        server=se;
        socketqueue= new LinkedList<Socket>();
    }
    public synchronized  void run() {

        while(!server.ready){
            try {
                Socket s = server.serversocket.accept();
                socketqueue.add(s);
                System.out.println("Entered into socketqueue");

            }catch(IOException  e){
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(server.ready);
    }
}

public class Server extends Thread
{

    private String gamename ;


    public ServerSocket serversocket;
    private ArrayList <Socket> socketsofclients;
    public ArrayList <DataOutputStream> sendclients;
    public ArrayList <DataInputStream> recievefromclients;
    public ServiceQueue sq;
    public String state;
    public boolean ready=false;
    public Process inputread;

    public synchronized void setreadflagtrue() throws InterruptedException{
        System.out.println("setting ready flag to true");
        this.ready=true;
        Thread.sleep(100);
    }
    public Server(String name) throws IOException {
        this.serversocket = new ServerSocket(5000);
        System.out.println(serversocket.getInetAddress());
        this.gamename = name;
        this.socketsofclients = new ArrayList<Socket>();
        this.sendclients = new ArrayList<DataOutputStream>();
        this.recievefromclients = new ArrayList<DataInputStream>();
        System.out.println(gamename);
        System.out.println("Waiting for people to connect");
        System.out.println("Enter start when everybody connected");

        sq = new ServiceQueue(this);
        sq.setDaemon(true);
        sq.start();
    }
    public ArrayList<Socket> getSocketList(){
        return socketsofclients;
    }
    public synchronized void  initialize()throws InterruptedException{
        int number=1;
        long t =System.currentTimeMillis();
        long end = t+15000;
        while(System.currentTimeMillis()<end) {
            try {
                while(!sq.socketqueue.isEmpty()) {
                    Socket server=sq.socketqueue.poll();
                    this.socketsofclients.add(server);
                    this.sendclients.add(new DataOutputStream(server.getOutputStream()));
                    this.recievefromclients.add(new DataInputStream(server.getInputStream()));
                    System.out.println("The client "+(number++)+" has entered");

                }
                Thread.sleep(100);
            }catch(IOException e){
                e.printStackTrace();
            }

        }
        ready=true;
        this.state="stopped";
        System.out.println("Ran code");
    }
    public void run() {
        try {
            initialize();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
