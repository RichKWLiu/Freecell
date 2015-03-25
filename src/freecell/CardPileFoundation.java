package freecell;

public class CardPileFoundation extends CardPile
{
	// Checking to see if it can accept a card if the card pile is empty, or if face value is lower by -1 and it is opposite colour
    public boolean rulesAllowAddingThisCard(Card card)
    {
        //Check to accept any type of ace on an empty pile
        if ((this.size() == 0) && (card.getFace() == Face.ACE))
        {
            return true;
        }
        
        if (size() > 0)
        {
            Card top = peekTop();
            if ((top.getSuit() == card.getSuit() &&
                    (top.getFace().ordinal() + 1 == card.getFace().ordinal())))
            {
                return true;
            }
        }
        return false;
        
    }
    
    //Check if card can be removed
    public boolean isRemovable() {
        return false;
    }
}
