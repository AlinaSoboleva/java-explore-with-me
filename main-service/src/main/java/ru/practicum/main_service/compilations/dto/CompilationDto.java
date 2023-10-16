package ru.practicum.main_service.compilations.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.main_service.events.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@ToString
public class CompilationDto {

    private Long id;

    private String title;


    @Builder.Default
    private List<EventShortDto> events = new ArrayList<>();

    private Boolean pinned;
}
