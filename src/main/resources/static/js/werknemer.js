"use strict"

import {byId, setText} from "./util.js";
const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`
document.querySelector("title").innerText = naam;
setText("naam", naam);
const imgFoto = byId("foto");
imgFoto.alt = naam;
imgFoto.src = `images/${werknemer.id}.jpg`;
setText("nummer", werknemer.id);
setText("budget", werknemer.budget);