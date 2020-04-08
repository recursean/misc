"use strict";

function HumanPlayer(deck){
    Player.call(this,deck);
}

HumanPlayer.prototype = Object.create(Player.prototype);

HumanPlayer.prototype.srchHand = function(arr, elt) {
    var ind = -1;
    for(var i = 0; i < arr.length; i++){
	if(elt == arr[i]){
	    ind = i;
	    break;
	}
    }
    
    return ind;
};

HumanPlayer.prototype.takeATurn = function(deck, pile){
    var firstTime = true;
    do{
	if(firstTime == false)
	    window.alert("Bad input. Please try again.");
	var number = window.prompt("Your hand: " + this.list.toString() + "\nTop card of pile: " + pile.getTopCard() + "\nEnter 'p' to draw, otherwise card to play: ");
	if(firstTime)
	    firstTime = false;
	{
	    var card;
	    if(number.length == 2){
		card = new Card(number.slice(1,2), number.slice(0,1));
	    }
	    else if(number.length == 3){
		card = new Card(number.slice(2,3), number.slice(0,2));
	    }
	    else if(number == "p"){
		break;
	    }
	}
    }
    while(HumanPlayer.prototype.srchHand(this.list,number) == -1 ||  !(number == "p"  || (pile.isValidToPlay(card))));

    if(number == "p")
	this.list.unshift(deck.dealACard());
    else{
//	var card = this.list[this.list.indexOf(number)];
	this.remove(HumanPlayer.prototype.srchHand(this.list,number));
	pile.acceptACard(card);
	if(card.getValue() === "8"){
	    var suit;
	    do{
		suit = prompt("What suit would you like the 8 to represent: " + "\nc, d, h, or s?")
	    }
	    while(!(suit === "c" || suit === "d" || suit === "h" || suit === "s"));
	    pile.setAnnouncedSuit(suit);
	}
    }
}
