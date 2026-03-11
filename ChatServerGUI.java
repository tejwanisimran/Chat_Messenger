//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  File Name   : ChatServerGUI.java
//  Description : Server-side GUI for chat application. Accepts client connection through socket,
//                sends/receives messages, and displays them inside a Swing-based interface.
//  Author      : Rekha Shankarlal Kumawat
//  Date        : 24/11/2025
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 
//  Required Libraries
//  - java.net      : For networking (Socket)
//  - java.io       : For input/output streams
//  - javax.swing   : For GUI components
//  - java.awt      : GUI styling and layout
//  - java.awt.event: For handling button click events
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 
//  Chat Class : Handles GUI, socket communication, message sending & receiving
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Chat implements ActionListener
{
    // GUI Components

    JPanel jobj1;                                                                   // Panel for input controls
    JPanel jobj2;                                                                   // Panel for chat display Area
    JFrame fobj ;                                                                   // Main Window
    JButton bobj ;                                                                  // For Send Button
    JTextField tobj ;                                                               // Text field to type messages
    JLabel lobj ;                                                                   // Label above text Field
    JScrollPane scroll;                                                             // Scrollbar for chat Area
    JTextArea taobj;                                                                // Text areaa to show messages
    
    // Networking Components

    ServerSocket ssobj;                                                            // Server socket (waits for client)
    Socket sobj;                                                                   // Client connection socket
    BufferedReader bobj1;                                                          // For reading client messages
    PrintStream pobj;                                                              // For sending messages to client

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Constructor : Builds GUI and establishes server-client connection
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public Chat( String title , int width , int height)
    {
        fobj = new JFrame(title);
        try
        {
            ssobj = new ServerSocket(2300);
            sobj = ssobj.accept();
            bobj1 = new BufferedReader(new InputStreamReader(sobj.getInputStream()));
            pobj= new PrintStream(sobj.getOutputStream());

        }
        catch(Exception eobj)
        {
            System.out.println("Connection Error:" +eobj);
        }

        lobj = new JLabel("Message");
        lobj.setBounds(10, 20, 100, 25);

        tobj = new JTextField();
        tobj.setBounds(90, 20, 150, 25);

        bobj = new JButton("SEND");
        bobj.setBounds(80, 60, 100, 30);
        bobj.setBackground(Color.cyan);

        jobj1 = new JPanel();
        jobj1.setLayout(null);
        jobj1.setBounds(10, 10, 260, 100);
        jobj1.setBackground(Color.LIGHT_GRAY);

        jobj2 = new JPanel();
        jobj2.setLayout(null);
        jobj2.setBounds(10, 120, 260,330 );
        jobj2.setBackground(Color.LIGHT_GRAY);

        taobj = new JTextArea();
        taobj.setEditable(false);
        taobj.setFont(new Font("Arial", Font.PLAIN, 14));

        scroll = new JScrollPane(taobj);
        scroll.setBounds(10, 10, 240, 310);
        
        // Add components to panels

        jobj1.add(bobj);
        jobj1.add(lobj);
        jobj1.add(tobj);
        jobj2.add(scroll);

        // Add panels to main window

        fobj.add(jobj2);
        fobj.add(jobj1);
        
        bobj.addActionListener(this);                                                // Listen to button clicks

        // Set Window size, layout and behavior 

        fobj.setSize(width, height);
        fobj.setLayout(null);
        fobj.setVisible(true);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  actionPerformed : Invoked when SEND button is clicked
    //                    Sends message to client and displays it in the GUI
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
    public void actionPerformed(ActionEvent aobj) 
    {
        String str1 = null;
        str1= tobj.getText();                                                    // Read message from input box
        pobj.println(str1);                                                      // Send message to client
        addToChat("You: "+str1);                                                 // Display on GUI
        tobj.setText("");                                                     // clear input field
     
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Display : Continuously receives messages from client and displays them
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void Display() 
    {
        try
        {
             while(true)
            {
            
                String str2 = null;
                str2 = bobj1.readLine();                                        // Read message sent by client
                if(str2.equals("END"))                                // If client sends the END,then stop communtication
                {
                    addToChat("Client Disconnected.");
                    break;
                    
                }
                addToChat("Client : "+str2);                                  // Display client message in chat area
            }
        }
        catch(Exception eobj)
        {
            System.out.println("Recieve Error : "+eobj);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  addToChat : Appends text to chat window with auto-scroll
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
    public void addToChat(String str1)
    {
        taobj.append(str1 + "\n");                                         // Add message to chat area

        taobj.setCaretPosition(taobj.getDocument().getLength());           //Automatically move to last message
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 
//  ChatServerGUI : Main class that launches the server GUI
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class ChatServerGUI
{
    public static void main (String a[]) throws Exception
    {

        Chat cobj = new Chat("Server" , 300,500);
        cobj.Display();
        
        
        
    }
}