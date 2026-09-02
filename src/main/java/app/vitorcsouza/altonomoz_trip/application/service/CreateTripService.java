package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.port.in.CreateTripUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.exception.TripAlreadyExistsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTripService implements CreateTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public Trip execute(Trip trip) {
        if (tripRepositoryPort.existsByOs(trip.getOs())) {
            throw new TripAlreadyExistsException(trip.getOs());
        }

        trip.calcularValoresDerivados();
        return tripRepositoryPort.save(trip);
    }
}
