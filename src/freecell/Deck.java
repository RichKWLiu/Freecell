package freecell;

public class Deck extends CardPile
{
	//Generation of a new Deck, and shuffling to ensure it is generated differently
    public Deck()
    {
        for (Suit s : Suit.values())
        {
            for (Face f : Face.values())
            {
                Card c = new Card(f, s);
                c.turnFaceUp();
                this.push(c);
            }
        }
        shuffle();
    }
}
