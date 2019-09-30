/**
 * Play the game Crazy Eights.
 * The computer player in this version always draws a card.
 * If the deck runs out, the program will throw an exception and terminate.
 */
public class Game {
  private Deck deck;
  private Pile pile;
  private HumanPlayer human;
  private ComputerPlayer computer;
  
  /**
   * Initialize game by creating and shuffling the deck,
   * dealing one card (other than an 8) to the discard pile,
   * and dealing 7 cards to each player.
   */
  public Game() {
    deck = new Deck();
    do {
      deck.shuffle();
    } while (deck.isTopCardAnEight());
    pile = new Pile();
    pile.acceptACard(deck.dealACard());
    
    human = new HumanPlayer(deck);
    computer = new ComputerPlayer(deck);
    
  }
  
  /**
   * Play one complete game.
   */
  public void play() { 
    
    do {
      human.takeATurn(deck, pile);
      if (!human.isHandEmpty()) {
        computer.takeATurn(deck, pile);
      }
    } while (!(human.isHandEmpty() || computer.isHandEmpty()));
    if (human.isHandEmpty()) {
      System.out.println("Congratulations!");
    }
    else {
      System.out.println("Thanks for being a good loser.");
    }
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    game.play();
  }
}
