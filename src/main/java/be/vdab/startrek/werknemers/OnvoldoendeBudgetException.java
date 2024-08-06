package be.vdab.startrek.werknemers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
class OnvoldoendeBudgetException extends RuntimeException {

    public OnvoldoendeBudgetException() {
        super("Onvoldoende budget");
    }
}
