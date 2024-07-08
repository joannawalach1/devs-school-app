package com.devs.controllers;

import com.devs.domain.Student;
import com.devs.domain.dto.StudentDto;
import com.devs.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private StudentDto studentDto;
    private String studentDtoJson;
    private List<StudentDto> studentDtos;
    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        student = new Student();
        student.setId(1L);
        student.setName("Wojtek");
        student.setEmail("wojtek12@op.pl");

        studentDto = new StudentDto();
        studentDto.setName("Wojtek");
        studentDto.setEmail("wojtek12@op.pl");
        studentDtos = Arrays.asList(
                new StudentDto("John", "Doe"),
                new StudentDto("Jane", "Smith")
        );

        when(studentService.save(any())).thenReturn(studentDto);
        studentDtoJson = objectMapper.writeValueAsString(studentDto);
    }

    @Test
    void saveStudent() throws Exception {
        when(studentService.save(studentDto)).thenReturn(studentDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void findByEmail() throws Exception {
        String studentEmail = "tomek@op.pl";

        mockMvc.perform(MockMvcRequestBuilders.get("/student/findByEmail/{email}", studentEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/student/findById/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }



    @Test
    void findAll() throws Exception {
        when(studentService.findAll()).thenReturn(studentDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void updateStudent() throws Exception {
        Long studentId = 1L;
        studentDto.setName("Updated Name");

        when(studentService.update(eq(studentId), any(StudentDto.class))).thenReturn(Optional.of(studentDto));

        MvcResult result = mockMvc.perform(put("/student/update/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        ;
    }

    @Test
    void deleteStudent() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
}
