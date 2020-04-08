"use strict";

function Player(deck){
    this.list = new Array();
    for(var i = 1; i <= 7; i++){
	this.list.push(deck.dealACard());
    }
}

Player.prototype = {
    isHandEmpty: function(){
	return this.list.length == 0;
    },
    remove: function(i){
	this.list.splice(i, 1);
    }
    
};
