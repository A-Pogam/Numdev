package com.openclassrooms.starterjwt.unit.service;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @Tag("UserService.findById()")
    @DisplayName("get user by id")
    public void testFindUserById() {
        // * Arrange
        Long userId = 1L;
        User user = new User("John", "Doe", "john.doe@example.com", "password",
                false);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // * Act
        User result = userService.findById(userId);

        // * Assert
        verify(userRepository).findById(userId);

        assertEquals(user, result);
    }

    @Test
    @Tag("UserService.findById()")
    @DisplayName("Get user by id error")
    public void testFindUserByIdNotFound() {
        // * Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // * Act
        User result = userService.findById(userId);

        // * Assert
        verify(userRepository).findById(userId);

        assertNull(result);
    }

    @Test
    @Tag("UserService.delete()")
    @DisplayName("Delete a user by its ID")
    public void testDeleteUser() {
        // * Arrange
        Long userId = 1L;

        // * Act
        userService.delete(userId);

        // * Assert
        verify(userRepository).deleteById(userId);
    }
}
