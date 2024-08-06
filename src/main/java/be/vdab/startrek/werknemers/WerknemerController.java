package be.vdab.startrek.werknemers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
class WerknemerController {
    private final WerknemerService werknemerService;

    WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }
    @GetMapping("werknemers")
    List<Werknemer> findAll() {
        return werknemerService.findAll();
    }

}
