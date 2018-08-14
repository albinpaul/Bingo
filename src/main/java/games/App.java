package games;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.SocketHandler;

import javax.swing.*;
class connectToSql{
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL ="jdbc:mysql://localhost/";
    //database credentials
    static final String USER ="client";
    static final String PASSWORD ="password";

    public void connect(){
        Connection conn =null;
        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            stmt = conn.createStatement();
            String sql = "use game";
            stmt.executeUpdate(sql);
            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
public class App  {
    private static ArrayList <Bingogame> bingoarray=new ArrayList<>();
    App(){

    }

    public static void publish(Bingogame game) throws InterruptedException {
        System.out.println("Game finished");
        for (int i =0;i<bingoarray.size();i++){
            bingoarray.get(i).noticetext.setText("Player "+String.valueOf(game.id));
            Thread.sleep(100);
            System.out.println(bingoarray.get(i).id);
            System.out.println("Player "+String.valueOf(game.id));
        }
        Thread.sleep(15000);
        System.exit(0);

    }
    void creategame(){
        try {
            Server s = new Server("Sample game ");
            //s.setDaemon(true);
            System.out.println("Waiting about 15 seconds for everyone to connect");
            s.start();


            s.join();
            System.out.println("Server finished");
            System.out.println("Let the game begin");
            System.out.println(s.recievefromclients.size());
            int numberofclients = s.recievefromclients.size();
            assert (numberofclients == s.recievefromclients.size() && numberofclients == s.sendclients.size());
            for (int t = 0; t < 25; t++) {
                for (int i = 0; i < numberofclients; i++) {

                    s.sendclients.get(i).writeUTF("send");

                    String number = s.recievefromclients.get(i).readUTF();
                    if(number.contains("Player")){
                        System.out.println(number);
                        for (int j = 0; j < numberofclients; j++) {
                            if (i == j) continue;
                            s.sendclients.get(j).writeUTF(number);
                        }
                        Thread.sleep(1000);
                        System.exit(0);
                    }
                    for (int j = 0; j < numberofclients; j++) {
                        if (i == j) continue;
                        s.sendclients.get(j).writeUTF(number);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Exited the system");
            System.exit(0);
        }


    }
    public static void main(String args[]) throws Exception {
        App a =new App();
        a.creategame();


    }

}