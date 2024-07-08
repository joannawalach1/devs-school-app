package com.devs.service;

import com.devs.domain.Subject;
import com.devs.domain.dto.SubjectDto;
import com.devs.exceptions.SubjectNotFoundException;
import com.devs.exceptions.SubjectWithSuchNameExistsException;
import com.devs.mappers.SubjectMapper;
import com.devs.repository.SubjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceTest {
    @Mock
    private SubjectRepo subjectRepo;
    @Mock
    private SubjectMapper subjectMapper;
    @InjectMocks
    private SubjectService subjectService;
    private SubjectDto subjectDto;
    private Subject subject;
    private SubjectDto expectedSubjectDto;
    private List<Subject> subjects;
    private Subject updatedSubject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // given
        subjectDto = new SubjectDto();
        subjectDto.setName("Math");

        subject = new Subject();
        subject.setId(1L);
        subject.setName(subjectDto.getName());

        expectedSubjectDto = new SubjectDto();
        expectedSubjectDto.setName("Math");

        updatedSubject = new Subject();
        updatedSubject.setId(1L);
        updatedSubject.setName(subject.getName());

        subjects = List.of(subject);

    }

    @Test
    void save() throws SubjectWithSuchNameExistsException {
        // given
        when(subjectMapper.toEntity(subjectDto)).thenReturn(subject);
        when(subjectRepo.existsByName(subject.getName())).thenReturn(false);
        when(subjectRepo.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(expectedSubjectDto);

        // when
        SubjectDto savedSubjectDto = subjectService.save(subjectDto);

        // then
        assertNotNull(savedSubjectDto);
        assertEquals(expectedSubjectDto.getName(), savedSubjectDto.getName());
        verify(subjectMapper, times(1)).toEntity(subjectDto);
        verify(subjectRepo, times(1)).existsByName(subject.getName());
        verify(subjectRepo, times(1)).save(subject);
        verify(subjectMapper, times(1)).toDto(subject);
    }

    @Test
    void findById() throws SubjectNotFoundException {
        // given
        when(subjectRepo.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // when
        Optional<SubjectDto> subjectFoundById = subjectService.findById(subject.getId());

        // then
        assertTrue(subjectFoundById.isPresent());
        assertEquals(subjectDto.getName(), subjectFoundById.get().getName());
        verify(subjectRepo, times(1)).findById(subject.getId());
        verify(subjectMapper, times(1)).toDto(subject);
    }

    @Test
    void update() throws SubjectNotFoundException {
        // Given
        when(subjectRepo.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectRepo.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(expectedSubjectDto);

        // When
        Optional<SubjectDto> updatedSubjectDto = subjectService.update(subject.getId(), subjectDto);

        // Then
        assertTrue(updatedSubjectDto.isPresent());
        assertEquals(updatedSubject.getName(), updatedSubjectDto.get().getName());
        verify(subjectRepo, times(1)).findById(eq(subject.getId()));
        verify(subjectRepo, times(1)).save(eq(subject));
        verify(subjectMapper, times(1)).toDto(eq(subject));
    }

    @Test
    void deleteById() throws SubjectNotFoundException {
        // given
        when(subjectRepo.findById(anyLong())).thenReturn(Optional.of(subject));
        doNothing().when(subjectRepo).deleteById(anyLong());

        // when
        subjectService.deleteById(subject.getId());

        // then
        verify(subjectRepo, times(1)).findById(subject.getId());
        verify(subjectRepo, times(1)).deleteById(subject.getId());
    }
}