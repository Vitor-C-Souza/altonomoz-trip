package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.TripRequestDTO;
import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.TripResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TripWebMapper {
    public Trip toDomain(TripRequestDTO request) {
        if (request == null) {
            return null;
        }

        return Trip.builder()
                .os(request.os())
                .data(request.data())
                .origem(request.origem())
                .paradas(request.paradas())
                .destino(request.destino())
                .km(request.km())
                .tempo(request.tempo())
                .valor(request.valor())
                .build();
    }

    public TripResponseDTO toResponse(Trip trip) {
        if (trip == null) {
            return null;
        }

        String tempoFormatado = null;
        if (trip.getTempo() != null) {
            long segundosTotais = trip.getTempo().getSeconds();
            tempoFormatado = String.format("%02d:%02d:%02d",
                    segundosTotais / 3600,
                    (segundosTotais % 3600) / 60,
                    segundosTotais % 60);
        }

        return new TripResponseDTO(
                trip.getOs(),
                trip.getData(),
                trip.getOrigem(),
                trip.getDestino(),
                trip.getKm(),
                tempoFormatado,
                trip.getValor(),
                trip.getValorPorKm(),
                trip.getValorPorMinuto(),
                trip.isPago()
        );
    }
}
