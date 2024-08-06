package be.vdab.startrek.werknemers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql("/werknemers.sql")
@AutoConfigureMockMvc
class WerknemerControllerTest {
    private final static String WERKNEMERS_TABLE = "werknemers";
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;


    WerknemerControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findAllVindtAlleWerknemers() throws Exception {
        var aantalWerknemers = JdbcTestUtils.countRowsInTable(jdbcClient,
                WERKNEMERS_TABLE);
        mockMvc.perform(get("/werknemers"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(aantalWerknemers));
    }
}