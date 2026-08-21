package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.port.in.ImportTripsFromSheetUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.CsvReaderPort;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportTripsFromSheetService implements ImportTripsFromSheetUseCase {

    private final CsvReaderPort csvReaderPort;
    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public void execute() {
        List<Trip> trips = csvReaderPort.readTrips();

        for (Trip trip : trips) {
            tripRepositoryPort.save(trip);
        }
    }
}
