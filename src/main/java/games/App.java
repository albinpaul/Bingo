package games;
import java.sql.*;
import java.util.ArrayList;

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
    void creategame() throws Exception{
        Server s = new Server("Sample game ");
        //s.setDaemon(true);

        s.start();



        for(int i =0;i<2;i++){
            bingoarray.add(new Bingogame(s));
            bingoarray.get(i).setVisible(true);
        }

        s.join();
        System.out.println("Server finished");
        System.out.println("Let the game begin");
        assert (bingoarray.size()==s.recievefromclients.size());
        System.out.println(s.recievefromclients.size());

        for(int t=0;t<25;t++){
            for(int i=0;i<2;i++){
                bingoarray.get(i).inputallowed=true;

                String number=s.recievefromclients.get(i).readUTF();
                for(int j=0;j<2;j++){
                    if(i==j)continue;
                    s.sendclients.get(j).writeUTF(number);
                }
            }
        }



    }
    public static void main(String args[]) throws Exception {
        App a =new App();
        a.creategame();


    }

}