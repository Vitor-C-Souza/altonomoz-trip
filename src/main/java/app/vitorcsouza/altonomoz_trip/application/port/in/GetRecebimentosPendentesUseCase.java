package app.vitorcsouza.altonomoz_trip.application.port.in;

import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.RecebimentoDiarioDTO;

import java.util.List;

public interface GetRecebimentosPendentesUseCase {
    List<RecebimentoDiarioDTO> execute();
}
