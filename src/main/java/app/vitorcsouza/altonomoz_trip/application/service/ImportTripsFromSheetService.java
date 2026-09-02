package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.exception.TripAlreadyExistsException;
import app.vitorcsouza.altonomoz_trip.application.port.in.ImportTripsFromSheetUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.CsvReaderPort;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ImportTripsFromSheetService implements ImportTripsFromSheetUseCase {

    private final CsvReaderPort csvReaderPort;
    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public void execute() {
        List<Trip> trips = csvReaderPort.readTrips();

        for (Trip trip : trips) {
            if (tripRepositoryPort.existsByOs(trip.getOs())) {
                continue;
            }

            trip.calcularValoresDerivados();

            try {
                tripRepositoryPort.save(trip);
            } catch (TripAlreadyExistsException exception) {
                // Another import may have inserted the same OS between the existence check and save.
            }
        }
    }
}
