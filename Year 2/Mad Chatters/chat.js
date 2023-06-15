const app = require("express")();
const http = require("http").Server(app);
const path = require("path");
const { emit } = require("process");
const io = require("socket.io")(http);
const storage = require("./storage.js");
const commandHandler = require("./commandHandler");
const thorgan = require("./thorgan.js");

const PORT = 8080;
const WEBROOT = "public";

app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, WEBROOT, "/chat.html"));
});

app.get("/*", (req, res) => {
  let files = {
    "favicon.ico": "",
    "chat.css": path.join(__dirname, WEBROOT, "styles/chat.css"),
    "chat.html": path.join(__dirname, WEBROOT, "/chat.html"),
    "chatClient.js": path.join(__dirname, WEBROOT, "scripts/chatClient.js"),
    "close_white_24dp.svg": path.join(
      __dirname,
      WEBROOT,
      "/icons/close_white_24dp.svg"
    ),
    "menu_white_24dp.svg": path.join(
      __dirname,
      WEBROOT,
      "/icons/menu_white_24dp.svg"
    ),
    "send_white_24dp.svg": path.join(
      __dirname,
      WEBROOT,
      "/icons/send_white_24dp.svg"
    ),
  };
  try {
    res.sendFile(files[path.parse(req.url).base]);
  } catch {
    res.sendFile(files["chat.html"]);
  }
});

function updateUsers() {
  let connectedUsers = [];
  storage.userList.forEach((user) => {
    connectedUsers.push({
      name: user.name,
      colour: user.colour,
    });
  });
  io.emit("updateUsers", connectedUsers);
}

let mainChat = new storage.PrivateChat([]);
storage.roomList.push(mainChat);

io.on("connection", (socket) => {
  socket.emit("requestUsername");
  let addr = socket.handshake;
  console.log(`Login from ${addr.url}`);
  console.log(`${io.engine.clientsCount} connections`);

  // welcome screen
  socket.on(
    "createUserRequest",
    /**
     *
     * @param {String} newName
     */
    (newName) => {
      let alreadyTaken = false;
      storage.userList.forEach((el) => {
        if (newName.toLowerCase() == el.name.toLowerCase()) {
          alreadyTaken = true;
        }
      });
      let user;
      if (alreadyTaken) {
        socket.emit("createUserRequest", false);
      } else {
        storage.addUser(newName, socket.id, socket);
        user = storage.getUser(socket.id);
        mainChat.addUser(user);
        socket.emit("createUserRequest", {
          name: user.name,
          colour: user.colour,
        });
        io.emit("chat", {
          name: "Server",
          message: `${newName} is now part of the Mad Chatters!`,
        });
        updateUsers();
      }
    }
  );

  // just in case server restart
  socket.on(
    "serverRestart",
    /**
     *
     * @param {JSON} info
     */
    (info) => {
      storage.addUser(info.username, socket.id, socket);
      let user = storage.getUser(socket.id);
      user.colour = info.colour;
      user.changeRoom("room-000");
      if (info.room != "room-000") {
        socket.emit("chat", {
          name: "Server",
          message: "WARNING!\n:: Server Reset! ::\nSent back to main chat!",
        });
      }
      updateUsers();
      storage.updateAllInvitations();
    }
  );

  socket.on("disconnect", () => {
    let user = storage.getUser(socket.id);
    io.emit("chat", {
      name: "Server",
      message: `${user.name} really did just rage quit:(`,
    });
    storage.removeUser(socket.id);
    updateUsers();
    storage.updateAllInvitations();
  });

  //todo: handle a chat event
  socket.on(
    "chat",
    /**
     *
     * @param {JSON} msg
     */
    (msg) => {
      if (msg.message.startsWith("!")) {
        let serverResp = commandHandler.parse(
          msg.message,
          storage.getUser(socket.id),
          io
        );
        if (serverResp) {
          socket.emit("chat", {
            name: "Server",
            message: serverResp,
          });
        }
      } else {
        let user = storage.getUser(socket.id);
        socket.to(user.roomId).emit("chat", {
          name: user.name,
          message: msg.message,
          colour: user.colour,
        });
        thorgan.chat(user, msg.message);
      }
    }
  );

  socket.on(
    "cancelInvite",
    /**
     *
     * @param {String} id
     */
    (id) => {
      storage.deleteInvitation(id);
    }
  );

  socket.on(
    "acceptInvite",
    /**
     *
     * @param {String} id
     */
    (id) => {
      let us = storage.getUser(socket.id);
      let invit = storage.getInvitation(id);
      let owner = storage.getUserByName(invit.sender);

      if (invit.type == "priv") {
        for (let i = 0; i < invit.receiver.length; i++) {
          if (invit.receiver[i].toLowerCase() == us.name.toLowerCase()) {
            if (owner.roomId == "room-000") {
              storage.createRoom([owner, us]);
            } else {
              storage.getRoom(owner.roomId).addUser(us);
            }
            invit.receiver.splice(i, 1);
          }
        }
      } else if (invit.type == "rps") {
        storage.createGameRoom(us, owner);
        storage.deleteInvitation(id);
        storage.invitationList.map((rl) => {
          if (
            rl.sender.toLowerCase() == owner.name.toLowerCase() ||
            rl.sender.toLowerCase() == us.name.toLowerCase()
          ) {
            storage.deleteInvitation(rl.id);
          }
          rl.receiver.map(
            /**
             *
             * @param {String} rec
             * @param {Number} index
             */
            (rec, index) => {
              if (
                rec.toLowerCase() == us.name.toLowerCase() ||
                rec.toLowerCase() == us.name.toLowerCase()
              ) {
                rl.receiver.splice(index, 1);
              }
            }
          );
        });
      }
      storage.updateAllInvitations();
    }
  );

  socket.on(
    "makeGameChoice",
    /**
     *
     * @param {String} info
     */
    (info) => {
      storage.getRoom(info.id).select(storage.getUser(socket.id), info.choice);
    }
  );
});

http.listen(PORT, () => {
  console.log(`listening on port: ${PORT}`);
});
