package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.port.in.GetRecebimentosPendentesUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.RecebimentoDiarioDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetRecebimentosPendentesService implements GetRecebimentosPendentesUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public List<RecebimentoDiarioDTO> execute() {
        List<Trip> tripsPendentes = tripRepositoryPort.findPendingTrips();

        Map<LocalDate, BigDecimal> agrupado = tripsPendentes.stream()
                .filter(t -> t.getData() != null && t.getValor() != null)
                .collect(Collectors.groupingBy(
                        Trip::getDataRecebimento,
                        Collectors.reducing(BigDecimal.ZERO, Trip::getValor, BigDecimal::add)
                ));

        return agrupado.entrySet().stream()
                .map(e -> new RecebimentoDiarioDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RecebimentoDiarioDTO::dia))
                .toList();
    }
}
