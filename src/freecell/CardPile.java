package freecell;

import java.util.*;

public class CardPile implements Iterable<Card>
{

    //Instance variables
    private ArrayList<Card> _cards = new ArrayList<Card>(); // Array list of all of the cards
    
    //Add new Card
    public void pushIgnoreRules(Card newCard) {
        _cards.add(newCard);
    }
    
    //Remove Card
    public Card popIgnoreRules()
    {
        int lastIndex = size()-1;
        Card crd = _cards.get(lastIndex);
        _cards.remove(lastIndex);
        return crd;
    }
    
    //Check if Card can be added
    public boolean push(Card newCard)
    {
        if (rulesAllowAddingThisCard(newCard))
        {
            _cards.add(newCard);
            return true;
        } else {
            return false;
        }
    }
    
    //Checking if Cards can be added
    public boolean rulesAllowAddingThisCard(Card card)
    {
        return true;
    }
    
    //Check the size of the Card Pile
    public int size()
    {
        return _cards.size();
    }
    
    //Check if Card can be removed
    public Card pop()
    {
        if (!isRemovable())
        {
            throw new UnsupportedOperationException("Illegal attempt to remove.");
        }
        return popIgnoreRules();
    }
    
    //Shuffle through the collection of Cards (Sorting)
    public void shuffle()
    {
        Collections.shuffle(_cards);
    }
    
    //Check top of Cards
    public Card peekTop()
    {
        return _cards.get(_cards.size() - 1);
    }
    
    // Iterate through cards
    public Iterator<Card> iterator()
    {
        return _cards.iterator();
    }
    
    // Reverse Iterate through the cards using size
    public ListIterator<Card> reverseIterator()
    {
        return _cards.listIterator(_cards.size());
    }
    
    //Clear Cards
    public void clear()
    {
        _cards.clear();
    }
    
    //Check if can remove Card
    public boolean isRemovable()
    {
        return true;
    }
}