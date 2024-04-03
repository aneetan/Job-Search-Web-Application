package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class JobDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobId;
    private String title;
    private String location;
    private String empType;
    private String jobDescription;
    private String qualification;
    private String skills;
    private String salary;
    private String responsibilities;
    private LocalDate deadline;
    private String daysRemaining;

    private String jobStatus;

    @ManyToOne
    @JoinColumn(name="companyId")
    private Company company;

    @ManyToOne
    @JoinColumn(name="docId")
    private CompanyDocs companyDocs;

    @OneToMany(mappedBy = "jobDetails", cascade = CascadeType.ALL)
    private List<Applicant> applicantList = new ArrayList<>();

    @ManyToMany(mappedBy = "jobDetails", fetch = FetchType.LAZY)
    private List<userDocs>  userDocs;




    public JobDetails(){

    }

    public JobDetails(String title, String location, String empType, String jobDescription, String qualification, String skills, String salary, String responsibilities, LocalDate deadline) {
        this.title = title;
        this.location = location;
        this.empType = empType;
        this.jobDescription = jobDescription;
        this.qualification = qualification;
        this.skills = skills;
        this.salary = salary;
        this.responsibilities = responsibilities;
        this.deadline = deadline;
    }
}
