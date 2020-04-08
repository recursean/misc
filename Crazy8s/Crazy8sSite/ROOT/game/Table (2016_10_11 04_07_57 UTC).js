"use strict";

/**
 * Data structure holding draw deck and discard pile.
 */
function Table() {
  this.deck = new Deck(this);
  do {
    this.deck.shuffle();
  } while (this.deck.isTopCardAnEight());

  this.pile = new Pile();
  this.pile.acceptACard(this.deck.dealACard());
}
