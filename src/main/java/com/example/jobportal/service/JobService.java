package com.example.jobportal.service;

import com.example.jobportal.model.Applicant;
import com.example.jobportal.model.JobDetails;
import com.example.jobportal.repository.ApplicantRepository;
import com.example.jobportal.repository.JobDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {
    @Autowired
    private JobDetailsRepository jobRepository;

    @Autowired
    ApplicantRepository appRepo;

    public List<JobDetails> searchJobs(String query) {
        // Implement your search logic here, such as searching by job title
        return jobRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Applicant> getApplicantsByJobTitle(String jobTitle) {
        // Retrieve job details by title
        JobDetails jobDetails = jobRepository.findByTitle(jobTitle);

        // Retrieve applicants for the selected job
        List<Applicant> applicants = jobDetails.getApplicantList();

        return applicants;
    }

    public List<Applicant> getApprovedApplicantsByJobTitle(String jobTitle) {
        // Retrieve job details by title
        JobDetails jobDetails = jobRepository.findByTitle(jobTitle);

        // Retrieve applicants for the selected job
        List<Applicant> approvedApplicants = appRepo.findByJobDetailsAndStatus(jobDetails, "Approved");

        return approvedApplicants;
    }

    public Map<JobDetails, Long> calculateDaysRemainingForJobs(List<JobDetails> jobs) {
        Map<JobDetails, Long> daysRemainingMap = new HashMap<>();
        LocalDate today = LocalDate.now();

        for (JobDetails job : jobs) {
            LocalDate deadline = job.getDeadline();
            long daysRemaining = ChronoUnit.DAYS.between(today, deadline);
            daysRemainingMap.put(job, daysRemaining);
        }

        return daysRemainingMap;
    }

}
