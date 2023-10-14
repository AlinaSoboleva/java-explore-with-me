package ru.practicum.main_service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import ru.practicum.main_service.events.entity.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@Data
@ToString
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;


    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    LocalDateTime eventDate;

    private Integer category;

    private Location location;

    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration = true;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
