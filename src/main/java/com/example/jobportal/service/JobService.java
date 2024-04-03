package com.example.jobportal.service;

import com.example.jobportal.model.JobDetails;
import com.example.jobportal.repository.JobDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobDetailsRepository jobRepository;

    public List<JobDetails> searchJobs(String query) {
        // Implement your search logic here, such as searching by job title
        return jobRepository.findByTitleContainingIgnoreCase(query);
    }
}
