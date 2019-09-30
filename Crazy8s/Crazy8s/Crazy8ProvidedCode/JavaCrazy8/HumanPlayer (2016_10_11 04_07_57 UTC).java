import java.util.Scanner;
/**
 * Interact with the human player to obtain their desired play.
 */
public class HumanPlayer extends Player {
  
  public HumanPlayer(Deck deck) {
    super(deck);
  }
  
  /**
   * Allow the human player to take a turn by either
   * selecting a card to play to the discard pile from their hand or
   * drawing a card from the deck.
   */
  public void takeATurn(Deck deck, Pile pile) {
    System.out.println("Your hand: ");
    for (int i=0; i<list.size(); i++) {
      System.out.println(i+1 + ": " + list.get(i));
    }
    System.out.println("Top card of pile: " + pile.getTopCard());
    int number;
    Scanner keyboard = new Scanner(System.in);
    boolean firstTime = true;
    do {
      if (firstTime) {
        firstTime = false;
      }
      else {
        System.out.println("Bad input. Please try again.");
      }
      System.out.print("Enter 0 to draw, otherwise number of card to play: ");
      number = keyboard.nextInt();
    } while (number < 0
               ||
             number > list.size()
               ||
             !(number == 0 || pile.isValidToPlay(list.get(number-1))));
    if (number == 0) {
      list.add(deck.dealACard());
    }
    else {
      Card card = list.get(number-1);
      remove(list.indexOf(card));
      pile.acceptACard(card);
      if (card.getValue().equals("8")) {
        String suit;
        do {
          System.out.println("What suit would you like the 8 to represent");
          System.out.print(" (club, diamond, heart, or spade)?");
          suit = keyboard.next();
        } while (!(suit.equals("club") || suit.equals("diamond") ||
                   suit.equals("heart") || suit.equals("spade")));
        pile.setAnnouncedSuit(suit);
      }
    }
  }
}
