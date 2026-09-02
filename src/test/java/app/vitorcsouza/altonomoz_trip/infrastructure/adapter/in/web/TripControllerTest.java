package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web;

import app.vitorcsouza.altonomoz_trip.application.exception.TripAlreadyExistsException;
import app.vitorcsouza.altonomoz_trip.application.port.in.CreateTripUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.GetGanhosMensaisUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.GetRecebimentosPendentesUseCase;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TripControllerTest {

    private CreateTripUseCase createTripUseCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        createTripUseCase = mock(CreateTripUseCase.class);
        GetRecebimentosPendentesUseCase pendingUseCase = mock(GetRecebimentosPendentesUseCase.class);
        GetGanhosMensaisUseCase monthlyUseCase = mock(GetGanhosMensaisUseCase.class);

        TripController controller = new TripController(
                createTripUseCase,
                pendingUseCase,
                monthlyUseCase,
                new TripWebMapper()
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldCreateTripAndReturnCreated() throws Exception {
        Trip created = trip("OS-001");
        created.calcularValoresDerivados();
        when(createTripUseCase.execute(org.mockito.ArgumentMatchers.any(Trip.class)))
                .thenReturn(created);

        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "os": "OS-001",
                                  "data": "2026-09-02T10:00:00",
                                  "origem": "São Paulo",
                                  "paradas": ["Santos"],
                                  "destino": "Praia Grande",
                                  "km": 100,
                                  "tempo": "PT50M",
                                  "valor": 500
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/trips/OS-001"))
                .andExpect(jsonPath("$.os").value("OS-001"))
                .andExpect(jsonPath("$.valorPorKm").value(5.0))
                .andExpect(jsonPath("$.valorPorMinuto").value(10.0));
    }

    @Test
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "os": "",
                                  "data": null,
                                  "origem": "",
                                  "destino": "",
                                  "km": 0,
                                  "tempo": "PT0S",
                                  "valor": -1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnConflictWhenTripAlreadyExists() throws Exception {
        when(createTripUseCase.execute(org.mockito.ArgumentMatchers.any(Trip.class)))
                .thenThrow(new TripAlreadyExistsException("OS-001"));

        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "os": "OS-001",
                                  "data": "2026-09-02T10:00:00",
                                  "origem": "São Paulo",
                                  "destino": "Praia Grande",
                                  "km": 100,
                                  "tempo": "PT50M",
                                  "valor": 500
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    private static Trip trip(String os) {
        return Trip.builder()
                .os(os)
                .data(LocalDateTime.of(2026, 9, 2, 10, 0))
                .origem("São Paulo")
                .paradas(List.of("Santos"))
                .destino("Praia Grande")
                .km(new BigDecimal("100"))
                .tempo(Duration.ofMinutes(50))
                .valor(new BigDecimal("500.00"))
                .build();
    }
}
