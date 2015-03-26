package freecell;

import java.awt.*;
import javax.swing.*;

public class Card
{
    //Constant Variables
    private static final Class CLASS = Card.class;
    private static final String PACKAGE_NAME;
    private static final ClassLoader CLSLDR;	
    
    public  static final int CARD_HEIGHT; // Fixed height for Card	
    public  static final int CARD_WIDTH;  // Fixed Width for Card

    private static final String    IMAGE_PATH = "/cardimages/"; //Path for images for Cards
    private static final ImageIcon BACK_IMAGE;  // The back image of Card
    
    //static Variables
    static
    {   	
        PACKAGE_NAME = CLASS.getPackage().getName();
        CLSLDR = CLASS.getClassLoader();
        String urlPath = PACKAGE_NAME + IMAGE_PATH + "b.gif"; //Default Image
        java.net.URL imageURL = CLSLDR.getResource(urlPath);
        BACK_IMAGE = new ImageIcon(imageURL);
        
        CARD_WIDTH  = BACK_IMAGE.getIconWidth(); // Fixed Width for back of Card
        CARD_HEIGHT = BACK_IMAGE.getIconHeight(); // Fixed height for back Card	
    }
    
    //Instance Variables
    private int     _x;
    private int     _y;    
    private Face    _face;
    private Suit    _suit;
    private ImageIcon _faceImage;
    private boolean _faceUp  = true;
    
    //Card
    public Card(Face face, Suit suit)
    {
        //Set default card position to 0,0 (X,Y)
        _x = 0;
        _y = 0;    	
        
        //Sets the default value of face and suit
        _face = face;
        _suit = suit;
        
        //... By default the cards are face up.
        _faceUp = false;
        
        //... Create the file name from the face and suit
        char faceChar = "facetest".charAt(_face.ordinal());
        char suitChar = "suittest".charAt(_suit.ordinal());
        String cardFilename = "" + faceChar + suitChar + ".gif";
        
        String path = PACKAGE_NAME + IMAGE_PATH + cardFilename;
        java.net.URL imageURL = CLSLDR.getResource(path);
        
        //Retrieve the image from the URL
        _faceImage = new ImageIcon(imageURL);
    }
    
    // Set the Position of the Card
    public void setPosition(int x, int y)
    {
        _x = x;
        _y = y;
    }    
    
    //  Returns face value of card
    public Face getFace()
    {
        return _face;
    }
    
    
    // Returns the suit
    public Suit getSuit()
    {
        return _suit;
    }
    
    // Draws the face of the card or the back of the card.
    public void draw(Graphics g) {
        if (_faceUp)
        {
            _faceImage.paintIcon(null, g, _x, _y);
        } 
        else
        {
            BACK_IMAGE.paintIcon(null, g, _x, _y);
        }
    }
    
    // Given a point, it tells whether this is inside card image.
    // Used to determine if mouse was pressed in card.
    public boolean isInside(int x, int y)
    {
        return (x >= _x && x < _x+CARD_WIDTH) && (y >= _y && y < _y+CARD_HEIGHT);
    }
    
    //Sets the position of the X and the position of Y
    public void setX(int x)
    {
    	_x = x;
    }
    public void setY(int y)
    {
    	_y = y;
    }    
    
    //Gets the position of the X and the position of Y
    public int getX()
    {
    	return _x;
    }
    public int getY()
    {
    	return _y;
    }
    
    //Set Card to FaceUp
    public void turnFaceUp()
    {
    	_faceUp = true;
    }
    
    //Set Card to FaceDown
    public void turnFaceDown()
    {
    	_faceUp = false;
    }    
    
    public String toString() {
        return "" + _face + " of " + _suit;
    }
}