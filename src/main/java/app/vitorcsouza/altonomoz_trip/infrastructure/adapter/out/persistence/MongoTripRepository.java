package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTripRepository extends MongoRepository<TripDocument, String> {
}
