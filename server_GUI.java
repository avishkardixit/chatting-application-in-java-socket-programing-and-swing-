import java.io.*;
import java.net.*;

import javax.swing.Action;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class server_GUI extends JFrame
{
    
    //variables to create server and socket 
    ServerSocket server;
    Socket socket;

    //variabble to get inpute and output stream
    BufferedReader br;
    PrintWriter out;

    //Declearing GUI components
    private JLabel heading = new JLabel("SERVER");
    private JTextArea msgArea = new JTextArea();
    private JTextField inputeArea = new JTextField();

    //for send button 
    private JPanel panel = new JPanel();
    private JButton send = new JButton("Send");

    private Font font = new Font("Georgia",Font.PLAIN,20);

    server_GUI() 
    {
        try
        {
            //created new server at port 4999 at variable "server " of type "ServerSocket"
            server = new ServerSocket(4999);
            System.out.println("server is ready...");
            System.out.println("waiting...");

            //accept the request from server 
            socket = server.accept();


            //here we have initiated the "br" with reading the input stream from "socket" variable by getInputStream
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //here we have initiated "out" to write the output from "socket" variable by getOutputStream
            out = new PrintWriter(socket.getOutputStream());

            create_GUI();
            handelEvent();
            //start reading the stream
            startReading();

            // //start writing the stream
            // startWriting();
        

        }
        catch(Exception e)
        {
            e.printStackTrace();//it will print message when program will throw an exception 
        }

    
    }

  
    private void handelEvent()
     {
        
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = inputeArea.getText();
                msgArea.append("me  :"+msg+"\n");
                out.println(msg);
                out.flush();
                inputeArea.setText("");

            
             }
        
        });

    }


    private void create_GUI()
    {
          //code for  GUI
          this.setTitle("SERVER");
          this.setSize(300,630);
          this.setLocationRelativeTo(null);
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          this.setLayout(new BorderLayout());

          heading.setHorizontalAlignment(SwingConstants.CENTER);
          heading.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

          heading.setFont(font);
          msgArea.setFont(font);
          inputeArea.setFont(font);

          this.add(heading,BorderLayout.NORTH);
          JScrollPane scrollMsgArea = new JScrollPane(msgArea);
          this.add(scrollMsgArea,BorderLayout.CENTER);

          panel.setLayout(new BorderLayout());
          panel.add(inputeArea,BorderLayout.NORTH);
          panel.add(send,BorderLayout.SOUTH);

          this.add(panel,BorderLayout.SOUTH);

          
        msgArea.setBackground(Color.decode("#f6ebf4"));
        send.setBackground(Color.decode("#482673") );
       

        
        send.setForeground(Color.decode("#f6ebf4") );
     
        heading.setForeground(Color.decode("#f6ebf4") );


        heading.setBackground(Color.decode("#301008"));
        heading.setOpaque(true); 

       

          this.setVisible(true);



    }


  


    public void startReading()
    {
        
         //these is a tread for reading
        Runnable r1 =()->{

            try
            {
                System.out.println("reading ...");

                 while(true)
                 {
                    
                     String cliMsg = br.readLine();
                     if(cliMsg == "bye")
                     {
                          System.out.println("chat ended ");
                          JOptionPane.showMessageDialog(null, "chat terminated");
                          inputeArea.setEnabled(false);
                          break;
                          
                     }
                
                     msgArea.append("client  :"+cliMsg+"\n");
                }
            }catch(Exception e)
            {
                  e.printStackTrace();
            }

        };

        new Thread(r1).start();//start the runnable thread
        
        
    }

    public void startWriting()
    {
        //these is a tread for writing 
        Runnable r2 =()->{

            try
            {
                System.out.println("writing...");

                while(true)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String serMsg = br.readLine();
                    out.println(serMsg);
                    out.flush();//to forcefully send the data
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();//start the runnable thread
    }



    public static void main(String[] args)throws IOException
    {
        System.out.println("server is live...");
      
        
        new server_GUI();//called the object of server constructor
    }
}


