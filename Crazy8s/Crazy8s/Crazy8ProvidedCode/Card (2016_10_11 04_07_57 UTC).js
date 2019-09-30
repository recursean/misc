"use strict";

/**
 * A single playing card.
 */
function Card(aSuit, aValue) {
  this.suit = aSuit;
  this.value = aValue;
}
Card.prototype = {
  getSuit: function () { return this.suit; },
  getValue: function () { return this.value; },
  /**
   * Return a string representation of this card.
   */
  toString: function () {
    return this.value + this.suit ;
  }
};
