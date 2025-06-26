package com.openclassrooms.starterjwt.unit.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;


    @Mock
    private UserRepository userRepository;

    private Session session;

    @BeforeEach
    void setUp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(currentTime)
                .setUpdatedAt(currentTime);

        User mockedUser = new User("Toto", "Toto",
                "Toto69", "totoBruv", false);
        mockedUser.setId(1L);

        List<User> arrayOfUsers = new ArrayList<>();
        arrayOfUsers.add(mockedUser);

        session = new Session(1L, "Test session",
                currentDate, "Test",
                teacher,
                arrayOfUsers, currentTime,
                currentTime);

        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    @Tag("SessionService.create()")
    @DisplayName("Creating a new Session")
    public void testCreatingNewSession() {
        // * Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        Session result = sessionService.create(session);

        // * Assert
        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    @Test
    @Tag("SessionService.delete()")
    @DisplayName("Deleting a session")
    public void testDeletingASession() {
        // * Arrange
        doNothing().when(sessionRepository).deleteById(session.getId());

        // * Act
        sessionService.delete(session.getId());

        // * Assert
        verify(sessionRepository).deleteById(session.getId());
    }

    @Test
    @Tag("SessionService.findAll()")
    @DisplayName("Finding all the sessions")
    public void testFindingAllSessions() {
        // * Arrange
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // * Act
        List<Session> result = sessionService.findAll();

        // * Assert
        verify(sessionRepository).findAll();

        assertEquals(sessions, result);
    }

    @Test
    @Tag("SessionService.getById()")
    @DisplayName("Get session by id")
    public void testGetSessionByValidId() {
        // * Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // * Act
        Session result = sessionService.getById(session.getId());

        // * Assert
        verify(sessionRepository).findById(session.getId());

        assertEquals(session, result);
    }

    @Test
    @Tag("SessionService.update()")
    @DisplayName("Updating a session")
    public void testUpdatingASession() {
        // * Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        Session result = sessionService.update(session.getId(), session);

        // * Assert
        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    @Test
    @Tag("SessionService.participate()")
    @DisplayName("Participate in a session")
    public void testParticipateSession() {
        // * Arrange
        Long userId = 69L;
        Long sessionId = 1L;

        User mockedUser = new User("Test", "Test",
                "Test", "Test1", false);
        mockedUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));

        // * Act
        sessionService.participate(sessionId, userId);

        // * Assert
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);

        assertTrue(session.getUsers().contains(mockedUser));
    }

    @Test
    @Tag("SessionService.noLogerParticipate()")
    @DisplayName("Cancel a session")
    public void testNoLongerParticipateSession() {
        // * Arrange
        Long userIdToRemove = 420L;
        Long sessionId = 1L;

        User mockedUser = new User("Test2", "Test2",
                "Test23", "TestPP2", false);
        mockedUser.setId(userIdToRemove);

        session.getUsers().add(mockedUser);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        sessionService.noLongerParticipate(sessionId, userIdToRemove);

        // * Assert
        verify(sessionRepository).findById(sessionId);

        assertFalse(session.getUsers().contains(mockedUser));
    }
}
