package ru.practicum.main_service.statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.main_service.util.Constants.FORMATTER_FOR_DATETIME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    private final Statistic statistic;

    public Map<Long, Long> returnMapViewStats(List<Event> events, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        List<StatResponseDto> statResponseDtos = statistic.statsClient.getStats(rangeStart.format(FORMATTER_FOR_DATETIME),
                rangeEnd.format(FORMATTER_FOR_DATETIME), uris, true);

        Map<Long, Long> views = new HashMap<>();
        for (StatResponseDto statResponseDto : statResponseDtos) {
            Long id = Long.parseLong(statResponseDto.getUri().split("/events/")[1]);
            views.put(id, views.getOrDefault(id, 0L) + 1);
        }
        return views;
    }

    public List<StatResponseDto> getAllStats(List<String> uris, String start, String end) throws JsonProcessingException {
        return statistic.statsClient.getStats(start, end, uris, true);
    }

    @Transactional
    public void saveStats(StatRequestDto statRequestDto) {
        statistic.statsClient.saveHit(statRequestDto);
    }
}
