package com.devs.service;

import com.devs.domain.Subject;
import com.devs.domain.dto.SubjectDto;
import com.devs.exceptions.SubjectNotFoundException;
import com.devs.exceptions.SubjectWithSuchNameExistsException;
import com.devs.mappers.SubjectMapper;
import com.devs.repository.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;
    private final SubjectMapper subjectMapper;

    @Transactional
    public SubjectDto save(SubjectDto subjectDto) throws SubjectWithSuchNameExistsException {
        if (subjectDto.getName() == null) {
            throw new IllegalArgumentException("Subject name cannot be null");
        }
            if (subjectRepo.existsByName(subjectDto.getName())) {
                throw new SubjectWithSuchNameExistsException("Subject with name " + subjectDto.getName() + " already exists");
            }
            Subject subject = subjectMapper.toEntity(subjectDto);

            Subject savedSubject = subjectRepo.save(subject);
            return subjectMapper.toDto(savedSubject);
        }

        public Optional<SubjectDto> findById (Long id) throws SubjectNotFoundException {
            Subject subject = subjectRepo.findById(id)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + id));
            return Optional.of(subjectMapper.toDto(subject));
        }

        @Transactional
        public Optional<SubjectDto> update (Long id, SubjectDto subjectDto) throws SubjectNotFoundException {
            Subject subjectToUpdate = subjectRepo.findById(id)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject with id: " + id + "not found"));
            subjectToUpdate.setName(subjectDto.getName());
            subjectToUpdate.setId(id);
            Subject updatedSubject = subjectRepo.save(subjectToUpdate);
            return Optional.of(subjectMapper.toDto(updatedSubject));
        }

        @Transactional
        public void deleteById (Long id) throws SubjectNotFoundException {
            Optional<Subject> subjectToDelete = Optional.ofNullable(subjectRepo.findById(id)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject with id: " + id + "not found")));
            subjectRepo.deleteById(id);
        }
    }
