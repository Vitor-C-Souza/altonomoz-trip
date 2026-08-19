package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {

    public static ErrorResponseDTO of(int status, String error, String message) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, null);
    }

    public static ErrorResponseDTO of(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, fieldErrors);
    }
}
