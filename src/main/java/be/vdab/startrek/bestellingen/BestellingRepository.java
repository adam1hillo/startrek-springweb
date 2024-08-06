package be.vdab.startrek.bestellingen;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BestellingRepository {

    private final JdbcClient jdbcClient;

    BestellingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<Bestelling> findByWerknemerId(long werknemerId) {
        String sql = """
                select id, werknemerId, omschrijving, bedrag, moment
                from bestellingen
                where werknemerId = ?
                order by id
                """;
        return jdbcClient.sql(sql)
                .param(werknemerId)
                .query(Bestelling.class)
                .list();
    }
    long create(Bestelling bestelling) {
        String sql = """
                insert into bestellingen (werknemerId, omschrijving, bedrag, moment)
                values (?, ?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(bestelling.getWerknemerId(), bestelling.getOmschrijving(), bestelling.getBedrag(), bestelling.getMoment())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }
}
