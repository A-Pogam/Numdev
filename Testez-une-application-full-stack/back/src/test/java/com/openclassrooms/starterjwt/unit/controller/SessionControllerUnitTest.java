package com.openclassrooms.starterjwt.unit.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openclassrooms.starterjwt.integration.SessionController;
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

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Session controller test: api/session")
public class SessionControllerUnitTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        // Initialize the controller with mock dependencies
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("get the session from the database of the given id")
    public void getSessionById_returnsSessionWithGivenId() throws Exception {
        // * Arrange
        Long sessionId = 1L;
        Session mockSession = new Session();

        when(sessionService.getById(sessionId)).thenReturn(mockSession);

        // * Act
        ResponseEntity<?> result = sessionController.findById(sessionId.toString());

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("return a 404 error")
    public void getSessionById_withInvalidId_returnsBadRequest() {
        // * Arrange
        Long sessionId = 0L;

        when(sessionService.getById(sessionId)).thenReturn(null);

        // * Act
        ResponseEntity<?> result = sessionController.findById(sessionId.toString());

        // * Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @Tag("get_api/session")
    @DisplayName("retrieve all the sessions from the database as an empty or full array")
    public void getAllSessions_returnsListOfAllSessions() {
        // * Arrange
        // * Act
        ResponseEntity<?> result = sessionController.findAll();

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("post_api/session")
    @DisplayName("create the session and return a 201 status code")
    public void createSessionWithValidSessionDto_createsNewSession() {
        // * Arrange
        String isoString = "2025-06-23T10:27:21";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Session session = Session.builder()
                .id(1L)
                .name("session-1")
                .teacher(teacher)
                .users(null)
                .description("test")
                .date(new Date())
                .build();

        List<Long> userIdsList = new ArrayList<Long>();
        userIdsList.add(1L);
        userIdsList.add(2L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setTeacher_id(session.getTeacher().getId());
        sessionDto.setName(session.getName());
        sessionDto.setUsers(userIdsList);
        sessionDto.setDate(session.getDate());
        sessionDto.setDescription(session.getDescription());
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());

        when(sessionService.create(sessionMapper.toEntity(sessionDto))).thenReturn(session);
        // * Act
        ResponseEntity<?> result = sessionController.create(sessionDto);

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("put_api/session/{id}")
    @DisplayName(" update the existing session and return a 200 status code")
    public void updateSession_withValidId_returnsUpdatedSession() {
        // * Arrange
        String isoString = "2025-06-23T10:27:21";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Session session = Session.builder()
                .id(1L)
                .name("updated-session-1")
                .teacher(teacher)
                .users(null)
                .description("updated test")
                .date(new Date())
                .build();

        List<Long> userIdsList = new ArrayList<Long>();
        userIdsList.add(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setTeacher_id(session.getTeacher().getId());
        sessionDto.setName(session.getName());
        sessionDto.setUsers(userIdsList);
        sessionDto.setDate(session.getDate());
        sessionDto.setDescription(session.getDescription());
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());

        when(sessionService.update(sessionDto.getId(), sessionMapper.toEntity(sessionDto))).thenReturn(session);

        // * Act
        ResponseEntity<?> result = sessionController.update(sessionDto.getId().toString(), sessionDto);

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("delete the session and return a 200 status code")
    public void deleteSession_withValidId_returnsBadRequest() {
        // * Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        Session mockSession = new Session();

        when(sessionService.getById(sessionDto.getId())).thenReturn(mockSession);
        // * Act
        ResponseEntity<?> result = sessionController.save(sessionDto.getId().toString());

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("return a 404 status code")
    public void deleteSession_withNonExistentId_returnsNotFound() {
        // * Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(0L);

        when(sessionService.getById(sessionDto.getId())).thenReturn(null);
        // * Act
        ResponseEntity<?> result = sessionController.save(sessionDto.getId().toString());

        // * Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("should return a 400 status code")
    public void deleteSession_withInvalidId_returnsBadRequest() {
        // * Arrange
        // * Act
        ResponseEntity<?> result = sessionController.save("invalid");

        // * Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    @Tag("post_api/session/{id}/participate/{userId}")
    @DisplayName("should create the session and return a 200 status code")
    public void addUserToSessionWithValidIds_shouldAddTheUserToSession() {
        // * Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setTeacher_id(1L);

        // * Act
        ResponseEntity<?> result = sessionController.participate(sessionDto.getId().toString(),
                sessionDto.getTeacher_id().toString());

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
