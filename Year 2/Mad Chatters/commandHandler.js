const storage = require("./storage");
const thorgan = require("./thorgan");

let commandHandler = {
  /**
   *
   * @param {String} cmd
   * @param {User} user
   * @param {io} io
   * @@description parses and handles the response
   */
  parse: (cmd, user, io) => {
    // split the command up and remove any
    let tokens = cmd.replace(/\s+/g, " ").split(" ");
    if (tokens[0].startsWith("!!")) {
      publicCommand(tokens, user, io);
    } else if (tokens[0].startsWith("!")) {
      privateCommand(tokens, user, io);
    }
  },
};

module.exports = commandHandler;

/**
 *
 * @param {Array} tokens
 * @param {User} user
 * @param {io} io
 * @returns boolean
 */
function publicCommand(tokens, user, io) {
  const publicCommands = {
    play: () => {
      if (tokens.length > 1) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "The !!play command is standalone, no arguments required (ex. !!play)",
        });
      }
      let sender = user.name;
      let receiver = [];
      storage.userList.map((us) => {
        if (us.name != sender) {
          receiver.push(us.name);
        }
      });
      storage.createInvitation(sender, receiver, "game");
      user.socket.emit("chat", {
        name: "Server",
        message: "invited all users to a game of RPSLS!",
      });
    },
  };
  const commandExecuted = tokens.splice(0, 1)[0].replace("!!", "");
  try {
    publicCommands[commandExecuted]();
  } catch {
    user.socket.emit("chat", {
      name: "Server",
      message: `!!${commandExecuted} is not a valid command`,
    });
  }
}

/**
 *
 * @param {Array} tokens
 * @param {User} user
 * @returns boolean
 */
function privateCommand(tokens, user) {
  const privateCommands = {
    help: () => {
      user.socket.emit("chat", {
        name: "Server",
        message:
          "Commands:\r\n!help: display this text\r\n!play [username...]: request a game with someone\r\n!!play: ask anyone in the server to play\r\n !invite [username...]: invite someone to a new or current private room\r\n!leave: leave the current chat that you are in\r\n!cancel: cancel all invitations that you sent\r\n!<username> [message] to whisper to someone specific",
      });
    },
    play: () => {
      if (tokens.length < 1) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "the !play command requires you to select the players you wish to go up agaisnt (ex. !play KendrickLamar)",
        });
      } else if (user.roomId.startsWith("game")) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "you can't play with anyone else while already in a game, that would be cheating ;P",
        });
      } else {
        let receivers = [];

        // Array -> Iterator -> Array
        let possibleReceivers = Array.from(storage.userList.values());
        let errMessage = "Err:\n";
        tokens.forEach((token) => {
          if (token.toLowerCase() != user.name.toLowerCase()) {
            let foundUser = false;
            possibleReceivers.forEach((us, index) => {
              if (token.toLowerCase() == us.name.toLowerCase()) {
                possibleReceivers.splice(index, 1);
                receivers.push(token);
                foundUser = true;
              }
            });
            if (!foundUser) {
              errMessage += `${token} : user not found!\n`;
            }
          } else {
            errMessage += `${token} : you  played yourself, oh wait... you can't!\n`;
          }
        });
        if (errMessage != "Err:\n") {
          errMessage += `Invite not sent`;
          user.socket.emit("chat", {
            name: "Server",
            message: errMessage,
          });
        } else {
          storage.createInvitation(user.name, receivers, "rps");
          user.socket.emit("chat", {
            name: "Server",
            message: "Invitation to a game of RPSLS sent!",
          });
        }
      }
    },
    invite: () => {
      if (tokens.length < 1) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "the !invite command requires you to select the users you wish to invite to a private room (ex. !invite totallyNotAllan)",
        });
      } else if (user.roomId.startsWith("game")) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "Cannot invite someone while In-game, please leave the game using the !leave command first",
        });
      } else {
        let receivers = [];

        // Array -> Iterator -> Array
        let possibleReceivers = Array.from(storage.userList.values());
        let errMessage = "Err:\n";
        tokens.forEach((token) => {
          if (token.toLowerCase() != user.name.toLowerCase()) {
            let foundUser = false;
            possibleReceivers.forEach((us, index) => {
              if (token.toLowerCase() == us.name.toLowerCase()) {
                possibleReceivers.splice(index, 1);
                receivers.push(token);
                foundUser = true;
              }
            });
            if (!foundUser) {
              errMessage += `${token} : user not found!\n`;
            }
          } else {
            errMessage += `${token} : you can't invite yourself!\n`;
          }
        });
        if (errMessage != "Err:\n") {
          errMessage += `Invite not sent`;
          user.socket.emit("chat", {
            name: "Server",
            message: errMessage,
          });
        } else {
          storage.createInvitation(user.name, receivers, "priv");
          user.socket.emit("chat", {
            name: "Server",
            message: "Invitation to a private room sent!",
          });
        }
      }
    },
    leave: () => {
      if (tokens.length > 0) {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "The !leave command is standalone, no arguments required (ex. !leave)",
        });
      }
      if (user.roomId == "room-000") {
        user.socket.emit("chat", {
          name: "Server",
          message: "You can not leave the public chat!",
        });
      } else {
        user.changeRoom("room-000");
        user.socket.emit("chat", {
          name: "Server",
          message: "Successfully left the private chat",
        });
      }
    },
    whisper: (receiver) => {
      if (receiver.name.toLowerCase() == user.name.toLowerCase()) {
        user.socket.emit("chat", {
          name: "Server",
          message: "You messaged yourself, oh wait! you can't!",
        });
      } else {
        if (!tokens.join(" ").trim()) {
          user.socket.emit("chat", {
            name: "Server",
            message: "Please add a message to send (ex. !<username> [message])",
          });
        } else {
          user.socket.emit("whispered", {
            receiver: receiver.name,
            message: tokens.join(" "),
          });
          receiver.socket.emit("chat", {
            name: `(Whisper from ${user.name}) ${user.name}`,
            message: tokens.join(" "),
            colour: user.colour,
          });
          thorgan.whisper(user, receiver, tokens.join(" "));
        }
      }
    },
  };
  const commandExecuted = tokens.splice(0, 1)[0].replace("!", "");
  let isWhisper = false;
  storage.userList.map((us) => {
    if (us.name == commandExecuted) {
      isWhisper = true;
      privateCommands.whisper(us);
    }
  });
  try {
    if (!isWhisper) {
      if (commandExecuted != "whisper") {
        privateCommands[commandExecuted]();
      } else {
        user.socket.emit("chat", {
          name: "Server",
          message:
            "!whisper is an invalid command, to whisper to someone, please type !<username> [message]",
        });
      }
    }
  } catch (e) {
    console.log(e);
    user.socket.emit("chat", {
      name: "Server",
      message: `!${commandExecuted} is not a valid command, type !help for a list of all the commands.`,
    });
  }
}
