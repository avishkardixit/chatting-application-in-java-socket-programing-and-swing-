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
                                //these made a class make window by JFrame
public class client_GUI extends JFrame
{
    //created the socket variable 
     Socket socket;

     //variabble to get inpute and output stream
    BufferedReader br;
    PrintWriter out;

    //Dclearing GUI components
    private JLabel heading = new JLabel("Client");
    private JTextArea msgArea = new JTextArea();
    private JTextField inputeArea = new JTextField();
    

    
 

    //for send buttons
    private JPanel panel = new JPanel();
    private JButton send = new JButton("Send");
    // private JButton encrypt = new JButton("Encrypt");
     
    private Font font = new Font("Georgia",Font.PLAIN,20);
    private Font fonthead = new Font("Georgia",Font.PLAIN,20);
    public client_GUI()
    {
        try
        {
           
            System.out.println("sending request to server ...");//ready to create connection 

            //here we have created new socket and connected to ip address of server and at port number 4999
            socket = new Socket("192.168.48.243",4999);//192.168.48.243//

            System.out.println("[--connected--]");
  
                //here we have initiated the "br" with reading the input stream from "socket" variable by getInputStream
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //here we have initiated "out" to write the output from "socket" variable by getOutputStream
                out = new PrintWriter(socket.getOutputStream());
    

                   createGUI();
                   handelEvents();
                //start reading the stream
                startReading();
    
                //start writing the stream
               // startWriting();
            
        }
        catch(Exception e)
        {
               e.printStackTrace();
        }
    }

    public void createGUI()
    { 
       
        //code of GUI
        this.setTitle("CLIENT");
        this.setSize(300,630);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //code for heading 
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
  
        

        //setting font of the all elements
        heading.setFont(fonthead);
        msgArea.setFont(font);
        inputeArea.setFont(font);

        //code for setting layout
        this.setLayout(new BorderLayout());

        //added three components 
        this.add(heading,BorderLayout.NORTH);
        JScrollPane scrollMsgArea = new JScrollPane(msgArea);
        this.add(scrollMsgArea,BorderLayout.CENTER);
        

        panel.setLayout(new BorderLayout());
        panel.add(inputeArea,BorderLayout.NORTH);
        panel.add(send,BorderLayout.SOUTH);
       
        
        this.add(panel,BorderLayout.SOUTH);
        

        msgArea.setBackground(Color.decode("#f6ebf4"));
        send.setBackground(Color.decode("#482673") );
       // encrypt.setBackground(Color.decode("#301008") );

        
        send.setForeground(Color.decode("#f6ebf4") );
      //  encrypt.setForeground(Color.decode("#f6ebf4") );
        heading.setForeground(Color.decode("#f6ebf4") );


        heading.setBackground(Color.decode("#301008"));
        heading.setOpaque(true); 
        
    

       msgArea.setEditable(false);



        this.setVisible(true);

    }

    public void handelEvents()
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

  
    public void startReading()
    {
        
         //these is a tread for reading
        Runnable r3 =()->{

            try
            {
                System.out.println("reading ...");

                 while(true)
                 {
                    
                     String serMsg = br.readLine();
                     if(serMsg == "bye")
                     {
                          System.out.println("chat ended ");
                          JOptionPane.showMessageDialog(null, "chat terminated");
                          inputeArea.setEnabled(false);
                          break;
                     }
                     msgArea.append("server  :"+serMsg+"\n");
                }
            }catch(Exception e)
            {
                  e.printStackTrace();
            }

        };

        new Thread(r3).start();//start the runnable thread
        
        
    }

    public void startWriting()
    {
        //these is a tread for writing 
        Runnable r4 =()->{

            try
            {
                System.out.println("writing...");

                while(true)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String cliMsg = br.readLine();
                    out.println(cliMsg);
                    out.flush();//to forcefully send the data
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        };

        new Thread(r4).start();//start the runnable thread
    }





    public static void main(String[] args) 
    {
        System.out.println("these is client...");
        new client_GUI();
    }
    
}
