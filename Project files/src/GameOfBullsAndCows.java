import java.io.*;
import java.net.*;
import javax.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfBullsAndCows implements ActionListener {

    private JFrame StartWindow;
    private JButton NewGame,HostB, ClientB , EnterIP , EnterPort, Apply , NextNum ,GameNum, GiveUp;
    private JLabel YourNum ,OponentNum, BGStart , BGClient , BGplay ;
    private JPanel StartP , NotePanel ,  ClientSetup , GamePanel;
    private JTextArea log ,log1 , log2;
    private JScrollPane JS, JS1, JS2;

    NumPanelButton buttons[]= new NumPanelButton[10];

    private int ServerPort = 11111;
    private String ServerAddress = "127.0.0.1";
    private SimpleEchoServerThread serv;
    private OneClient oc;

    //False = Host ..... True = Client
    private boolean Player;
    public int ClientNumber=0000;


    public GameOfBullsAndCows(){
        GUI();
    }



    public void GUI(){
        //Start Window
        //====================================================================================



        StartWindow = new JFrame("Guess the number");
        StartWindow.setBounds(0, 0, 300, 200);
        StartWindow.setLocationRelativeTo(null);
        StartWindow.setVisible(true);
        StartWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartWindow.setResizable(false);
        StartWindow.setLayout(null);

        Font font = new Font("Rockwell", Font.PLAIN, 20);
        Font font1 = new Font("Rockwell", Font.PLAIN, 15);
        //Font font = new Font("Times New Roman", Font.PLAIN, 24);


        //Start Panel =========================================================================

        BGStart = new JLabel(new ImageIcon(this.getClass().getResource("Start.jpg")));
        BGStart.setBounds(0,0,300,200);

        StartP = new JPanel();
        StartP.setBounds(0,0,300,200);
        StartP.setLayout(null);




        HostB = new JButton("Host");
        HostB.addActionListener(this);
        HostB.setBounds(25,25,112,100);
        HostB.setContentAreaFilled(false);
        HostB.setFont(font);

        ClientB = new JButton("Client");
        ClientB.addActionListener(this);
        ClientB.setBounds(162,25,112,100);
        ClientB.setContentAreaFilled(false);
        ClientB.setFont(font);


        StartWindow.add(StartP);
        StartP.add(HostB);
        StartP.add(ClientB);
        StartP.add(BGStart);
        StartP.setVisible(true);

        StartWindow.validate();
        StartWindow.repaint();



        //Client IP/Port setup
        //====================================================================================
        ClientSetup = new JPanel();
        ClientSetup.setBackground(Color.blue);
        ClientSetup.setBounds(0,0,301,200);
        ClientSetup.setLayout(null);

        BGClient = new JLabel(new ImageIcon(this.getClass().getResource("ClientMenu.png")));
        BGClient.setBounds(0,0,300,200);

        EnterIP = new JButton("Enter IP");
        EnterIP.addActionListener(this);
        EnterIP.setBounds(15,10,135,70);
        EnterIP.setContentAreaFilled(false);
        EnterIP.setFont(font);

        EnterPort = new JButton("Enter Port");
        EnterPort.addActionListener(this);
        EnterPort.setBounds(15,95,135,70);
        EnterPort.setContentAreaFilled(false);
        EnterPort.setFont(font);

        Apply = new JButton("Apply");
        Apply.addActionListener(this);
        Apply.setBounds(172,10,110,155);
        Apply.setContentAreaFilled(false);
        Apply.setFont(font);

        ClientSetup.add(EnterIP);
        ClientSetup.add(EnterPort);
        ClientSetup.add(Apply);
        ClientSetup.add(BGClient);

        StartWindow.validate();
        StartWindow.repaint();

        //Play Panel
        //====================================================================================



        NotePanel = new JPanel();
        NotePanel.setOpaque(false);
        NotePanel.setBounds(50,50,500,50);
        NotePanel.setLayout(new GridLayout(1,10));
        for(int i=0;i<10;i++){
            buttons[i]=new NumPanelButton();
            buttons[i].setContentAreaFilled(false);
            NotePanel.add(buttons[i]);


        }

        //Status = new JLabel("");
        //Status.setBounds(50, 535, 700 , 50);


        NextNum= new JButton("Enter next number");
        NextNum.addActionListener(this);
        NextNum.setBounds(515,347,170,60);
        NextNum.setContentAreaFilled(false);
        NextNum.setFont(font1);


        GiveUp= new JButton("Give up!");
        GiveUp.addActionListener(this);
        GiveUp.setBounds(515,487,170,60);
        GiveUp.setContentAreaFilled(false);
        GiveUp.setFont(font);

        GameNum= new JButton("Set your number");
        GameNum.addActionListener(this);
        GameNum.setBounds(515,272,170,60);
        GameNum.setContentAreaFilled(false);
        GameNum.setFont(font1);

        NewGame= new JButton("New game");
        NewGame.addActionListener(this);
        NewGame.setBounds(515,562,170,60);
        NewGame.setContentAreaFilled(false);
        NewGame.setFont(font);

        log = new JTextArea("");
        log.setEditable(false);
        log.setOpaque(false);
        log.setBackground(new Color(0,0,0,0));
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JS =new JScrollPane(log);
        JS.setBounds(700,50,450,150);
        JS.setOpaque(false);
        JS.getViewport().setOpaque(false);
        JS.setWheelScrollingEnabled(false);


        log1 = new JTextArea("");
        log1.setEditable(false);
        log1.setOpaque(false);
        log1.setBackground(new Color(0,0,0,0));
        log1.setFont(font1);
        DefaultCaret caret1 = (DefaultCaret)log1.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JS1 = new JScrollPane(log1);
        JS1.setBounds(50, 272, 450 , 350);
        JS1.setOpaque(false);
        JS1.getViewport().setOpaque(false);


        log2 = new JTextArea("");
        log2.setEditable(false);
        log2.setOpaque(false);
        log2.setBackground(new Color(0,0,0,0));
        log2.setFont(font1);
        DefaultCaret caret2 = (DefaultCaret)log2.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);


        JS2 = new JScrollPane(log2);
        JS2.setBounds(700, 272, 450 , 350);
        JS2.setOpaque(false);
        JS2.getViewport().setOpaque(false);

        YourNum = new JLabel();
        YourNum.setText("   Your numbers.                                                                           Your number is: " + ClientNumber);
        YourNum.setBounds(50,252,450,20);

        OponentNum = new JLabel();
        OponentNum.setText("   Your opponents numbers.");
        OponentNum.setBounds(700,252,450,20);

        GamePanel = new JPanel();
        GamePanel.setBounds(0,0,1200,672);
        GamePanel.setLayout(null);

        BGplay = new JLabel(new ImageIcon(this.getClass().getResource("Play2.jpg")));
        BGplay.setBounds(0,0,1200,672);

        GamePanel.add(NotePanel);
        GamePanel.add(NextNum);
        GamePanel.add(GiveUp);
        GamePanel.add(JS1);
        GamePanel.add(JS2);
        GamePanel.add(YourNum);
        GamePanel.add(OponentNum);
        GamePanel.add(JS);
        GamePanel.add(GameNum);
        GamePanel.add(NewGame);
        GamePanel.add(BGplay);
    }


    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        //Opening game as Server Host ==================================================
        if (source == HostB) {
            StartWindow.setBounds(0, 0, 1200, 672);
            StartWindow.setLocationRelativeTo(null);
            StartWindow.remove(StartP);
            StartWindow.add(GamePanel);
            StartWindow.validate();
            StartWindow.repaint();
            //Starting Server ==========================================================
            if (serv == null)
            {
                log.append("Server starting...\n");
                serv = new SimpleEchoServerThread(log, ServerPort);
                serv.start();
                log.append("Server started and listening on port " + ServerPort + "\n");
                System.out.println(ServerAddress + ServerPort);
            }
            else
            if (serv.getState() == Thread.State.RUNNABLE)
            {
                log.append("Server is already running.\n");
            }
            else
            if (serv.getState() == Thread.State.TERMINATED)
            {
                log.append("Server starting...\n");
                serv = new SimpleEchoServerThread(log, ServerPort);
                serv.start();
                log.append("Server started and listening on port " + ServerPort + "\n");
            }
            //Starting host connection====================================================

            if (oc == null)
            {
                log.append("Client starting...\n");
                oc = new OneClient(log2,log, ServerAddress, ServerPort);
                oc.start();
                log.append("Client started.\n");
            }
            else
            if (oc.getState() == Thread.State.RUNNABLE)
            {
                log.append("Client is already running.\n");
            }
            else
            if (oc.getState() == Thread.State.TERMINATED)
            {
                log.append("Client starting...\n");
                oc = new OneClient(log2,log, ServerAddress, ServerPort);
                oc.start();
                log.append("Client started.\n");
            }
            StartWindow.validate();
            StartWindow.repaint();
        }
        //Opening game as Client =========================================================
        else
        if (source == ClientB) {
            StartWindow.setBounds(0, 0, 301, 200);
            StartWindow.setLocationRelativeTo(null);
            StartWindow.remove(StartP);
            StartWindow.add(ClientSetup);
            StartWindow.validate();
            StartWindow.repaint();

        }
        //Client enter host ip ==============================================================
        else
        if (source == EnterIP) {
            String input = JOptionPane.showInputDialog("Set server address:");
            ServerAddress = input;
            StartWindow.validate();
            StartWindow.repaint();
        }
        //Client enter host port =============================================================
        else
        if (source == EnterPort) {
            String input = JOptionPane.showInputDialog("Set server port:");
            try
            {
                ServerPort = Integer.parseInt(input);
                if (!(ServerPort > 0 && ServerPort < 65535))
                    throw new NumberFormatException();
            }
            catch (NumberFormatException ex)
            {
                log.append("Incorrect port value.\n");
            }
            StartWindow.validate();
            StartWindow.repaint();
        }
        //Client connect to Host server/game =================================================
        else
        if (source == Apply) {
            StartWindow.setBounds(0, 0, 1200, 672);
            StartWindow.setLocationRelativeTo(null);
            StartWindow.remove(ClientSetup);
            StartWindow.add(GamePanel);
            //Starting the client ============================================================
            if (oc == null)
            {
                log.append("Client starting...\n");
                oc = new OneClient(log2,log, ServerAddress, ServerPort);
                oc.start();
                log.append("Client started.\n");
            }
            else
            if (oc.getState() == Thread.State.RUNNABLE)
            {
                log.append("Client is already running.\n");
            }
            else
            if (oc.getState() == Thread.State.TERMINATED)
            {
                log.append("Client starting...\n");
                oc = new OneClient(log2,log, ServerAddress, ServerPort);
                oc.start();
                log.append("Client started.\n");
            }
            StartWindow.validate();
            StartWindow.repaint();
        }
        //Setting players number ========================================================
        else
        if(source == GameNum){
            StartWindow.validate();
            StartWindow.repaint();
            if(ClientNumber==0) {
                if (oc != null && (oc.getState() == Thread.State.RUNNABLE)) {
                    boolean p = false;
                    while (p == false) {
                        String input = JOptionPane.showInputDialog("Set your number");
                        try {
                            ClientNumber = Integer.parseInt(input);

                            if (!(ClientNumber > 999 && ClientNumber < 10000)) {
                                throw new NumberFormatException();
                            }
                            int temp[] = new int[4], i = 0;
                            int cl = ClientNumber;
                            while (cl != 0) {
                                temp[i] = cl % 10;
                                cl = cl / 10;
                                i++;
                            }
                            if (temp[0] == 0) {
                                throw new NumberFormatException();
                            }
                            for (i = 0; i < 4; i++) {
                                for (int j = 0; j < 4; j++) {
                                    if (temp[i] == temp[j] && i != j) {
                                        throw new NumberFormatException();
                                    }
                                }
                            }

                            //Status.setText("Your number is: "+ ClientNumber);
                            p = true;
                        } catch (NumberFormatException ex) {
                            StartWindow.validate();
                            StartWindow.repaint();
                            log.append("Enter four digit number," + "\n" + "where all the digits are different numbers"
                                    + "\n" + "and your first number cant be 0.\n");
                            StartWindow.validate();
                            StartWindow.repaint();
                        }
                    }
                    YourNum.setText("   Your numbers.                                                                           Your number is: " + ClientNumber);
                    ProtocolObject po = new ProtocolObject();
                    po.ClientNumber = ClientNumber;
                    oc.send(po);
                }
            }
            else {
                return;}
            StartWindow.validate();
            StartWindow.repaint();
        }
        //Sending users next number =============================================================
        else
        if (source == NextNum)
        {
            Player=GetOponentNum.Player;
            if(Player == true) {
                boolean temp = true;
                while (temp == true) {
                    StartWindow.validate();
                    StartWindow.repaint();
                    try {

                        if (GetOponentNum.OpponentNum != 0 && ClientNumber != 0) {
                            String input = JOptionPane.showInputDialog("Enter your next number:");
                            if (oc != null && (oc.getState() == Thread.State.RUNNABLE)) {

                                int NextNumber = Integer.parseInt(input);

                                ProtocolObject po = new ProtocolObject();
                                int ON[] = new int[4];
                                int NN[] = new int[4];
                                int i = 0, tempON = GetOponentNum.OpponentNum, tempNN = NextNumber;
                                int bull = 0, cow = 0;

                                while (tempON != 0) {
                                    ON[i] = tempON % 10;
                                    tempON = tempON / 10;
                                    i++;
                                }
                                i = 0;
                                while (tempNN != 0) {
                                    NN[i] = tempNN % 10;
                                    tempNN = tempNN / 10;
                                    i++;
                                }
                                for (i = 0; i < 4; i++) {
                                    if (ON[i] == NN[i]) {
                                        bull++;
                                    }
                                    for (int j = 0; j < 4; j++) {
                                        if (NN[i] == NN[j] && i != j) {
                                            throw new NumberFormatException();
                                        }
                                        if (ON[i] == NN[j] && i != j) {
                                            cow++;
                                        }
                                    }
                                }
                                log1.append(("     " + input + "                                                         Bulls: "
                                        + bull + "        Cows: " + cow + "\n"));
                                if (bull == 4) {
                                    po.win=true;
                                }
                                po.cow = cow;
                                po.bull = bull;
                                po.NextNumber = NextNumber;
                                oc.send(po);
                                if (po.win == true) {
                                    log1.append("\n                                       You WIN !!!! :)");
                                }
                            }
                            temp = false;
                            Player= false;
                            GetOponentNum.Player=false;
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        log.append("Both players need to enter game numbers!" + "\n" + "You cant have the same digit twice \n");
                        return;
                    }
                }
            }
            else {return;}
            StartWindow.validate();
            StartWindow.repaint();
        }
        else if (source == GiveUp){
            if (oc != null && (oc.getState() == Thread.State.RUNNABLE))
            {
                ProtocolObject po = new ProtocolObject();
                po.giveUp = true;
                log1.append("\n                               You gave up ! You lost !");
                oc.send(po);
            }


            StartWindow.validate();
            StartWindow.repaint();

        }
        else if (source == NewGame){
            Player=true;
            GetOponentNum.Player=true;
            GetOponentNum.OpponentNum=0;
            ClientNumber=0;
            log1.setText(null);
            log2.setText(null);
            StartWindow.validate();
            StartWindow.repaint();

        }

    }

    public static void main (String[] arhs){
        new GameOfBullsAndCows();
    }
}

class OneClient extends Thread
{
    private ProtocolObject po;
    private Socket sock;
    private String ServerAddress = "localhost";
    private int PortNumber = 11111;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean stop = false;
    private JTextArea log , log2;


    public OneClient(JTextArea l, JTextArea l2, String sa, int pn)
    {
        ServerAddress = sa;
        PortNumber = pn;
        log = l;
        log2 = l2;

    }



    public void run()
    {
        try
        {
            SocketFactory socketFactory = SocketFactory.getDefault();
            sock = socketFactory.createSocket(ServerAddress, PortNumber);
            System.out.println("Connecting...");
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
            System.out.println("Connecting...");
        }
        catch (ConnectException ex)
        {
            ex.printStackTrace();
            halt();
        }
        catch (UnknownHostException ex)
        {
            ex.printStackTrace();
            halt();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            halt();
        }

        while (!stop)
        {
            try
            {
                po = (ProtocolObject)ois.readObject();
                if(GetOponentNum.OpponentNum==0000)
                {
                    GetOponentNum.OpponentNum= po.ClientNumber;
                }
                if(!(po.NextNumber==0)) {
                    log.append("     "+ po.NextNumber + "                                                         Bulls: "
                            + po.bull + "        Cows: " + po.cow + "\n");
                }

                GetOponentNum.Player=true;

                if(po.win==true){
                    log.append("\n                                          You lost!");
                }
                if(po.giveUp==true){
                    log.append("\n                          Your opponent gave up! You WIN !!!! :)");
                }


            }
            catch (EOFException ex)
            {
                ex.printStackTrace();
                halt();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                halt();
            }
            catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
                halt();
            }

        }
    }


    public void send(ProtocolObject data)
    {
        try
        {
            oos.writeObject(data);
            oos.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            halt();
        }
    }


    public void halt()
    {
        try
        {
            if (ois != null)
                ois.close();
            if (oos != null)
                oos.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        stop = true;
    }
}


class SimpleEchoServerThread extends Thread
{
    private ServerSocket MyService;

    private Socket clientSocket;
    private volatile boolean stop = false;
    private SocketAddress sa;
    private int port = 11111;


    private ServerSocketFactory ssocketFactory;
    private ServerSocket ssocket;
    private Vector<HandleClient> clients;

    private final int MaxClients = 2;
    private JTextArea log;

    public SimpleEchoServerThread(JTextArea l, int p)
    {
        clients = new Vector<HandleClient>();
        port = p;
        log = l;
    }


    public synchronized Vector<HandleClient> getClients()
    {
        return clients;
    }

    public int getMaxClients()
    {
        return MaxClients;
    }


    public synchronized void addClient(HandleClient hc)
    {
        log.append("Client connected.\n");
        clients.addElement(hc);
    }


    public synchronized void removeClient(HandleClient hc)
    {
        log.append("Client disconnected.\n");
        clients.removeElement(hc);
    }


    public synchronized int getClientNumber()
    {
        return clients.size();
    }

    public void initServer()
    {
        try
        {
            MyService = new ServerSocket(port, 20);
        }
        catch (BindException ex)
        {
            System.out.println("The specified port is already in use.");
            System.exit(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        while (!stop)
        {
            clientSocket = null;
            try
            {
                clientSocket = MyService.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (clientSocket != null)
            {
                HandleClient hc = new HandleClient(clientSocket, this);
                hc.start();
                log.append(hc + " connected.\n");
            }
        }
    }


    public void halt()
    {
        try
        {
            MyService.close();
        }
        catch (IOException ex)
        {
        }
        stop = true;
    }

    public void run()
    {
        initServer();
    }
}



class HandleClient extends Thread
{
    private Socket mySocket;
    private InetSocketAddress sa;
    private InetAddress isa;
    private SimpleEchoServerThread Srv;
    private volatile boolean stop = false;
    private InputStream input;
    private OutputStream output;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ProtocolObject po;
    private ProtocolObject temp;

    public HandleClient(Socket clientSocket, SimpleEchoServerThread srv)
    {
        mySocket = clientSocket;
        Srv = srv;
        po = new ProtocolObject();
    }

    public void run()
    {
        try
        {
            sa = (InetSocketAddress)mySocket.getRemoteSocketAddress();
            isa = sa.getAddress();
            System.out.println(isa); //интересна информация.
            output = mySocket.getOutputStream();
            input = mySocket.getInputStream();
            ois = new ObjectInputStream(input);
            oos = new ObjectOutputStream(output);
        }
        catch (StreamCorruptedException ex)
        {
            System.out.println(ex.getMessage());
            halt();
        }
        catch (SocketException ex)
        {
            ex.printStackTrace();
            halt();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            halt();
        }

        System.out.println("Srv.getClientNumber() " + Srv.getClientNumber() + " Srv.getMaxClients() " + Srv.getMaxClients());

        if (Srv.getClientNumber() > Srv.getMaxClients())
        {
            po.message = "Server is full.";
            try
            {
                oos.writeObject(po);
                oos.flush();
            }
            catch (IOException ex)
            {
            }
            halt();
        }
        else
        {
            Srv.addClient(this);
        }

        while (!stop)
        {
            try
            {

                temp = (ProtocolObject)ois.readObject();
                for (int i = 0; i < Srv.getClients().size(); i++)
                {
                    if (this != Srv.getClients().get(i))
                    {
                        Srv.getClients().get(i).send(temp);

                    }
                }
            }
            catch (ClassNotFoundException ce)
            {
                ce.printStackTrace();
                halt();
                continue;
            }
            catch (EOFException ex)
            {
                ex.printStackTrace();
                halt();
                continue;
            }
            catch (IOException ex2)
            {
                ex2.printStackTrace();
                halt();
                continue;
            }
        }
    }

    public void send(ProtocolObject data)
    {
        try
        {
            oos.writeObject(data);
            oos.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            halt();
        }
    }

    public void halt()
    {

        Srv.removeClient(this);
        try
        {
            stop = true;

            if (ois != null)
                ois.close();

            if (oos != null)
                oos.close();

            if (input != null)
                input.close();

            if (output != null)
                output.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

class ProtocolObject
        implements Serializable
{
    public int cow, bull;
    public int NextNumber;
    public int ClientNumber;
    public boolean win=false, giveUp=false;
    public String message = "";
}







