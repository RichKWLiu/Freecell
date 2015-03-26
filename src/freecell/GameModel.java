package freecell;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameModel implements Iterable<CardPile> {
    
	private CardPile[] _foundation;     
	private CardPile[] _tableau;
    private CardPile[] _freeCells;
    
    private ArrayList<ChangeListener> _changeListeners;
    private ArrayList<CardPile> _allPiles;

        
    //Create a stack for the CardPile
    //Pushing the Piles when a card is moved
    //Popping the Pile when a card move is incorrect or undone
    private ArrayDeque<CardPile> _undoStack = new ArrayDeque<CardPile>();
    
    //GameModel
    public GameModel()
    {        
        _allPiles = new ArrayList<CardPile>();
        // Array of card piles
        _freeCells  = new CardPile[4];
        _foundation = new CardPile[4];        
        _tableau    = new CardPileTableau[8];
        
        // Create an empty pile to hold the free cells
        for (int pile = 0; pile < _freeCells.length; pile++)
        {
            _freeCells[pile] = new CardPileFreeCell();
            _allPiles.add(_freeCells[pile]);
        }        
        
        // Create an empty pile to hold the foundation pile
        for (int pile = 0; pile < _foundation.length; pile++)
        {
            _foundation[pile] = new CardPileFoundation();
            _allPiles.add(_foundation[pile]);
        }
        
        // Sort out the cards into the piles
        for (int pile = 0; pile < _tableau.length; pile++)
        {
            _tableau[pile] = new CardPileTableau();
            _allPiles.add(_tableau[pile]);
        }
        
        _changeListeners = new ArrayList<ChangeListener>();
        reset();
    }
    
    // Resetting if a new game is selected
    public void reset() {
    	
    	//Generate a new deck
        Deck deck = new Deck();
        deck.shuffle();
        
        // Clear the piles of cards
        for (CardPile p : _allPiles)
        {
            p.clear();
        }
        
        // Generate a new set of cards into piles
        int whichPile = 0;
        for (Card crd : deck)
        {
            _tableau[whichPile].pushIgnoreRules(crd);
            whichPile = (whichPile + 1) % _tableau.length;
        }
        
        _notifyEveryoneOfChanges();
    }
    
    //TODO: This is a little messy right now, having methods that both 
    //      return a pile by number, and the array of all piles.
    //      Needs to be simplified.
    
    // Iterate through card pile
    public Iterator<CardPile> iterator()
    {
        return _allPiles.iterator();
    }
    
    // Get value in TableauPile
    public CardPile[] getTableauPiles()
    {
        return _tableau;
    }    
    
    // Get array value in TableauPile
    public CardPile getTableauPile(int i)
    {
        return _tableau[i];
    }
    
    // Get array value in FreeCellPiles
    public CardPile getFreeCellPile(int cellNum)
    {
        return _freeCells[cellNum];
    }    
    // Get value in FreeCellPiles
    public CardPile[] getFreeCellPiles()
    {
        return _freeCells;
    }
    
    // Get array value in FoundationPiles
    public CardPile getFoundationPile(int cellNum)
    {
        return _foundation[cellNum];
    }    
    
    // Get value in FoundationPiles
    public CardPile[] getFoundationPiles()
    {
        return _foundation;
    }
    

    
    //Moving card from one pile to another pile
    public boolean moveFromPileToPile(CardPile source, CardPile target)
    {
        boolean result = false;
        if (source.size() > 0)
        {
            Card crd = source.peekTop();
            
            if (target.rulesAllowAddingThisCard(crd))
            {
                target.push(crd);
                source.pop();
                
                _notifyEveryoneOfChanges();
                
                //Keep track of card movement using the undo stack
                _undoStack.push(source);
                _undoStack.push(target);
                
                result = true;
            }
        }
        return result;
    }

    // Checking if a move can be made or has been made
    public void makeAllPlays()
    {
        
        boolean worthTrying;  // Flag to see whether a move has been made

        do {
            worthTrying = false;  // Toggle flag

            for (CardPile freePile : _freeCells)
            {
                for (CardPile gravePile : _foundation)
                {
                    worthTrying |= moveFromPileToPile(freePile, gravePile);
                }
            }
            for (CardPile cardPile : _tableau) {
                for (CardPile gravePile : _foundation) {
                    worthTrying |= moveFromPileToPile(cardPile, gravePile);
                }
            }
        } while (worthTrying);
    }
    
    // Listener to see if there are any changes made
    public void addChangeListener(ChangeListener someoneWhoWantsToKnow)
    {
        _changeListeners.add(someoneWhoWantsToKnow);
    }
    
    // Notifier to tell when a change is made
    private void _notifyEveryoneOfChanges()
    {
        for (ChangeListener interestedParty : _changeListeners)
        {
            interestedParty.stateChanged(new ChangeEvent("Game state changed."));
        }
    }
    
    // Moving card from one pile to another pile, not kept track of by undo stack
    private void _forceMoveFromPileToPile(CardPile source, CardPile target)
    {
        if (source.size() > 0)
        {
            target.push(source.pop());
            _notifyEveryoneOfChanges();
        }
    }
}