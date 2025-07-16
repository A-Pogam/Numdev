package com.openclassrooms.starterjwt.unit.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("User controller test: api/user")
public class UserControllerUnitTest {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, userMapper);
    }

    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("get the user from the database from the id")
    public void getUserById_shouldReturnUserWithGivenId() {
        // * Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(new UserDto());

        // * Act
        ResponseEntity<?> result = userController.findById("1");

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("return a 404 error for an invalid id")
    public void getUserWithInvalidId_shouldReturnNotFoundError() {
        // * Assert
        Long nonExistentId = 0L;

        when(userService.findById(nonExistentId)).thenReturn(null);

        // * Act
        ResponseEntity<?> result = userController.findById(nonExistentId.toString());

        // * Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("delete the user and return a 200 status code")
    public void deleteUserWithValidId_shouldReturnOk() {
        // * Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // * Act
        ResponseEntity<?> result = userController.save(userId.toString());

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("return a 401 status code for an unauthorized user")
    public void deleteUserWithUnauthorizedUser_shouldReturnUnauthorized() {
        // * Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("unauthorized@example.com");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // * Act
        ResponseEntity<?> result = userController.save(userId.toString());

        // * Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName(" return a 404 error for non existent id")
    public void deleteUserWithNonExistentId_shouldReturnNotFoundError() {
        // * Arrange
        Long nonExistentId = 0L;
        when(userService.findById(anyLong())).thenReturn(null);

        // * Act
        ResponseEntity<?> result = userController.save(nonExistentId.toString());

        // * Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("return a 400 error for a bad request")
    public void deleteUserWithInvalidId_shouldReturnNotFoundError() {
        // * Act
        ResponseEntity<?> result = userController.save("invalid");

        // * Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
