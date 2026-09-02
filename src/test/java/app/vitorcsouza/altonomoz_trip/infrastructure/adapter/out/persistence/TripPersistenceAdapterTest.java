package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.persistence;

import app.vitorcsouza.altonomoz_trip.application.exception.TripAlreadyExistsException;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TripPersistenceAdapterTest {

    @Test
    void shouldTranslateDuplicateKeyToDomainException() {
        MongoTripRepository mongoRepository = mock(MongoTripRepository.class);
        TripPersistenceMapper mapper = mock(TripPersistenceMapper.class);
        TripPersistenceAdapter adapter = new TripPersistenceAdapter(mongoRepository, mapper);
        Trip trip = Trip.builder().os("OS-001").build();

        when(mapper.toDocument(trip)).thenReturn(TripDocument.builder().os("OS-001").build());
        when(mongoRepository.save(any(TripDocument.class)))
                .thenThrow(new DuplicateKeyException("duplicate key"));

        assertThrows(TripAlreadyExistsException.class, () -> adapter.save(trip));
    }

    @Test
    void shouldReturnPendingTripsMappedToDomain() {
        MongoTripRepository mongoRepository = mock(MongoTripRepository.class);
        TripPersistenceMapper mapper = mock(TripPersistenceMapper.class);
        TripPersistenceAdapter adapter = new TripPersistenceAdapter(mongoRepository, mapper);
        TripDocument document = TripDocument.builder().os("OS-001").pago(false).build();
        Trip trip = Trip.builder().os("OS-001").build();

        when(mongoRepository.findByPagoFalse()).thenReturn(List.of(document));
        when(mapper.toDomain(document)).thenReturn(trip);

        org.junit.jupiter.api.Assertions.assertEquals(List.of(trip), adapter.findPendingTrips());
    }
}
