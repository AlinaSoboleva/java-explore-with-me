package ru.practicum.main_service.events.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.users.dto.UserShortDto;

@Builder
@Data
@ToString
public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private String eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
