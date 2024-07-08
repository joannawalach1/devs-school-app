package com.devs.controllers;

import com.devs.domain.dto.StudentDto;
import com.devs.exceptions.StudentNotFoundException;
import com.devs.exceptions.StudentWithSuchIdExists;
import com.devs.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDto> saveStudent(@RequestBody StudentDto studentDto) throws StudentWithSuchIdExists {
        StudentDto createdStudent = studentService.save(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping(value = "/findById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDto> findById(@PathVariable Long id) throws StudentNotFoundException {
        Optional<StudentDto> foundStudentDto = studentService.findById(id);

        return foundStudentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDto> findByEmail(@PathVariable String email) throws StudentNotFoundException {
        Optional<StudentDto> foundStudentDto = studentService.findByEmail(email);

        return foundStudentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(value = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentDto>> findAll() {
        List<StudentDto> allStudents = studentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allStudents);
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) throws StudentNotFoundException {
        Optional<StudentDto> updatedStudent = studentService.update(id, studentDto);
        return updatedStudent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) throws StudentNotFoundException {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();}
}
