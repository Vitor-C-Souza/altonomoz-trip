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
import java.util.List;

@Component
public class GoogleSheetCsvReaderAdapter implements CsvReaderPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String csvUrl;

    public GoogleSheetCsvReaderAdapter(@Value("${google.sheets.csv-url}") String csvUrl) {
        this.csvUrl = csvUrl;
    }

    @Override
    public List<Trip> readTrips() {
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
                        .destino(record.get("Destino"))
                        .km(parseToDouble(record.get("Km")))
                        .tempo(parseToDuration(record.get("Tempo")))
                        .valor(parseToBigDecimal(record.get("Valor")))
                        .build();

                trips.add(trip);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o CSV do Google Sheets: " + e.getMessage(), e);
        }

        return trips;
    }

    private LocalDateTime parseData(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) return LocalDateTime.now();
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

    private Double parseToDouble(String value) {
        if (value == null || value.isBlank()) return 0.0;
        return Double.parseDouble(value.replace(",", ".").trim());
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
        String cleanValue = value.replace("R$", "").replace(" ", "").replace(".", "").replace(",", ".").trim();
        return new BigDecimal(cleanValue);
    }
}
