package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.exception.TripAlreadyExistsException;
import app.vitorcsouza.altonomoz_trip.application.port.out.CsvReaderPort;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImportTripsFromSheetServiceTest {

    @Test
    void shouldImportNewTripsWithDerivedValues() {
        FakeCsvReader reader = new FakeCsvReader(List.of(trip("OS-001")));
        FakeTripRepository repository = new FakeTripRepository();

        new ImportTripsFromSheetService(reader, repository).execute();

        assertEquals(1, repository.savedTrips.size());
        Trip saved = repository.savedTrips.getFirst();
        assertEquals(new BigDecimal("5.00"), saved.getValorPorKm());
        assertEquals(new BigDecimal("10.00"), saved.getValorPorMinuto());
    }

    @Test
    void shouldSkipTripsThatAlreadyExist() {
        FakeCsvReader reader = new FakeCsvReader(List.of(trip("OS-001")));
        FakeTripRepository repository = new FakeTripRepository();
        repository.existingOs.add("OS-001");

        new ImportTripsFromSheetService(reader, repository).execute();

        assertEquals(0, repository.savedTrips.size());
    }

    @Test
    void shouldIgnoreDuplicateCreatedBetweenCheckAndSave() {
        FakeCsvReader reader = new FakeCsvReader(List.of(trip("OS-001")));
        FakeTripRepository repository = new FakeTripRepository();
        repository.throwDuplicateOnSave = true;

        new ImportTripsFromSheetService(reader, repository).execute();

        assertEquals(0, repository.savedTrips.size());
    }

    private static Trip trip(String os) {
        return Trip.builder()
                .os(os)
                .km(new BigDecimal("100"))
                .tempo(Duration.ofMinutes(50))
                .valor(new BigDecimal("500.00"))
                .build();
    }

    private static class FakeCsvReader implements CsvReaderPort {
        private final List<Trip> trips;

        private FakeCsvReader(List<Trip> trips) {
            this.trips = trips;
        }

        @Override
        public List<Trip> readTrips() {
            return trips;
        }
    }

    private static class FakeTripRepository implements TripRepositoryPort {
        private final List<Trip> savedTrips = new ArrayList<>();
        private final List<String> existingOs = new ArrayList<>();
        private boolean throwDuplicateOnSave;

        @Override
        public Trip save(Trip trip) {
            if (throwDuplicateOnSave) {
                throw new TripAlreadyExistsException(trip.getOs());
            }
            savedTrips.add(trip);
            return trip;
        }

        @Override
        public boolean existsByOs(String os) {
            return existingOs.contains(os);
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
