import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Arrays;
/**
 * This class provides a GUI for solitaire games related to Elevens.
 */
public class CardGameGUI extends JFrame implements ActionListener {

    /** Height of the game frame. */
    private static final int DEFAULT_HEIGHT = 302;
    /** Width of the game frame. */
    private static final int DEFAULT_WIDTH = 800;
    /** Width of a card. */
    private static final int CARD_WIDTH = 73;
    /** Height of a card. */
    private static final int CARD_HEIGHT = 97;
    /** Row (y coord) of the upper left corner of the first card. */
    private static final int LAYOUT_TOP = 30;
    /** Column (x coord) of the upper left corner of the first card. */
    private static final int LAYOUT_LEFT = 30;
    /** Distance between the upper left x coords of
     *  two horizonally adjacent cards. */
    private static final int LAYOUT_WIDTH_INC = 100;
    /** Distance between the upper left y coords of
     *  two vertically adjacent cards. */
    private static final int LAYOUT_HEIGHT_INC = 125;
    /** y coord of the "Replace" button. */
    private static final int BUTTON_TOP = 30;
    /** x coord of the "Replace" button. */
    private static final int BUTTON_LEFT = 570;
    /** Distance between the tops of the "Replace" and "Restart" buttons. */
    private static final int BUTTON_HEIGHT_INC = 50;
    /** y coord of the "n undealt cards remain" label. */
    private static final int LABEL_TOP = 160;
    /** x coord of the "n undealt cards remain" label. */
    private static final int LABEL_LEFT = 540;
    /** Distance between the tops of the "n undealt cards" and
     *  the "You lose/win" labels. */
    private static final int LABEL_HEIGHT_INC = 35;

    /** The board (Board subclass). */
    private Board board;

    /** The main panel containing the game components. */
    private JPanel panel;
    /**The Switch Button */
    private JButton switchButton;
    /** The Replace button. */
    private JButton replaceButton;
    /** The Restart button. */
    private JButton restartButton;
    /** The "number of undealt cards remain" message. */
    private JLabel statusMsg;
    /** The "you've won n out of m games" message. */
    private JLabel totalsMsg;
    /** The card displays. */
    private JLabel[] displayCards;
    /** The win message. */
    private JLabel winMsg;
    /** The loss message. */
    private JLabel lossMsg;
    /** The coordinates of the card displays. */
    private Point[] cardCoords;

    /** kth element is true iff the user has selected card #k. */
    private boolean[] selections;
    /** The number of games won. */
    private int totalWins;
    /** The number of games played. */
    private int totalGames;

    /**counter for switching the mouseclick*/
    int counter = 0;
    /**counter for checking if switch button is toggled*/
    int cinter = 0;
    /**temp variables for switchButton*/
    int switch1 = 0;
    int switch2 = 0;
    /** toggle for switching the switchButton*/
    boolean toggle = true;
    
    ArrayList<Integer> swotch = new ArrayList<Integer>();
    ArrayList<Integer> arrayCounter = new ArrayList<Integer>();
    
    ArrayList<Integer> arrayCounterForCounter = new ArrayList<Integer>();
    
    boolean[] trueorfalse = new boolean[8];
    
    /**
     * Initialize the GUI.
     * @param gameBoard is a <code>Board</code> subclass.
     */
    public CardGameGUI(Board gameBoard) {
        board = gameBoard;
        totalWins = 0;
        totalGames = 0;

        // Initialize cardCoords using 5 cards per row
        cardCoords = new Point[board.size()];
        int x = LAYOUT_LEFT;
        int y = LAYOUT_TOP;
        for (int i = 0; i < cardCoords.length; i++) {
            cardCoords[i] = new Point(x, y);
            // bk 5 to 4
            //if (i % 5 == 4) {
                if (i % 4 == 3) {
                x = LAYOUT_LEFT;
                y += LAYOUT_HEIGHT_INC;
            } else {
                x += LAYOUT_WIDTH_INC;
            }
        }
        
        
        selections = new boolean[board.size()];
        initDisplay();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }
    /** adding stuff to this arrayCounter */
    
    
    /**
     * Run the game.
     */
    public void displayGame() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    /**
     * Draw the display (cards and messages).
     */
    public void repaint() {
        for (int k = 0; k < board.size(); k++) {
            String cardImageFileName =
                imageFileName(board.cardAt(k), selections[k]);
            URL imageURL = getClass().getResource(cardImageFileName);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                displayCards[k].setIcon(icon);
                displayCards[k].setVisible(true);
            } else {
                throw new RuntimeException(
                    "Card image not found: \"" + cardImageFileName + "\"");
            }
        }
        statusMsg.setText(board.deckSize()
            + " undealt cards remain.");
        statusMsg.setVisible(true);
        totalsMsg.setText("You've won " + totalWins
             + " out of " + totalGames + " games.");
        totalsMsg.setVisible(true);
        pack();
        panel.repaint();
    }

    /**
     * Initialize the display.
     */
    private void initDisplay()  {
        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        // If board object's class name follows the standard format
        // of ...Board or ...board, use the prefix for the JFrame title
        String className = board.getClass().getSimpleName();
        int classNameLen = className.length();
        int boardLen = "Board".length();
        String boardStr = className.substring(classNameLen - boardLen);
        if (boardStr.equals("Board") || boardStr.equals("board")) {
            int titleLength = classNameLen - boardLen;
            setTitle(className.substring(0, titleLength));
        }

        // Calculate number of rows of cards (5 cards per row)
        // and adjust JFrame height if necessary
        int numCardRows = (board.size() + 4) / 5;
        int height = DEFAULT_HEIGHT;
        if (numCardRows > 2) {
            height += (numCardRows - 2) * LAYOUT_HEIGHT_INC;
        }

        this.setSize(new Dimension(DEFAULT_WIDTH, height));
        panel.setLayout(null);
        panel.setPreferredSize(
            new Dimension(DEFAULT_WIDTH - 20, height - 20));
        displayCards = new JLabel[board.size()];
        for (int k = 0; k < board.size(); k++) {
            displayCards[k] = new JLabel();
            panel.add(displayCards[k]);
            displayCards[k].setBounds(cardCoords[k].x, cardCoords[k].y,
                                        CARD_WIDTH, CARD_HEIGHT);
            displayCards[k].addMouseListener(new MyMouseListener());
            // bk - all but card 4 (starts at 0)  set to originally true - originally all false
            //selections[k] = false;
            selections[k] = true;
            if (k==3|| k == 6 || k ==4 || k == 7 ) {selections[k] = false;}
        }
        replaceButton = new JButton();
        replaceButton.setText("Draw");
        panel.add(replaceButton);
        replaceButton.setBounds(BUTTON_LEFT, BUTTON_TOP, 100, 30);
        replaceButton.addActionListener(this);

        restartButton = new JButton();
        restartButton.setText("Restart");
        panel.add(restartButton);
        restartButton.setBounds(BUTTON_LEFT, BUTTON_TOP + BUTTON_HEIGHT_INC,100, 30);
        restartButton.addActionListener(this);

        switchButton = new JButton();
        switchButton.setText("Switch");
        panel.add(switchButton);
        switchButton.setBounds(BUTTON_LEFT, BUTTON_TOP + 2 * BUTTON_HEIGHT_INC,100, 30);
        switchButton.addActionListener(this);
        
        statusMsg = new JLabel(
            board.deckSize() + " undealt cards remain.");
        panel.add(statusMsg);
        statusMsg.setBounds(LABEL_LEFT, LABEL_TOP, 250, 30);

        winMsg = new JLabel();
        winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
        winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
        winMsg.setForeground(Color.GREEN);
        winMsg.setText("You win!");
        panel.add(winMsg);
        winMsg.setVisible(false);

        lossMsg = new JLabel();
        lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
        lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
        lossMsg.setForeground(Color.RED);
        lossMsg.setText("End of Game.");
        panel.add(lossMsg);
        lossMsg.setVisible(false);

        totalsMsg = new JLabel("You've won " + totalWins
            + " out of " + totalGames + " games.");
        totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + 2 * LABEL_HEIGHT_INC,
                                  250, 30);
        panel.add(totalsMsg);

        if (!board.anotherPlayIsPossible()) {
            signalLoss();
        }

        pack();
        getContentPane().add(panel);
        getRootPane().setDefaultButton(replaceButton);
        panel.setVisible(true);
    }

    /**
     * Deal with the user clicking on something other than a button or a card.
     */
    private void signalError() {
        Toolkit t = panel.getToolkit();
        t.beep();
    }

    /**
     * Returns the image that corresponds to the input card.
     * Image names have the format "[Rank][Suit].GIF" or "[Rank][Suit]S.GIF",
     * for example "aceclubs.GIF" or "8heartsS.GIF". The "S" indicates that
     * the card is selected.
     *
     * @param c Card to get the image for
     * @param isSelected flag that indicates if the card is selected
     * @return String representation of the image
     */
    private String imageFileName(Card c, boolean isSelected) {
        String str = "cards/";
        if (c == null) {
            return "cards/back1.GIF";
        }
        str += c.rank() + c.suit();
        if(!isSelected){
            System.out.println(c.toString());    
        }
        if (isSelected) {
          return "cards/back1.GIF";
          //str += "S";
        }
        str += ".GIF";
        return str;
    }
        
    /**
     * Respond to a button click (on either the "Replace" button
     * or the "Restart" button).
     * @param e the button click action event
     */
    
    public void actionPerformed(ActionEvent e) {
        for(int i =0; i<8; i++){
                arrayCounter.add(i);
                System.out.println(arrayCounter.get(i));
        }
        for(int i =0; i<8; i++){
                arrayCounterForCounter.add(i);
                System.out.println(arrayCounter.get(i));
        }
        if (e.getSource().equals(replaceButton)) {
            // Gather all the selected cards.
            board.deal(arrayCounter.get(arrayCounterForCounter.get(3)));
            System.out.println(arrayCounter.get(3));
            repaint();
        } else if (e.getSource().equals(restartButton)) {
            Collections.sort(arrayCounter);
            trueorfalse[0] = true;
            trueorfalse[1] = true;
            trueorfalse[2] = true;
            trueorfalse[3] = false;
            trueorfalse[4] = false;
            trueorfalse[5] = true;
            trueorfalse[6] = false;
            trueorfalse[7] = false;
            board.newGame();
            for(int i = 0; i< selections.length; i++){
                if(selections[arrayCounter.indexOf(i)] != trueorfalse[i]){
                    selections[i] = !selections[i];
                }
            }
            counter = 0;
            cinter = 0;
            switch1 = 0;
            switch2 = 0;
            repaint();
                } else if (e.getSource().equals(switchButton)) {
                    toggle = !toggle;
                    if(toggle == true) {
                        switchButton.setText("Switch");
                        //Collections.swap(arrayCounter,switch1, switch2);
                        
                        JLabel temp = displayCards[switch1];
                        displayCards[switch1] = displayCards[switch2];
                        displayCards[switch2] = temp;
                        
                        boolean temprrr = selections[switch1];
                        selections[switch1] = selections[switch2];
                        selections[switch2] = temprrr;
                        
                        repaint();
                        Collections.sort(arrayCounter);
                        for(int i = 0; i <arrayCounter.size(); i++){
                            System.out.println(arrayCounter.get(i));
                        }
                    } else {
                        switchButton.setText("Switching");
                        switch1 = 0;
                        switch2 = 0;
                        cinter = 0;
                    }
                    
                }
            }
        
    /**
     * Display a win.
     */
    private void signalWin() {
        getRootPane().setDefaultButton(restartButton);
        winMsg.setVisible(true);
        totalWins++;
        totalGames++;
    }

    /**
     * Display a loss.
     */
    private void signalLoss() {
        getRootPane().setDefaultButton(restartButton);
        lossMsg.setVisible(true);
        totalGames++;
    }

    /**
     * Receives and handles mouse clicks.  Other mouse events are ignored.
     */
    private class MyMouseListener implements MouseListener {

        /**
         * Handle a mouse click on a card by toggling its "selected" property.
         * Each card is represented as a label.
         * @param e the mouse event.
         */
        public void mouseClicked(MouseEvent e) {
            for (int k = 0; k < board.size(); k++) {
                if (e.getSource().equals(displayCards[k])
                        && board.cardAt(k) != null && toggle == true) {
                    if((counter < 2) && (selections[k] == true)) {
                        selections[k] = !selections[k];
                        counter++;
                    }
                    repaint();
                    return;
                } else if (e.getSource().equals(displayCards[k]) && toggle == false) {
                    if(cinter % 2 == 0) {
                        switch1 = k;
                    } else {
                        switch2 = k;
                    }
                    repaint();
                    cinter++;
                }
                    
            }
            signalError();
    }

        /**
         * Ignore a mouse exited event.
         * @param e the mouse event.
         */
        public void mouseExited(MouseEvent e) {
        }

        /**
         * Ignore a mouse released event.
         * @param e the mouse event.
         */
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * Ignore a mouse entered event.
         * @param e the mouse event.
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Ignore a mouse pressed event.
         * @param e the mouse event.
         */
        public void mousePressed(MouseEvent e) {
        }
    }
}