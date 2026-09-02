package app.vitorcsouza.altonomoz_trip.application.service;

import app.vitorcsouza.altonomoz_trip.application.port.in.GetGanhosMensaisUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.GanhoMensalDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetGanhosMensaisService implements GetGanhosMensaisUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    @Override
    public List<GanhoMensalDTO> execute() {
        Map<YearMonth, BigDecimal> agrupado = tripRepositoryPort.findPendingTrips().stream()
                .filter(trip -> trip.getDataRecebimento() != null && trip.getValor() != null)
                .collect(Collectors.groupingBy(
                        trip -> YearMonth.from(trip.getDataRecebimento()),
                        Collectors.reducing(BigDecimal.ZERO, Trip::getValor, BigDecimal::add)
                ));

        return agrupado.entrySet().stream()
                .map(entry -> new GanhoMensalDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(GanhoMensalDTO::mes))
                .toList();
    }
}
