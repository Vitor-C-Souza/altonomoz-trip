package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecebimentoDiarioDTO(
        LocalDate dia,
        BigDecimal valor
) {
}
