package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findByCompanyEmail(@Param("email") String companyEmail);
    boolean existsByCompanyEmail(String companyEmail);


}
