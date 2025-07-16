package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        teacher = teacherRepository.save(new Teacher().setFirstName("John").setLastName("Doe"));
        user = userRepository.save(new User("john.doe@test.com", "doe", "john", "password", false));
    }

    @Test
    @WithMockUser(username = "admin@studio.com", roles = "ADMIN")
    void createAndRetrieveSession() throws Exception {
        Session session = Session.builder()
                .name("Yoga Session")
                .description("Morning Yoga")
                .date(new Date())
                .teacher(teacher)
                .users(List.of(user))
                .build();

        // Create session
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());

        // Verify session is stored in DB
        List<Session> allSessions = sessionRepository.findAll();
        assertEquals(1, allSessions.size());
        Long sessionId = allSessions.get(0).getId();

        // Retrieve session by API
        mockMvc.perform(get("/api/session/{id}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Yoga Session")))
                .andExpect(jsonPath("$.description", is("Morning Yoga")))
                .andExpect(jsonPath("$.teacher.id", is(teacher.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "admin@studio.com", roles = "ADMIN")
    void deleteSession() throws Exception {
        Session session = sessionRepository.save(Session.builder()
                .name("Delete Test")
                .description("To delete")
                .teacher(teacher)
                .date(new Date())
                .build());

        mockMvc.perform(delete("/api/session/{id}", session.getId()))
                .andExpect(status().isOk());

        assertEquals(0, sessionRepository.count());
    }
}
