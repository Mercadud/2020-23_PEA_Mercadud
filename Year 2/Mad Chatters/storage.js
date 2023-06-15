const { Socket } = require("socket.io");
const thorgan = require("./thorgan.js");

let roomCounter = 0;

let storage = {
  Invitation: class Invitation {
    /**
     * @param {String} sender
     * @param {Array} receiver
     * @param {String} type
     */
    constructor(sender, receiver, type) {
      this.sender = sender;
      this.receiver = receiver;
      this.type = type;
      this.id = generateId();
    }

    /**
     *
     * @returns Boolean
     */
    checkSurvival() {
      let invitationSurvives = true;
      if (!storage.getUserByName(this.sender)) {
        invitationSurvives = false;
      }
      this.receiver.map(
        /**
         *
         * @param {String} el
         * @param {Number} index
         */
        (el, index) => {
          let foundUser = false;
          storage.userList.map(
            /**
             *
             * @param {User} us
             */
            (us) => {
              if (us.name == el) {
                foundUser = true;
              }
            }
          );
          if (!foundUser) {
            this.receiver.splice(index, 1);
          }
        }
      );
      if (this.receiver.length < 1) {
        invitationSurvives = false;
      }
      return invitationSurvives;
    }
  },

  invitationList: [],

  /**
   *
   * @param {String} sender
   * @param {Array} receiver
   * @param {String} type
   */
  createInvitation: (sender, receiver, type) => {
    let alreadySent = false;
    storage.invitationList.map(
      /**
       *
       * @param {Invitation} invit
       * @param {Number} index
       */
      (invit, index) => {
        if (
          invit.sender.toLowerCase() == sender.toLowerCase() &&
          type == invit.type
        ) {
          alreadySent = true;
          let duplicates = false;
          receiver.map((recer) => {
            invit.receiver.map((rec) => {
              if (invit.type == type) {
                if (recer.toLowerCase() == rec.toLowerCase()) {
                  duplicates = true;
                }
              }
            });
          });
          if (!duplicates) {
            receiver.map((el) => {
              storage.invitationList[index].receiver.push(el);
            });
          } else {
            storage.getUserByName(sender).socket.emit("chat", {
              name: "Server",
              message:
                "ERR:\nOne or more of the users selected for the invite has already been invited",
            });
          }
        }
      }
    );
    if (!alreadySent) {
      storage.invitationList.push(
        new storage.Invitation(sender, receiver, type)
      );
    }
    storage.updateAllInvitations();
  },

  /**
   *
   * @param {String} id
   */
  deleteInvitation: (id) => {
    storage.invitationList.map(
      /**
       *
       * @param {Invitation} invit
       * @param {Number} index
       */
      (invit, index) => {
        if (invit.id == id) {
          storage.invitationList.splice(index, 1);
        }
        return;
      }
    );
    storage.updateAllInvitations();
  },

  updateAllInvitations: () => {
    storage.invitationList.map(
      /**
       * @param {Invitation} invit
       * @param {Number} index
       */
      (invit, index) => {
        if (!invit.checkSurvival()) {
          storage.invitationList.splice(index, 1);
        }
      }
    );
    storage.userList.map((us) => {
      let invites = {
        sending: [],
        receiving: [],
      };
      storage.invitationList.map(
        /**
         *
         * @param {Invitation} invit
         */
        (invit) => {
          // check for sending
          if (us.name == invit.sender) {
            invites.sending.push({
              receiver: invit.receiver,
              type: invit.type,
              id: invit.id,
            });
          }
          // check for receiving
          invit.receiver.map((rec) => {
            if (rec.toLowerCase() == us.name.toLowerCase()) {
              invites.receiving.push({
                sender: invit.sender,
                colour: storage.getUserByName(invit.sender).colour,
                type: invit.type,
                id: invit.id,
              });
            }
            return;
          });
          return;
        }
      );
      us.socket.emit("updateInvitations", invites);
      return;
    });
    return;
  },

  getInvitation: (id) => {
    let invitation = false;
    storage.invitationList.map(
      /**
       *
       * @param {Invitation} invit
       */
      (invit) => {
        if (id == invit.id) {
          invitation = invit;
        }
      }
    );
    return invitation;
  },

  GameRoom: class GameRoom {
    /**
     *
     * @param {User} playerOne
     * @param {USer} playerTwo
     */
    constructor(playerOne, playerTwo) {
      // I mixed them on purpose just to be annoying
      this.playerOne = playerTwo;
      this.playerTwo = playerOne;

      this.gamesPlayed = 0;

      this.playerOneWins = 0;
      this.playerTwoWins = 0;

      this.playerOneChoice = "";
      this.playerTwoChoice = "";

      if (roomCounter < 10) {
        this.id = `game-room-00${roomCounter}`;
        roomCounter++;
      } else if (roomCounter < 100) {
        this.id = `game-room-0${roomCounter}`;
        roomCounter++;
      } else if (roomCounter < 1000) {
        this.id = `game-room-${roomCounter}`;
        roomCounter++;
      } else {
        process.exit(999);
      }
      this.playerOne.changeRoom(this.id);
      this.playerTwo.changeRoom(this.id);

      this.playerOne.socket.emit("startGame", {
        opponent: this.playerTwo.name,
        id: this.id,
      });
      this.playerTwo.socket.emit("startGame", this.playerOne.name);

      this.playerOne.socket.emit("resetGame", {
        opponent: this.playerTwo.name,
        roundsPlayed: this.gamesPlayed,
        roundsWon: this.playerOneWins,
      });
      this.playerTwo.socket.emit("resetGame", {
        opponent: this.playerOne.name,
        roundsPlayed: this.gamesPlayed,
        roundsWon: this.playerOneWins,
      });
    }

    select(user, choice) {
      switch (user.name.toLowerCase()) {
        case this.playerOne.name.toLowerCase():
          this.playerOneChoice = choice;
          break;
        case this.playerTwo.name.toLowerCase():
          this.playerTwoChoice = choice;
          break;
      }
      if (this.playerOneChoice && this.playerTwoChoice) {
        /**
         *
         * @param {String} n
         * @returns Number version of RPSLS
         */
        function nameToNumber(n) {
          switch (n) {
            case "rock":
              return 0;
            case "spock":
              return 1;
            case "paper":
              return 2;
            case "lizard":
              return 3;
            case "scissors":
              return 4;
            default:
              return 5;
          }
        }

        if (
          nameToNumber(this.playerOneChoice) ==
          nameToNumber(this.playerTwoChoice)
        ) {
          this.endRound(null, 3);
          thorgan.game(this.playerOne, this.playerTwo, true);
        } else if (
          (nameToNumber(this.playerOneChoice) -
            nameToNumber(this.playerTwoChoice)) %
            5 <
          3
        ) {
          this.endRound(this.playerOne.name, 3);
          thorgan.game(this.playerOne, this.playerTwo, false);
          this.playerOneWins++;
        } else {
          this.endRound(this.playerTwo.name, 3);
          thorgan.game(this.playerTwo, this.playerOne, false);
          this.playerTwoWins++;
        }
        this.gamesPlayed++;
        this.playerTwoChoice = "";
        this.playerOneChoice = "";
      }
    }

    /**
     *
     * @param {User} winner
     * @param {Number} time
     */
    endRound(winner, time) {
      let timer = time;
      // this so I won't have to deal with the async stuff
      let p1c = this.playerOneChoice;
      let p2c = this.playerTwoChoice;
      let p1 = this.playerOne;
      let p2 = this.playerTwo;
      let p1w = this.playerOneWins;
      let p2w = this.playerTwoWins;

      // this this extremely horribly made, but I don't feel like dealing weird stuff;
      p1.socket.emit("gameTimer", timer);
      p2.socket.emit("gameTimer", timer);
      timer--;
      setTimeout(() => {
        p1.socket.emit("gameTimer", timer);
        p2.socket.emit("gameTimer", timer);
        timer--;
        setTimeout(() => {
          p1.socket.emit("gameTimer", timer);
          p2.socket.emit("gameTimer", timer);
          timer--;
          setTimeout(() => {
            p1.socket.emit("displayGameResults", {
              usGuess: p1c,
              opponentGuess: p2c,
              winner: winner ? winner : "tie!",
              roundsPlayed: this.gamesPlayed,
              roundsWon: p1w,
            });
            p2.socket.emit("displayGameResults", {
              usGuess: p2c,
              opponentGuess: p1c,
              winner: winner ? winner : "tie!",
              roundsPlayed: this.gamesPlayed,
              roundsWon: p2w,
            });
            setTimeout(() => {
              this.playerOne.socket.emit("resetGame", {
                opponent: p2.name,
                roundsPlayed: this.gamesPlayed,
                roundsWon: p1w,
              });
              this.playerTwo.socket.emit("resetGame", {
                opponent: p1.name,
                roundsPlayed: this.gamesPlayed,
                roundsWon: p2w,
              });
            }, 5000);
          }, 1000);
        }, 1000);
      }, 1000);
    }

    endGame() {
      let gameEndMessage = {
        name: "Server",
        message: `Game Ended!\nOne user quit or left the game\n ${this.playerOne.name} -> ${this.playerOneWins} : ${this.playerTwoWins} <- ${this.playerTwo.name}\n Back to the main chat!`,
      };
      this.playerOne.socket.emit("chat", gameEndMessage);
      this.playerTwo.socket.emit("chat", gameEndMessage);
      this.playerOne.socket.emit("changeRoom", "room-000");
      this.playerTwo.socket.emit("changeRoom", "room-000");
      this.playerOne.roomId = "room-000";
      this.playerTwo.roomId = "room-000";
      storage.removeRoom(this.id);
    }
  },

  /**
   *
   * @param {User} playerOne
   * @param {User} playerTwo
   */
  createGameRoom: (playerOne, playerTwo) => {
    storage.roomList.push(new storage.GameRoom(playerOne, playerTwo));
  },
  /**
   *
   * @param {String} id
   */
  removeRoom: (id) => {
    storage.roomList.map((room, index) => {
      if (room.id == id) {
        storage.roomList.splice(index, 1);
      }
    });
  },

  PrivateChat: class PrivateChat {
    /**
     * @param {Array} users
     */
    constructor(users) {
      this.users = users;
      if (roomCounter < 10) {
        this.id = `room-00${roomCounter}`;
        roomCounter++;
      } else if (roomCounter < 100) {
        this.id = `room-0${roomCounter}`;
        roomCounter++;
      } else if (roomCounter < 1000) {
        this.id = `room-${roomCounter}`;
        roomCounter++;
      } else {
        process.exit(999);
      }
      this.users.map(
        /**
         *
         * @param {User} us
         */
        (us) => {
          us.changeRoom(this.id);
        }
      );
    }
    /**
     *
     * @param {User} user
     */
    addUser(user) {
      this.users.push(user);
      user.changeRoom(this.id);
    }
    /**
     *
     * @param {String} user
     */
    removeUser(user) {
      this.users.forEach((chatter, index) => {
        if (user == chatter.id) {
          this.users.splice(index, 1);
        }
      });
      storage.updateRooms();
    }
  },

  roomList: [],

  /**
   *
   * @param {Array} users
   */
  createRoom: (users) => {
    storage.roomList.push(new storage.PrivateChat(users));
  },

  /**
   *
   * @param {String} id
   * @returns room or false
   */
  getRoom: (id) => {
    let r = false;
    storage.roomList.map((room) => {
      if (room.id == id) {
        r = room;
      }
    });
    return r;
  },

  updateRooms: () => {
    storage.roomList.map(
      /**
       *
       * @param {PrivateChat} room
       * @param {Number} index
       * @returns
       */
      (room, index) => {
        if (room.id != "room-000") {
          let willSurvive = true;
          if (room.users.length < 2) {
            willSurvive = false;
          }
          if (!willSurvive) {
            room.users[0].socket.emit("chat", {
              name: "server",
              message:
                "Sent back to main chat, it's was far too lonely in there",
            });
            room.users[0].socket.leave(room.id);
            room.users[0].roomId = room.id;
            room.users[0].socket.join("room-000");
            room.users[0].socket.emit("changeRoom", "room-000");
            storage.roomList.splice(index, 1);
          }
        }
        return;
      }
    );
  },

  User: class User {
    /**
     * @param {String} name
     * @param {String} id
     * @param {Socket} socket
     */
    constructor(name, id, socket) {
      this.name = name;
      this.id = id;
      this.socket = socket;
      this.roomId = "room-000";
      // assign colour
      while (true) {
        let isDiff = true;
        this.colour = randomColour();
        if (this.colour.length == 5) {
          this.colour = `0${this.colour}`;
        }
        // compared to background colour and server colour
        if (
          !areDifferent(this.colour, "cccccc") ||
          !areDifferent(this.colour, "121212")
        ) {
          isDiff = false;
        }
        storage.userList.map((el) => {
          if (!areDifferent(this.colour, el.colour)) {
            isDiff = false;
          }
        });
        if (isDiff) {
          break;
        }
      }
    }

    /**
     *
     * @param {String} room
     */
    changeRoom(room) {
      if (this.roomId.startsWith("game")) {
        storage.getRoom(this.roomId).endGame();
        storage.removeRoom(this.roomId);
      } else {
        storage.getRoom(this.roomId).removeUser(this.id);
      }
      this.socket.leave(this.roomId);
      this.roomId = room;
      this.socket.join(room);
      this.socket.emit("changeRoom", room);
    }
  },
  userList: [],
  /**
   * @param {String} name
   * @param {String} id
   * @param {Socket} socket
   */
  addUser: (name, id, socket) => {
    storage.userList.push(new storage.User(name, id, socket));
    sortUsers();
  },
  /**
   * @param {String} id
   * @returns User
   */
  removeUser: (id) => {
    storage.userList.forEach(
      /**
       *
       * @param {User} el
       * @param {Number} index
       * @returns
       */
      (el, index) => {
        if (id == el.id) {
          let u = storage.userList[index];
          storage.userList.splice(index, 1);
          return u;
        }
      }
    );
  },
  /**
   * @param {String} id
   * @returns User
   */
  getUser: (id) => {
    let isTrue = false;
    storage.userList.forEach((el) => {
      if (id === el.id) {
        isTrue = el;
      }
    });
    return isTrue;
  },
  getUserByName: (name) => {
    let isTrue = false;
    storage.userList.forEach((el) => {
      if (name.toLowerCase() == el.name.toLowerCase()) {
        isTrue = el;
      }
    });
    return isTrue;
  },
};

module.exports = storage;

// priv
function sortUsers() {
  storage.userList.sort((a, b) => {
    return a.name.localeCompare(b.name);
  });
}

//Generates and returns a random 6-digit hexadecimal number as a string
//This number will correspond to a RGB colour code
let randomColour = () => Math.floor(Math.random() * 16777215).toString(16);

//Determines the perceptible difference between two colours
//Returns true if there is a sufficient differnce between the colours
//as can be perceived by the human eye, false otherwise
/**
 *
 * @param {String} colour1
 * @param {String} colour2
 * @returns
 */
let areDifferent = (colour1, colour2) => {
  let r1 = colour1.substring(0, 2);
  let g1 = colour1.substring(2, 4);
  let b1 = colour1.substring(4);

  let r2 = colour2.substring(0, 2);
  let g2 = colour2.substring(2, 4);
  let b2 = colour2.substring(4);

  let rnum1 = parseInt(r1, 16);
  let gnum1 = parseInt(g1, 16);
  let bnum1 = parseInt(b1, 16);
  let rnum2 = parseInt(r2, 16);
  let gnum2 = parseInt(g2, 16);
  let bnum2 = parseInt(b2, 16);

  return (
    Math.sqrt(
      (rnum1 - rnum2) ** 2 + (gnum1 - gnum2) ** 2 + (bnum1 - bnum2) ** 2
    ) > 75
  );
};

function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).substr(2);
}
