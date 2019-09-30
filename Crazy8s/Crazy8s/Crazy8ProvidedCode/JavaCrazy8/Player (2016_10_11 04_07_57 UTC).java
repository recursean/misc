import java.util.ArrayList;
/**
 * Base class for players.  Initializes player's hand
 * and determines whether or not the player's hand is empty.
 */
public class Player {
  /**
   * This player's hand.
   */
  protected ArrayList<Card> list = new ArrayList<Card>();
  
  /**
   * Get seven cards from the deck and store them in this hand.
   */
  public Player(Deck deck) {
    for (int i=1; i<=7; i++) {
      list.add(deck.dealACard());
    }
  } 
  
  /**
   * Return true when this hand is empty.
   */
  public boolean isHandEmpty() {
    return list.isEmpty();
  }

  /**
   * Remove the card at the specified position (0-based) in
   * this player's hand.
   */
    public void remove(int i) {
	list.remove(i);
    }
}
  
  
