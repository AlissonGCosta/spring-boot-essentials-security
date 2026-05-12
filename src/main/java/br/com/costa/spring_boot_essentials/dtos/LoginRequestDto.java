package br.com.costa.spring_boot_essentials.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}
