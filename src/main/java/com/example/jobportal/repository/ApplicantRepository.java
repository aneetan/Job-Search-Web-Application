package com.example.jobportal.repository;

import com.example.jobportal.model.Applicant;
import com.example.jobportal.model.JobDetails;
import com.example.jobportal.model.userDocs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {

    boolean existsByUserDocsAndJobDetails(userDocs userDocs, JobDetails jobDetails);

    List<Applicant> findByUserDocs(userDocs userDocs);

    Applicant findByUserDocsAndApplicantId(userDocs userDocs, int applicantId);

    List<Applicant> findByJobDetails(JobDetails jobDetails);

    List<Applicant> findByJobDetailsAndStatus(JobDetails jobDetails, String status);


}
