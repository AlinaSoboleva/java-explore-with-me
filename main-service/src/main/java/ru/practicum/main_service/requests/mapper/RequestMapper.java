package ru.practicum.main_service.requests.mapper;

import ru.practicum.main_service.requests.dto.RequestDto;
import ru.practicum.main_service.requests.entity.Request;

public class RequestMapper {

    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
