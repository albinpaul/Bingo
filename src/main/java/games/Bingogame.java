package games;
import com.sun.xml.internal.ws.util.StringUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.Collections;
import java.awt.EventQueue;
import java.util.*;
import java.util.concurrent.locks.*;

class Bingothread extends Thread{
    private DataOutputStream ostream;
    private DataInputStream istream;
    private Bingogame bingo;
    Bingothread(DataOutputStream oStream,DataInputStream iStream,Bingogame b){
        bingo=b;
        ostream=oStream;istream=iStream;
    }
    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {

            Double.parseDouble(strNum);

        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }
    public void run(){
        while(true){
            try {

                String recievestring = istream.readUTF();
                if(isNum(recievestring)){
                    bingo.setbitsarray(Integer.parseInt(recievestring));
                }else{
                    if(recievestring.contains("Player")){
                        System.out.println(recievestring);
                        System.exit(0);
                    }
                    bingo.inputallowed=true;
                };
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Bingogame extends JFrame{
    public static int running=1;
    private JButton Buttonarray[];
    static int counter=0;
    public int id ;
    public JLabel noticetext;
    private ArrayList <Integer> values;
    private ArrayList <Integer> setbits;
    private JPanel boxesPanel ;
    private DataOutputStream ostream;
    private DataInputStream istream;
    private Socket clientsocket;
    public Lock lock;
    public boolean inputallowed;
    private Bingothread bingoThread;

    public int check(){
        int count=0;
        for(int i=0;i<5;i++){
            int f=1;
            for(int j=0;j<5;j++){
                f*=setbits.get(5*i+j);
            }
            if(f==1){
                count++;
            }
        }
        for(int i=0;i<5;i++){
            int f=1;
            for(int j=0;j<5;j++){
                f*=setbits.get(i+5*j);
            }
            if(f==1){
                count++;
            }
        }
        int f=1;
        for(int i=0;i<5;i++){
            int j = i;
            f*=setbits.get(i*5+j);
        }
        if(f==1){
            count++;
        }
        f=1;
        for(int i=0;i<5;i++){
            int j = 5-i;
            f*=setbits.get(i*5+j);
        }
        if(f==1){
            count++;
        }
        if(count>=5){
            return 1;
        }
        return 0;
    }

    public void setbitsarray(int i) throws InterruptedException, IOException {

        for(int j=0;j<25;j++){
            if(values.get(j).equals(i))
            {
                setbits.set(j,1);
                Buttonarray[j].setBackground(Color.YELLOW);
            }
        }
        if(check()==1){
            System.out.println("Player "+String.valueOf(this.id));
            ostream.writeUTF("Player "+String.valueOf(this.id)+" Won" );
            System.out.println("Player "+String.valueOf(this.id)+" Won");
            Thread.sleep(3000);
            System.exit(0);


        }
    }
    Bingogame(InetAddress address, int port){


            System.out.println("id = "+id);
            setTitle(" Bingo ");
            setSize(600,600);
            noticetext= new JLabel("Bingo game");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            boxesPanel=new JPanel();
            noticetext.setBounds(250,00,250,100);
            add(noticetext);
            //boxesPanel.setLayout(new GridLayout(5,5));
            boxesPanel.setSize(500,500);
            Buttonarray = new JButton [25];
            values = new ArrayList<>();
            setbits=  new ArrayList<>();;
            System.out.println("Connecting to serverName "+address+" on port "+port);
            try{
                this.clientsocket = new Socket(address,port);
                this.ostream = new DataOutputStream(this.clientsocket.getOutputStream());
                this.istream = new DataInputStream(this.clientsocket.getInputStream());
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i =0;i<25;i++){
                values.add(i+1);
                setbits.add(0);
            }

            bingoThread = new Bingothread(ostream,istream,this);
            bingoThread.start();
            Collections.shuffle(values);
            for(int i =0;i<25;++i){
                Buttonarray[i]=new JButton();
                Buttonarray[i].setPreferredSize(new Dimension(65,65));
                Buttonarray[i].setText(String.valueOf(values.get(i)));
                Buttonarray[i].addActionListener(new buttonListener(this));

            }

            this.add(boxesPanel);
            boxesPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            for(int i =0;i<5;i++){
                for(int j=0;j<5;j++){
                    gbc.gridx=j;
                    gbc.gridy=i;
                    boxesPanel.add(Buttonarray[i*5+j],gbc);
                }
            }
            this.add(boxesPanel);
            //lock.unlock();
            inputallowed=false;
    }

    private class buttonListener implements ActionListener {
        private Bingogame player;
        buttonListener(Bingogame game){
            player=game;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            int flag=0;
            for(int no =0;no<setbits.size();no++){
                if(command.equals(Buttonarray[no].getText()) && setbits.get(no)==1 ){
                    flag=1;
                }
            }
            //player.noticetext.setText(command);
            if(inputallowed && flag==0) {


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                JButton button = (JButton) actionEvent.getSource();
                button.setBackground(Color.LIGHT_GRAY);
                for(int i=0;i<25;i++){
                    if(command.equals(Buttonarray[i].getText())){
                        setbits.set(i,1);
                    }
                }
                //setbits.get()
                try {
                if(check()==1){
                    player.ostream.writeUTF("Player ");
                    Thread.sleep(3000);
                    System.exit(0);

                }
                System.out.println(command);

                    player.ostream.writeUTF(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
               inputallowed=false;
            }
        }

    }


    public static void main(String [] args)throws IOException,InterruptedException{



        byte [] address = new byte []{ (byte) 192, (byte)168,(byte)125,(byte)201};
        Bingogame b = new Bingogame(InetAddress.getByAddress(address),5000);
        b.setVisible(true);
    }


}
