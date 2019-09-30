"use strict";

/**
 * Provide methods for interacting with the user.
 */
function View(presenter) {
    this.presenter = presenter;
    this.topCard = null;
    this.topCardString = "";
    this.errorString = "";
}

/**
 * Display information about the computer's hand.
 * Hand is an array of card's.
 */
View.prototype.displayComputerHand = function(hand) {
    var elt = window.document.getElementById("compHand");
    while(elt.hasChildNodes()){
	elt.removeChild(elt.childNodes[elt.childNodes.length-1]);
    }
    for(var i = 0; i < hand.length; i ++){
	var newElt = window.document.createElement("img");
	var imgURL = "../images/PlayingCards/back.png";
	var styleStr = "position:absolute;left:"+(i*15).toString()+"px;top:0;z-index:"
	    +(i+1).toString()+";width:71px;height:96px"
	newElt.setAttribute("style",styleStr);
	newElt.setAttribute("src",imgURL);
	elt.appendChild(newElt);
    }
};

/**
 * Display the top card of the discard pile (at the next opportunity).
 */
View.prototype.displayPileTopCard = function(card) {
    this.topCard = card;
    //this.topCardString = "Top card of pile: " + card;
    var elt = window.document.getElementById("table");
    while(elt.hasChildNodes()){
	elt.removeChild(elt.childNodes[elt.childNodes.length-1]);
    }
    var newElt = window.document.createElement("img");
    newElt.setAttribute("src",card.getBackURL());
    newElt.setAttribute("style","width:71px;height:96px");
    elt.appendChild(newElt);
    elt = window.document.getElementById("table");
    newElt = window.document.createElement("img");
    newElt.setAttribute("src",card.getURL());
    newElt.setAttribute("style","width:71px;height:96px");
    elt.appendChild(newElt);
};

View.prototype.setDeckListener = function(){
    var presenter = this.presenter;
    var deckClickHandler = function(event){
	presenter.deal();
    }
    var elt = window.document.getElementById("table");
    elt.addEventListener("click",deckClickHandler,false);
};

View.prototype.setHandListener = function(){
    var presenter = this.presenter;
    var handClickHandler = function(event){
	var card = event.target.getAttribute("alt");
	if(card.length > 2){
	    card = new Card(card.charAt(2),card.substring(0,2));
	}
	else{
	    card = new Card(card.charAt(1),card.charAt(0));
	}
	presenter.playHuman(card);
    }
    this.handClickHandler = handClickHandler;
};
/**
 * Display a "wrong card" message (at the next opportunity).
 */
View.prototype.displayWrongCardMsg = function(cardString) {
    this.errorString = "Bad input '" + cardString + "'. Please try again.";
    alert(this.errorString);
};

/**
 * Display the human hand.  This version also prompts the user 
 * and returns the string entered.
 */
View.prototype.displayHumanHand = function(hand) {
    var promptString = "";
    if (this.errorString) {
	promptString += this.errorString + "\n\n";
	this.errorString = "";
    }
    var elt = window.document.getElementById("playerHand");
    while(elt.hasChildNodes()){
	elt.removeChild(elt.childNodes[elt.childNodes.length-1]);
    }
    for(var i = 0; i < hand.length; i++){
	var newElt = window.document.createElement("img");
	var imgURL = hand[i].getURL();
	var styleStr = "position:absolute;left:"+(i*15).toString()+"px;top:0;z-index:"
	    +(i+1).toString()+";width:71px;height:96px";
	newElt.setAttribute("style",styleStr);
	newElt.setAttribute("src",imgURL);
	newElt.setAttribute("alt",hand[i]);
	elt.appendChild(newElt);
	newElt.addEventListener("click",this.handClickHandler,false);
    }
};

/**
 * Display the suit picker.  This version also prompts the user
 * and returns the string entered.
 */
View.prototype.displaySuitPicker = function(hand) {
    var elt = window.document.getElementById("suitPicker");
    elt.setAttribute("style","display:block");
};

View.prototype.setSuitListener = function(){
    var presenter = this.presenter;
    var suitClickHandler = function(event){
	var suit = event.target.getAttribute("id");
	presenter.setSuit(suit);
    };
    var clubs = window.document.getElementById("clubs");
    clubs.addEventListener("click", suitClickHandler, false);
    var spades = window.document.getElementById("spades");
    spades.addEventListener("click", suitClickHandler, false);
    var hearts = window.document.getElementById("hearts");
    hearts.addEventListener("click", suitClickHandler, false);
    var diamonds = window.document.getElementById("diamonds");
    diamonds.addEventListener("click", suitClickHandler, false);
};


/**
 * Announce that human has won.
 */
View.prototype.announceHumanWinner = function() {
    window.alert("Like, congrats 'n' 'at...");
    location.reload();
};

/**
 * Announce that I have won.
 */
View.prototype.announceComputerWinner = function() {
    window.alert("Oh yeah!!  I am a WINNER and you are a, well, non-winner.");
    location.reload();
};

