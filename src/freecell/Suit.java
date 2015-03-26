package freecell;

import java.awt.Color;

enum Suit
{
    //Constants for each type of card with associated colour
    SPADES(Color.BLACK), HEARTS(Color.RED), CLUBS(Color.BLACK), DIAMONDS(Color.RED);
    
    private final Color _colour;
    
    //Set Colour for each suit
    Suit(Color color)
    {
        _colour = color;
    }
    
    //Get Colour for each suit
    public Color getColor()
    {
        return _colour;
    }
}
