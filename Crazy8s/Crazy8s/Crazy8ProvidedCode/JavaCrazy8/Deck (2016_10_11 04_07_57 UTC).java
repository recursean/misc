import java.util.ArrayList;

/**
 * Deck of playing cards.
 */
public class Deck {
  /**
   * Deck of cards.
   */
  private ArrayList<Card> list = new ArrayList<Card>();
  
  /**
   * Initialize deck to represent regular 52 playing cards.
   */
  public Deck() {
    String[] suit = {"club", "diamond", "heart", "spade"};
    String[] value = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
      "jack", "queen", "king", "ace"};
    for (String s : suit) {
      for (String v : value) {
        list.add(new Card(s,v));
      }
    }
  }
  /**
   * Shuffle the deck.
   */
  public void shuffle() {
    java.util.Collections.shuffle(list);
  }
  /**
   * Remove top card from the deck and return it.
   */
  public Card dealACard() {
    return list.remove(0);
  }
  /**
   * Indicate whether or not top card of deck is an 8.
   * This method is intended to be used only during game
   * initialization to avoid starting the pile with an 8.
   */
  public boolean isTopCardAnEight() {
    return list.get(0).getValue().equals("8");
  }
}