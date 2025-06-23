package com.openclassrooms.starterjwt.unit.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    @Tag("TeacherService.findAll()")
    @DisplayName("Get all teachers")
    public void testFindAllTeachers() {
        // * Arrange
        LocalDateTime localDateTime = LocalDateTime.now();

        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Teacher teacher2 = new Teacher();
        teacher2
                .setId(2L)
                .setLastName("THIERCELIN")
                .setFirstName("Hélène")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        teachers.add(teacher1);
        teachers.add(teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        // * Act
        List<Teacher> result = teacherService.findAll();

        // * Assert
        verify(teacherRepository).findAll();

        assertEquals(teachers, result);
    }

    @Test
    @Tag("TeacherService.findById()")
    @DisplayName("Get teacher by id")
    public void testFindTeacherById() {
        // * Arrange
        LocalDateTime localDateTime = LocalDateTime.now();

        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // * Act
        Teacher result = teacherService.findById(teacherId);

        // * Assert
        verify(teacherRepository).findById(teacherId);

        assertEquals(teacher, result);
    }

    @Test
    @Tag("TeacherService.findById()")
    @DisplayName("Find teacher by id error")
    public void testFindTeacherByIdNotFound() {
        // * Arrange
        Long teacherId = 1L;

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // * Act
        Teacher result = teacherService.findById(teacherId);

        // * Assert
        verify(teacherRepository).findById(teacherId);

        assertNull(result);
    }
}
