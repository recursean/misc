"use strict";

function ComputerPlayer(deck){
    Player.call(this,deck);
}

ComputerPlayer.prototype = Object.create(Player.prototype);

ComputerPlayer.prototype.takeATurn =  function(deck, pile){
    this.list.unshift(deck.dealACard());
    window.alert("Computer now has " + this.list.length + " cards.");
}

