"use strict"
import {byId, setText, toon, verberg} from "./util.js";
const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
document.querySelector("title").innerText = naam;
setText("werknemer", naam);
setText("hyperlink", naam);

byId("bestel").onclick = async function() {
    verbergFouten();
    const omschrijvingInput = byId("omschrijving");
    if (!omschrijvingInput.checkValidity()) {
        toon("omschrijvingsFout");
        omschrijvingInput.focus();
        return;
    }
    const bedragInput = byId("bedrag");
    if (!bedragInput.checkValidity()) {
        toon("bedragFout");
        bedragInput.focus();
        return;
    }
    const nieuweBestelling = {
        omschrijving: omschrijvingInput.value,
        bedrag: Number(bedragInput.value)
    };
    bestel(werknemer.id, nieuweBestelling);
}

function verbergFouten() {
    verberg("omschrijvingsFout");
    verberg("bedragFout");
    verberg("storing");
    verberg("conflict")
}
async function bestel(werknemerId, nieuweBestelling) {
    verberg("nietGevonden")
    const response = await fetch(`werknemers/${werknemerId}/nieuwebestelling`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(nieuweBestelling)
        });
    if (response.ok) {
        werknemer.budget -= nieuweBestelling.bedrag;
        sessionStorage.setItem("werknemer", JSON.stringify(werknemer));
        window.location = "vorigebestellingen.html";
    } else {
        switch (response.status) {
            case 404:
                toon("nietGevonden");
                break;
            case 409:
                const responseBody = await response.json();
                setText("conflict", responseBody.message);
                toon("conflict");
                break;
            default:
                toon("storing");
        }
    }
}