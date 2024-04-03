package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDocs;
import com.example.jobportal.model.userDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface companyDocsRepository extends JpaRepository<CompanyDocs, Integer> {

    List<CompanyDocs> findByCompany_Status(String status);

//    boolean existsByCompany_Status(String status);

    boolean existsByCompany_Status(String status);

    CompanyDocs getCompanyDocsByCompany(Company company);

    CompanyDocs findAllByCompany(Company company);


}
