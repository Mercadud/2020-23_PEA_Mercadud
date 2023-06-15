let $$ = (selector) => document.querySelector(selector);

let username = "NEWUSER";
let colour = "#123456";
let roomId = "room-000";
let isAlreadyInChat = false;
let isPlaying = false;

addEventListener("load", () => {
  let socket = io();

  pageSetup(socket);

  // already in chat when server start
  socket.on("requestUsername", () => {
    console.log(`received: requestUsername`);
    if (isAlreadyInChat) {
      $$("#game").style.width = "0";
      socket.emit("serverRestart", {
        username: username,
        colour: colour,
        room: roomId,
      });
      console.log("relogged in");
    }
  });

  // welcome screen
  $$("#inputUsernameBtn").addEventListener("click", (e) => {
    if (!$$("#usernameInput").value.trim()) {
      $$("#welcomeScreen .middle .error").innerHTML = "Please Enter a Name!";
    } else if (
      document
        .querySelector("#usernameInput")
        .value.trim()
        .toLowerCase()
        .startsWith("!")
    ) {
      $$(
        "#welcomeScreen .middle .error"
      ).innerHTML = `Name cannot start with "!"!`;
    } else if ($$("#usernameInput").value.trim().toLowerCase() == "server") {
      $$("#welcomeScreen .middle .error").innerHTML = "This name is reserved!";
    } else if ($$("#usernameInput").value.trim().indexOf(" ") >= 0) {
      $$("#welcomeScreen .middle .error").innerHTML =
        "Names cannot have spaces in them!";
    } else if ($$("#usernameInput").value.trim().length > 45) {
      $$("#welcomeScreen .middle .error").innerHTML =
        "Names should be under 40 characters!";
    } else if (
      $$("#usernameInput").value.trim().toLowerCase() == "play" ||
      $$("#usernameInput").value.trim().toLowerCase() == "help" ||
      $$("#usernameInput").value.trim().toLowerCase() == "invite" ||
      $$("#usernameInput").value.trim().toLowerCase() == "leave" ||
      $$("#usernameInput").value.trim().toLowerCase() == "cancel" ||
      $$("#usernameInput").value.trim().toLowerCase() == "whisper"
    ) {
      $$("#welcomeScreen .middle .error").innerHTML =
        "Name reserved for command";
    } else {
      socket.emit("createUserRequest", $$("#usernameInput").value.trim());
    }
  });

  socket.on("createUserRequest", (res) => {
    console.log(`received: createUserRequest`);
    if (res == false) {
      $$("#welcomeScreen .middle .error").innerHTML = "Username Already Taken";
    } else {
      username = res.name;
      colour = res.colour;
      isAlreadyInChat = true;
      // remove welcome screen
      let a = $$("#welcomeScreen");
      a.style.opacity = "0";
      a.style.pointerEvents = "none";
    }

    socket.on("updateUsers", (connectedUsers) => {
      $$("#users .content").innerHTML = "";
      let me = document.createElement("div");
      me.style.margin = "auto";
      me.style.color = "white";
      me.style.border = `#${colour} solid`;
      me.style.display = "grid";
      me.style.padding = "4px";
      me.style.margin = "4px";
      $$("#users .content").appendChild(me);
      let myName = document.createElement("h3");
      myName.style.textAlign = "left";
      myName.textContent = `${username} (you!)`;
      me.appendChild(myName);
      me.addEventListener("mouseenter", () => {
        me.style.backgroundColor = "#434343";
      });
      me.addEventListener("mouseleave", () => {
        me.style.backgroundColor = "#212121";
      });

      connectedUsers.forEach((user) => {
        if (user.name != username) {
          let div = document.createElement("div");
          div.style.margin = "auto";
          div.style.color = "white";
          div.style.border = `#${user.colour} solid`;
          div.style.display = "grid";
          div.style.gridTemplateColumns = "1fr 1fr";
          div.style.padding = "4px";
          div.style.margin = "4px";

          let name = document.createElement("h3");
          name.style.textAlign = "left";
          name.textContent = user.name;
          div.appendChild(name);
          $$("#users .content").appendChild(div);
          div.addEventListener("mouseenter", () => {
            div.style.backgroundColor = "#434343";
          });
          div.addEventListener("mouseleave", () => {
            div.style.backgroundColor = "#212121";
          });
        }
      });
    });
  });

  socket.on("changeRoom", (id) => {
    console.log(`changed room : ${id}`);
    if (!id.startsWith("game")) {
      $$("#game").style.width = "0";
      isPlaying = false;
    }
    if (id == "room-000") {
      $$("header h1").textContent = "Main Chat";
    } else {
      $$("header h1").textContent = `Private Chat: ${id}`;
    }
    roomId = id;
  });

  // send/receive messages
  let sendMessage = () => {
    let msg = {
      message: $$("#textInput").value.trim(),
    };
    if (msg.message) {
      socket.emit("chat", msg);
      if (!msg.message.startsWith("!")) {
        addUserMessage(msg);
      }
      $$("#textInput").value = "";
    }
  };

  $$("#textInput").addEventListener("keypress", (e) => {
    if (e.key == "Enter") {
      sendMessage();
    }
  });
  document
    .querySelector("#sendMessageBTN")
    .addEventListener("click", sendMessage);

  // messaging
  socket.on("chat", (msg) => {
    if (isAlreadyInChat) {
      if (msg.name.toLowerCase() == "server") {
        addServerMessage(msg);
      } else {
        addOtherUserMessage(msg);
      }
    }
  });

  socket.on("whispered", (msg) => {
    let msgDiv = document.createElement("div");
    msgDiv.style.textAlign = "right";
    msgDiv.style.width = "calc(100% - 7px)";
    msgDiv.style.marginBottom = "10px";

    let innerDiv = document.createElement("div");
    msgDiv.appendChild(innerDiv);
    innerDiv.style.padding = "10px";
    innerDiv.style.maxWidth = "60%";
    innerDiv.style.width = "max-content";
    innerDiv.style.border = `#${colour} solid`;
    innerDiv.style.borderRadius = `5px`;
    innerDiv.style.marginLeft = "auto";
    innerDiv.style.marginRight = "0";
    innerDiv.style.background = "#ccc";

    let user = document.createElement("h4");
    innerDiv.appendChild(user);

    let msgContent = document.createElement("p");
    innerDiv.appendChild(msgContent);
    user.textContent = `(Whispered to ${msg.receiver})`;
    msgContent.textContent = msg.message;

    $$("#MessagesBox").appendChild(msgDiv);
    scrollNewestMessageIntoView();
  });

  // Invitations
  socket.on("updateInvitations", (invites) => {
    const inviteContent = $$("#invites .content");
    inviteContent.innerHTML = "";
    invites.sending.map((sned) => {
      let div = document.createElement("div");
      inviteContent.appendChild(div);
      div.style.width = "95%";
      div.style.border = `solid #${colour}`;
      let type = document.createElement("h2");
      div.appendChild(type);
      if (sned.type == "priv") {
        type.textContent = "Private Room Invite";
      } else if (sned.type == "rps") {
        type.textContent = "Game Invite";
      }

      let sentToTitle = document.createElement("h3");
      sentToTitle.textContent = "Sent to:";
      div.appendChild(sentToTitle);

      let sentTo = document.createElement("ul");
      div.appendChild(sentTo);

      sned.receiver.map((rec) => {
        let sentToUser = document.createElement("li");
        sentToUser.innerHTML = `- ${rec}`;
        sentTo.appendChild(sentToUser);
      });

      let cancelBtn = document.createElement("button");
      div.appendChild(cancelBtn);
      cancelBtn.style.marginLeft = "310px";
      cancelBtn.style.backgroundColor = "red";
      cancelBtn.style.padding = "5px";
      cancelBtn.style.border = "solid #121212";
      cancelBtn.style.borderRadius = "5px";
      cancelBtn.innerHTML = "Cancel";
      cancelBtn.addEventListener("click", () => {
        socket.emit("cancelInvite", sned.id);
      });
    });

    invites.receiving.map((rec) => {
      let div = document.createElement("div");
      inviteContent.appendChild(div);
      div.style.width = "95%";
      div.style.border = `solid #${rec.colour}`;
      let type = document.createElement("h2");
      div.appendChild(type);
      console.log(rec.type);
      if (rec.type == "priv") {
        type.textContent = "Private Room Invite";
      } else if (rec.type == "rps") {
        type.textContent = "Game Invite";
      }

      let sentToTitle = document.createElement("h3");
      sentToTitle.textContent = `Sent from: ${rec.sender}`;
      div.appendChild(sentToTitle);

      let acceptBtn = document.createElement("button");
      div.appendChild(acceptBtn);
      acceptBtn.style.marginLeft = "310px";
      acceptBtn.style.backgroundColor = "green";
      acceptBtn.style.padding = "5px";
      acceptBtn.style.border = "solid #121212";
      acceptBtn.style.borderRadius = "5px";
      acceptBtn.innerHTML = "Accept";
      acceptBtn.addEventListener("click", () => {
        socket.emit("acceptInvite", rec.id);
      });
    });
  });

  socket.on(
    "gameTimer",
    /**
     *
     * @param {Number} timer
     */
    (timer) => {
      let gameContent = $$("#game .content");
      gameContent.innerHTML = "";
      let timerDiv = document.createElement("h1");
      gameContent.appendChild(timerDiv);
      timerDiv.textContent = timer;
      timerDiv.classList.add("middle");
    }
  );

  socket.on("displayGameResults", (result) => {
    let gameContent = $$("#game .content");
    gameContent.innerHTML = "";
    let title = document.createElement("h4");
    gameContent.appendChild(title);
    title.innerHTML = `Game Results`;
    // complete info showing
    gameContent.innerHTML += `<p>your guess: ${result.usGuess}
    <br />opponents guess: ${result.opponentGuess}
    <br />winner: ${result.winner}
    <br />games played: ${result.roundsPlayed}
    <br />rounds won: ${result.roundsWon}</p>`;
  });

  socket.on("resetGame", (info) => {
    let gameContent = $$("#game .content");
    gameContent.innerHTML = `<h4>Game against ${info.opponent}</h4><p>games played/games won: ${info.roundsWon}/${info.roundsPlayed}</p>`;
    let choices = ["rock", "paper", "scissors", "lizard", "spock"];
    choices.map((ch) => {
      let button = document.createElement("button");
      gameContent.appendChild(button);
      button.innerHTML = ch;
      button.style.textAlign = "center";
      button.style.backgroundColor = "#121212";
      button.style.border = "solid #565656";
      button.style.borderRadius = "3x";
      button.style.padding = "3px";
      button.style.margin = "auto";
      button.style.marginBottom = "3px";
      button.style.color = "white";
      button.classList.add("gameChoice");
      button.addEventListener("click", () => {
        gameContent.innerHTML = "<p>now waiting for opponents response</p>";
        socket.emit("makeGameChoice", {
          id: roomId,
          choice: ch,
        });
      });
    });
  });
});

function addUserMessage(msg) {
  let msgDiv = document.createElement("div");
  msgDiv.style.textAlign = "right";
  msgDiv.style.width = "calc(100% - 7px)";
  msgDiv.style.marginBottom = "10px";

  let innerDiv = document.createElement("div");
  msgDiv.appendChild(innerDiv);
  innerDiv.style.padding = "10px";
  innerDiv.style.maxWidth = "60%";
  innerDiv.style.width = "max-content";
  innerDiv.style.border = `#${colour} solid`;
  innerDiv.style.borderRadius = `5px`;
  innerDiv.style.marginLeft = "auto";
  innerDiv.style.marginRight = "0";
  if (roomId == "room-000") {
    innerDiv.style.background = "#ccc";
  } else {
    innerDiv.style.backgroundColor = "#343434";
    innerDiv.style.color = "white";
  }

  let user = document.createElement("h4");
  innerDiv.appendChild(user);

  let msgContent = document.createElement("p");
  innerDiv.appendChild(msgContent);
  user.textContent = username;
  msgContent.textContent = msg.message;

  $$("#MessagesBox").appendChild(msgDiv);
  scrollNewestMessageIntoView();
}

function addServerMessage(msg) {
  let msgDiv = document.createElement("div");
  msgDiv.style.textAlign = "center";
  msgDiv.style.width = "calc(100% - 7px)";
  msgDiv.style.marginBottom = "10px";

  let innerDiv = document.createElement("div");
  msgDiv.appendChild(innerDiv);
  innerDiv.style.padding = "10px";
  innerDiv.style.maxWidth = "60%";
  innerDiv.style.width = "max-content";
  innerDiv.style.border = `#121212 solid`;
  innerDiv.style.borderRadius = `5px`;
  innerDiv.style.marginLeft = "auto";
  innerDiv.style.marginRight = "auto";
  innerDiv.style.background = "#ccc";
  innerDiv.style.whiteSpace = "pre-line";

  let user = document.createElement("h4");
  innerDiv.appendChild(user);

  let msgContent = document.createElement("p");
  innerDiv.appendChild(msgContent);
  user.textContent = "Server";
  msgContent.textContent = msg.message;

  $$("#MessagesBox").appendChild(msgDiv);
  scrollNewestMessageIntoView();
}

function addOtherUserMessage(msg) {
  let msgDiv = document.createElement("div");
  msgDiv.style.textAlign = "left";
  msgDiv.style.width = "calc(100% - 7px)";
  msgDiv.style.marginBottom = "10px";

  let innerDiv = document.createElement("div");
  msgDiv.appendChild(innerDiv);
  innerDiv.style.padding = "10px";
  innerDiv.style.maxWidth = "60%";
  innerDiv.style.width = "max-content";
  innerDiv.style.border = `#${msg.colour} solid`;
  innerDiv.style.borderRadius = `5px`;
  innerDiv.style.marginLeft = "8px";
  if (roomId == "room-000") {
    innerDiv.style.background = "#ccc";
  } else {
    innerDiv.style.backgroundColor = "#343434";
    innerDiv.style.color = "white";
  }

  let user = document.createElement("h4");
  innerDiv.appendChild(user);

  let msgContent = document.createElement("p");
  innerDiv.appendChild(msgContent);
  user.textContent = msg.name;
  msgContent.textContent = msg.message;

  $$("#MessagesBox").appendChild(msgDiv);
  scrollNewestMessageIntoView();
}

function scrollNewestMessageIntoView() {
  let msgs = document.querySelectorAll("#MessagesBox div");
  msgs[msgs.length - 1].scrollIntoView();
}

function pageSetup(soc) {
  const socket = soc;
  let textBarWidthDifference = 80;
  let chatBoxWidthDifference = 0;
  let headerWidthDifference = 0;

  // this is very out of place but its too late to change it
  socket.on("startGame", (opponent) => {
    console.log(opponent);
    isPlaying = true;
    openNav();
  });

  function openNav() {
    $$("#info").style.width = "400px";
    textBarWidthDifference = 470;
    chatBoxWidthDifference = 400;
    headerWidthDifference = 400;
    if (isPlaying) {
      $$("#game").style.width = "400px";
    }
    onWindowResize();
  }

  function closeNav() {
    $$("#info").style.width = "0";
    textBarWidthDifference = 80;
    chatBoxWidthDifference = 0;
    headerWidthDifference = 0;
    $$("#game").style.width = "0";
    onWindowResize();
  }

  function onWindowResize() {
    let textBar = $$("#textInput");
    let sendBtn = $$("#sendMessageBTN");
    let chatBox = $$("#MessagesBox");
    let header = $$("header");
    sendBtn.style.height = sendBtn.parentElement.height;
    textBar.style.width = `${window.innerWidth - textBarWidthDifference}px`;
    chatBox.style.height = `${window.innerHeight - 100}px`;
    chatBox.style.width = `${window.innerWidth - chatBoxWidthDifference}px`;
    header.style.width = `${window.innerWidth - headerWidthDifference}px`;
  }

  $$("#sideBarOpener").addEventListener("click", openNav);
  $$("#sideBarCloser").addEventListener("click", closeNav);
  window.addEventListener("resize", onWindowResize);
  onWindowResize();
}
