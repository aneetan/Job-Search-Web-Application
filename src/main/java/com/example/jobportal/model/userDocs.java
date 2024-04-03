package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class userDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userDocId;

    private String cvName;

    private String profileName;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private PersonalDetails personalDetails;

    @OneToOne
    @JoinColumn(name = "addId", referencedColumnName = "addId")
    private  additionalDetails additionalDetails;

    @OneToMany(mappedBy = "userDocs", cascade = CascadeType.ALL)
    private List<Applicant> applicantList = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "application",
    joinColumns = {
            @JoinColumn(name = "userId", referencedColumnName = "userDocId")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "jobId", referencedColumnName = "jobId")
    })

    private List<JobDetails> jobDetails;
    public userDocs(){

    }



    public userDocs(String cvName, String profileName) {
        this.cvName = cvName;
        this.profileName = profileName;
    }
}
