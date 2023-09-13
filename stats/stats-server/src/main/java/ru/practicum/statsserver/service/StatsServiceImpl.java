package ru.practicum.statsserver.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;
import ru.practicum.statsserver.mapper.StatsMapper;
import ru.practicum.statsserver.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatRepository statRepository;

    @Override
    @Transactional
    public void saveHit(StatRequestDto statRequestDto) {
        statRepository.save(StatsMapper.toEntity(statRequestDto));
    }

    @Override
    public List<StatResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            return statRepository.findAllUnique(start, end, uris);
        }
        return statRepository.findAll(start, end, uris);
    }
}
