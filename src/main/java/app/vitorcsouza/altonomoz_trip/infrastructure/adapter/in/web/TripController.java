package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web;

import app.vitorcsouza.altonomoz_trip.application.port.in.CreateTripUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.GetRecebimentosPendentesUseCase;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.RecebimentoDiarioDTO;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.TripRequestDTO;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.TripResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final CreateTripUseCase createTripUseCase;
    private final GetRecebimentosPendentesUseCase getRecebimentosPendentesUseCase;
    private final TripWebMapper mapper;

    @PostMapping
    public ResponseEntity<TripResponseDTO> create(@RequestBody @Valid TripRequestDTO request, UriComponentsBuilder builder) {
        Trip tripToCreate = mapper.toDomain(request);
        Trip createdTrip = createTripUseCase.execute(tripToCreate);
        TripResponseDTO response = mapper.toResponse(createdTrip);
        URI uri = builder.path("/api/v1/trips/{id}").buildAndExpand(createdTrip.getOs()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/recebimentos/pendentes")
    public ResponseEntity<List<RecebimentoDiarioDTO>> getRecebimentosPendentes() {
        return ResponseEntity.ok(getRecebimentosPendentesUseCase.execute());
    }
}
