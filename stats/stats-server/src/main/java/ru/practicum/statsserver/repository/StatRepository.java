package ru.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statsdto.StatResponseDto;
import ru.practicum.statsserver.entity.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stats, Long> {

    @Query("select new ru.practicum.statsdto.StatResponseDto(s.app, s.uri, COUNT(distinct s.ip))" +
            "from Stats as s " +
            "where s.timestamp > :start and s.timestamp < :end " +
            "and ((:uris) is null or s.uri in :uris) " +
            "group by s.app, s.uri " +
            "order by count (distinct s.ip) desc")
    List<StatResponseDto> findAllUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("select new ru.practicum.statsdto.StatResponseDto(s.app, s.uri, count(s.ip)) " +
            "from Stats as s " +
            "where s.timestamp between :start and :end " +
            "and ((:uris) is null or s.uri in :uris) " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<StatResponseDto> findAll(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

}
