package app.vitorcsouza.altonomoz_trip.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Trip {

    private String os;
    private LocalDateTime data;
    private String origem;
    private String destino;
    private Double km;
    private Duration tempo;
    private BigDecimal valor;

    public BigDecimal calcularValorPorKm() {
        if (km == null || km == 0) {
            return BigDecimal.ZERO;
        }
        return valor.divide(BigDecimal.valueOf(km), 2, RoundingMode.HALF_UP);
    }
}
