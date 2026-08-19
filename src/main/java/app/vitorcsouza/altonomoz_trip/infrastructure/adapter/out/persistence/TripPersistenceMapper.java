package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TripPersistenceMapper {
    public TripDocument toDocument(Trip trip) {
        if (trip == null) {
            return null;
        }

        BigDecimal valorPorKm = BigDecimal.ZERO;
        if (trip.getKm() != null && trip.getKm() > 0 && trip.getValor() != null) {
            valorPorKm = trip.getValor().divide(BigDecimal.valueOf(trip.getKm()), 2, RoundingMode.HALF_UP);
        }

        return TripDocument.builder()
                .os(trip.getOs())
                .data(trip.getData())
                .origem(trip.getOrigem())
                .destino(trip.getDestino())
                .km(trip.getKm())
                .tempo(trip.getTempo())
                .valor(trip.getValor())
                .valorPorKm(valorPorKm)
                .build();
    }

    public Trip toDomain(TripDocument doc) {
        if (doc == null) {
            return null;
        }

        return Trip.builder()
                .os(doc.getOs())
                .data(doc.getData())
                .origem(doc.getOrigem())
                .destino(doc.getDestino())
                .km(doc.getKm())
                .tempo(doc.getTempo())
                .valor(doc.getValor())
                .build();
    }
}
