package ru.practicum.statsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    public StatsClient(@Value("${STATS_SERVER_URL:http://localhost:9090}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<StatResponseDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("unique", unique);

        if (uris != null) {
            StringBuilder urisString = new StringBuilder();

            // String url = "/stats/?start={start}&end={end}" + urisString + "&unique={unique}";
            String path = UriComponentsBuilder.fromUriString("/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("unique", unique)
                    .buildAndExpand()
                    .toUriString();
            urisString.append(path);
            for (String s : uris) {
                urisString.append("&uris=").append(s);
            }

            return getStats(urisString.toString());
        } else {
            String url = "/stats/?start={start}&end={end}&unique={unique}";

            return getStats(url);
        }
    }

    public ResponseEntity<Object> saveHit(StatRequestDto statRequestDto) {
        return post("/hit", statRequestDto);
    }

}
