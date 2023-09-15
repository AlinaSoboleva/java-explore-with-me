package ru.practicum.statsserver.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;
import ru.practicum.statsserver.service.StatsService;
import ru.practicum.statsserver.util.LocalDateTimeParser;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    void saveHit(@RequestBody @Valid StatRequestDto statRequestDto) {
        log.info("Сохранение информации, что к эндпоинту был запрос: " + statRequestDto);
        service.saveHit(statRequestDto);
        log.info("Информация сохранена");
    }

    @GetMapping("/stats")
    List<StatResponseDto> getStatistics(@RequestParam("start") String start,
                                        @RequestParam("end") String end,
                                        @RequestParam(value = "uris", required = false) List<String> uris,
                                        @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        log.info("Получение статистики по посещениям с {}  по {}", start, end);
        List<StatResponseDto> responseDtos = service.getStatistics(LocalDateTimeParser.parse(start),
                LocalDateTimeParser.parse(end),
                uris,
                unique);
        log.info("Статистика полученна : {}", responseDtos);
        return responseDtos;
    }
}
