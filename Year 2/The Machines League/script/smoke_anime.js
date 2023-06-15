let number = 100;
function bubble() {
  let b = document.createElement("div");
  let size = Math.random() * 200 + 50;
  let skew = Math.random() * 20;
  b.classList.add("smoke");
  b.style.width = size + "px";
  b.style.height = size + "px";
  b.style.transform =
    Math.random() < 0.5
      ? "skew(" + skew * -1 + "deg)"
      : "skew(" + skew + "deg)";
  b.style.left = Math.random() * (window.innerWidth - 75) + "px";
  b.style.animationDelay = Math.random() * 10 + "s";
  b.style.animationDuration = Math.random() * 10 + 2 + "s";
  document.body.appendChild(b);
}

setTimeout(function () {
  for (let i = 0; i < number; i++) {
    bubble();
  }
}, 1);
