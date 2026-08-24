package app.vitorcsouza.altonomoz_trip.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Trip {

    private String os;
    private LocalDateTime data;
    private String origem;
    private List<String> paradas;
    private String destino;
    private Double km;
    private Duration tempo;
    private BigDecimal valor;
    private BigDecimal valorPorKm;
    private BigDecimal valorPorMinuto;
    private boolean pago;

    public void calcularValoresDerivados() {
        this.valorPorKm = calcularValorPorKm();
        this.valorPorMinuto = calcularValorPorMinuto();
    }

    public BigDecimal calcularValorPorKm() {
        if (km == null || km == 0 || valor == null) {
            return BigDecimal.ZERO;
        }
        return valor.divide(BigDecimal.valueOf(km), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularValorPorMinuto() {
        if (tempo == null || tempo.isZero() || valor == null) {
            return BigDecimal.ZERO;
        }
        long totalMinutes = tempo.toMinutes();
        if (totalMinutes == 0) return BigDecimal.ZERO;

        return valor.divide(BigDecimal.valueOf(totalMinutes), 2, RoundingMode.HALF_UP);
    }

    public LocalDate getDataFechamentoDezena() {
        if (this.data == null) return null;
        LocalDate dataViagem = this.data.toLocalDate();
        int dia = dataViagem.getDayOfMonth();

        if (dia <= 10) return dataViagem.withDayOfMonth(10);
        if (dia <= 20) return dataViagem.withDayOfMonth(20);
        return dataViagem.withDayOfMonth(dataViagem.lengthOfMonth());
    }

    public LocalDate getDataRecebimento() {
        LocalDate fechamento = getDataFechamentoDezena();
        return fechamento != null ? fechamento.plusDays(20) : null;
    }
}
