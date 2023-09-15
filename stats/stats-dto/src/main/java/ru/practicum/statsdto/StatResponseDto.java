package ru.practicum.statsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
