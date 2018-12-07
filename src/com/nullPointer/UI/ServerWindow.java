package com.nullPointer.UI;

import com.nullPointer.Domain.Controller.CommunicationController;
import com.nullPointer.Domain.Controller.PlayerController;
import com.nullPointer.Domain.Model.GameEngine;
import com.nullPointer.Domain.Model.Player;
import com.nullPointer.Domain.Observer;
import com.nullPointer.Domain.Server.ServerInfo;
import com.nullPointer.Utils.ColorSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Random;


public class ServerWindow extends JPanel implements Observer {
    private JButton startGame;
    private JButton addPlayer;
    private JButton quitServer;
    private CommunicationController communicationController = CommunicationController.getInstance();
    private PlayerController playerController = PlayerController.getInstance();
    private GameEngine gameEngine = GameEngine.getInstance();
    private ServerInfo serverInfo = ServerInfo.getInstance();
    private Navigator navigator = Navigator.getInstance();
    private JPanel buttonPanel, playerPanel, cPanel;
    private JScrollPane scrollPane;
    private JTextField textField;
    private List<ClientDisplay> clientDisplayList;
    private ArrayList<CustomButton> bList = new ArrayList<CustomButton>();
    private int buttonHeight = 40;
    private int buttonWidth = 180;

    private int pButtonHeight = 50;
    private int pButtonWidth = 200;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Image background;
    private File backgroundSrc = new File("./assets/background2.jpg");
    JLabel back;

    public ServerWindow() {

        buttonPanel = new JPanel();
        //buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        //buttonPanel.add(new JLabel("Server Screen"));
        buttonPanel.setPreferredSize(new Dimension(buttonWidth, 4 * buttonHeight));
        buttonPanel.setOpaque(false);
        this.add(buttonPanel);
        addButtons(buttonPanel);

        gameEngine.subscribe(this);

        try {
            background = ImageIO.read(backgroundSrc);
            background = background.getScaledInstance(
                    screenSize.width,
                    screenSize.height,
                    Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createClientDisplay();
        createPlayerDisplay();

        ImageIcon backgroundIcon = new ImageIcon(background);
        back = new JLabel();
        back.setIcon(backgroundIcon);
        add(back);
        this.setOpaque(false);

    }

    private void addButtons(JPanel panel) {

        startGame = new CustomButton("Start Game");
        startGame.setToolTipText("Start the game ");
        startGame.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
                //navigator.gameScreen();
            }
        });
        panel.add(startGame);

//        addPlayer = new CustomButton("Add player");
//        addPlayer.setToolTipText("add new player ");
//        addPlayer.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
//        addPlayer.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                Player player = new Player("Test", serverInfo.getClientID());
//                communicationController.sendClientMessage(player);
//                //navigator.gameScreen();
//            }
//        });
//        panel.add(addPlayer);

        quitServer = new CustomButton("Quit Server ");
        quitServer.setToolTipText("Quit from the server");
        quitServer.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        quitServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                communicationController.removeClient();
                navigator.menuScreen();
            }
        });
        panel.add(quitServer);

    }

    private void startServer() {
        communicationController.sendClientMessage("game/start");
    }

//    public void createPlayerCreationDisplay() {
//        pCreation = new PlayerCreationDisplay();
//        pCreation.setPreferredSize(new Dimension(200,200));
//        this.add(pCreation);
//    }

    public List<ClientDisplay> createClientDisplay() {
        List<Integer> clientList = serverInfo.getClientList();
        clientDisplayList = new ArrayList<>();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        for (int i = 0; i < clientList.size(); i++) {
            if (i < 6) {
                ClientDisplay clientDisplay = new ClientDisplay("Computer " + (i + 1), new Point(50, i * height / 6), ColorSet.getPlayerColors().get(i));
                clientDisplayList.add(clientDisplay);
            } else {
                ClientDisplay clientDisplay = new ClientDisplay("Computer " + (i + 1), new Point(400, (i - 6) * height / 6), ColorSet.getPlayerColors().get(i));
                clientDisplayList.add(clientDisplay);
            }
        }
        return clientDisplayList;
    }

    public void addClient() {
        createClientDisplay();
    }

    public void createPlayerDisplay() {
        int panelWidth = pButtonWidth + 15;
        int panelHeight = 6 * (pButtonHeight + 5);
        playerPanel = new JPanel();
        //playerPanel.set (new Dimension(pButtonWidth + 30,12 * (pButtonHeight + 10) ));
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBackground(ColorSet.serverWindowLightBackground);
        //playerPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        cPanel = new JPanel();
        cPanel.setPreferredSize(new Dimension(panelWidth, 100));
        //cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.setOpaque(false);
        scrollPane = new JScrollPane(playerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(panelWidth, panelHeight));
        textField = new JTextField("Enter player name here!");
        textField.setPreferredSize(new Dimension(230, 50));
        textField.setFont(new Font("Corbel", Font.PLAIN, 15));
        textField.setBackground(ColorSet.serverWindowLightBackground);
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                textField.setText("");
            }
        });
        addPlayer = new CustomButton("Add Player");
        addPlayer.setToolTipText("Press to add your player!");
        addPlayer.setPreferredSize(new Dimension(230, buttonHeight));
        addPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player player = new Player(textField.getText(), serverInfo.getClientID());
                communicationController.sendClientMessage(player);
                //navigator.gameScreen();
                textField.setText("Enter player name here!");
            }
        });
        cPanel.add(textField);
        cPanel.add(addPlayer);
        this.add(scrollPane);
        this.add(cPanel);
    }

    public void updateButtonColor() {
        playerPanel.removeAll();
        playerPanel.validate();
        repaint();
        ArrayList<Player> pList = PlayerController.getInstance().getPlayers();
        for (Player player : pList) {
            addPlayerButton(player);
        }
        playerPanel.validate();
        repaint();
    }

    public void addPlayerButton(Player player) {
        List<Integer> clientList = serverInfo.getClientList();
        CustomButton newButton = new CustomButton(player.getName());
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              player.setClientID(serverInfo.getClientID());
              communicationController.sendClientMessage(PlayerController.getInstance());
            }
        });
        newButton.setPrimaryColor(ColorSet.getPlayerColors().get(clientList.indexOf(player.getClientID())));
        newButton.setPreferredSize(new Dimension(pButtonWidth, pButtonHeight));
        newButton.setMaximumSize(new Dimension(pButtonWidth, pButtonHeight));
        newButton.setMinimumSize(new Dimension(pButtonWidth, pButtonHeight));
        bList.add(newButton);

        playerPanel.add(newButton);
        playerPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void addPlayer() {
        ArrayList<Player> pList = playerController.getPlayers();
        if (pList.size() > 0) {
            Player player = pList.get(pList.size() - 1);
            addPlayerButton(player);
        }
    }

    public void addOtherPlayers() {
        if (bList.size() == 0) {
            ArrayList<Player> pList = playerController.getPlayers();
            for (Player player : pList) {
                addPlayerButton(player);
            }
        }
    }

    public void paint(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.paint(g);
        //g.drawImage(background, 0, 0, null);
        back.setLocation(0, 0);
        clientDisplayList.forEach(clientDisplay -> clientDisplay.paint(g));
        //bList.forEach(customButton -> customButton.paint(g));
        buttonPanel.setLocation((screenSize.width - buttonPanel.getWidth()) / 2, 200);
        scrollPane.setLocation((screenSize.width) / 4 * 3, 100);
        cPanel.setLocation((screenSize.width) / 4 * 3, scrollPane.getHeight() + 100);
    }

    @Override
    public void onEvent(String message) {
        if (message.equals("newClient")) {
            this.addClient();
            repaint();
        } else if (message.equals("newPlayer")) {
            addPlayer();
        } else if (message.equals("refreshPlayerDisplay")) {
            //playerController = PlayerController.getInstance();
            //addOtherPlayers();
            updateButtonColor();
            repaint();
        }
    }
}

class ClientDisplay {

    String clientName;
    Point position;
    int width = 300;
    int height = 100;
    Random rand = new Random();
    Color clientColor;

    ClientDisplay(String name, Point position, Color color) {
        clientName = name;
        this.position = position;
        clientColor = color;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(clientColor);
        g2.setFont(new Font("Corbel", Font.PLAIN, 20));
        g2.drawString(clientName, position.x + 100, position.y + height / 2);
        g2.setStroke(new BasicStroke(2.0F));
        g2.drawRect(position.x, position.y, width, height);
    }
}