package com.devs.controllers;

import com.devs.domain.dto.SubjectDto;
import com.devs.exceptions.SubjectNotFoundException;
import com.devs.exceptions.SubjectWithSuchNameExistsException;
import com.devs.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

    @RestController
    @RequestMapping("/subject")
    public class SubjectController {
        private final SubjectService subjectService;

        public SubjectController(SubjectService subjectService) {
            this.subjectService = subjectService;
        }

        @PostMapping
        public ResponseEntity<SubjectDto> saveSubject(@RequestBody SubjectDto subjectDto) throws SubjectNotFoundException, SubjectWithSuchNameExistsException {
            SubjectDto createdSubject = subjectService.save(subjectDto);
            return ResponseEntity.status(HttpStatus.OK).body(createdSubject);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Optional<SubjectDto>> findById(@PathVariable Long id) throws SubjectNotFoundException {
            Optional<SubjectDto> subjectDto = subjectService.findById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(subjectDto);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Optional<SubjectDto>> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) throws SubjectNotFoundException {
            Optional<SubjectDto> updatedSubject = subjectService.update(id, subjectDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSubject);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Long id) throws SubjectNotFoundException {
            subjectService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
