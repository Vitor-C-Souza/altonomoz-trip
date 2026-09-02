package app.vitorcsouza.altonomoz_trip.application.port.in;

import app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto.GanhoMensalDTO;

import java.util.List;

public interface GetGanhosMensaisUseCase {
    List<GanhoMensalDTO> execute();
}
