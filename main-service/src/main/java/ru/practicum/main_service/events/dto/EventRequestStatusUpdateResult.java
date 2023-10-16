package ru.practicum.main_service.events.dto;

import lombok.Data;
import ru.practicum.main_service.requests.dto.RequestDto;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {

    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;

}
