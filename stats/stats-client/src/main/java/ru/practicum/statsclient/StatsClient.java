package ru.practicum.statsclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsdto.StatRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("unique", unique);

        if (uris != null) {
            StringBuilder urisString = new StringBuilder();

            for (String s : uris) {
                urisString.append("&uris=").append(s);
            }
            String url = "/stats/?start={start}&end={end}" + urisString + "&unique={unique}";

            return get(url, params);
        } else {
            String url = "/stats/?start={start}&end={end}&unique={unique}";

            return get(url);
        }
    }

    public ResponseEntity<Object> saveHit(StatRequestDto statRequestDto) {
        return post("/hit", statRequestDto);
    }

}
