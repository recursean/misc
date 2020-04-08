"use strict";

/**
 * Logic for the game Crazy Eights between a human and the computer.
 */
function Presenter() {
    /** Constructor:
     * Initialize game by creating and shuffling the deck, 
     * dealing one card (other than an 8) to the discard pile, and
     * dealing 7 cards to each player.  
     * Then create View object, which will be responsible for the
     * user interface.
     */
    this.deck = new Deck();
    do {
	this.deck.shuffle();
    } while (this.deck.isTopCardAnEight());
    this.pile = new Pile();
    this.pile.acceptACard(this.deck.dealACard());
    this.human = new Player(this.deck);
    this.computer = new Player(this.deck);

    // Create View.  
    // In this program, the view does not need to be able to
    // refer back to the Presenter, but some programs might need to
    // pass a reference to the Presenter (this) to the View constructor.
    this.view = new View(this);
    this.view.setSuitListener();
    this.view.setDeckListener();
    this.view.setHandListener();
    this.view.displayPileTopCard(this.pile.getTopCard());
    this.view.displayHumanHand(this.human.list);
    this.view.displayComputerHand(this.computer.list);
}
/**
 * Allow human to play.
 */
Presenter.prototype.playHuman = function(card) {
    var hand = this.human.getHandCopy(); // copy of hand for convenience
    if ((!this.pile.isValidToPlay(card))) {
        this.view.displayWrongCardMsg(card);
    }
    else {
	for(var i = 0; i < this.human.list.length; i++){
	    if(this.human.list[i].toString() === card.toString()){
		this.human.remove(i);
		break;
	    }
	}
	this.view.displayHumanHand(this.human.list);
	this.pile.acceptACard(card);
	this.view.displayPileTopCard(this.pile.getTopCard());
        if (this.pile.getTopCard().getValue() == "8") {
	    this.view.displaySuitPicker(this.human.getHandCopy());
	    return;
        }
	if (this.human.isHandEmpty()) {
	    this.view.announceHumanWinner();
	}
	if(!this.human.isHandEmpty())
	    this.playComputer();
    }

};

Presenter.prototype.deal = function(){
    this.human.add(this.deck.dealACard());
    this.view.displayHumanHand(this.human.list);
    this.playComputer();
}

/**
 * Play for the computer.  In this version, the computer always plays
 * the first card in its hand that is playable.  If it plays an 8,
 * the suit implicitly announced is the suit on the card.
 */
Presenter.prototype.playComputer = function() {
    // Play the first playable card, or pick if none is playable.
    var i=0;
    var hand = this.computer.getHandCopy(); // copy of hand for convenience
    var card = hand[0];
    while (!this.pile.isValidToPlay(card) && i<hand.length-1) {
	i++;
	card = hand[i];
    }
    hand = null; // actual hand will change below, so don't continue to use copy
    if (this.pile.isValidToPlay(card)) {
	this.computer.remove(i);
	this.pile.acceptACard(card);
	this.view.displayPileTopCard(card);
	if (this.pile.getTopCard().getValue() == "8") {
	    this.pile.setAnnouncedSuit(card.getSuit());
	}    
	this.view.displayComputerHand(this.computer.getHandCopy());
	if (this.computer.isHandEmpty()) {
	    this.view.announceComputerWinner();
	}
    }
    else {
	this.computer.add(this.deck.dealACard());
	this.view.displayComputerHand(this.computer.getHandCopy());
    }
};

Presenter.prototype.setSuit = function(suit){
    if(suit == "clubs"){
	suit = "c";
	this.pile.topCard = new Card("c",8);
	this.view.displayPileTopCard(this.pile.topCard);
    }
    else if(suit == "spades"){
	suit = "s";
	this.pile.topCard = new Card("c",8);
	this.view.displayPileTopCard(this.pile.topCard);
    }
    else if(suit == "hearts"){
	suit = "h";
	this.pile.topCard = new Card("c",8);
	this.view.displayPileTopCard(this.pile.topCard);
    }
    else if(suit == "diamonds"){
	suit = "d";
	this.pile.topCard = new Card("c",8);
	this.view.displayPileTopCard(this.pile.topCard);
    }
    this.pile.setAnnouncedSuit(suit);
    var elt = window.document.getElementById("suitPicker");
    elt.setAttribute("style","display:none");
    this.playComputer();
};
