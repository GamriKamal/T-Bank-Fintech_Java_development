package tbank.mr_irmag.tbank_kudago_task.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.JwtAuthenticationResponse;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.PasswordResetRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignInRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignUpRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.User;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Role;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        return new JwtAuthenticationResponse(jwtService.generateToken(user, false));
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.userDetailsService().loadUserByUsername(request.getUsername());

        return new JwtAuthenticationResponse(jwtService.generateToken(user, request.isRememberMe()));
    }


    public JwtAuthenticationResponse resetPassword(PasswordResetRequest request) {
        log.info(request.toString());
        if (!"0000".equals(request.getConfirmationCode())) {
            throw new IllegalArgumentException("Неверный код подтверждения");
        }

        var user = userService.getByUsername(request.getUsername());
        log.info(user.toString() );

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userService.update(user);

        User updatedUser = userService.getByUsername(request.getUsername());
        log.info(updatedUser.toString());

        return new JwtAuthenticationResponse(jwtService.generateToken(updatedUser, true));

    }
}
