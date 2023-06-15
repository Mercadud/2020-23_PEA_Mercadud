window.onload = correctUser();
let something = new Round();
let user = new User(
  localStorage.getItem("username"),
  localStorage.getItem("firstName"),
  localStorage.getItem("lastName"),
  parseInt(localStorage.getItem("bankRoll"))
);

document.getElementById("pSwitch").style.display = "none";
document.getElementById("pPlayAgain").style.display = "none";
document.getElementById("youLose").style.display = "none";

document.getElementById("submitBid").addEventListener("click", placeBet);
document.getElementById("switchSub").addEventListener("click", replaceCard);
document.getElementById("playAgain").addEventListener("click", playAgain);
document.getElementById("leaveNow").addEventListener("click", leave);

let selectedCards = [];
let selectedCardsCoulour = [];
function placeBet() {
  let bid = parseInt(document.getElementById("betInput").value);
  if (bid < 0 || bid > user.Money || !bid) {
    document.getElementById("errorMessageBid").innerHTML = `ERR: invalid Bid`;
    return false;
  } else {
    document.getElementById("errorMessageBid").innerHTML = ``;
    user.placeBet(bid);
    document.getElementById("pBet").style.display = "none";
    document.getElementById("pSwitch").style.display = "block";
    something.Hand.updatePictures();
    let cards = document.querySelectorAll("img");
    for (let i = 0; i < cards.length; i++) {
      cards[i].addEventListener("click", function (e) {
        let card = e.target;
        if (selectedCards.indexOf(card.parentElement.id) == -1) {
          selectedCards.push(card.parentElement.id);
          selectedCardsCoulour.push(card);
          card.style.width = "80%";
          card.setAttribute(`class`, `darken`);
        } else {
          selectedCards.pop(card.parentElement.id);
          selectedCardsCoulour.pop(card);
          card.style.width = "100%";
        }
      });
    }
  }
}

function playAgain() {
  document.getElementById("pPlayAgain").style.display = "none";
  document.getElementById("pBet").style.display = "block";
  document.getElementById("Winner").innerHTML = ``;
  something = new Round();
}

function leave() {
  document.getElementById(
    "youLose"
  ).innerHTML = `You ended the game with ${user.Money}$<br> well done!`;
  document.getElementById("pPlayAgain").style.display = "none";
  document.getElementById("pBet").style.display = "none";
  document.getElementById("Winner").innerHTML = ``;
  document.getElementById("youLose").style.display = "block";
  let today = new Date();
  let ampm = time.getHours() >= 12 ? "p.m." : "a.m.";
  localStorage.setItem(
    "lastVisit",
    `Your last visit was ${today.getMonth()} ${today.getDate()}, ${today.getFullYear()} at ${today.getHours()}:${today.getMinutes()} ${ampm}`
  );
}

function correctUser() {
  let popup = document.getElementById("popupWindow");

  document.getElementById(
    "poppers"
  ).innerHTML = `Welcome back, ${localStorage.getItem(
    "firstName"
  )} ${localStorage.getItem(
    "lastName"
  )}.<br> Your phone number is: ${localStorage.getItem(
    "phoneNum"
  )} and you are located in ${localStorage.getItem(
    "city"
  )}.<br>You have ${localStorage.getItem(
    "bankRoll"
  )} left in your bank account`;

  popup.style.top = `0px`;
  popup.style.left = `0px`;

  let butt = document.querySelectorAll("button");
  butt[0].addEventListener("click", function () {
    popup.style.display = "none";
  });

  butt[1].addEventListener("click", function () {
    localStorage.removeItem("username");
    localStorage.removeItem("firstName");
    localStorage.removeItem("lastName");
    localStorage.removeItem("phoneNum");
    localStorage.removeItem("city");
    localStorage.removeItem("email");
    localStorage.removeItem("bankRoll");
    localStorage.removeItem("lastVisit");
    location.href = "index.html";
  });
}

let imgColour = 1;

function replaceCard() {
  let hand = something.Hand;
  for (let i = 0; i < selectedCards.length; i++) {
    hand.replaceCard(parseInt(selectedCards[i].charAt(4)));
    document
      .querySelectorAll("img")
      [selectedCards[i].charAt(4)].setAttribute(`class`, `lighten`);
  }
  selectedCards = [];
  selectedCardsCoulour = [];
  something.confirmHand();
  if (user.Money < 1) {
    document.getElementById("youLose").style.display = "block";
    document.getElementById("pSwitch").style.display = "none";
    localStorage.removeItem("username");
    localStorage.removeItem("firstName");
    localStorage.removeItem("lastName");
    localStorage.removeItem("phoneNum");
    localStorage.removeItem("city");
    localStorage.removeItem("email");
    localStorage.removeItem("bankRoll");
    localStorage.removeItem("lastVisit");
  } else {
    document.getElementById("pPlayAgain").style.display = "block";
    document.getElementById("pSwitch").style.display = "none";
    $(".congrats")[0].style.display = "block";
    $(".congrats").animate({opacity: "1"}, "slow", "linear", function() {
      $(".congrats").animate({opacity: "0"}, "slow", "linear", function() {
        $(".congrats")[0].style.display = "none";
      });
    });
  }
}
