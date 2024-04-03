package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findByCompanyEmail(@Param("email") String companyEmail);
    boolean existsByCompanyEmail(String companyEmail);

    Company findByCompanyId(int companyId);

    Company findByStatus(String status);


}
