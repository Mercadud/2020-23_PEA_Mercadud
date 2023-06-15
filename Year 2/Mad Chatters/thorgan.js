const path = require("path");
const fs = require("fs").promises;

const logLoc = path.join(__dirname, "/log");

// Alternative to Morgan
let thorgan = {
  /**
   *
   * @param {User} user
   * @param {String} message
   */
  chat: (user, message) => {
    log(
      `[${getMessageDate()}] <${user.name}> {${user.roomId}} ${message} : ${
        message.length
      }`
    );
  },
  /**
   *
   * @param {User} user
   * @param {User} receiver
   * @param {String} message
   */
  whisper: (user, receiver, message) => {
    log(
      `[${getMessageDate()}] <${user.name}> {${receiver.name}} ${message} : ${
        message.length
      }`
    );
  },
  /**
   *
   * @param {User} winner USer
   * @param {User} loser User
   * @param {String} message
   */
  game: (winner, loser, tie) => {
    log(
      `[${getMessageDate()}] <${winner.name}> ${
        tie ? "tied agaisnt" : "won against"
      } <${loser.name}> {${winner.roomId}}`
    );
  },
};

/**
 *
 * @param {String} msg
 */
function log(msg) {
  let date = new Date();
  let fileName = "";
  fileName += date.getFullYear();
  fileName += date.getMonth();
  fileName += date.getDate() + ".log";
  // it automatically 
  fs.appendFile(path.join(logLoc, fileName), msg + "\n").catch(() => {
      console.log(`Thorgan : "I probably don't have the write permissions or something"`);
  });
}

module.exports = thorgan;

function getMessageDate() {
  let date = new Date();
  let msg = "";
  msg += date.getFullYear() + "/";
  msg += date.getMonth() + "/";
  msg += date.getDate() + " ";
  msg += date.getHours() + ":";
  msg += date.getMinutes() + ":";
  msg += date.getMilliseconds();
  return msg;
}
