package app.vitorcsouza.altonomoz_trip.application.port.out;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;

import java.util.List;

public interface CsvReaderPort {
    List<Trip> readTrips();
}
