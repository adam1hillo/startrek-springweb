package be.vdab.startrek.bestellingen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql({"/werknemers.sql", "/bestellingen.sql"})
@AutoConfigureMockMvc
class BestellingControllerTest {
    private static final String WERKNEMERS_TABLE = "werknemers";
    private static final String BESTELLINGEN_TABLE = "bestellingen";
    private static final Path TEST_RESOURCES = Path.of("src/test/resources");
    private final JdbcClient jdbcClient;
    private final MockMvc mockMvc;

    BestellingControllerTest(JdbcClient jdbcClient, MockMvc mockMvc) {
        this.jdbcClient = jdbcClient;
        this.mockMvc = mockMvc;
    }
    private long idVanWerknemerTest1() {
        return jdbcClient.sql("select id from werknemers where voornaam = 'test1'")
                .query(Long.class)
                .single();
    }

    @Test
    void findBestellingenVanVindtAlleBestellingenVanEenWerknemer() throws Exception {
        long idWerknemerTest1 = idVanWerknemerTest1();
        var aantalGevondenBestellingen = JdbcTestUtils.countRowsInTableWhere(jdbcClient, BESTELLINGEN_TABLE,
                "werknemerId = " + idWerknemerTest1);
        mockMvc.perform(get("/werknemers/{id}/bestellingen", idWerknemerTest1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(aantalGevondenBestellingen));
    }
    @Test
    void bestelVoegtEenBestellingToeEnWijzigtWerknemersBudget() throws Exception{
        long idWerknemerTest1 = idVanWerknemerTest1();
        String jsonData = Files.readString(TEST_RESOURCES.resolve("correcteBestelling.json"));
        mockMvc.perform(post("/werknemers/{id}/nieuwebestelling", idWerknemerTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpectAll(status().isOk());
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, WERKNEMERS_TABLE,
                "budget = 990 and id = " + idWerknemerTest1)).isOne();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, BESTELLINGEN_TABLE,
                "omschrijving = 'test4' and bedrag = 10 and werknemerId = " + idWerknemerTest1)).isOne();
    }
    @Test
    void bestelMetOnbestaandeWerknemerMislukt() throws Exception{
        String jsonData = Files.readString(TEST_RESOURCES.resolve("correcteBestelling.json"));
        mockMvc.perform(post("/werknemers/{id}/nieuwebestelling", Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isNotFound());
    }
    @ParameterizedTest
    @ValueSource(strings = {"bestellingZonderOmschrijving.json", "bestellingMetLegeOmschrijving.json",
            "bestellingZonderBedrag.json", "bestellingMetNegatiefBedrag.json"})
    void bestelMetOnjuisteDataMislukt(String besteling) throws Exception{
        long idWerknemerTest1 = idVanWerknemerTest1();
        String jsonData = Files.readString(TEST_RESOURCES.resolve(besteling));
        mockMvc.perform(post("/werknemers/{id}/nieuwebestelling", idWerknemerTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }
    @Test
    void bestelMetOnvoldoendeBudgetMislukt() throws Exception{
        long idWerknemerTest1 = idVanWerknemerTest1();
        String jsonData = Files.readString(TEST_RESOURCES.resolve("bestellingMetTeGrootBedrag.json"));
        mockMvc.perform(post("/werknemers/{id}/nieuwebestelling", idWerknemerTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isConflict());
    }
}
