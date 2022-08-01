import random
from time import sleep

class Card:
    def __init__(self, suite, value):
        self.suite = suite
        self.value = value
        self.char_value = f'{value}'

        if value == 1:
            self.char_value = 'A'

        elif value > 10:
            if value == 11:
                self.char_value = 'J'
            elif value == 12:
                self.char_value = 'Q'
            elif value == 13:
                self.char_value = 'K'

            self.value = 10

class Deck:
    def __init__(self):
        self.cards = []
        self.discard_pile = []

    def shuffle(self):
        print('Shuffling deck')
        random.shuffle(self.cards)

    def draw_card(self):
        if len(self.cards) == 0:
            print('Out of cards. Shuffling.')
            self.cards = self.discard_pile
            self.discard_pile = []
            self.shuffle()
        
        return self.cards.pop()

class Dealer:
    def __init__(self):
        self.wins = 0
        self.have_ace = False

    def deal(self, deck, player):
        self.hand = []
        self.total = 0
        self.have_ace = False

        show_card = deck.draw_card()
        self.total = self.total + show_card.value
        if show_card.value == 1:
            self.have_ace = True
        self.hand.append(show_card)
        player.get_card(deck.draw_card())
       
        self.hand.append(deck.draw_card())
        player.get_card(deck.draw_card())

    def deal_card(self, deck, player):
        player.get_card(deck.draw_card())

    def print_hand(self, hide=True):
        hand_str = ''
        for i,card in enumerate(self.hand):
            if i == 1 and hide == True:
                hand_str = hand_str + 'hidden '
                continue
            hand_str = hand_str + f'{card.char_value}-{card.suite} '
        if self.have_ace and self.total - 1 + 11 <= 21:
            hand_str = hand_str + f'// TOTAL = {self.total} or {self.total - 1 + 11}'
        else:
            hand_str = hand_str + f'// TOTAL = {self.total}'
        return hand_str

    def finish_round(self, deck, bust):
        self.total = self.total + self.hand[1].value
        if not bust:
            print('Dealer finishing round')
            if self.have_ace:
                while self.total < 17 and self.total - 1 + 11 < 17:
                    new_card = deck.draw_card()
                    self.hand.append(new_card)
                    self.total += new_card.value

                if self.total < 17:
                    self.total = self.total - 1 + 11
            else:
                while self.total < 17:
                    new_card = deck.draw_card()
                    self.hand.append(new_card)
                    self.total += new_card.value

class Player:
    def __init__(self, id):
        self.id = id
        self.hand = []
        self.total = 0
        self.wins = 0
        self.have_ace = False

    def empty_hand(self):
        self.hand = []
        self.total = 0
        self.have_ace = False

    def get_card(self, card):
        self.hand.append(card)
        self.total = self.total + card.value
        if card.value == 1:
            self.have_ace = True

    def print_hand(self):
        hand_str = ''
        for card in self.hand:
            hand_str = hand_str + f'{card.char_value}-{card.suite} '   
        if self.have_ace and self.total - 1 + 11 <= 21:
            hand_str = hand_str + f'// TOTAL = {self.total} or {self.total - 1 + 11}'
        else:
            hand_str = hand_str + f'// TOTAL = {self.total}'
        return hand_str

    def take_turn(self):
        move = ''
        while move != 'h' and move != 's':
            print('Enter a move: h (hit) or s (stay)')
            move = str(input())

        return move

    def finish_round(self):
        if self.have_ace and self.total - 1 + 11 <= 21:
            self.total = self.total - 1 + 11

class Game:
    def __init__(self):
        print('== Blackjack ==\n')

        self.dealer = Dealer()
        self.player = Player(1)

        self.deck = self.create_deck(4)

        while True:
            print('Starting round')
            print(f'Cards remaining: {len(self.deck.cards)}\n')

            self.dealer.deal(self.deck, self.player)

            self.print_state(True)

            bust = False
            while self.player.take_turn() == 'h':
                self.dealer.deal_card(self.deck, self.player)

                self.print_state(True)

                if self.player.total > 21:
                    self.dealer_win(True)
                    bust = True
                    break
                elif self.player.total == 21:
                    break

            self.player.finish_round()
            self.dealer.finish_round(self.deck, bust)
            sleep(2)
            self.print_state(False)

            if not bust:
                if self.dealer.total <= 21:
                    if self.dealer.total > self.player.total:
                        self.dealer_win(False)
                    elif self.dealer.total < self.player.total:
                        self.player_win(False)
                    else:
                        print('DRAW')
                else:
                    self.player_win(True)

            self.print_stats()

            for card in self.player.hand:
                self.deck.discard_pile.append(card)
                 
            for card in self.dealer.hand:
                self.deck.discard_pile.append(card)

            self.player.empty_hand()
            
            print('Press < enter > to start another round.')
            input()

    def create_deck(self, decks):
        suites = {
            0:'SPD',
            1:'CLUB',
            2:'HRT',
            3:'DMND'
        }

        deck = Deck()

        print(f'Creating and combining {decks} decks')
        for _ in range(decks):
            for suite in range(4):
                for value in range(13):
                    deck.cards.append(Card(suites[suite], value+1))

        deck.shuffle()

        return deck 

    def print_state(self, hide):
        print(f'----------------------------------------------------------')
        print(f'  DEALER: {self.dealer.print_hand(hide)}')
        print(f'  PLAYER: {self.player.print_hand()}')
        print(f'----------------------------------------------------------')
        print()

    def dealer_win(self, bust):
        print('* ', end='')
        if bust:
            print('BUST - ', end='')
        print('DEALER WINS *')
        self.dealer.wins = self.dealer.wins + 1

    def player_win(self, bust):
        print('* ', end='')
        if bust:
            print('BUST - ', end='')
        print('PLAYER WINS *')
        self.player.wins = self.player.wins + 1

    def print_stats(self):
        print(f'++++++++++++++++++++++++++++++++++')
        print(f'  DEALER wins: {self.dealer.wins}')
        print(f'  PLAYER wins: {self.player.wins}')
        print(f'++++++++++++++++++++++++++++++++++')
        print()
Game()