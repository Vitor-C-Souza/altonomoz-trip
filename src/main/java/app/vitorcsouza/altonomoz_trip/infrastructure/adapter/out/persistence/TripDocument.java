package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trips")
public class TripDocument {
    @Id
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
}
