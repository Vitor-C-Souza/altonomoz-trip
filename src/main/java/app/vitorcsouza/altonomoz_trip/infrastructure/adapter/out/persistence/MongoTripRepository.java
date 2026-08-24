package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoTripRepository extends MongoRepository<TripDocument, String> {
    List<TripDocument> findByPagoFalse();
}
