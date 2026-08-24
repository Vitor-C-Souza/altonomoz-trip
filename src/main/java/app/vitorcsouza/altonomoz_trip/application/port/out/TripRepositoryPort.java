package app.vitorcsouza.altonomoz_trip.application.port.out;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;

import java.util.List;
import java.util.Optional;

public interface TripRepositoryPort {
    Trip save(Trip trip);
    boolean existsByOs(String os);
    Optional<Trip> findByOs(String os);

    List<Trip> findPendingTrips();
}
