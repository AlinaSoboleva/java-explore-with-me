package ru.practicum.main_service.compilations.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class UpdateCompilationRequest {
    private Set<Long> events = new HashSet<>();
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
