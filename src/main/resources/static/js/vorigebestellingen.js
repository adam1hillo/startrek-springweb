"use strict"
import {byId, setText, toon} from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"))
const titel = document.querySelector("title");
titel.innerText = `${werknemer.voornaam} ${werknemer.familienaam}`;
setText("werknemer", `${werknemer.voornaam} ${werknemer.familienaam}`);
setText("hyperlink", `${werknemer.voornaam} ${werknemer.familienaam}`);
const response = await fetch(`werknemers/${werknemer.id}/bestellingen`);
if (response.ok) {
    const bestellingen = await response.json();
    const bestellingenBody = byId("bestellingenBody")
    for (const bestelling of bestellingen) {
        const tr = bestellingenBody.insertRow();
        tr.insertCell().innerText = bestelling.id;
        tr.insertCell().innerText = bestelling.omschrijving;
        tr.insertCell().innerText = bestelling.bedrag;
        tr.insertCell().innerText = new Date(bestelling.moment).toLocaleString("nl-BE");
    }
} else {
    toon("storing");
}