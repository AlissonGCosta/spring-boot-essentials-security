package br.com.costa.spring_boot_essentials.dtos;

public record TokenResponseDto(String token, long expiresIn) {
}
