package freecell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;

class UICardPanel extends JComponent implements MouseListener, MouseMotionListener, ChangeListener
{
    // Constant Variables
    private static final int NUMBER_OF_PILES = 8;
 
    // Constant Variables specifying position of display elements
    private static final int GAP = 10;
    private static final int FOUNDATION_TOP = GAP;
    private static final int FOUNDATION_BOTTOM = FOUNDATION_TOP + Card.CARD_HEIGHT;
    private static final int FREE_CELL_TOP = GAP + FOUNDATION_TOP;
    private static final int FREE_CELL_BOTTOM = FREE_CELL_TOP + Card.CARD_HEIGHT;
    private static final int TABLEAU_TOP = 2 * GAP + Math.max(FOUNDATION_BOTTOM, FREE_CELL_BOTTOM);
    private static final int TABLEAU_INCR_Y  = 15;
    private static final int TABLEAU_START_X = GAP;
    private static final int TABLEAU_INCR_X  = Card.CARD_WIDTH + GAP;
    
    private static final int DISPLAY_WIDTH = GAP + NUMBER_OF_PILES * TABLEAU_INCR_X;
    private static final int DISPLAY_HEIGHT = TABLEAU_TOP + 3 * Card.CARD_HEIGHT + GAP;
    private static final Color BACKGROUND_COLOR = new Color(0, 200, 0);
       
    //Default coordinates for mouse (x,y)
    private int _initX     = 0;
    private int _initY     = 0;
    
    //Default coordinates when dragging mouse (x,y)
    private int _dragFromX = 0;
    private int _dragFromY = 0;
    
    //If a card is selected, checking the card and the card pile when dragged
    private Card     _draggedCard = null;
    private CardPile _draggedFromPile = null;
    
    // Stores location of the card pile
    private IdentityHashMap<CardPile, Rectangle> _whereIs = new IdentityHashMap<CardPile, Rectangle>();
    
    private boolean _autoComplete = false;
    private GameModel _model;
    

    UICardPanel(GameModel model) {
        _model = model;
        
        // Set the width and height of the window of the application
        setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        //Set the background colour of the application
        setBackground(Color.blue);
        
        // Initialise mouse and mouse motion listeners to track mouse
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        // Initialise the positions of each of the card piles
        int x = TABLEAU_START_X;   // Initial x position.
        for (int pileNum = 0; pileNum < NUMBER_OF_PILES; pileNum++)
        {
            CardPile p;
            if (pileNum < 4)
            {
                p = _model.getFreeCellPile(pileNum);
                _whereIs.put(p, new Rectangle(x, FREE_CELL_TOP, Card.CARD_WIDTH, Card.CARD_HEIGHT));
            } 
            else 
            {
                p = _model.getFoundationPile(pileNum - 4);
                _whereIs.put(p, new Rectangle(x, FOUNDATION_TOP, Card.CARD_WIDTH, Card.CARD_HEIGHT));
            }
            p = _model.getTableauPile(pileNum);
            _whereIs.put(p, new Rectangle(x, TABLEAU_TOP, Card.CARD_WIDTH, 3 * Card.CARD_HEIGHT));
            
            x += TABLEAU_INCR_X;
        }
        _model.addChangeListener(this);
    }
    
    
    //Draw the cards onto the application
    public void paintComponent(Graphics g)
    {
    	// Get the set width and height
        int width  = getWidth();
        int height = getHeight();
        
        // Paint the background with set colour 
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        
        // Draw each of the card piles
        for (CardPile pile : _model.getFreeCellPiles())
        {
            _drawPile(g, pile, true);
        }
        for (CardPile pile : _model.getFoundationPiles())
        {
            _drawPile(g, pile, true);
        }
        for (CardPile pile : _model.getTableauPiles())
        {
            _drawPile(g, pile, false);
        }
        
        // Draw the card being dragged
        if (_draggedCard != null)
        {
            _draggedCard.draw(g);
        }
    }
    
    //Drawing the card pile
    private void _drawPile(Graphics g, CardPile pile, boolean topOnly)
    {
        Rectangle loc = _whereIs.get(pile);
        g.drawRect(loc.x, loc.y, loc.width, loc.height);
        int y = loc.y;
        if (pile.size() > 0)
        {
            if (topOnly)
            {
                Card card = pile.peekTop();
                if (card != _draggedCard)
                {
                    card.setPosition(loc.x, y);
                    card.draw(g);
                }
            }
            else
            {
                // Draw all the cards in card piles
                for (Card card : pile)
                {
                    if (card != _draggedCard)
                    {
                        card.setPosition(loc.x, y);
                        card.draw(g);
                        y += TABLEAU_INCR_Y;
                    }
                }
            }
        }
    }
    
    //When the mouse is clicked
    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();   // Get the current x coordinate of the mouse click
        int y = e.getY();   // Get the current y coordinate of the mouse click
        
        //Check the top of every each card pile
        _draggedCard = null;
        for (CardPile pile : _model)
        {
            if (pile.isRemovable() && pile.size() > 0)
            {
                Card testCard = pile.peekTop();
                if (testCard.isInside(x, y))
                {
                    _dragFromX = x - testCard.getX();  // Check the X coordinate of the card
                    _dragFromY = y - testCard.getY();  // Check the Y coordinate of the card
                    _draggedCard = testCard;  // Card being dragged
                    _draggedFromPile = pile;
                    break;   // When card found escape
                }
            }
        }
    }
    
    //Repaint when a change happens
    public void stateChanged(ChangeEvent e)
    {
        _clearDrag();
        this.repaint(); 
    }
    
    // For the user interface
    void setAutoCompletion(boolean autoComplete)
    {
        _autoComplete = autoComplete;
    }
    

    // Set the X coordinates and Y coordinates to mouse position and once finished then repaint due to new positioning
    public void mouseDragged(MouseEvent e)
    {
        if (_draggedCard == null)
        {
            return;
        }
        int newX;
        int newY;
       
        newX = e.getX() - _dragFromX;
        newY = e.getY() - _dragFromY;
        
        // Disallow movement of image from left or right of application
        newX = Math.max(newX, 0);
        newX = Math.min(newX, getWidth() - Card.CARD_WIDTH);
        
        // Disallow movement of image from top or bottom of application
        newY = Math.max(newY, 0);
        newY = Math.min(newY, getHeight() - Card.CARD_HEIGHT);
        
        _draggedCard.setPosition(newX, newY);
        this.repaint();
    }
    
    //When the mouse is released/dragged
    public void mouseReleased(MouseEvent e)
    {
        if (_draggedFromPile != null)
        {
            int x = e.getX();
            int y = e.getY();
            CardPile targetPile = _findPileAt(x, y);
            if (targetPile != null)
            {
                // Allow movement of card
                _model.moveFromPileToPile(_draggedFromPile, targetPile);
                if (_autoComplete)
                {
                    // Check if a card can be moved into a foundation pile
                    _model.makeAllPlays();
                }
            }
            _clearDrag();
            this.repaint();
        }
    }
    
    // After dragging a card, to set the default values to null
    private void _clearDrag()
    {
        _draggedCard = null;
        _draggedFromPile = null;
    }
    
    // Find the card in the pile
    private CardPile _findPileAt(int x, int y)
    {
        for (CardPile pile : _model)
        {
            Rectangle loc = _whereIs.get(pile);
            if (loc.contains(x, y))
            {
                return pile;
            }
        }
        return null;
    }
    
    //Other mouse listener events
    public void mouseMoved  (MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {;}
}
