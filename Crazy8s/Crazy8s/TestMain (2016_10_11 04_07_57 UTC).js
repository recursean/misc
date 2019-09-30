"use strict";

// Test Card
var card = new Card("h", "10");
if (card != "10h") {
  alert("Fail Card test: " + card);
}

// Test Pile
var pile = new Pile();
pile.acceptACard(card);
if (pile.getTopCard() != card) {
  alert("Fail Pile test 1: " + pile.getTopCard());
}
var card2 = new Card("s", "7");
pile.acceptACard(card2);
if (pile.getTopCard() != card2) {
  alert("Fail Pile test 2: " + pile.getTopCard());
}
var card3 = new Card("s", "j");
if (!pile.isValidToPlay(card3)) {
  alert("Fail Pile test 3: " + pile.getTopCard());
}
var card4 = new Card("c", "7");
if (!pile.isValidToPlay(card4)) {
  alert("Fail Pile test 4: " + pile.getTopCard());
}
var card5 = new Card("h", "8");
if (!pile.isValidToPlay(card5)) {
  alert("Fail Pile test 5: " + pile.getTopCard());
}
var card6 = new Card("h", "10");
if (pile.isValidToPlay(card6)) {
  alert("Fail Pile test 6: " + pile.getTopCard());
}
pile.acceptACard(card5);
pile.setAnnouncedSuit("c");
if (!pile.isValidToPlay(card4)) {
  alert("Fail Pile test 7: " + pile.getTopCard());
}
if (pile.isValidToPlay(card6)) {
  alert("Fail Pile test 8: " + pile.getTopCard());
}
if (!pile.isValidToPlay(card5)) {
  alert("Fail Pile test 9: " + pile.getTopCard());
}
pile.acceptACard(card6);
if (pile.isValidToPlay(card4)) {
  alert("Fail Pile test 10: " + pile.getTopCard());
}
if (pile.hasOwnProperty("isValidToPlay")) {
  alert("Fail Pile test 11");
}
if (!Pile.prototype.hasOwnProperty("isValidToPlay")) {
  alert("Fail Pile test 12");
}

// Test Player
var deck = new Deck(); // unshuffled; 2-8 of clubs on top
var player = new Player(deck); 
for (var i=1; i<=7; i++) {
  if (player.isHandEmpty()) {
    alert("Fail Player test " + i);
  }
  player.remove(0);
}
if (!player.isHandEmpty()) {
  alert("Fail Player test 8");
}

// Test ComputerPlayer
deck = new Deck(); // unshuffled; 2-8 of clubs on top
var computerPlayer = new ComputerPlayer(deck);
if (!(computerPlayer instanceof Player)) {
  alert("Fail ComputerPlayer test 1");
}
if (computerPlayer.hasOwnProperty('takeATurn')) {
  alert("Fail ComputerPlayer test 2");
}
if (!ComputerPlayer.prototype.hasOwnProperty('takeATurn')) {
  alert("Fail ComputerPlayer test 3");
}

// Test HumanPlayer // unshuffled; 2-8 of clubs on top
deck = new Deck();
var humanPlayer = new HumanPlayer(deck);
if (!(humanPlayer instanceof Player)) {
  alert("Fail HumanPlayer test 1");
}
if (humanPlayer.hasOwnProperty('remove')) {
  alert("Fail HumanPlayer test 2");
}
if (HumanPlayer.prototype.hasOwnProperty('remove')) {
  alert("Fail HumanPlayer test 3");
}
if (humanPlayer.hasOwnProperty('isHandEmpty')) {
  alert("Fail HumanPlayer test 4");
}
if (HumanPlayer.prototype.hasOwnProperty('isHandEmpty')) {
  alert("Fail HumanPlayer test 5");
}
if (humanPlayer.hasOwnProperty('takeATurn')) {
  alert("Fail HumanPlayer test 6");
}
if (!HumanPlayer.prototype.hasOwnProperty('takeATurn')) {
  alert("Fail HumanPlayer test 7");
}

// Test Game
var game = new Game();
if (game.hasOwnProperty('play')) {
  alert("Fail Game test 1");
}
if (!Game.prototype.hasOwnProperty('play')) {
  alert("Fail Game test 2");
}


// If we reach this with no alerts, all tests passed.  
alert("Tests completed.");