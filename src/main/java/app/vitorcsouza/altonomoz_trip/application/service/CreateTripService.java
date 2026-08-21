package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.port.in.CreateTripUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTripService implements CreateTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public Trip execute(Trip trip) {
        if (tripRepositoryPort.existsByOs(trip.getOs())) {
            throw new IllegalArgumentException("Já existe uma viagem registrada com a OS: " + trip.getOs());
        }
        return tripRepositoryPort.save(trip);
    }
}
