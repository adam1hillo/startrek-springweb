package be.vdab.startrek.bestellingen;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class BestellingController {
    private final BestellingService bestellingService;

    BestellingController(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }
    @GetMapping("werknemers/{id}/bestellingen")
    List<Bestelling> findBestellingenVan(@PathVariable long id) {
        return bestellingService.findByWerknemerId(id);
    }

    @PostMapping("werknemers/{id}/nieuwebestelling")
    void bestel(@PathVariable long id, @RequestBody @Valid NieuweBestelling nieuweBestelling) {
        bestellingService.create(id, nieuweBestelling);
    }
}
