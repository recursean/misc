"use strict";

function Game(){
    this.deck = new Deck();
    do {
	this.deck.shuffle();
    }
    while(this.deck.isTopCardAnEight());
    this.pile = new Pile();
    this.pile.acceptACard(this.deck.dealACard());
    
    this.human = new HumanPlayer(this.deck);
    this.computer = new ComputerPlayer(this.deck);
}

Game.prototype = {
    play: function(){
	do{
	    this.human.takeATurn(this.deck, this.pile);
	    if(!(this.human.isHandEmpty())){
		this.computer.takeATurn(this.deck, this.pile);
	    } 
	}
	while(!(this.human.isHandEmpty() || this.computer.isHandEmpty()));
	if(this.human.isHandEmpty){
	    alert("Congratulations!");
	}
	else{
	    alert("Thanks for being a good loser.");
	}
    }
};
