package br.com.costa.spring_boot_essentials.services;

import br.com.costa.spring_boot_essentials.config.TokenProvider;
import br.com.costa.spring_boot_essentials.database.model.AlunosEntity;
import br.com.costa.spring_boot_essentials.database.model.RolesEntity;
import br.com.costa.spring_boot_essentials.database.model.repository.IAlunosRepository;
import br.com.costa.spring_boot_essentials.database.model.repository.RolesRepository;
import br.com.costa.spring_boot_essentials.dtos.AlunoDto;
import br.com.costa.spring_boot_essentials.dtos.LoginRequestDto;
import br.com.costa.spring_boot_essentials.dtos.RegisterRequestDto;
import br.com.costa.spring_boot_essentials.dtos.TokenResponseDto;
import br.com.costa.spring_boot_essentials.enums.RoleTypeEnum;
import br.com.costa.spring_boot_essentials.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthencationService {

    private final IAlunosRepository alunosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${JWT_EXPIRATION:900000}")
    private long expirationTime;

    public void register(@Valid RegisterRequestDto Dto) throws BadRequestException {
        AlunosEntity aluno =  alunosRepository.findByEmail(Dto.getEmail())
                .orElse(null);

        if (aluno != null) {
            throw new BadRequestException("Aluno Ja Cadastro com este email");
        }

        RolesEntity roles = rolesRepository.findByNome(RoleTypeEnum.ROLE_ALUNO.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                        .nome(RoleTypeEnum.ROLE_ALUNO.name())
                        .build()));

        alunosRepository.save(AlunosEntity.builder()
                .nome(Dto.getNome())
                .email(Dto.getEmail())
                        .roles(Set.of(roles))
                .senha( passwordEncoder.encode(Dto.getSenha()))
                .build());
    }

    public TokenResponseDto login(@Valid LoginRequestDto dto) throws BadRequestException {
        try{
          Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
         String token = tokenProvider.gerarToken(authentication);

         return  new TokenResponseDto(token, expirationTime);

        }catch(BadCredentialsException e){
            throw new BadRequestException("Credenciais invalidas");
        }catch(Exception e){
            throw e;
        }

    }
}
