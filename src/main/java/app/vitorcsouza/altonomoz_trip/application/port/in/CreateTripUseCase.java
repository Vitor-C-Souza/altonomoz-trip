package app.vitorcsouza.altonomoz_trip.application.port.in;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;

public interface CreateTripUseCase {
    Trip execute(Trip trip);
}
