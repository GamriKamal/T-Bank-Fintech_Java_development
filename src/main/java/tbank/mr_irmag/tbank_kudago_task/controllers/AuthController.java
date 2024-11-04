package tbank.mr_irmag.tbank_kudago_task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.JwtAuthenticationResponse;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.PasswordResetRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignInRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignUpRequest;
import tbank.mr_irmag.tbank_kudago_task.services.AuthenticationService;
import tbank.mr_irmag.tbank_kudago_task.services.LogoutService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
@Log4j2
public class AuthController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info(token);
            logoutService.invalidateToken(token);
        }

        return ResponseEntity.ok("You have successfully logged out");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<JwtAuthenticationResponse> resetPassword(@RequestBody @Valid PasswordResetRequest request, HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        JwtAuthenticationResponse response = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info(token);
            response = authenticationService.resetPassword(request);
            logoutService.invalidateToken(token);
        }
        return ResponseEntity.ok(response);
    }
}
