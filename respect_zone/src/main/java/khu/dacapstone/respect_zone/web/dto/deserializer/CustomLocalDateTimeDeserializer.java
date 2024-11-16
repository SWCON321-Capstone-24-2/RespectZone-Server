package khu.dacapstone.respect_zone.web.dto.deserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_DATE_TIME, // 2024-03-19T15:30:45.123+09:00
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"), // 2024-03-19T15:30:45.123
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // 2024-03-19T15:30:45
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), // 2024-03-19 15:30:45
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"), // 2024-03-19T15:30
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") // 2024-03-19 15:30
    );

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText().trim();

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        throw new IOException("Unable to parse date: " + dateStr);
    }
}