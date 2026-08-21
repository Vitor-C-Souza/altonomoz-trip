package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web;

import app.vitorcsouza.altonomoz_trip.application.port.in.ImportTripsFromSheetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips/import")
@RequiredArgsConstructor
public class TripImportController {

    private final ImportTripsFromSheetUseCase importTripsFromSheetUseCase;

    @PostMapping
    public ResponseEntity<String> importCsv() {
        try {
            importTripsFromSheetUseCase.execute();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao importar planilha: " + e.getMessage());
        }
    }
}
