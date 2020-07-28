//Bakitas Theofanis icsd15133 Askisi 2i 

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends JFrame implements Serializable {

    //dilosi metavliton
    //Variable Declaration
    private Socket connection;
    private final int port;

    static ObjectInputStream inputstream;
    static ObjectOutputStream outputstream;
    private String current, status;// gia elegxo sindesis
    private final JButton search_announcement_button, add_ann_button, delete_ann_button, mod_ann_button;

    private final JTabbedPane jtabs;
    private JPanel kartela1, kartela2, kartela3, kartela4;
    private JLabel new_titlelabel, new_descrlabel, tab_name1, tab_name2, tab_name3, tab_name4, sdate_lab1, user_label4, pass_label4, edate_lab1, title_label4, user_label1, eventdate_label4, descr_label4, pass_label1, title_label1, descr_label1, user_label3, pass_label3, title_label3, descr_label3, eventdate_label, eventdate_label2;
    private JTextField username, password, description2, title2, username2, password2, title3, description3, description4, title4, username4, password4, new_title, new_descr;
    String pattern = "dd-MM-yyyy";

    //Constructor
    public Client() {
        super("Client Application");
        port = 443;//initialize the port
        status = "Running....";
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        jtabs = new JTabbedPane();//ftiaxnoume tis karteles/ creating the tabs
        getContentPane().add(jtabs);
        getContentPane().setLayout(new GridLayout(1, 1));
        kartela1 = new JPanel();
        kartela1.setLayout(new GridLayout(20, 1));
        kartela2 = new JPanel();
        kartela2.setLayout(new GridLayout(20, 1));
        kartela3 = new JPanel();
        kartela3.setLayout(new GridLayout(20, 1));
        kartela4 = new JPanel();
        kartela4.setLayout(new GridLayout(20, 1));

        //--- ( TAB 1) SHOW ANNOUNCEMENTS 
        tab_name1 = new JLabel("Show Announcements", JLabel.CENTER);
        // Prostheto ta katallila labels kai apo kato ta textfield tous 
        // Adding the  labels and textfields accordingly 
        //Tab 1 
        kartela1.add(tab_name1);

        DateFormat format = new SimpleDateFormat(pattern);
        JFormattedTextField starting_date1 = new JFormattedTextField(format);
        sdate_lab1 = new JLabel("Starting Date");
        JFormattedTextField ending_date1 = new JFormattedTextField(format);
        edate_lab1 = new JLabel("Ending Date");

        // prostheto ta stoixeia pou prepei na perilamvanei i kartela 
        kartela1.add(sdate_lab1);
        kartela1.add(starting_date1);
        kartela1.add(edate_lab1);
        kartela1.add(ending_date1);

        search_announcement_button = new JButton("Search");
        kartela1.add(search_announcement_button);//------------TELOS KARTELAS ------------------------------------------

        //-----------------------------------------------TAB 2 CREATE an Announcement
        //kartela 2 pou dimiourgei announcements 
        tab_name2 = new JLabel("Create an Announcement", JLabel.CENTER);
        // Prostheto ta katallila labels kai apo kato ta textfield tous 
        // Adding the  labels and textfields accordingly 
        //Tab 1 
        kartela2.add(tab_name2);
        user_label1 = new JLabel("Username");
        pass_label1 = new JLabel("Password");
        title_label1 = new JLabel("Title");
        descr_label1 = new JLabel("Description");
        eventdate_label2 = new JLabel("Event Date");
        username2 = new JTextField();
        password2 = new JTextField();
        title2 = new JTextField();
        description2 = new JTextField();
        JFormattedTextField event_date2 = new JFormattedTextField(format);

        // prostheto ta stoixeia pou prepei na perilamvanei i kartela 
        kartela2.add(user_label1);
        kartela2.add(username2);
        kartela2.add(pass_label1);
        kartela2.add(password2);

        kartela2.add(title_label1);
        kartela2.add(title2);

        kartela2.add(descr_label1);
        kartela2.add(description2);

        add_ann_button = new JButton("Add Announcement");
        kartela2.add(add_ann_button);//------------TELOS KARTELAS ------------------------------------------

        // -------------------------------TAB 3 Delete Announcements
        tab_name3 = new JLabel("Delete an Announcement", JLabel.CENTER);
        // Prostheto ta katallila labels kai apo kato ta textfield tous 
        // Adding the  labels and textfields accordingly 

        kartela3.add(tab_name3);
        user_label3 = new JLabel("Username");
        pass_label3 = new JLabel("Password");
        title_label3 = new JLabel("Title");
        descr_label3 = new JLabel("Description");
        eventdate_label = new JLabel("Event Date");
        username = new JTextField();
        password = new JTextField();
        title3 = new JTextField();
        description3 = new JTextField();

        JFormattedTextField event_date = new JFormattedTextField(format);

        kartela3.add(user_label3);
        kartela3.add(username);
        kartela3.add(pass_label3);
        kartela3.add(password);

        kartela3.add(title_label3);
        kartela3.add(title3);

        kartela3.add(descr_label3);
        kartela3.add(description3);
        kartela3.add(eventdate_label);
        kartela3.add(event_date);

        delete_ann_button = new JButton("Delete");
        kartela3.add(delete_ann_button);

        //------------TELOS KARTELAS ------------------------------------------   
        // -----------------Tab 4 Modify an Announcement
        //  tab_name4 = new JLabel("Modify an Announcement", JLabel.CENTER);
        tab_name4 = new JLabel("Modify an Announcement", JLabel.CENTER);
        // Prostheto ta katallila labels kai apo kato ta textfield tous 
        // Adding the  labels and textfields accordingly 
        //Tab 1 
        kartela4.add(tab_name4);
        user_label4 = new JLabel("Username");
        pass_label4 = new JLabel("Password");
        title_label4 = new JLabel("Title");
        descr_label4 = new JLabel("Description");
        eventdate_label4 = new JLabel("Event Date");
        username4 = new JTextField();
        password4 = new JTextField();
        title4 = new JTextField();
        description4 = new JTextField();

        new_titlelabel = new JLabel("New Title");
        new_descrlabel = new JLabel("New Description");
        new_title = new JTextField();
        new_descr = new JTextField();

        JFormattedTextField event_date4 = new JFormattedTextField(format);

        // prostheto ta stoixeia pou prepei na perilamvanei i kartela 
        kartela4.add(user_label4);
        kartela4.add(username4);
        kartela4.add(pass_label4);
        kartela4.add(password4);

        kartela4.add(title_label4);
        kartela4.add(title4);

        kartela4.add(descr_label4);
        kartela4.add(description4);
        kartela4.add(eventdate_label4);
        kartela4.add(event_date4);

        kartela4.add(new_titlelabel);
        kartela4.add(new_title);

        kartela4.add(new_descrlabel);
        kartela4.add(new_descr);

        mod_ann_button = new JButton("Modify");
        kartela4.add(mod_ann_button);

        jtabs.addTab("Search", null, kartela1, "Search an Announcement within a specific time frame ");
        jtabs.addTab("Create", null, kartela2, "Create an Announcement");
        jtabs.addTab("Del", null, kartela3, "Delete  an Announcement");
        jtabs.addTab("Mod", null, kartela4, "Modify an Announcement");

        setSize(new Dimension(600, 700));
        setLocationRelativeTo(null);

        search_announcement_button.addActionListener(new ActionListener() {// stelnei arxiki kai teliki imerominia gia anazitisi
            @Override
            public void actionPerformed(ActionEvent e) {
                if(   ((String) starting_date1.getText()).equals("") || ((String) ending_date1.getText()).equals("") )
                        {
                         JOptionPane.showMessageDialog(null, "Fill the blanks", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                else{

                String data = (String) starting_date1.getText() + ":" + (String) ending_date1.getText();
                System.out.println("Start date: " + (String) starting_date1.getText());
                System.out.println("Ending date: " + (String) ending_date1.getText());
                
                if (data != null && data.length() > 0) {
                    try {
                        outputstream.writeObject("SEARCH");// stelnei search request 
                        outputstream.flush();
                        System.out.println("Stelno aitima Search gia  " + data);

                        outputstream.writeObject(starting_date1.getText());// gia search request 
                        outputstream.flush();
                        outputstream.writeObject(ending_date1.getText());// gia search request 
                        outputstream.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String str;
                    try {
                        // diavazei apotelesmata anazitisis
                        str = (String) inputstream.readObject();
                        if (str.isEmpty()) {
                            JOptionPane.showMessageDialog(null, " No matching results found ", "Results", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            System.out.println(str);
                            JOptionPane.showMessageDialog(null, str, "Results", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                }
                starting_date1.setText(null);
                ending_date1.setText(null);

            }
        });

        add_ann_button.addActionListener(new ActionListener() { // stelnei anakoinosi gia prosthiki
            @Override
            public void actionPerformed(ActionEvent e) {
                
                 if(   username2.getText().equals("") ||password2.getText().equals("")||title2.getText().equals("") || description2.getText().equals("")  )
                        {
                         JOptionPane.showMessageDialog(null, "Fill the blanks", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                else{
                
                
                
                Announcement announ;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  // to idio format me to opoio xrisimopoiisame gia to input sta grafika

                Date datenow = calendar.getTime();
                String strDate = formatter.format(datenow);//string convertion

                announ = new Announcement(title2.getText(), description2.getText(), username2.getText(), strDate); // torini imerominia 
                System.out.println(announ.getAnnouncement_date());
                if (announ != null) {

                    try {
                        outputstream.writeObject("ADD");// stelnei add request /clarifies that this is going to be an add request 
                        outputstream.flush();
                        System.out.println(announ.toString());

                        try { // antikatastasi send request

                            Object obj = announ;
                            System.out.println(((Announcement) obj).toString()); // stelnei to antikeimeno 
                            outputstream.writeObject(obj);
                            outputstream.flush();
                            // kommati pou an einai neos o xristis zitaei password
                            //   "Send Password"
                            String str = "";
                            try {

                                str = (String) inputstream.readObject();// diavazei send password

                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (str.equals("Send Password")) // an zitithike to password gia neo xristi tote to stelnei 
                            {
                                String a = password2.getText();
                                outputstream.writeObject(a);// stelnei password
                                outputstream.flush();
                            }

                        } catch (IOException ex) {
                            System.out.println("IO Exception @Sending object");
                        }

                        String str = "";
                        try {

                            str = (String) inputstream.readObject();//diavazei an teliose i diadikasia
                            System.out.println(str);
                            if (str.equals("DONE")) {// success case

                                JOptionPane.showMessageDialog(null, "Your Announcement has been added!", "Results", JOptionPane.INFORMATION_MESSAGE);

                            }
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Exception 293");
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (IOException ex) {
                        System.out.println("Exception 301");
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                title2.setText(null);
                description2.setText(null);
                username2.setText(null);
                password2.setText(null);
                starting_date1.setText(null);
                ending_date1.setText(null);

            }
            }
        });

        delete_ann_button.addActionListener(new ActionListener() { // stelnei ena antikeimeno pros diagrafi
            @Override
            public void actionPerformed(ActionEvent e) {
                
                 if(   username.getText().equals("") ||password.getText().equals("")||title3.getText().equals("") || description3.getText().equals("") ||((String) event_date.getText()).equals("") )
                        {
                         JOptionPane.showMessageDialog(null, "Fill the blanks", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                else{
                
                try {
                    outputstream.writeObject("DELETE");// stelnei remove request 
                    outputstream.flush();

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                Announcement announ;
                // username 
                // password 
                //  title3 
                //  description3 
                //
                announ = new Announcement(username.getText(), password.getText(),description3.getText() , event_date.getText());//ftiaxnei to antikeimeno pou tha steilei 
                //username , password , description,imerominia anti gia titlos,perigrafi,username,imerominia 
                if (announ != null) {
                    SendRequest(announ);//stelnei to antikeimeno (1)

                    String str;
                    try {
                        // diavazei apotelesmata anazitisis
                        str = (String) inputstream.readObject(); //(4) dexetai apantisi apo to server gia to an svistike i oxi kapoio antikeimeno 
                        if (str.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nothing was deleted ", "Results", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            System.out.println(str);
                            JOptionPane.showMessageDialog(null, str, "Results", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                title3.setText(null);
                username.setText(null);
                password.setText(null);
                description3.setText(null);
                event_date.setText(null);
            }
            }
        });

        mod_ann_button.addActionListener(new ActionListener() { //MODIFY
            @Override
            public void actionPerformed(ActionEvent e) {
                
                 if(   username4.getText().equals("") ||password4.getText().equals("")||title4.getText().equals("") || description4.getText().equals("") ||new_title.getText().equals("")|| new_descr.getText().equals("")  )
                 {
                  JOptionPane.showMessageDialog(null, "Fill the blanks", "Error!", JOptionPane.ERROR_MESSAGE);
                 }
                 
                 else{

                try {
                    outputstream.writeObject("MODIFY");// stelnei modify request (0)
                    outputstream.flush();

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date datenow = calendar.getTime();
                String strDate = formatter.format(datenow);

                System.out.println("events date: " + event_date4.getText());

                Announcement announ;
                announ = new Announcement(title4.getText(), description4.getText(), username4.getText(), event_date4.getText());//ARXIKO ANTIKEIMENO PROS ANAZITISI 
                System.out.println("Antikeimeno pou prokeitai na steilo " + announ.toString());

                if (announ != null) { // AN IPARXEI TO ANTIKEIMENO 

                    try { // antikatastasi send request

                        outputstream.writeObject(announ);//stelnei to antikeimeno (2)
                        outputstream.flush();
                    } catch (IOException ex) {
                        System.out.println("mod1 io exception");
                    }

                    String str;
                    try {

                        // diavazei apotelesmata anazitisis
                        str = (String) inputstream.readObject(); // (4) an iparxei to antikeimeno tote dexetai send password 

                        if (str.equals("Send Password!")) { // an iparxei to antikeimeno kai matcharei kai o xristis tote stelnei ena neo antikeimeno pou tha exei mesa (neo titlo, neo descr, password ,imerominia ekeini tin stigmi )

                            announ = new Announcement(new_title.getText(), new_descr.getText(), password4.getText(), strDate);
                            try { // antikatastasi send request

                                outputstream.writeObject(announ);//stelnei to antikeimeno  (5) 
                                outputstream.flush();
                            } catch (IOException ex) {
                                System.out.println("mod1 io exception");
                            }

                            try {
                                // diavazei apotelesmata anazitisis
                                str = (String) inputstream.readObject();// 8 LAMVANEI TELIKO MINIMA 
                                
                                if (str.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, " Error receiving final data.... ", "Results", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    System.out.println(str);
                                    JOptionPane.showMessageDialog(null, str, "Results", JOptionPane.INFORMATION_MESSAGE);
                                }

                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {

                            JOptionPane.showMessageDialog(null, str, "Results", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
//                try {
//                    inputstream.reset(); // cleans out the buffer
//                } catch (IOException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            
            
        });

        this.addWindowListener(new WindowAdapter() {// listener pou kleinei tin sindesi otan kleisei o server
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        //Start Client
        RunClient();
    }

    private void RunClient() {

        connection = connectToServer();// sindesi client me server

        System.out.println("mpika");
        InitializeStreams(connection); // initialize the connecting streams 

    }

    private Socket connectToServer() {// Connection Method
        try {

            connection = new Socket("127.0.0.1", port);
            return connection;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to connect !\n Exiting...", "IO Exception", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            this.dispose();
            return connection;
        }
    }

    //Methods that initialize our Input/Output Streams
    private void InitializeStreams(Socket connection) {
        try {
            outputstream = new ObjectOutputStream(connection.getOutputStream());
            inputstream = new ObjectInputStream(connection.getInputStream());
            whileConnected();
        } catch (IOException ex) {

        }
    }

    private void disconnect() { // Method that terminated connection
        try {
            //kleinei tin sindesi 
            status = "closure";
            connection.close();
        } catch (IOException ex) {
        }
    }

    private void SendRequest(Object obj) { // Method that sends a stream request to the server through the connecting output stream
        try {

            System.out.println(obj.toString());
            System.out.println("Sending Request");
            outputstream.writeObject(obj);
            outputstream.flush();
        } catch (IOException ex) {
        }
    }

    private void whileConnected() {//methodos gia otan sindethei 
        current = "Running...";
        // stelnei connect gia na kseroume oti egine i sindesi 
        try {
            outputstream.writeObject("CONNECT");
            outputstream.flush();
        } catch (IOException ex) {

        }
        if (current.equals("closure")) {// an parei closure tote kleinei to programma me tin disconnect
            disconnect();
        }
    }

}
