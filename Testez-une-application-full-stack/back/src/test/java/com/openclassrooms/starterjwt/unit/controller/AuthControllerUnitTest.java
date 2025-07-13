package com.openclassrooms.starterjwt.unit.controller;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Auth controller test: api/auth")
public class AuthControllerUnitTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder,
                jwtUtils, userRepository);
    }

    @Test
    @Tag("post_api/auth/login")
    @DisplayName("login and return a JWT")
    void authenticateValidUser_shouldReturnJwtResponse() throws Exception {
        // * Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Test")
                .username("Test@tffzec.com")
                .password("test!1234")
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword())))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");

        User mockedUser = new User(userDetails.getUsername(), userDetails.getLastName(),
                userDetails.getFirstName(), userDetails.getPassword(), false);

        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockedUser));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userDetails.getUsername());
        loginRequest.setPassword(userDetails.getPassword());

        // * Act
        ResponseEntity<?> result = authController.authenticateUser(loginRequest);

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("post_api/auth/register")
    @DisplayName("register the user")
    void registerValidUser_shouldReturnMessageResponse() throws Exception {
        // * Arrange
        SignupRequest signUpRequest = new SignupRequest();

        signUpRequest.setEmail("test2@test487543.com");
        signUpRequest.setLastName("Test");
        signUpRequest.setFirstName("Test");
        signUpRequest.setPassword("test!1234");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(new User());

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        // * Act
        ResponseEntity<?> result = authController.registerUser(signUpRequest);

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) result.getBody()).getMessage());

    }

    @Test
    @Tag("post_api/auth/register")
    @DisplayName("invalid register")
    void registerAlreadyRegisteredUser_shouldReturnErrorResponse() throws Exception {
        // * Arrange
        SignupRequest signUpRequest = new SignupRequest();

        signUpRequest.setEmail("yoga@studio.com");
        signUpRequest.setLastName("Admin");
        signUpRequest.setFirstName("Admin");
        signUpRequest.setPassword("test!1234");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // * Act
        ResponseEntity<?> result = authController.registerUser(signUpRequest);

        // * Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse) result.getBody()).getMessage());
    }
}
