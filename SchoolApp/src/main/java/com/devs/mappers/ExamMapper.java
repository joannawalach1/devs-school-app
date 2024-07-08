package com.devs.mappers;

import com.devs.domain.Exam;
import com.devs.domain.Student;
import com.devs.domain.Subject;
import com.devs.domain.dto.ExamDto;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {
    public ExamDto toDto(Exam exam) {
        ExamDto examDto = new ExamDto();
        examDto.setNameOfExam(exam.getNameOfExam());
        examDto.setDateOfExam(exam.getDateOfExam());
        examDto.setStudentId(exam.getStudent().getId());
        examDto.setSubjectId(exam.getSubject().getId());
        return examDto;
    }

    public Exam toEntity(ExamDto examDto) {
        Exam exam = new Exam();
        exam.setNameOfExam(examDto.getNameOfExam());
        exam.setDateOfExam(examDto.getDateOfExam());
        exam.setStudent(new Student(examDto.getStudentId()));
        exam.setSubject(new Subject(examDto.getSubjectId()));
        return exam;
    }
}
