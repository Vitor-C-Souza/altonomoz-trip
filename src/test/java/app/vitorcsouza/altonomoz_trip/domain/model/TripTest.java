package app.vitorcsouza.altonomoz_trip.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripTest {

    @Test
    void shouldCalculateValuePerKilometer() {
        Trip trip = Trip.builder()
                .km(new BigDecimal("125.50"))
                .valor(new BigDecimal("251.00"))
                .build();

        assertEquals(new BigDecimal("2.00"), trip.calcularValorPorKm());
    }

    @Test
    void shouldCalculateValuePerMinute() {
        Trip trip = Trip.builder()
                .tempo(Duration.ofMinutes(125))
                .valor(new BigDecimal("250.00"))
                .build();

        assertEquals(new BigDecimal("2.00"), trip.calcularValorPorMinuto());
    }

    @Test
    void shouldCalculateBothDerivedValues() {
        Trip trip = Trip.builder()
                .km(new BigDecimal("100"))
                .tempo(Duration.ofMinutes(50))
                .valor(new BigDecimal("500.00"))
                .build();

        trip.calcularValoresDerivados();

        assertEquals(new BigDecimal("5.00"), trip.getValorPorKm());
        assertEquals(new BigDecimal("10.00"), trip.getValorPorMinuto());
    }

    @Test
    void shouldReturnZeroForInvalidDistance() {
        Trip trip = Trip.builder()
                .km(BigDecimal.ZERO)
                .valor(new BigDecimal("100.00"))
                .build();

        assertEquals(BigDecimal.ZERO, trip.calcularValorPorKm());
    }

    @Test
    void shouldReturnZeroForInvalidDuration() {
        Trip trip = Trip.builder()
                .tempo(Duration.ZERO)
                .valor(new BigDecimal("100.00"))
                .build();

        assertEquals(BigDecimal.ZERO, trip.calcularValorPorMinuto());
    }
}
