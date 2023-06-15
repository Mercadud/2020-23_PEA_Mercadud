if (localStorage.getItem("username") == null) {
  location.href = "index.html";
}
let print = (selector) => console.log(selector);
class Card {
  constructor(type, number) {
    this.cardType = type;
    this.cardNumber = number;
    this.cardPhoto = `./Pictures/cards/${this.cardType}${this.cardNumber}.png`;
  }

  get Card() {
    return `${this.cardNumber}${this.cardType}`;
  }

  get cPhoto() {
    return this.cardPhoto;
  }

  get cardT() {
    return this.cardType;
  }

  get CardN() {
    return this.cardNumber;
  }
}

class Deck {
  constructor() {
    this.deck = [];
    this.initializeDeck();
    this.shuffleDeck();
    this.topCard = 51;
  }

  initializeDeck() {
    for (let type = 1, typo = ""; type < 5; type++) {
      for (let number = 1; number < 14; number++) {
        if (type == 1) {
          typo = "H";
        }
        if (type == 2) {
          typo = "D";
        }
        if (type == 3) {
          typo = "S";
        }
        if (type == 4) {
          typo = "C";
        }
        this.deck.push(new Card(typo, number));
      }
    }
  }

  shuffleDeck() {
    let tempX;
    let randomCard;
    for (let shuffles = 0; shuffles < 8; shuffles++) {
      for (let index = this.deck.length - 1; index > -1; index -= 1) {
        randomCard = Math.floor(Math.random() * index);
        tempX = this.deck[index];
        this.deck[index] = this.deck[randomCard];
        this.deck[randomCard] = tempX;
      }
    }
  }

  get Deck() {
    return this.deck;
  }

  pickTopCard() {
    this.topCard -= 1;
    return this.deck[this.topCard];
  }
}

class User {
  constructor(userName, firstName, lastName, money) {
    this.setUsername(userName);
    this.firstName = firstName;
    this.lastName = lastName;
    document.querySelector(
      "#fullName"
    ).textContent = `name: ${firstName} ${lastName}`;
    this.money = parseInt(money);
    this.updateMoney();
    this.bet;
  }

  placeBet(betAmount) {
    this.bet = betAmount;
    document.querySelector("#bet").textContent = `bet: ${this.bet}`;
  }

  roundDone(state, multiplier) {
    if (state) {
      this.money += this.bet * multiplier;
    } else if (!state) {
      this.money -= this.bet;
    } else {
      console.log("an error has occured! // user.roundDone()");
    }
    this.updateMoney();
  }

  setUsername(u) {
    this.userName = u;
    document.querySelector(
      "#userName"
    ).textContent = `username: ${this.userName}`;
  }
  
  get UserName() {
    return this.userName;
  }

  get Name() {
    return `${this.firstName} ${this.lastName}`;
  }

  get Money() {
    return this.money;
  }

  updateMoney() {
    document.querySelector("#mon").innerHTML = `Money: ${this.money}`;
    localStorage.setItem("bankRoll", this.money);
  }
}

class Hand {
  constructor(deckLoc) {
    this.deck = deckLoc;
    this.hand = [];
    this.initializeHand();
  }

  
  initializeHand() {
    for (let i = 0; i < 6; i++) {
      this.hand.push(this.deck.pickTopCard());
    }
    console.log(this.hand);
  }

  updatePictures() {
    for (let i = 0; i < 6; i++) {
      let handCard = document.getElementById(`hand${i}`);
      handCard.innerHTML = `<img src="${this.hand[i].cPhoto}">`;
    }
  }
  updateFaceDown() {
    for (let i = 0; i < 6; i++) {
      let handCard = document.getElementById(`hand${i}`);
      handCard.innerHTML = `<img src="Pictures/cards/BackgroundBlack.png">`;
    }
  }

  replaceCard(cardPos) {
    print(cardPos);
    this.hand[cardPos] = this.deck.pickTopCard();
    this.updatePictures();
  }

  onePair() {
    let isTrue = false;
    for (let i = 0; i < this.hand.length; i++) {
      for (let m = i; m < this.hand.length; m++) {
        if (i !== m) {
          if (this.hand[i].CardN === this.hand[m].CardN) {
            console.log(this.hand[i].CardN + " and " + this.hand[m].CardN);
            isTrue = true;
          }
        }
      }
    }
    return isTrue;
  }

  threePair() {
    let counter = 0;
    for (let i = 0; i < this.hand.length; i++) {
      for (let m = 0; m < this.hand.length; m++) {
        if (i != m) {
          if (this.hand[i].CardN == this.hand[m].CardN) {
            counter++;
            continue;
          }
        }
      }
    }
    if (counter / 2 == 3) {
      return true;
    }
    return false;
  }

  threeOfAKind() {
    let count = 0;
    for (let i = 0; i < this.hand.length; i++) {
      for (let m = i; m < this.hand.length; m++) {
        for (let p = m; p < this.hand.length; p++) {
          if (i != m && m != p) {
            if (
              this.hand[i].CardN == this.hand[m].CardN &&
              this.hand[m].CardN == this.hand[p].CardN
            ) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  Straight() {
    let numbers = [];
    for (let i = 0; i < this.hand.length; i++) {
      numbers.push(this.hand[i].CardN);
    }
    numbers.sort((a, b) => a - b);
    if (
      ((((numbers[0] == numbers[1] - 1) == numbers[2] - 2) == numbers[3] - 3) ==
        numbers[4] - 4) ==
      numbers[5] - 5
    ) {
      return true;
    }
    return false;
  }

  Flush() {
    if (
      (((this.hand[0].CardT == this.hand[1].CardT) == this.hand[2].CardT) ==
        this.hand[3].CardT) ==
      this.hand[4].CardT
    ) {
      return true;
    }
    return false;
  }

  fullHouse() {
    let twoOfKind;
    let partOne = false;

    for (let i = 0; i < this.hand.length; i++) {
      if (
        this.hand.indexOf(this.hand[i].CardN) !=
        this.hand.lastIndexOf(this.hand[i].CardN)
      ) {
        twoOfKind = this.hand[i].CardN;
        partOne = true;
      }
    }
    let count = 0;
    if (partOne) {
      for (let i = 0; i < this.hand.length; i++) {
        for (let m = 0; m < this.hand.length; m++) {
          for (let p = 0; p < this.hand.length; p++) {
            if (i != m && m != p) {
              if (
                this.hand[i].CardN == this.hand[m].CardN &&
                this.hand[m].CardN == this.hand[p].CardN &&
                this.hand[i].CardN != twoOfKind
              ) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  fourOfAKind() {
    let count = 0;
    for (let i = 0; i < this.hand.length; i++) {
      for (let m = 0; m < this.hand.length; m++) {
        for (let p = 0; p < this.hand.length; p++) {
          for (let q = 0; q < this.hand.length; q++) {
            if (i != m && m != p && p != q) {
              if (
                parseInt(this.hand[i]) == parseInt(this.hand[m]) &&
                parseInt(this.hand[m]) == parseInt(this.hand[p]) &&
                parseInt(this.hand[p]) == parseInt(this.hand[q])
              ) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  StraightFlush() {
    if (this.Flush() && this.Straight()) {
      return true;
    }
    return false;
  }

  royalFlush() {
    if (this.Flush() && this.Straight()) {
      for (let i = 0; i < this.hand.length; i++) {
        if (this.hand[i].CardN >= 10) {
          return rtue;
        }
      }
    }
    return false;
  }
}

class Round {
  constructor() {
    this.resetRound();
  }

  confirmHand() {
    if (this.hand.royalFlush()) {
      this.winner(300, "RoyalFlush");
    } else if (this.hand.StraightFlush()) {
      this.winner(100, "StraightFlush");
    } else if (this.hand.fourOfAKind()) {
      this.winner(50, "4 of a Kind");
    } else if (this.hand.fullHouse()) {
      this.winner(30, "Full House");
    } else if (this.hand.Flush()) {
      this.winner(25, "Flush");
    } else if (this.hand.Straight()) {
      this.winner(20, "Straight");
    } else if (this.hand.threeOfAKind()) {
      this.winner(15, "Three of a kind");
    } else if (this.hand.threePair()) {
      this.winner(10, "Three Pairs");
    } else if (this.hand.onePair()) {
      this.winner(5, "One Pair");
    } else {
      this.winner(0, "You lost lul");
    }
  }

  winner(multiplier, log) {
    if (multiplier != 0) {
      user.roundDone(true, multiplier);
      document.getElementById("Winner").innerHTML = `You Won! // ${log}`;
    } else {
      user.roundDone(false, 0);
    }
  }

  resetRound() {
    this.deck = new Deck();
    this.hand = new Hand(this.deck);
    this.hand.updateFaceDown();
  }

  get Deck() {
    return this.deck;
  }

  get Hand() {
    return this.hand;
  }
}
