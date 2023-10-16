package ru.practicum.statsserver.mapper;

import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsserver.entity.Stats;
import ru.practicum.statsserver.util.LocalDateTimeParser;

public class StatsMapper {

    public static Stats toEntity(StatRequestDto statRequestDto) {
        if (statRequestDto == null) return null;

        Stats stats = new Stats();
        stats.setApp(statRequestDto.getApp());
        stats.setIp(statRequestDto.getIp());
        stats.setUri(statRequestDto.getUri());
        stats.setTimestamp(LocalDateTimeParser.toLDT(statRequestDto.getTimestamp()));

        return stats;
    }
}
