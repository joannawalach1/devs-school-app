package com.devs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
        private String name;
        private String email;
        private List<ExamDto> examDtoList;


    public StudentDto(String name, String email) {
    }
}
