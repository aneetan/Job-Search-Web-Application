package com.example.jobportal.repository;

import com.example.jobportal.model.userDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocsRepository extends JpaRepository<userDocs, Integer> {
}
