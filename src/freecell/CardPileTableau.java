package freecell;

public class CardPileTableau extends CardPile {
	// Checking to see if it can accept a card if the card pile is empty, or if face value is lower by -1 and it is opposite colour, pushing the card
    public boolean rulesAllowAddingThisCard(Card card)
    {
        if ((this.size() == 0) ||
                (this.peekTop().getFace().ordinal() - 1 == card.getFace().ordinal() &&
                this.peekTop().getSuit().getColor() != card.getSuit().getColor()))
        {
            return true;
        }
        return false;
    }
}
