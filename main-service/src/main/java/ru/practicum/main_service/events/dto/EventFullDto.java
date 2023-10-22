package ru.practicum.main_service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.events.entity.Location;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.users.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@Data
@Builder
@ToString
public class EventFullDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime eventDate;


    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Long views;

    private Long rating;
}
