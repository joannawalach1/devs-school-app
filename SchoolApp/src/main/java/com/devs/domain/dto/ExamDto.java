package com.devs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {
        private String nameOfExam;
        private LocalDateTime dateOfExam;
        private Long subjectId;
        private Long studentId;
}
