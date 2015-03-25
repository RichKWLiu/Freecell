package freecell;

public class CardPileFreeCell extends CardPile
{
    //Check to see if it can accept a card if the card pile is empty
    public boolean rulesAllowAddingThisCard(Card card)
    {
        //Check to only allow if the current card pile is empty
        return size() == 0;
    }
}
