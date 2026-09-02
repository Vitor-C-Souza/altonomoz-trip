package app.vitorcsouza.altonomoz_trip.infrastructure.adapter.out.csv;

import app.vitorcsouza.altonomoz_trip.application.port.out.CsvReaderPort;
import app.vitorcsouza.altonomoz_trip.domain.model.Trip;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GoogleSheetCsvReaderAdapter implements CsvReaderPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String csvUrl;

    public GoogleSheetCsvReaderAdapter(@Value("${google.sheets.csv-url:}") String csvUrl) {
        this.csvUrl = csvUrl;
    }

    @Override
    public List<Trip> readTrips() {
        if (csvUrl == null || csvUrl.isBlank()) {
            return List.of();
        }

        List<Trip> trips = new ArrayList<>();
        String csvContent = restTemplate.getForObject(csvUrl, String.class);

        if (csvContent == null || csvContent.isBlank()) {
            return trips;
        }

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (CSVParser parser = new CSVParser(new StringReader(csvContent), format)) {
            for (CSVRecord record : parser) {
                if (record.get("OS") == null || record.get("OS").isBlank()) continue;

                Trip trip = Trip.builder()
                        .os(record.get("OS"))
                        .data(parseData(record.get("Data")))
                        .origem(record.get("Origem"))
                        .paradas(parseParadas(record.get("Parada")))
                        .destino(record.get("Destino"))
                        .km(parseToBigDecimal(record.get("Km")))
                        .tempo(parseToDuration(record.get("Tempo")))
                        .valor(parseToBigDecimal(record.get("Valor")))
                        .build();

                validateBusinessRules(trip);
                trips.add(trip);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o CSV do Google Sheets: " + e.getMessage(), e);
        }

        return trips;
    }

    private void validateBusinessRules(Trip trip) {
        requireText(trip.getOs(), "OS");
        if (trip.getData() == null) {
            throw new IllegalArgumentException("Data é obrigatória para a viagem " + trip.getOs());
        }
        requireText(trip.getOrigem(), "Origem");

        boolean cancelada = "CANCELADA".equalsIgnoreCase(trip.getOrigem().trim());

        if (!cancelada) {
            requireText(trip.getDestino(), "Destino");

            if (trip.getKm() == null || trip.getKm().signum() <= 0) {
                throw new IllegalArgumentException("Km deve ser maior que zero para a viagem " + trip.getOs());
            }

            if (trip.getTempo() == null || trip.getTempo().isZero() || trip.getTempo().isNegative()) {
                throw new IllegalArgumentException("Tempo deve ser maior que zero para a viagem " + trip.getOs());
            }
        } else {
            if (trip.getKm() == null || trip.getKm().signum() < 0) {
                throw new IllegalArgumentException("Km não pode ser negativo para a viagem cancelada " + trip.getOs());
            }

            if (trip.getTempo() == null || trip.getTempo().isNegative()) {
                throw new IllegalArgumentException("Tempo não pode ser negativo para a viagem cancelada " + trip.getOs());
            }
        }

        if (trip.getValor() == null || trip.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero para a viagem " + trip.getOs());
        }
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " é obrigatório");
        }
    }

    private LocalDateTime parseData(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            throw new IllegalArgumentException("Data é obrigatória");
        }

        String cleanDate = rawDate.trim();

        try {
            return LocalDateTime.parse(cleanDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDate.parse(cleanDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
        } catch (DateTimeParseException ignored) {
        }

        return LocalDateTime.parse(cleanDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    private List<String> parseParadas(String rawParadas) {
        if (rawParadas == null || rawParadas.isBlank()) {
            return List.of();
        }

        return Arrays.stream(rawParadas.split("[;,\n]"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private Duration parseToDuration(String value) {
        if (value == null || value.isBlank()) return Duration.ZERO;
        String cleanValue = value.trim();

        if (cleanValue.contains(":")) {
            String[] parts = cleanValue.split(":");
            if (parts.length == 3) {
                return Duration.ofHours(Long.parseLong(parts[0]))
                        .plusMinutes(Long.parseLong(parts[1]))
                        .plusSeconds(Long.parseLong(parts[2]));
            } else if (parts.length == 2) {
                return Duration.ofMinutes(Long.parseLong(parts[0]))
                        .plusSeconds(Long.parseLong(parts[1]));
            }
        }
        return Duration.ofMinutes(Long.parseLong(cleanValue));
    }

    private BigDecimal parseToBigDecimal(String value) {
        if (value == null || value.isBlank()) return BigDecimal.ZERO;

        String cleanValue = value
                .replace("R$", "")
                .replace(" ", "")
                .trim();

        boolean hasComma = cleanValue.contains(",");
        boolean hasDot = cleanValue.contains(".");

        if (hasComma && hasDot) {
            if (cleanValue.lastIndexOf(',') > cleanValue.lastIndexOf('.')) {
                cleanValue = cleanValue.replace(".", "").replace(',', '.');
            } else {
                cleanValue = cleanValue.replace(",", "");
            }
        } else if (hasComma) {
            cleanValue = cleanValue.replace(',', '.');
        }

        return new BigDecimal(cleanValue);
    }
}
