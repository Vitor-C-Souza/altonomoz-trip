package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripResponseDTO(
        String os,
        LocalDateTime data,
        String origem,
        String destino,
        BigDecimal km,
        String tempo,
        BigDecimal valor,
        BigDecimal valorPorKm,
        BigDecimal valorPorMinuto,
        boolean pago
) {
}
