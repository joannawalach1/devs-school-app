package com.devs.mappers;

import com.devs.domain.Student;
import com.devs.domain.dto.StudentDto;
import com.devs.repository.StudentRepo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StudentMapper {

    private final ExamMapper examMapper;
    private final StudentRepo studentRepo;

    public StudentMapper(ExamMapper examMapper, StudentRepo studentRepo) {
        this.examMapper = examMapper;
        this.studentRepo = studentRepo;
    }

    public Student toEntity(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }

        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setExams(studentDto.getExamDtoList().stream()
                .map(examDto -> examMapper.toEntity(examDto))
                .collect(Collectors.toList()));
        return student;
    }

    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setExamDtoList(student.getExams().stream()
                .map(examMapper::toDto)
                .collect(Collectors.toList()));
        return studentDto;
    }
}
