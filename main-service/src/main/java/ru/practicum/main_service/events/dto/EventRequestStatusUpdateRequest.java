package ru.practicum.main_service.events.dto;

import lombok.Data;
import ru.practicum.main_service.events.enumerations.StateStatusRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    private StateStatusRequest status;

}
