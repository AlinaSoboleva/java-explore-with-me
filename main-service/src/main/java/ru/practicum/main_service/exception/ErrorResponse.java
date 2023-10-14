package ru.practicum.main_service.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ErrorResponse {
    private HttpStatus status;

    private String reason;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime timestamp;
}
