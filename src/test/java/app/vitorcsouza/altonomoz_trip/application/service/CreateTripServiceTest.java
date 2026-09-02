package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.exception.TripAlreadyExistsException;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateTripServiceTest {

    @Test
    void shouldCalculateDerivedValuesBeforeSaving() {
        FakeTripRepository repository = new FakeTripRepository();
        CreateTripService service = new CreateTripService(repository);

        Trip trip = Trip.builder()
                .os("OS-001")
                .km(new BigDecimal("100"))
                .tempo(Duration.ofMinutes(50))
                .valor(new BigDecimal("500.00"))
                .build();

        Trip saved = service.execute(trip);

        assertEquals(new BigDecimal("5.00"), saved.getValorPorKm());
        assertEquals(new BigDecimal("10.00"), saved.getValorPorMinuto());
        assertEquals(saved, repository.savedTrip);
    }

    @Test
    void shouldRejectDuplicateOrder() {
        FakeTripRepository repository = new FakeTripRepository();
        repository.existing = true;
        CreateTripService service = new CreateTripService(repository);

        Trip trip = Trip.builder().os("OS-001").build();

        assertThrows(TripAlreadyExistsException.class, () -> service.execute(trip));
    }

    private static class FakeTripRepository implements TripRepositoryPort {
        private boolean existing;
        private Trip savedTrip;

        @Override
        public Trip save(Trip trip) {
            savedTrip = trip;
            return trip;
        }

        @Override
        public boolean existsByOs(String os) {
            return existing;
        }

        @Override
        public Optional<Trip> findByOs(String os) {
            return Optional.empty();
        }

        @Override
        public List<Trip> findPendingTrips() {
            return List.of();
        }
    }
}
