package ru.practicum.main_service.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(max = 50, min = 1)
    private String name;
}
