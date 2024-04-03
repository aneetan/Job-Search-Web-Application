package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDetailsRepository extends JpaRepository<JobDetails, Integer> {
    List<JobDetails> findAllByJobStatus(String jobStatus);

    JobDetails findAllByJobId(int jobId);

    List<JobDetails> findAllByCompany(Company company);

    List<JobDetails> findByCompanyAndJobStatus(Company company, String jobStatus);

    List<JobDetails> findByTitleContainingIgnoreCase(String query);



}
