"use strict";

function Pile(){
    this.list = new Array();
    this.annoucedSuit;
}

Pile.prototype = {
    isValidToPlay: function(card) {
	var retVal = false;
	var topCard = this.list[0];
	if(card.getValue() == "8"){
	    retVal = true;
	}
	else if (topCard.getValue() == "8") {
	    retVal = (card.getSuit() == this.announcedSuit);
	}
	else if (card.getSuit() == topCard.getSuit()
		 || 
		 card.getValue() == topCard.getValue()) {
	    retVal = true;
	}
	return retVal;
	
    },
    acceptACard: function(card){
	this.list.unshift(card);
    },
    setAnnouncedSuit: function(suit){
	this.announcedSuit = suit;
    },
    getTopCard: function(){
	return this.list[0];
    }
};
