package app.vitorcsouza.altonomoz_trip.infrastructure.exception;

public class TripAlreadyExistsException extends RuntimeException {

    public TripAlreadyExistsException(String os) {
        super("Já existe uma viagem registrada com a OS: " + os);
    }
}
