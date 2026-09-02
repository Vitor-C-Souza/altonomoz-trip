package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripPersistenceMapper {
    public TripDocument toDocument(Trip trip) {
        if (trip == null) {
            return null;
        }

        return TripDocument.builder()
                .os(trip.getOs())
                .data(trip.getData())
                .origem(trip.getOrigem())
                .paradas(trip.getParadas())
                .destino(trip.getDestino())
                .km(trip.getKm())
                .tempo(trip.getTempo())
                .valor(trip.getValor())
                .valorPorKm(trip.getValorPorKm())
                .valorPorMinuto(trip.getValorPorMinuto())
                .pago(trip.isPago())
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
                .paradas(doc.getParadas())
                .destino(doc.getDestino())
                .km(doc.getKm())
                .tempo(doc.getTempo())
                .valor(doc.getValor())
                .valorPorKm(doc.getValorPorKm())
                .valorPorMinuto(doc.getValorPorMinuto())
                .pago(doc.isPago())
                .build();
    }
}
