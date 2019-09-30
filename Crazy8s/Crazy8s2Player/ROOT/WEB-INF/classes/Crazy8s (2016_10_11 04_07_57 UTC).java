
import java.io.PrintWriter;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.servlet.*;
import java.io.IOException;
import java.util.regex.*;
import java.util.Random;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.parsers.ParserConfigurationException;

import java.util.ArrayList;
import java.util.Iterator;

@WebServlet( urlPatterns={"/Crazy8s"} )
public class Crazy8s extends HttpServlet{
    
    public int playerNum;
    public Game recentGame;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
    {
	HttpSession session = request.getSession();
	
	playerNum++;
		
	if(playerNum % 2 == 1){
	    recentGame = new Game();
	    session.setAttribute("game", recentGame);
	    // response.sendRedirect("/game/Crazy8.html?player=" + playerNum);
	}
	else{
	    session.setAttribute("game", recentGame);
	    //response.sendRedirect("/game/Crazy8.html?player=" + playerNum);
	}
	response.sendRedirect("/game/Crazy8.html?player=" + playerNum);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
    {
	String rtype = request.getParameter("rtype");
	System.out.println(rtype);
	PrintWriter servletOut = response.getWriter();
	HttpSession session = request.getSession();
	if(session.isNew()){
	    doGet(request, response);
	}
	
	else if(request.getParameter("rtype").equals("play")){
	    Game game = (Game)session.getAttribute("game");
	    String value = request.getParameter("value");
	    String suit = request.getParameter("suit");
	    String asuit = request.getParameter("asuit");
	    game.playCard(game.getNextPlayer(), value, suit, asuit); 
	    game.toggleTurn();
	}
	else if(request.getParameter("rtype").equals("pick")){
	    Game game = (Game)session.getAttribute("game");
	    Card card = game.getDeck().dealACard();
	    game.getOtherPlayer(game.getNextPlayer()).add(card);
	    game.toggleTurn();
	    sendDealtCard(servletOut, game, playerNum, card);
	}
	else if(rtype.equals("poll")){
	    Game game = (Game)session.getAttribute("game");
	    response.setContentType("text/xml; charset=\"UTF-8\"");
	    displayGame(servletOut, game, playerNum);
	    System.out.println("sessionPLayer" + (int)session.getAttribute("player"));
	}
	
    }
	
    private void sendDealtCard(PrintWriter servletOut, Game game, int player, Card cardPick){
	try{
	    DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = dBFactory.newDocumentBuilder();
	    Document document = docBuilder.newDocument();
	    
	    Element card = document.createElement("card");
	    Attr value = document.createAttribute("value");
	    value.setValue(cardPick.getValue());
	    Attr suit = document.createAttribute("suit");
	    suit.setValue(cardPick.getSuit());
	    card.setAttributeNode(value);
	    card.setAttributeNode(suit);
	    document.appendChild(card);

	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    transformer.transform(new DOMSource(document), new StreamResult(servletOut));
	    transformer.transform(new DOMSource(document), new StreamResult(System.out)); 
	}
      	catch(TransformerConfigurationException e){System.out.println("ERROR3");}
	catch(TransformerException e){System.out.println("ERROR");}
	catch(ParserConfigurationException e){System.out.println("ERROR2");}
	servletOut.close();
    }

    private void displayGame(PrintWriter servletOut, Game game, int player){
	try{
	    System.out.println("playerNum: " + playerNum);
	    System.out.println("nextPlayer: " + game.getNextPlayer());
	    DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = dBFactory.newDocumentBuilder();
	    Document document = docBuilder.newDocument();
	    
	    Element root = document.createElement("game");
	    document.appendChild(root);
	    
	    Element turn = document.createElement("playerTurn");
	    turn.appendChild(document.createTextNode(Integer.toString(game.getNextPlayer())));
	    root.appendChild(turn);
	 
	    Element pile = document.createElement("pile");
	    Attr pileSuit = document.createAttribute("suit");
	    pileSuit.setValue(game.getPile().getTopCard().getSuit());
	    Attr pileValue = document.createAttribute("value");
	    pileValue.setValue(game.getPile().getTopCard().getValue());
	    Attr asuit = document.createAttribute("asuit");
	    asuit.setValue(game.getPile().getAnnouncedSuit());
	    pile.setAttributeNode(pileSuit);
	    pile.setAttributeNode(pileValue);
	    pile.setAttributeNode(asuit);
	    root.appendChild(pile);
	    Element oppCards = document.createElement("opponentCards");
	    oppCards.appendChild(document.createTextNode(Integer.toString(game.getThisPlayer(game.getNextPlayer()).getNCards())));
	    root.appendChild(oppCards);
	    
	    Element cards = document.createElement("cards");
	    root.appendChild(cards);
	    
	    Element card;
	    Attr suit;
	    Attr value;
	    for(int i = 0; i < game.getThisPlayer(player).getNCards(); i++){
		card = document.createElement("card");
		suit = document.createAttribute("suit");
		value = document.createAttribute("value");
		suit.setValue(game.getThisPlayer(player).list.get(i).getSuit());
		value.setValue(game.getThisPlayer(player).list.get(i).getValue());
		card.setAttributeNode(suit);
		card.setAttributeNode(value);
		cards.appendChild(card);
	    }
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    transformer.transform(new DOMSource(document), new StreamResult(servletOut));
	    transformer.transform(new DOMSource(document), new StreamResult(System.out));
	    System.out.println("^^");
	}
      	catch(TransformerConfigurationException e){System.out.println("ERROR3");}
	catch(TransformerException e){System.out.println("ERROR");}
	catch(ParserConfigurationException e){System.out.println("ERROR2");}
	servletOut.close();

    }
}



/**
 * A single playing card.
 */
class Card {
  private String suit;
  private String value;
  public Card(String aSuit, String aValue)
  { suit = aSuit; value = aValue; }
  public String getSuit() { return suit; }
  public String getValue() { return value; }
  
  /**
   * Return a string representation of this card.
   * This is what will be printed if a Card is passed
   * as an argument to System.out.println().
   */
  public String toString() {
    return value + suit;
  }
}




//import java.util.ArrayList;

/**
 * Deck of playing cards.
 */
class Deck {
  /**
   * Deck of cards.
   */
    private ArrayList<Card> list = new ArrayList<Card>();

    /**
     * Discard pile (take cards from this if deck runs out).
     */
    private Pile pile; 

  /**
   * Initialize deck to represent regular 52 playing cards.
   */
  public Deck(Pile pile) {
    String[] suit = {"c", "d", "h", "s"};
    String[] value = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
      "j", "q", "k", "a"};
    for (String s : suit) {
      for (String v : value) {
        list.add(new Card(s,v));
      }
    }
    this.pile = pile;
  }
  /**
   * Shuffle the deck.
   */
  public void shuffle() {
    java.util.Collections.shuffle(list);
  }
  /**
   * Remove top card from the deck and return it.
   * If deck is empty, replenish deck from discard pile.
   */
  public Card dealACard() {
      if (list.size() == 0) {
	  list = pile.removeAllButTop();
	  shuffle();
      }       
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




/**
 * Play the game Crazy Eights.
 * The computer player in this version always draws a card.
 * If the deck runs out, the program will throw an exception and terminate.
 */
class Game {
    private Deck deck;
    private Pile pile;
    private Player[] player = new Player[2];
    private int nextPlayer = 0; // Second player, #0, goes first.
    
  /**
   * Initialize game by creating and shuffling the deck,
   * dealing one card (other than an 8) to the discard pile,
   * and dealing 7 cards to each player.
   */
    public Game() {
	pile = new Pile();
	deck = new Deck(pile);
	do {
	    deck.shuffle();
	} while (deck.isTopCardAnEight());
	pile.acceptACard(deck.dealACard());
	player[0] = new Player(deck);
	player[1] = new Player(deck);
    }

    public void playCard(int playerNum,
			 String value, String suit, String announcedSuit) {
	Card playedCard = player[playerNum].find(value+suit);
	player[playerNum].remove(playedCard);
	pile.acceptACard(playedCard);
	if (value.equals("8")) {
	    pile.setAnnouncedSuit(announcedSuit);
	}
    }

    public void addCard(int playerNum, Card card) {
	player[playerNum].add(card);
    }

    public void toggleTurn() {
	nextPlayer = (nextPlayer+1)%2;
    }

    public Deck getDeck() { return deck; }
    public Pile getPile() { return pile; }
    public Player getThisPlayer(int playerNum) { 
	return player[playerNum%2];
    }
    public Player getOtherPlayer(int playerNum) {
	return player[(playerNum+1)%2];
    }
    public int getNextPlayer() { return nextPlayer; }
}




//import java.util.ArrayList;
/**
 * Discard pile of cards.
 */
class Pile {
  /** List of cards on the pile. */
  // For simplicity, 0 is considered the top card of the pile.
  private ArrayList<Card> list = new ArrayList<Card>();
  
  /** If an 8 is played, this is the announced suit preference. */
  private String announcedSuit;
  
  public Pile() { }
  
  /**
   * Return true if the given card can be legally played on the
   * current pile.
   */
  public boolean isValidToPlay(Card card) {
    boolean retVal = false;
    Card topCard = list.get(0);  // would be more efficient to
                                 // make last card the top
    if (card.getValue().equals("8")) {
	retVal = true;
    }
    else if (topCard.getValue().equals("8")) {
	retVal = (card.getSuit().equals(announcedSuit));
    }
    else if (card.getSuit().equals(topCard.getSuit())
               || 
             card.getValue().equals(topCard.getValue())) {
	retVal = true;
    }
    return retVal;
  }
  
  /**
   * Accept a card and make it the new top of the discard pile.
   */
  public void acceptACard(Card card) {
    list.add(0, card);
  }
  /**
   * Remember the suit preference announced when the most recent
   * 8 was played.
   */ 
  public void setAnnouncedSuit(String suit) {
    announcedSuit = suit;
  }

    public String getAnnouncedSuit() {
	return announcedSuit;
    }

  /**
   * Return the card that is on top of the pile.  The card is not removed.
   */
  public Card getTopCard() {
    return list.get(0);
  }

    /** 
     * Remove all cards but the top one from this list and return
     * the removed cards as a list. 
     */
    public ArrayList<Card> removeAllButTop() {
	ArrayList<Card> retList = list;
	list = new ArrayList<Card>();
	list.add(retList.remove(0));
	return retList;
    }
}




//import java.util.ArrayList;
//import java.util.Iterator;
/**
 * Base class for players.  Initializes player's hand
 * and determines whether or not the player's hand is empty.
 */
class Player {
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
   * Add the given Card object to this player's hand.
   */
  public void add(Card card) {
    list.add(card);
  }

  /**
   * Remove the card at the specified position (0-based) in
   * this player's hand.
   */
    public void remove(int i) {
	list.remove(i);
    }

    /** Remove the given card from the player's hand. */
    public void remove(Card card) {
	list.remove(card);
    }

    /**
     * Find card with given string representation in this hand.
     */
    public Card find(String cardString) {
	int i = 0;
	Card card = null;
	while (i<this.list.size() && card == null) {
	    if (this.list.get(i).toString().equals(cardString)) {
		card = this.list.get(i);
	    }
	    i++;
	}
	return card;
    }

    public int getNCards() {
	return list.size();
    }
    public Iterator<Card> getCardIterator() {
	return list.iterator();
    }
}
  
  

