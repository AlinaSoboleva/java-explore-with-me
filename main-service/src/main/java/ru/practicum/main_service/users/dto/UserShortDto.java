package ru.practicum.main_service.users.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class UserShortDto {
    private Long id;
    private String name;
}
