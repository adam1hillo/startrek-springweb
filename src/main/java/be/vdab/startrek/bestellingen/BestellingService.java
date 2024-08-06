package be.vdab.startrek.bestellingen;

import be.vdab.startrek.werknemers.Werknemer;
import be.vdab.startrek.werknemers.WerknemerNietGevondenException;
import be.vdab.startrek.werknemers.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final WerknemerRepository werknemerRepository;

    BestellingService(BestellingRepository bestellingRepository, WerknemerRepository werknemerRepository) {
        this.bestellingRepository = bestellingRepository;
        this.werknemerRepository = werknemerRepository;
    }

    List<Bestelling> findByWerknemerId(long werknemerId) {
        return bestellingRepository.findByWerknemerId(werknemerId);
    }

    @Transactional
    void create(long werknemerId, NieuweBestelling nieuweBestelling) {
        Werknemer werknemer = werknemerRepository.findAndLockById(werknemerId)
                .orElseThrow(()-> new WerknemerNietGevondenException(werknemerId));
        werknemer.bestel(nieuweBestelling.bedrag());
        werknemerRepository.updateBudget(werknemer.getBudget(), werknemer.getId());
        Bestelling bestelling = new Bestelling(0, werknemerId, nieuweBestelling.omschrijving(), nieuweBestelling.bedrag(), LocalDateTime.now());
        bestellingRepository.create(bestelling);
    }
}
