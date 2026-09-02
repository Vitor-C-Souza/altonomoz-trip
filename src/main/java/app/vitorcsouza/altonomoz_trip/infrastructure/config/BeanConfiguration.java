package app.vitorcsouza.altonomoz_trip.infrastructure.config;

import app.vitorcsouza.altonomoz_trip.application.port.in.CreateTripUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.GetGanhosMensaisUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.GetRecebimentosPendentesUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.in.ImportTripsFromSheetUseCase;
import app.vitorcsouza.altonomoz_trip.application.port.out.CsvReaderPort;
import app.vitorcsouza.altonomoz_trip.application.port.out.TripRepositoryPort;
import app.vitorcsouza.altonomoz_trip.application.service.CreateTripService;
import app.vitorcsouza.altonomoz_trip.application.service.GetGanhosMensaisService;
import app.vitorcsouza.altonomoz_trip.application.service.GetRecebimentosPendentesService;
import app.vitorcsouza.altonomoz_trip.application.service.ImportTripsFromSheetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public CreateTripUseCase createTripUseCase(TripRepositoryPort tripRepositoryPort) {
        return new CreateTripService(tripRepositoryPort);
    }

    @Bean
    public ImportTripsFromSheetUseCase importTripsFromSheetUseCase(
            CsvReaderPort csvReaderPort,
            TripRepositoryPort tripRepositoryPort) {
        return new ImportTripsFromSheetService(csvReaderPort, tripRepositoryPort);
    }

    @Bean
    public GetRecebimentosPendentesUseCase getRecebimentosPendentesUseCase(TripRepositoryPort tripRepositoryPort) {
        return new GetRecebimentosPendentesService(tripRepositoryPort);
    }

    @Bean
    public GetGanhosMensaisUseCase getGanhosMensaisUseCase(TripRepositoryPort tripRepositoryPort) {
        return new GetGanhosMensaisService(tripRepositoryPort);
    }
}
