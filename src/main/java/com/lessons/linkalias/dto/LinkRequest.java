package com.lessons.linkalias.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Тип для создания простой ссылки")
public class LinkRequest {

    @NotBlank
    @Schema(description = "Ссылка для создания короткой ссылки", example = "http://localhost:8080/adasdasd/asdasdasd/asdasd")
    private String url;

    @NotNull
    @Min(0)
    @Schema(description = "Время существования ссылки, если значение 0, то ссылка вечная, значение в милисекундах", example = "1000", defaultValue = "0")
    private Long TTL;

}
