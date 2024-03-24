package com.example.jobportal.repository;

import com.example.jobportal.model.CompanyDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface companyDocsRepository extends JpaRepository<CompanyDocs, Integer> {

}
