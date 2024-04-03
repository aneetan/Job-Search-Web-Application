package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDetails;
import com.example.jobportal.model.CompanyDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Integer> {

    CompanyDetails findByCompany(Company company);

}
