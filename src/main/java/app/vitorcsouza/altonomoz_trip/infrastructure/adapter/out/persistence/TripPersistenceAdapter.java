package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripPersistenceAdapter implements TripRepositoryPort {

    private final MongoTripRepository mongoRepository;
    private final TripPersistenceMapper mapper;

    @Override
    public Trip save(Trip trip) {
        TripDocument document = mapper.toDocument(trip);
        TripDocument saved = mongoRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByOs(String os) {
        return mongoRepository.existsById(os);
    }

    @Override
    public Optional<Trip> findByOs(String os) {
        return mongoRepository.findById(os).map(mapper::toDomain);
    }
}
