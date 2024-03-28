// Select The Elements
var hamburger_menu;

function declare() {
  hamburger_menu = document.querySelector(".hamburger-menu");
}

const main = document.querySelector("main");

declare();

function toggleMenu() {
  // Toggle the "active" class on the big-wrapper element
  document.querySelector('.big-wrapper').classList.toggle('active');
}

function events() {
  hamburger_menu.addEventListener("click", toggleMenu);
}

events();
const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});
