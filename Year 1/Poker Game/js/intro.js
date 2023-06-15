//Python gangggggg
let print = (selector) => console.log(selector);
document.getElementById("reForm").onsubmit = checkForm;

if (localStorage.getItem("username") !== null) {
  location.href = "game.html";
}

jQuery.validator.addMethod(
  "startMonCheck",
  function(mon) {
    if (mon > 15000 || mon < 5 || mon%3 != 0) {
      return false;
    }
    return true;
  },
  "Please enter a number between 5 and 1500 and is divisble by 3"
);

$("#reForm").validate({
  rules: {
    username: {
      required: true,
      pattern: /^[A-Z][a-z]{3}[0-5]{1}$/,
    },
    firstName: {
      required: true,
      pattern: /^[A-Za-z\s'`-]{0,19}[A-Za-z\s'-]{1}$/,
    },
    lastName: {
      required: true,
      pattern: /^[A-Za-z\s'`-]{0,29}[A-Za-z\s'-]{1}$/,
    },
    emailAddr: {
      required: true,
      pattern: /^[A-Za-z0-9_\.-]+[@]{1}[A-Za-z0-9_]+[\.](ca|org)$/,
    },
    phoneNumber: {
      required: true,
      pattern: /^[(]\d{3}[)][\s][\d]{3}[-][\d]{4}$/,
    },
    city: {
      required: true,
      pattern: /^[A-Za-z\s]{1,49}$/,
    },
    startingMoney: {
      required: true,
      startMonCheck : [parseInt(document.querySelector("#startingMoney").value)],
    },
  },
  messages: {
    username: {
      required: "Please Enter your username (ex. Gton5)",
    },
  },
});

function checkForm() {
  let isValid = true;

  let username = document.getElementById("username").value;
  let firstName = document.getElementById("firstName").value;
  let lastName = document.getElementById("lastName").value;
  let phone = document.getElementById("phoneNumber").value;
  let city = document.getElementById("city").value;
  let email = document.getElementById("emailAddr").value;
  let money = parseInt(document.getElementById("startingMoney").value);

  let errorMessage = document.querySelectorAll(".error");

  // let reg;
  // //check username
  // reg = new RegExp(/[A-Z][a-z]{3}[0-5]{1}/g);
  // if (reg.exec(username) != username) {
  //   isValid = false;
  //   errorMessage[0].innerHTML = `${username} is an invalid username "Xxxx5"`;
  // }
  // //check first name
  // reg = new RegExp(/[A-Za-z\s'`-]{0,19}[A-Za-z\s'-]{1}/g);
  // if (reg.exec(firstName) != firstName) {
  //   isValid = false;
  //   errorMessage[1].innerHTML = `${firstName} is an invalid first name`;
  // }
  // //check last name
  // reg = new RegExp(/[A-Za-z\s'`-]{0,29}[A-Za-z\s'-]{1}/g);
  // if (reg.exec(lastName) != lastName) {
  //   isValid = false;
  //   errorMessage[2].innerHTML = `${lastName} is an invalid last name`;
  // }
  // //check phone number
  // reg = new RegExp(/[(][\d]{3}[)][\s][\d]{3}[-][\d]{4}/g);
  // if (reg.exec(phone) != phone) {
  //   isValid = false;
  //   errorMessage[3].innerHTML = `${phone} is an invalid phone number "(###) ###-####"`;
  // }
  // //check city
  // reg = new RegExp(/[A-Za-z]{1,49}/g);
  // if (reg.exec(city) != city) {
  //   isValid = false;
  //   errorMessage[4].innerHTML = `${city} is an invalid city`;
  // }
  // //check email
  // reg = new RegExp(/[A-Za-z0-9_\.-]+[@]{1}[A-Za-z0-9_]+[\.](ca|org)/g);
  // if (!reg.exec(email)) {
  //   isValid = false;
  //   errorMessage[5].innerHTML = `${email} is an invalid email "xxxx@xxxx.ca/org"`;
  // }
  // //check starting money
  // if (money < 5 || money > 15000) {
  //   isValid = false;
  //   errorMessage[6].innerHTML = `${money} needs to be bigger than 5 and smaller than 15000`;
  // }
  // if (money % 3 != 0) {
  //   isValid = false;
  //   errorMessage[6].innerHTML = `${money} should be divisible by 3`;
  // }
  if (isValid) {
    localStorage.setItem("username", username);
    localStorage.setItem("firstName", firstName);
    localStorage.setItem("lastName", lastName);
    localStorage.setItem("phoneNum", phone);
    localStorage.setItem("city", city);
    localStorage.setItem("email", email);
    localStorage.setItem("bankRoll", money);
    let today = new Date();
    let ampm = today.getHours() >= 12 ? "p.m." : "a.m.";
    localStorage.setItem(
      "lastVisit",
      `Your last visit was ${today.getMonth()} ${today.getDate()}, ${today.getFullYear()} at ${today.getHours()}:${today.getMinutes()} ${ampm}`
    );
  }
  return isValid;
}
