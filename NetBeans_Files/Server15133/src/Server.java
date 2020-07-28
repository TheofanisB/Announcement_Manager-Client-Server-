//Bakitas Theofanis icsd15133 Askisi 2i 
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends JFrame implements Serializable {

    //connection declaration
    private ServerSocket server;
    private Socket connection;
    private int port;
    private String receiveddata;
    static ObjectInputStream instream;
    static ObjectOutputStream outstream;

    //graphical declaration
    private JPanel panel_1;
    private JLabel label_1;
    private JTextArea result;
    private JScrollPane scrollpane_1;

    static FileOutputStream outputFile;//Ftiaxnuome ena outputstream gia na mporoume na grapsoume sto arxeio 
    static ObjectOutputStream outputObject;//Roi gia antikeimena mesa apo arxeio 
    ArrayList<Account> acc_list = new ArrayList<Account>();
    ArrayList<Announcement> ann_db = new ArrayList<Announcement>();

    //Server constructor 
    public Server() {

        super("------Server------");
        port = 443;//initialize the port

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        getContentPane().setLayout(new BorderLayout());
        panel_1 = new JPanel();
        panel_1.setLayout(new FlowLayout());
        label_1 = new JLabel("Server Log:");
        label_1.setFont(new Font("Arial", Font.BOLD, 25));
        panel_1.add(label_1);

        result = new JTextArea();
        result.setFont(new Font("Arial", Font.PLAIN, 18));
        result.setEditable(false);
        scrollpane_1 = new JScrollPane(result);
        scrollpane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(panel_1, BorderLayout.PAGE_START);
        add(scrollpane_1);
        setSize(500, 500);
        setLocationRelativeTo(null);

        //prosthiki admin
        Account acc1 = new Account("Admin", "Admin");
        acc_list.add(acc1);

        //Initialize ServerSocket and connections
        try {
            server = new ServerSocket(443);// 
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server Socket I/O Exception during initialization!", "IO Exception", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            this.dispose();
        }
        this.addWindowListener(new WindowAdapter() {// when closing the connection window 
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        //Start Server
        Run();
    }

    private void Run() {
        while (true) {
            ConnectionInit();//ksekinaei tin sindesi
            streamInit();// anoigei ta streams
            acceptRequests();// dexetai aitimata 

        }
    }//telos run

    private void ConnectionInit() {
        try {
            //waiting part
            currentLog("Pending client connection..");
            connection = server.accept();
            //connection success
            currentLog("Connection Successfully Initiated ! Client address:" + connection.getInetAddress().getHostName());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "I/O Error while trying to accept connections", "IO Exception", JOptionPane.ERROR_MESSAGE);
            System.out.println("Debug: " + ex.toString());
        }
    }//telos connection initialization

   
    private void currentLog(final String data) {// method that appends data to the Server User Interface 
        SwingUtilities.invokeLater( 
                new Runnable() {
            @Override
            public void run() {
                result.append(data + " \n");
            }
        }
        );
    }

    //stream initializer
    private void streamInit() {
        try {
            instream = new ObjectInputStream(connection.getInputStream());
            outstream = new ObjectOutputStream(connection.getOutputStream());

            currentLog("Streams have been successfully initialized!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "I/O issue while setting up the streams in the server!", "IO Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendData(String message) {// method that sends data to the user 
        try {
            outstream.writeObject(message);
            outstream.flush();
        } catch (IOException ex) {
            currentLog("There was an IOException during the data transfer from the server to the client ");
        }
    }

    private void acceptRequests() {// methodos pou dexetai sinexos aitimata 
        try {
            receiveddata = (String) instream.readObject();
            Object obj = null;

            if (receiveddata.equals("CONNECT")) {
                System.out.println("CONNECT");
                do {
                    receiveddata = (String) instream.readObject();
                    System.out.println(receiveddata);
                    ann_db.clear();
                    switch (receiveddata) {
                        case "SEARCH"://done
                            //Search announcements
                            System.out.println("SEARCH");
                            String startdate = (String) instream.readObject();
                            String enddate = (String) instream.readObject();

                           // System.out.println(startdate);
                           // System.out.println(enddate);

                            searchAnnouncement(startdate, enddate);//arxiki kai teliki imerominia san parametroi

                            break;
                        case "ADD":// Add case
                            //Add an announcement
                            System.out.println("ADD");//EKTIPONETAI

                            obj = instream.readObject();
                            
                            addAnnouncement(obj);
                            outstream.writeObject("DONE");
                            
                            break;
                        case "DELETE"://delete case
                            //Send File
                            System.out.println("DELETE");
                            obj = instream.readObject();
                            deleteAnnouncement(obj);
                            break;
                        case "MODIFY":// mod case
                            System.out.println("MODIFY");
                            //   antikeimeno pros anazitisi , password , neos titlos , neo description ,imerominia
                            obj = instream.readObject();
                            modAnnouncement(obj);
                            break;
                        default:// an stalthei akiro paketo
                            sendData("Unknown action!");
                            break;
                    }
                } while (!receiveddata.equals("closure"));//mexri na parei closure gia na stamatisei 
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "I/O issue while accepting Client request", "IO Exception", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            currentLog("Casting error");
        }
    }

    private void disconnect() {
        try {
            sendData("ENDING SESSION....");
            connection.close();
            server.close();
        } catch (IOException ex) {
            currentLog("IO Exception during socket and server closure!");
        }
    }

    private void searchAnnouncement(String startdate, String enddate) throws ClassNotFoundException {// pernei 2 imerominies kai vriskei 
        ObjectInputStream in = null;
        Announcement ann = null;
        ArrayList<Announcement> ann_db = new ArrayList<Announcement>();
        try {
            in = new ObjectInputStream(new FileInputStream("database.txt"));
            while ((ann = (Announcement) in.readObject()) != null) {
                ann_db.add(ann); // file to list conversion 
            }
           
        } catch (EOFException ex) {
            System.out.println("End of File reached!!");
        } catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(null, "File Not Found!!", "FileNotFound Exception", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "Error with I/O processes!!", "IO Exception", JOptionPane.ERROR_MESSAGE);
        }

        //string to date 
        //kommati sigkrisis listas me tin nea anakoinosi

       // System.out.println("Stelno dedomena ston client 1");
        int x = 0;
        for (int i = 0; i < ann_db.size(); i++) { // 
           // System.out.println("i" + i + (dateComp(ann_db.get(i).getAnnouncement_date(), startdate)));
          // System.out.println("i" + i + (dateComp(ann_db.get(i).getAnnouncement_date(), enddate)));
            if (((dateComp(ann_db.get(i).getAnnouncement_date(), startdate)) == -1)// an i imerominia pou vale o xristis einai meta tin arxiki
                    && ((dateComp(ann_db.get(i).getAnnouncement_date(), enddate)) == 1)) {// kai prin tin teliki 
                
              //  System.out.println("Stelno dedomena ston client 2 ");
                InformClient(ann_db.get(i).toString() + "\n");
                x++;
            }
        }
        if (x == 0) {
            InformClient("");
        }
//        try {
//            in = new ObjectInputStream(new FileInputStream("database.txt"));
//            while ((ann = (Announcement) in.readObject()) != null) {
//                
//            }
//        } catch (IOException e) { // error during file creation 
//            System.out.println("IO Exception during file creation");
//        }

    }//telos search_announcement

    private void addAnnouncement(Object obj) {
        //  in = new ObjectInputStream(new FileInputStream("database.txt"));
       
        System.out.println("Added Announcement: " + ((Announcement) obj).toString());
        try {
           
            outputFile = new FileOutputStream(new File("database.txt"));// fileoutput stream sto arxeio database.txt
            outputObject = new ObjectOutputStream(outputFile);// objectoutput stream sto parapano filestream 
            outputObject.writeObject(obj);// pernaei to object sto arxeio 
            outputObject.flush();
           

        } catch (IOException e) { // error during file creation 
            System.out.println("IO Exception during file creation");
        }

        if (obj instanceof Announcement) {

            int found = 0;
            for (int i = 0; i < acc_list.size(); i++) {
                if (acc_list.get(i).getUsername().equals(((Announcement) obj).getAnnouncer())) {
                    found = 1;
                    break;
                }

            }
            if (found == 0) {

                String pw = "";
                try { // den doulevei mono me ena try catch
                    try {
                        InformClient("Send Password");//stelnei afto to minima gia na zitisei deutero antikeimeno me ta nea stoixeia kai to password (sti thesi tou username)
                       
                        pw = (String) instream.readObject();
                       
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                Account new_acc = new Account(((Announcement) obj).getAnnouncer(), pw);// an den iparxei idi o xristis stin lista ftiaxnei neo acc kai to vazei stin lista 
                acc_list.add(new_acc);
               
            } else {
                InformClient("No password Needed!");

            }

        }

    }//telos add_announcement

    private void deleteAnnouncement(Object obj) throws ClassNotFoundException, IOException { //delete an announcement 
        
        try {
            outputFile = new FileOutputStream(new File("database.txt"));// fileoutput stream sto arxeio library.txt
            outputObject = new ObjectOutputStream(outputFile);// objectoutput stream sto parapano filestream 
        } catch (IOException e) { // error during file creation 
            System.out.println("IO Exception during file creation");
        }

        // ftiaxno pali tin lista gia na sigkrino kai na entopiso to antikeimeno prokeimenou na ginei svisimo
        ObjectInputStream in = null;
        Announcement ann;
       
        try {
            in = new ObjectInputStream(new FileInputStream("database.txt")); 
            
            while ((ann = (Announcement) in.readObject()) != null) {//dexetai to antikeimeno (2)

                ann_db.add(ann);
            }
           
        } catch (EOFException ex) {
            System.out.println("End of File reached!!");
        } catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(null, "File Not Found!!", "FileNotFound Exception", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "Error with I/O 359  processes!!", "IO Exception", JOptionPane.ERROR_MESSAGE);
        }
        //string to date 
        //kommati sigkrisis listas me tin nea anakoinosi
        int x = 0;
        
        System.out.println(ann_db.size());
        for (int i = 0; i < ann_db.size(); i++) {
           
            // exo valei ston client anti gia description na stelnei ton kodiko otan kanei delete request
            //USERNAME + PASSWORD CHECK  ( AN EINAI ADMIN MPOREI NA KANEI EDIT TA PANTA )
            
            //afto pou stelno einai username , password , description,imerominia anti gia titlos,perigrafi,username,imerominia 
      
            if ((((((Announcement) obj).getAnnouncer()).equals(ann_db.get(i).getAnnouncer())) && ((((Announcement) obj).getDescription()).equals(ann_db.get(i).getAnnouncer())))
                    || (((((Announcement) obj).getAnnouncer()).equals("Admin") && ((((Announcement) obj).getDescription()).equals("Admin"))))) //announcement check 
            {
                if (((((Announcement) obj).getTitle()).equals(ann_db.get(i).getTitle())) // title comp
                        && ((((Announcement) obj).getAnnouncer()).equals(ann_db.get(i).getAnnouncer())) // announcer comp
                        && (dateComp(((Announcement) obj).getAnnouncement_date(), ann_db.get(i).getAnnouncement_date()) == 0))// date comp
                {
                    //UpdateLog("Announcement found: ");
                    InformClient(ann_db.get(i).toString() + " WAS DELETED");// Stelnei apantisi (3)
                    ann_db.remove(i);
                    // adeiazo to arxeio
                    clearTheFile();
                    x++;
                    
                    break;

                }//telos if 
                else{
                 InformClient("");// Stelnei apantisi (3)
                
                }
            }

        }
       
        // ksanapernao tin lista sto arxeio 
        for (int i = 0; i < ann_db.size(); i++) {

            // pernaei tin lista sto arxeio 
            try {
                outputObject.writeObject(ann_db.get(i).toString());// pernaei to object sto arxeio 
                outputObject.flush();// katharizei to buffer 
                //Ksanagrafei tin lista sto arxeio xoris afto pou diagraftike

            } catch (FileNotFoundException ex) { // an den vrei arxeio
                System.out.println("File Not Found!");
            } catch (IOException ex) { // an iparxei io exception
                System.out.println("IO Exception during Issue Injection!");
            }

        }
        if (x == 0) {
            InformClient("");
        }

        //--------------------------------------TEST
        try {
            in = new ObjectInputStream(new FileInputStream("database.txt"));
            while ((ann = (Announcement) in.readObject()) != null) {
               // System.out.println(ann.toString());
            }
        } catch (IOException e) { // error during file creation 
            System.out.println("IO Exception during file creation");
        }

    }

    
    //modify announcement 
    private void modAnnouncement(Object obj) throws ClassNotFoundException, IOException {
        //  in = new ObjectInputStream(new FileInputStream("database.txt"));
        System.out.println("Eftasa 439");
        
      
        
        ObjectInputStream in = null;
        //  Announcement ann;
        Object obj1 = null;
        ann_db.clear();
        ArrayList<Announcement> ann_db = new ArrayList<Announcement>();
        try { // mesa edo kolaei 
           
            in = new ObjectInputStream(new FileInputStream("database.txt"));
            
            while ((obj1 = in.readObject()) != null) { // convert file to list 
               
                if (obj1 instanceof Announcement) {
                   
                    ann_db.add((Announcement) obj1);
                    break;
                }
            }
           

        } catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(null, "File Not Found!! 460", "FileNotFound Exception", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "Error with I/O processes 463!!", "IO Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        for (int i = 0; i < ann_db.size(); i++) {
            
            
           
            
            
            if ((((Announcement) obj).getAnnouncer().equals(ann_db.get(i).getAnnouncer()))
                    && (((Announcement) obj).getTitle().equals(ann_db.get(i).getTitle()))
                    && (((Announcement) obj).getDescription().equals(ann_db.get(i).getDescription()))
                    && (((Announcement) obj).getAnnouncement_date().equals(ann_db.get(i).getAnnouncement_date()))) // // an ola  ta stoixeia matcharoun
            {
                System.out.print("Mpika");
                System.out.println("483 " + ((Announcement) obj).getAnnouncer());
              
                for (int j = 0; j < acc_list.size(); j++) {
                    
                    System.out.println("505 " + acc_list.get(j).getUsername());
                     System.out.println("506 " +((Announcement) obj).getAnnouncer());
                    if (acc_list.get(j).getUsername().equals(((Announcement) obj).getAnnouncer())) {
                        
                        // an iparxei to username tote zitaei kai password 
                        
                        InformClient("Send Password!");//stelnei afto to minima me skopo na steilei o client to neo antikeimeno pou periexei kai to password  ( 3)
                        
                        Object new_obj = null;
                        
                        new_obj = instream.readObject(); //6 Diavazei to antikeimeno 
                         
                        // System.out.println(acc_list.get(j).getPassword());
                      // System.out.println(((Announcement) new_obj).getAnnouncer());
                         
                        if (acc_list.get(j).getPassword().equals(((Announcement) new_obj).getAnnouncer()))// to get announcer edo ousiastika einai password
                        {System.out.println("douleuei");
                            ((Announcement) obj).setTitle(((Announcement) new_obj).getTitle()); //allazei titlo
                            ((Announcement) obj).setDescription(((Announcement) new_obj).getDescription());// allazei perigrafi 
                            ((Announcement) obj).setAnnouncement_date(((Announcement) new_obj).getAnnouncement_date());//allazei tin imerominia stin stigmi pou o xristis patise mofidy 
                            InformClient("Announcement Succesfully modified! " +((Announcement) obj).toString() ); // 7 STELNEI PISO TO ANANEOMENO ANTIKEIMENO  
                            System.out.println(((Announcement) obj).toString());
                        } else {

                            InformClient("Wrong Password!!! ");
                        }
                    } else {
                        // stelnei oti den isxiei etsi oste na stamatisei i diadikasia 
                        InformClient("Send Password!");// The user does not have access to the announcement!!!

                    }

                    // na kano update to arxeio 
                }
            }

            //NA FTIAKSO KAI TO MODIFY 
        }
    }

    private void InformClient(final String message) {//methodos enimerosis tou client 
        try {
            outstream.writeObject(message);
            outstream.flush();
        } catch (IOException ex) {
            UpdateLog("IOException:" + " " + "Message cannot be send");
        }
    }

    private void UpdateLog(final String message) {// methodos pou stelnei dedomena stin othoni tou server 
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                result.append(message + "\n");
            }
        }
        );

    }

    private int dateComp(String date1, String date2) {
        int result = 0;
        String[] datecheck1 = date1.split("-");
        String[] datecheck2 = date2.split("-");
        if (parseInt(datecheck1[2]) != parseInt(datecheck2[2]))//xronia 
        {
            if (parseInt(datecheck1[1]) > parseInt(datecheck2[1]))// i proti imerominia einai pio meta logo xronias
            {
                return -1;
            } else {// i deuteri imerominia einai pio meta logo xronias 
                return 1;
            }
            // return -1;
        } else {// an einai idia i xronia 
            if (parseInt(datecheck1[1]) != parseInt(datecheck2[1]))// an einai diaforetikos o minas 
            {
                if (parseInt(datecheck1[1]) > parseInt(datecheck2[1])) {
                    return -1;
                } else {
                    return 1;
                }
            } else {// an einai idios kai o minas
                if (parseInt(datecheck1[0]) != parseInt(datecheck2[0])) // logo imeras
                {
                    if (parseInt(datecheck1[0]) > parseInt(datecheck2[0])) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return 0;// an einai ola idia tote epistrefei 0 
                }//telos imeras
            }//telos mina   
        }//telos xronou
    }

    public static void clearTheFile() throws IOException {// methodos pou adeiazei to arxeio 
        FileWriter fwOb = new FileWriter("database.txt", false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

}//telos server  class

