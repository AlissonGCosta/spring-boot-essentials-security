package br.com.costa.spring_boot_essentials.controller;

import br.com.costa.spring_boot_essentials.dtos.LoginRequestDto;
import br.com.costa.spring_boot_essentials.dtos.RegisterRequestDto;
import br.com.costa.spring_boot_essentials.services.AuthencationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthencationService authencationService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequestDto registerRequestDto) throws Exception {
        authencationService.register(registerRequestDto);

    }

    @PostMapping("/login")
    public void register(@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception {

        authencationService.login(loginRequestDto);

    }
}
