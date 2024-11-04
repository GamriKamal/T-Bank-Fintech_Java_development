package tbank.mr_irmag.tbank_kudago_task.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.JwtAuthenticationResponse;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.PasswordResetRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignInRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.SignUpRequest;
import tbank.mr_irmag.tbank_kudago_task.services.AuthenticationService;
import tbank.mr_irmag.tbank_kudago_task.services.LogoutService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("signUp_ValidRequest_ShouldReturnJwtAuthenticationResponse")
    void signUp_ValidRequest_ShouldReturnJwtAuthenticationResponse() {
        // Arrange
        SignUpRequest request = new SignUpRequest("user", "email@example.com", "password");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(expectedResponse);

        // Act
        JwtAuthenticationResponse actualResponse = authController.signUp(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(authenticationService, times(1)).signUp(request);
    }

    @Test
    @DisplayName("signUp_InvalidRequest_ShouldThrowException")
    void signUp_InvalidRequest_ShouldThrowException() {
        // Arrange
        SignUpRequest invalidRequest = new SignUpRequest("", "email", ""); // Некорректные данные
        when(authenticationService.signUp(any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.signUp(invalidRequest));
        verify(authenticationService, times(1)).signUp(invalidRequest);
    }

    @Test
    @DisplayName("signIn_ValidRequest_ShouldReturnJwtAuthenticationResponse")
    void signIn_ValidRequest_ShouldReturnJwtAuthenticationResponse() {
        // Arrange
        SignInRequest request = new SignInRequest("user", "password", true);
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        // Act
        JwtAuthenticationResponse actualResponse = authController.signIn(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(authenticationService, times(1)).signIn(request);
    }

    @Test
    @DisplayName("signIn_InvalidCredentials_ShouldThrowException")
    void signIn_InvalidCredentials_ShouldThrowException() {
        // Arrange
        SignInRequest invalidRequest = new SignInRequest("user", "wrongPassword", false);
        when(authenticationService.signIn(any(SignInRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.signIn(invalidRequest));
        verify(authenticationService, times(1)).signIn(invalidRequest);
    }

    @Test
    @DisplayName("logout_WithBearerToken_ShouldInvalidateTokenAndReturnSuccessMessage")
    void logout_WithBearerToken_ShouldInvalidateTokenAndReturnSuccessMessage() {
        // Arrange
        String token = "token";
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Act
        ResponseEntity<?> response = authController.logout(httpServletRequest);

        // Assert
        assertEquals(ResponseEntity.ok("You have successfully logged out"), response);
        verify(logoutService, times(1)).invalidateToken(token);
    }

    @Test
    @DisplayName("resetPassword_WithValidToken_ShouldResetPasswordAndReturnJwtAuthenticationResponse")
    void resetPassword_WithValidToken_ShouldResetPasswordAndReturnJwtAuthenticationResponse() {
        // Arrange
        PasswordResetRequest request = new PasswordResetRequest("user", "0000", "newPassword");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(authenticationService.resetPassword(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<JwtAuthenticationResponse> response = authController.resetPassword(request, httpServletRequest);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(authenticationService, times(1)).resetPassword(request);
        verify(logoutService, times(1)).invalidateToken("token");
    }

    @Test
    @DisplayName("resetPassword_InvalidConfirmationCode_ShouldThrowException")
    void resetPassword_InvalidConfirmationCode_ShouldThrowException() {
        // Arrange
        PasswordResetRequest request = new PasswordResetRequest("user", "wrongCode", "newPassword");
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(authenticationService.resetPassword(request)).thenThrow(new IllegalArgumentException("Invalid confirmation code"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.resetPassword(request, httpServletRequest));
        verify(authenticationService, times(1)).resetPassword(request);
    }

    @Test
    @DisplayName("resetPassword_WithInvalidToken_ShouldReturnNullResponse")
    void resetPassword_WithInvalidToken_ShouldReturnNullResponse() {
        // Arrange
        PasswordResetRequest request = new PasswordResetRequest("user", "0000", "newPassword");
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<JwtAuthenticationResponse> response = authController.resetPassword(request, httpServletRequest);

        // Assert
        assertEquals(ResponseEntity.ok(null), response);
        verify(authenticationService, never()).resetPassword(request);
        verify(logoutService, never()).invalidateToken(anyString());
    }
}
