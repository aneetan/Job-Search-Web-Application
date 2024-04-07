package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDocs;
import com.example.jobportal.model.JobDetails;
import com.example.jobportal.model.userDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobDetailsRepository extends JpaRepository<JobDetails, Integer> {
    List<JobDetails> findAllByJobStatus(String jobStatus);

    JobDetails findAllByJobId(int jobId);

    List<JobDetails> findAllByCompany(Company company);

    JobDetails findAllByCompanyAndAndCompanyDocs(Company company, CompanyDocs companyDocs);
    JobDetails findByJobIdAndCompany(int jobId, Company company);

    JobDetails findByUserDocs(userDocs userDocs);

    List<JobDetails> findByCompanyAndJobStatus(Company company, String jobStatus);

    List<JobDetails> findByTitleContainingIgnoreCase(String query);

    JobDetails findByJobId(int jobId);

    JobDetails findByTitle(String title);


    List<JobDetails> findByDeadlineBeforeAndJobStatusNot(LocalDate deadline, String status);
//    @Query("SELECT DISTINCT j.title FROM JobDetails j WHERE j.company = :company AND j.jobStatus = 'Active'")
//    List<String> findDistinctActiveTitlesByCompany(@Param("company") Company company);

    @Query("SELECT DISTINCT j.title FROM JobDetails j WHERE j.company = :company AND j.jobStatus IN ('Active', 'Inactive')")
    List<String> findDistinctTitlesByCompanyAndActiveOrInactiveStatus(@Param("company") Company company);



}
