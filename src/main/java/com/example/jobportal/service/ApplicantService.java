package com.example.jobportal.service;

import com.example.jobportal.model.Applicant;
import com.example.jobportal.model.JobDetails;
import com.example.jobportal.model.userDocs;
import com.example.jobportal.repository.ApplicantRepository;
import com.example.jobportal.repository.JobDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicantService {

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    JobDetailsRepository jdRepo;

    public void applyForJob(userDocs user, JobDetails job) {
        if (applicantRepository.existsByUserDocsAndJobDetails(user, job)) {
            throw new IllegalStateException("User has already applied for this job");
        }

        Applicant applicant = new Applicant();
        applicant.setUserDocs(user);
        applicant.setJobDetails(job);
        applicant.setStatus("Pending");

        applicantRepository.save(applicant);
    }

    public boolean hasUserAppliedForJob(userDocs userDocs, JobDetails jobDetails) {

        return applicantRepository.existsByUserDocsAndJobDetails(userDocs,jobDetails);
    }

    public List<JobDetails> getAppliedJobsForUser(userDocs userDocs) {
        List<Applicant> userApplications = applicantRepository.findByUserDocs(userDocs);

        // Extract job details from user applications
        List<JobDetails> appliedJobs = new ArrayList<>();
        for (Applicant application : userApplications) {
            appliedJobs.add(application.getJobDetails());
        }

        return appliedJobs;
    }


    public List<JobDetails> findUnappliedJobs(userDocs docs){

        List<Applicant> userApplications = applicantRepository.findByUserDocs(docs);

        // Retrieve active jobs
        List<JobDetails> activeJobs = jdRepo.findAllByJobStatus("Active");

        // Filter out jobs that the user has already applied for
        List<JobDetails> unappliedJobs = new ArrayList<>();
        for (JobDetails job : activeJobs) {
            boolean applied = false;
            for (Applicant application : userApplications) {
                if (application.getJobDetails().equals(job)) {
                    applied = true;
                    break;
                }
            }
            if (!applied) {
                unappliedJobs.add(job);
            }
        }

        return unappliedJobs;
    }
}
