package ru.practicum.main_service.requests.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.main_service.requests.entity.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class RequestDto {
    private LocalDateTime created;
    private Long event;

    private Long id;

    private Long requester;

    private RequestStatus status;
}
