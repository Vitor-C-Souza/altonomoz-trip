package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record GanhoMensalDTO(
        YearMonth mes,
        BigDecimal valor
) {
}
