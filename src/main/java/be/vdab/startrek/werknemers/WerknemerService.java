package be.vdab.startrek.werknemers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
class WerknemerService {
    private final WerknemerRepository werknemerRepository;

    WerknemerService(WerknemerRepository werknemerRepository) {
        this.werknemerRepository = werknemerRepository;
    }
    List<Werknemer> findAll() {
        return werknemerRepository.findAll();
    }
}
