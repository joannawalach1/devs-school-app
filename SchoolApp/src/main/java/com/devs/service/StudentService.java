package com.devs.service;

import com.devs.domain.Student;
import com.devs.domain.dto.StudentDto;
import com.devs.exceptions.StudentNotFoundException;
import com.devs.exceptions.StudentWithSuchIdExists;
import com.devs.mappers.StudentMapper;
import com.devs.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;

    public StudentDto save(StudentDto studentDto) throws StudentWithSuchIdExists {
        Student student = studentMapper.toEntity(studentDto);
        if (student.getEmail() != null && studentRepo.existsByEmail(student.getEmail())) {
            throw new StudentWithSuchIdExists("Student with email" + student.getEmail() + "already exists");
        }
        Student savedStudent = studentRepo.save(student);
        return studentMapper.toDto(savedStudent);
    }

    public Optional<StudentDto> findById(Long id) throws StudentNotFoundException {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        return Optional.of(studentMapper.toDto(student));
    }

    @Cacheable(value = "FindByEmailCache")
    public Optional<StudentDto> findByEmail(String email) throws StudentNotFoundException {
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with email:" + email));
        return Optional.of(studentMapper.toDto(student));
    }

    @Cacheable(value = "FindAllCache")
    public List<StudentDto> findAll() {
        List<Student> students = studentRepo.findAll();
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<StudentDto> update(Long id, StudentDto studentDto) throws StudentNotFoundException {
        Student studentToUpdate = studentRepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        studentToUpdate.setName(studentDto.getName());
        studentToUpdate.setEmail(studentDto.getEmail());

        Student updatedStudent = studentRepo.save(studentToUpdate);
        return Optional.of(studentMapper.toDto(updatedStudent));
    }

    public void deleteById(Long id) throws StudentNotFoundException {
        Optional<Student> studentToDelete = Optional.of(studentRepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id)));
        studentRepo.deleteById(id);
    }
}