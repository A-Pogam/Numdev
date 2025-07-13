package com.openclassrooms.starterjwt.integration.controllers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {
    @MockBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ApplicationContext context;


    @Test
    @Tag("post_api/session/--get_api/session/{id}")
    @DisplayName(" Register and login successfully")
    @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
    public void testSessionCreationAndRetrieval() throws Exception {
        // * Arrange
        SessionDto sessionDto = createMockedSessionDto();

        Session mockSession = createMockedSession(sessionDto);

        when(sessionService.create(any(Session.class))).thenReturn(mockSession);

        when(sessionService.getById(69L)).thenReturn(mockSession);

        // * Act
        // * Assert
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", 69L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));

    }

    @Test
    void printSessionBeans() {
        Arrays.stream(context.getBeanDefinitionNames())
                .filter(bean -> bean.contains("session"))
                .forEach(System.out::println);
    }

    @Test
    @Tag("put_api/session/{id}--get_api/session/{id}")
    @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
    public void testSessionUpdateAndRetrieval() throws Exception {
        // * Arrange
        SessionDto sessionDto = createMockedSessionDto();

        Session mockSession = createMockedSession(sessionDto);

        when(sessionService.update(any(Long.class), any(Session.class))).thenReturn(mockSession);

        when(sessionService.getById(69L)).thenReturn(mockSession);
        // * Act
        mockMvc.perform(put("/api/session/{id}", 69L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());

        // * Assert
        mockMvc.perform(get("/api/session/{id}", 69L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));
    }

    @Test
    @Tag("delete_api/session/{id}--get_api/session/{id}")
    @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
    public void testSessionDeletionAndRetrieval() throws Exception {
        // * Arrange
        SessionDto sessionDto = createMockedSessionDto();

        Session mockSession = createMockedSession(sessionDto);

        when(sessionService.getById(69L)).thenReturn(mockSession);

        // * Act
        mockMvc.perform(delete("/api/session/{id}", 69L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(sessionService.getById(69L)).thenReturn(null);
        // * Assert
        mockMvc.perform(get("/api/session/{id}", 69L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private SessionDto createMockedSessionDto() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        return sessionDto;
    }

    private Teacher createMockedTeacher(SessionDto sessionDto) {
        String isoString = "2025-06-22T10:27:21";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

        Teacher teacher = new Teacher();
        teacher.setId(sessionDto.getTeacher_id())
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        return teacher;
    }

    private Session createMockedSession(SessionDto sessionDto) {
        Teacher teacher = createMockedTeacher(sessionDto);

        User mockedUser1 = new User("Test", "Test",
                "TestN", "testP", false);

        mockedUser1.setId(1L);

        User mockedUser2 = new User("Test2", "Test2",
                "TestNN", "testPP", false);
        mockedUser2.setId(2L);

        List<User> arrayOfUsers = new ArrayList<>();
        arrayOfUsers.add(mockedUser1);
        arrayOfUsers.add(mockedUser2);

        Session mockedSession = Session.builder()
                .id(69L)
                .name(sessionDto.getName())
                .teacher(teacher)
                .users(arrayOfUsers)
                .description(sessionDto.getDescription())
                .date(new Date())
                .build();

        return mockedSession;
    }
}
