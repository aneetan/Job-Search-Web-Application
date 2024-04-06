package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.engine.internal.Cascade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Entity
@Transactional
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyId;
    private String companyName;
    private String companyEmail;
    private String companyPassword;
    private String status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company", cascade = CascadeType.ALL)
    private Set<JobDetails> jobDetails;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private CompanyDetails companyDetails;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private CompanyDocs companyDocs;

    public Company(){

    }
    public Company(String companyName, String companyEmail, String companyPassword){
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPassword = companyPassword;
    }

}
