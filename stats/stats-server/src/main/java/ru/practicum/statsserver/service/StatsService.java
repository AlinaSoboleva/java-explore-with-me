package ru.practicum.statsserver.service;

import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveHit(StatRequestDto statRequestDto);

    List<StatResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
