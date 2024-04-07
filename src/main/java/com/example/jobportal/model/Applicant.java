package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicantId;

    private String status;

    @ManyToOne
    @JoinColumn(name="userDocId")
    private userDocs userDocs;

    @ManyToOne
    @JoinColumn(name = "jobId")
    private JobDetails jobDetails;





}
