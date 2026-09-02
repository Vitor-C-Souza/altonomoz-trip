package app.vitorcsouza.altonomoz_trip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "CSV_URL=",
        "spring.mongodb.uri=mongodb://localhost:27017/altonomoz-trip-test"
})
class AltonomozTripApplicationTests {

    @Test
    void contextLoads() {
    }

}
