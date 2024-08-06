package be.vdab.startrek.werknemers;


import java.math.BigDecimal;


public class Werknemer {
    private final long id;
    private final String voornaam;
    private final String familienaam;
    private BigDecimal budget;

    public Werknemer(long id, String voornaam, String familienaam, BigDecimal budget) {
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.budget = budget;
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void bestel(BigDecimal bedrag) {
        if (bedrag.compareTo(budget) > 0) {
            throw new OnvoldoendeBudgetException();
        }
        budget = budget.subtract(bedrag);
    }
}
