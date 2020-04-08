"use strict";

/**
 * Deck of playing cards.
 */
function Deck(table) {
  // Reference to table provides access to pile.
  this.table = table;

  var suit = ["c", "d", "h", "s"];
  var value = ["2", "3", "4", "5", "6", "7", "8", "9", "10",
      	       "j", "q", "k", "a"];
  this.list = new Array();
  for (var i=0; i<suit.length; i++) {
    for (var j=0; j<value.length; j++) {
      this.list.push(new Card(suit[i],value[j]));
    }
  }
  // Use a seeded pseudo-random number generator if
  // a "seed" parameter with numeric value is present in the URL,
  // otherwise use the built-in Math prng.
  // Loop begins at 1 because search string begins with ?,
  // which results in empty string in params[0].
  var params = window.location.search.split(/[?=&]/);
  this.prng = null;
  for (var k=1; k<params.length && this.prng==null; k+=2) {
    if (params[k] == "seed") {
      var seed = Number(params[k+1]);
      this.prng = new xorshift(seed);
    }
  }
  if (this.prng == null) {
    this.prng = Math;
  }
}
Deck.prototype = {
  /**
   * Shuffle the deck.
   */
  shuffle: function () {
    for (var n=this.list.length; n>=2; n--) {
      var r = Math.floor(this.prng.random()*n);
      // Swap cards at r and n-1
      var card = this.list[r];
      this.list[r] = this.list[n-1];
      this.list[n-1] = card;
    }
  },

  /**
   * Remove top card from the deck and return it.
   */
  dealACard: function () {
    if (this.list.length == 0) {
      this.table.pile.refreshDeck(this);
    }       
    return this.list.shift();
  },

  /**
   * Create a new deck with the cards supplied, then shuffle it.
   */
  replenish: function (cards) {
    this.list = cards.slice(0); // Copy of cards is new list
    this.shuffle();
  },
  /**
   * Indicate whether or not top card of deck is an 8.
   * This method is intended to be used only during game
   * initialization to avoid starting the pile with an 8.
   */
  isTopCardAnEight: function () {
    return this.list[0].getValue() == "8";
  }
};
