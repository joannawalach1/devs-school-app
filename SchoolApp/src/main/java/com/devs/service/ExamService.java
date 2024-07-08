package com.devs.service;

import com.devs.domain.Exam;
import com.devs.domain.Student;
import com.devs.domain.Subject;
import com.devs.domain.dto.ExamDto;
import com.devs.exceptions.ExamNotFoundException;
import com.devs.exceptions.ExamWithSuchNameExistsException;
import com.devs.mappers.ExamMapper;
import com.devs.repository.ExamRepo;
import com.devs.repository.StudentRepo;
import com.devs.repository.SubjectRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepo examRepo;
    private final ExamMapper examMapper;
    private StudentRepo studentRepo;
    private SubjectRepo subjectRepo;

    public ExamService(ExamRepo examRepo, ExamMapper examMapper, StudentRepo studentRepo, SubjectRepo subjectRepo) {
        this.examRepo = examRepo;
        this.examMapper = examMapper;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
    }

    @Transactional
    public ExamDto save(ExamDto examDto) throws ExamWithSuchNameExistsException {
        if (examRepo.existsByNameOfExam(examDto.getNameOfExam())) {
            throw new ExamWithSuchNameExistsException("Exam with name " + examDto.getNameOfExam() + " already exists.");
        }

        Exam exam = examMapper.toEntity(examDto);

        Student student = studentRepo.findById(examDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + examDto.getStudentId()));
        Subject subject = subjectRepo.findById(examDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + examDto.getSubjectId()));

        exam.setStudent(student);
        exam.setSubject(subject);
        exam = examRepo.save(exam);
        return examMapper.toDto(exam);
    }


    @Transactional(readOnly = true)
    public Optional<ExamDto> findById(Long id) throws ExamNotFoundException {
        Exam foundExam = examRepo.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with ID: " + id));
        return Optional.of(examMapper.toDto(foundExam));
    }

    @Transactional
    public Optional<ExamDto> update(Long id, ExamDto updatedExamDto) throws ExamNotFoundException {
        Exam examToUpdate = examRepo.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with ID: " + id));
        examToUpdate.setNameOfExam(updatedExamDto.getNameOfExam());
        examToUpdate.setDateOfExam(updatedExamDto.getDateOfExam());
        examToUpdate.setStudent(new Student(updatedExamDto.getStudentId()));
        examToUpdate.setSubject(new Subject(updatedExamDto.getSubjectId()));

        Exam updatedExam = examRepo.save(examToUpdate);
        return Optional.of(examMapper.toDto(updatedExam));
    }

    @Transactional
    public void deleteById(Long id) throws ExamNotFoundException {
        Optional<Exam> examToDelete = Optional.of(examRepo.findById(id)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + id)));
        examRepo.deleteById(id);
    }
}
