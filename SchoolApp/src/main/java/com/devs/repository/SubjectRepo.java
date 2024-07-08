

package com.devs.repository;

import com.devs.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface SubjectRepo extends JpaRepository<Subject, Long> {
        boolean existsByName(String name);
    }

