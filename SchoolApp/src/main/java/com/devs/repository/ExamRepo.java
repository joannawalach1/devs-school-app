
package com.devs.repository;

import com.devs.domain.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Long> {
    boolean existsByNameOfExam(String nameOfExam);
}
