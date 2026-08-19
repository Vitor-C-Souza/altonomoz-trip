package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripRequestDTO(
        @NotBlank(message = "A OS é obrigatória.")
        String os,

        @NotNull(message = "A data é obrigatória.")
        LocalDateTime data,

        @NotBlank(message = "A origem é obrigatória.")
        String origem,

        @NotBlank(message = "O destino é obrigatório.")
        String destino,

        @NotNull(message = "A distância em km é obrigatória.")
        @Positive(message = "A distância em km deve ser maior que zero.")
        Double km,

        @NotNull(message = "O tempo em minutos é obrigatório.")
        @Positive(message = "O tempo em minutos deve ser maior que zero.")
        Integer tempo,

        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor
) {
}
